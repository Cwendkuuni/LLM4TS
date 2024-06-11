import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 checker = compiler.getChecker();
 }

 @Test
 public void testSafeUsesOfThis() {
 SourceFile source = new SourceFile("Test.js", "function foo() { this.x = 42; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testUnsafeUseOfThisAsFunctionName() {
 SourceFile source = new SourceFile("Test.js", "function thisIsBad() { }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), hasItem(
     new DiagnosticType("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")
     ));
 }

 @Test
 public void testUnsafeUseOfThisAsFunctionProperty() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = 42; } Foo.prototype.bar = function() { this.x = 43; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), hasItem(
     new DiagnosticType("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")
     ));
 }

 @Test
 public void testUnsafeUseOfThisAsMethodName() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = 42; } Foo.prototype.bar = function() { this.bar = function() { this.x = 43; } }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testUnsafeUseOfThisInConstructor() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = 42; }");
 checker.check(source, new CompilerOptions().withOption(CompilerOptions.WarningLevelOption, WARNING));
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testUnsafeUseOfThisInPrototypeMethod() {
 SourceFile source = new SourceFile("Test.js", "function Foo() {} Foo.prototype.bar = function() { this.x = 42; }");
 checker.check(source, new CompilerOptions().withOption(CompilerOptions.WarningLevelOption, WARNING));
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testUnsafeUseOfThisInFunctionWithNoAnnotation() {
 SourceFile source = new SourceFile("Test.js", "function evil() { this.x = 42; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testSafeUseOfThisInPrototypeMethodWithAssignment() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = null; } Foo.prototype.bar = function() { this.x = 42; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testSafeUseOfThisInPrototypeMethodWithPropertyAccess() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = null; } Foo.prototype.bar = function() { this.y = 42; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testSafeUseOfThisInPrototypeMethodWithDeclaration() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = null; } Foo.prototype.bar = function() { var x = this.x; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testSafeUseOfThisInPrototypeMethodWithPrototypePropertyAccess() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = null; } Foo.prototype.bar = function() { this.prototype.x = 42; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }

 @Test
 public void testSafeUseOfThisInPrototypeMethodWithLocalVariable() {
 SourceFile source = new SourceFile("Test.js", "function Foo() { this.x = null; } Foo.prototype.bar = function() { var x = this.x; }");
 checker.check(source, new CompilerOptions());
 assertThat(checker.getDiagnostics(), empty());
 }
}