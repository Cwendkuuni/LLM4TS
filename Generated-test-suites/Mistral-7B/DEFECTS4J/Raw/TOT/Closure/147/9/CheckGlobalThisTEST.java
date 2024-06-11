import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {
 @Override
 protected Checker getChecker() {
 return checker;
 }
 };
 checker = CheckerFactory.create(compiler, CheckLevel.WARNING);
 }

 @Test
 public void testSafeUsageOfThis() {
 String code = "function Foo() { this.x = 1; }";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyErrorCount(compiler, 0);
 }

 @Test
 public void testUnsafeUsageOfThisAsProperty() {
 String code = "var a = {}; a.x = this;";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyError(compiler, "JSC_USED_GLOBAL_THIS", 1);
 }

 @Test
 public void testUnsafeUsageOfThisAsFunctionArgument() {
 String code = "function bar(x) { x(); } function Foo() { bar(this); }";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyError(compiler, "JSC_USED_GLOBAL_THIS", 1);
 }

 @Test
 public void testUnsafeUsageOfThisAsFunctionReturnValue() {
 String code = "function bar() { return this; } function Foo() { return bar(); }";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyError(compiler, "JSC_USED_GLOBAL_THIS", 1);
 }

 @Test
 public void testUnsafeUsageOfThisAsFunctionConstructor() {
 String code = "function Foo() { this.x = 1; } new Foo();";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyErrorCount(compiler, 0);
 }

 @Test
 public void testSafeUsageOfThisAsPrototypeProperty() {
 String code = "function Foo() { this.x = 1; } Foo.prototype.y = function() { this.x = 2; }";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyErrorCount(compiler, 0);
 }

 @Test
 public void testSafeUsageOfThisAsPrototypeMethod() {
 String code = "function Foo() { this.x = 1; } Foo.prototype.y = function() { this.x = 2; } Foo.prototype.y();";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyErrorCount(compiler, 0);
 }

 @Test
 public void testSafeUsageOfThisAsFunctionCall() {
 String code = "function Foo() { this.x = 1; } Foo();";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyErrorCount(compiler, 0);
 }

 @Test
 public void testSafeUsageOfThisAsFunctionConstructorWithArguments() {
 String code = "function Foo(a, b) { this.x = a; this.y = b; } new Foo(1, 2);";
 SourceFile source = new SourceFile("Foo.js", code);
 Node root = Parser.parse(source, null, null);
 NodeTraversal traversal = new NodeTraversal(root, compiler);
 checker.process(root, traversal);
 CompilationTestHelper.verifyErrorCount(compiler, 0);
 }
}