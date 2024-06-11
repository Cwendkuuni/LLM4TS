import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

 private CheckerFactory checkerFactory;
 private Compiler compiler;
 private CheckGlobalThis checker;

 @Before
 public void setUp() {
 checkerFactory = mock(CheckerFactory.class);
 compiler = mock(Compiler.class);
 checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 }

 @Test
 public void testSafeUsage() {
 Node functionNode = Node.newFunction(null, null, null, null, null);
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, functionNode);
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testUnsafeAssignment() {
 Node node = Node.newAssign(Token.ASSIGN, Node.newIdentifier("window"), Node.newThis());
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, node);
 when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(CheckLevel.WARNING), Mockito.eq(DiagnosticType.warning(
 "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);
 checker.visit(traversal, node, parentNode);
 verify(compiler).report(traversal, eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
 }

 @Test
 public void testUnsafeFunctionCall() {
 Node functionNode = Node.newFunction(null, null, null, null, null);
 Node functionCallNode = Node.newCall(null, Node.newIdentifier("window"), Node.newDot(functionNode, "call"));
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, functionCallNode);
 when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(CheckLevel.WARNING), Mockito.eq(DiagnosticType.warning(
 "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);
 checker.visit(traversal, functionCallNode, parentNode);
 verify(compiler).report(traversal, eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
 }

 @Test
 public void testSafePrototypeAccess() {
 Node node = Node.newGetProp(Token.GETPROP, Node.newThis(), Node.newIdentifier("prototype"));
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, node);
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testSafePropertyAssignment() {
 Node node = Node.newAssign(Token.ASSIGN, Node.newGetProp(Token.GETPROP, Node.newThis(), Node.newIdentifier("x")), Node.newNumber(1));
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, node);
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testSafeFunctionWithThisAnnotation() {
 Node functionNode = Node.newFunction(null, new Node[]{Node.newJSDocInfo(new JSDocInfo.FunctionInfo(null, null, null, null, null, new String[]{"@this"})), null, null, null, null}, null, null, null, null);
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, functionNode);
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testSafeConstructor() {
 Node node = Node.newFunction(Token.FUNCTION, null, null, new Node[]{Node.newIdentifier("MyClass")}, null, new Node[]{Node.newJSDocInfo(new JSDocInfo.ConstructorInfo(null, null, null, null))});
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, node);
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testSafeFunctionWithNoAnnotation() {
 Node node = Node.newFunction(Token.FUNCTION, null, null, null, null, null);
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, node);
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testNestedFunctionWithThisAnnotation() {
 Node functionNode = Node.newFunction(null, null, null, null, null, null);
 Node innerFunctionNode = Node.newFunction(Token.FUNCTION, null, null, null, null, new Node[]{Node.newJSDocInfo(new JSDocInfo.FunctionInfo(null, null, null, null, null, new String[]{"@this"}))});
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, Node.newBlock(null, Node.newAssign(Token.ASSIGN, Node.newIdentifier("x"), Node.newGetProp(Token.GETPROP, Node.newThis(), Node.newIdentifier("prototype"))), functionNode));
 checker.visit(traversal, innerFunctionNode, Node.newBlock(Node.newAssign(Token.ASSIGN, Node.newIdentifier("x"), Node.newNumber(1))));
 assert (checker.assignLhsChild == null);
 }

 @Test
 public void testNestedFunctionWithNoAnnotation() {
 Node functionNode = Node.newFunction(null, null, null, null, null, null);
 Node innerFunctionNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null);
 NodeTraversal traversal = NodeTraversal.create(checker, null, checkerFactory, null);
 Node parentNode = Node.newBlock(null);
 NodeTraversal.traverse(traversal, parentNode, Node.newBlock(null, Node.newAssign(Token.ASSIGN, Node.newIdentifier("x"), Node.newGetProp(Token.GETPROP, Node.newThis(), Node.newIdentifier("prototype"))), functionNode));
 checker.visit(traversal, innerFunctionNode, Node.newBlock(Node.newAssign(Token.ASSIGN, Node.newIdentifier("x"), Node.newNumber(1))));
 assert (checker.assignLhsChild == null);
 }
}