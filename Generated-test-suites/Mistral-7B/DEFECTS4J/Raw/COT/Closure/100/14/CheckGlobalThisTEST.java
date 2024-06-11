import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private NodeTraversal traversal;
 private Checker checker;

 @Before
 public void setup() {
 // Initialize the compiler and checker for testing
 compiler = new AbstractCompiler();
 checker = new Checker(compiler, new SourceFile("Test", "test.js", null));
 }

 @Test
 public void testEmptyFunction() {
 Node functionNode = checker.parseCompilationUnit("function() {}", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testPrototypeFunction() {
 Node functionNode = checker.parseCompilationUnit("var obj = {get x() { return this; }}; function test() {} obj.test = test;", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testConstructorFunction() {
 Node functionNode = checker.parseCompilationUnit("function MyConstructor() {}", null, null);
 JSDocInfo jsDoc = functionNode.getJSDocInfo();
 assertTrue(jsDoc.isConstructor());
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testThisAnnotatedFunction() {
 Node functionNode = checker.parseCompilationUnit("@this function test() {}", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
 Node functionNode = checker.parseCompilationUnit("function test() {}", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testAssignmentToThisOnLeftSide() {
 Node assignmentNode = checker.parseCompilationUnit("var a = function() { this.x = 5; }; a();", null, null).getFirstChild();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, assignmentNode, null);
 assertEquals(1, checker.compiler.getDiagnostics().size());
 assertEquals("JSC_USED_GLOBAL_THIS", checker.compiler.getDiagnostics().get(0).getCode());
 }

 @Test
 public void testAssignmentToPrototypeProperty() {
 Node functionNode = checker.parseCompilationUnit("function test() {} test.prototype.x = function() { this.y = 5; }; test.prototype.x();", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testAssignmentToPrototypeSubProperty() {
 Node functionNode = checker.parseCompilationUnit("function test() {} test.prototype.x = { get y() { return this.z; } }; test.prototype.x.y();", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, functionNode, null);
 assertTrue(checker.compiler.getDiagnostics().isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testNullParentNode() {
 Node functionNode = checker.parseCompilationUnit("function test() {}", null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
 checker.traverse(checker, NodeTraversal.PREORDER, null, functionNode);
 }
}