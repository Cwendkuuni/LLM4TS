import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {
 @Override
 public CompilerOptions getOptions() {
 return new CompilerOptions();
 }
 };
 }

 @Test
 public void testCheckGlobalThis_FunctionWithPrototypeMethod() {
 String jsCode = "function Foo() {}\n" +
 "Foo.prototype.bar = function() { this.property = 42; };";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithThisAnnotation() {
 String jsCode = "@this class Foo {}\n" +
 "function Foo() { this.property = 42; }";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithNoThisAnnotation() {
 String jsCode = "var a = function() { this.property = 42; };";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithGlobalThis() {
 String jsCode = "function evil() { this.property = 42; }";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().size() > 0);
 }

 @Test
 public void testCheckGlobalThis_FunctionWithAssignment() {
 String jsCode = "var a = function() { this.property = 42 };";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertFalse(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithPrototypeGet() {
 String jsCode = "function Foo() {}\n" +
 "Foo.prototype.getBar = function() { return this.property; };";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithPrototypeGetChain() {
 String jsCode = "function Foo() {}\n" +
 "Foo.prototype.getBar = function() { return this.property.property; };";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testCheckGlobalThis_FunctionWithGlobalGet() {
 String jsCode = "function evil() { return this.property; }";
 Node node = NodeUtil.parseJsString(jsCode);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 new NodeTraversal(compiler, checker).traverse(node);
 assertTrue(compiler.getDiagnostics().size() > 0);
 }
}