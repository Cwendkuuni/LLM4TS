import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CheckSideEffects checkSideEffects;

 @Before
 public void setUp() {
 compiler = new Compiler();
 checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
 }

 @Test
 public void testProcessEmptyExternsAndRoot() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createRootNode();
 checkSideEffects.process(externs, root);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testProcessWithVoidNodeAtBlockLevel() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createBlockNode("", Lists.newArrayList());
 Node voidNode = NodeOperations.createVoidNode();
 root.addChildToBack(voidNode);
 checkSideEffects.process(externs, root);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testProcessWithExpressionsThatDoNotChangeState() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createBlockNode("", Lists.newArrayList());
 Node a = NodeOperations.createNumberNode(1);
 Node b = NodeOperations.createNameNode("Math", true);
 Node c = NodeOperations.createCallNode(b, Lists.newArrayList(a));
 root.addChildToBack(c);
 checkSideEffects.process(externs, root);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testProcessWithSimpleOperator() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createBlockNode("", Lists.newArrayList());
 Node a = NodeOperations.createNumberNode(1);
 Node b = NodeOperations.createNumberNode(2);
 Node add = NodeOperations.createAdditionNode(null, a, b);
 root.addChildToBack(add);
 checkSideEffects.process(externs, root);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test(expected = AssertionError.class)
 public void testProcessWithStatementThatChangesState() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createBlockNode("", Lists.newArrayList());
 Node a = NodeOperations.createNameNode("x", true);
 Node assign = NodeOperations.createAssignmentNode(a, NodeOperations.createNumberNode(1));
 root.addChildToBack(assign);
 checkSideEffects.process(externs, root);
 fail("Should have thrown a warning");
 }

 @Test
 public void testVisitWithVoidNodeAtBlockLevel() {
 Node n = NodeOperations.createVoidNode();
 Node parent = NodeOperations.createBlockNode("", Lists.newArrayList());
 checkSideEffects.visit(null, parent, null);
 parent.replaceChild(parent.getFirstChild(), n);
 checkSideEffects.visit(null, parent, null);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitWithStatementsThatDoNotChangeState() {
 Node n1 = NodeOperations.createNumberNode(1);
 Node n2 = NodeOperations.createNameNode("Math", true);
 Node n3 = NodeOperations.createCallNode(n2, Lists.newArrayList(n1));
 Node parent = NodeOperations.createBlockNode("", Lists.newArrayList());
 checkSideEffects.visit(null, parent, null);
 parent.addChildToBack(n3);
 checkSideEffects.visit(null, parent, null);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitWithSimpleOperator() {
 Node n1 = NodeOperations.createNumberNode(1);
 Node n2 = NodeOperations.createNumberNode(2);
 Node add = NodeOperations.createAdditionNode(null, n1, n2);
 Node parent = NodeOperations.createBlockNode("", Lists.newArrayList());
 checkSideEffects.visit(null, parent, null);
 parent.addChildToBack(add);
 checkSideEffects.visit(null, parent, null);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitWithStatementThatChangesState() {
 Node n1 = NodeOperations.createNameNode("x", true);
 Node n2 = NodeOperations.createNumberNode(1);
 Node assign = NodeOperations.createAssignmentNode(n1, n2);
 Node parent = NodeOperations.createBlockNode("", Lists.newArrayList());
 checkSideEffects.visit(null, parent, null);
 parent.addChildToBack(assign);
 checkSideEffects.visit(null, parent, null);
 assertTrue("A warning was expected", checkSideEffects.problemNodes.size() > 0);
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitWithForLoop() {
 Node n1 = NodeOperations.createNameNode("i", true);
 Node n2 = NodeOperations.createNumberNode(0);
 Node n3 = NodeOperations.createNameNode("arr", true);
 Node n4 = NodeOperations.createGetPropertyNode(n3, n1);
 Node n5 = NodeOperations.createComparisonNode(Token.OP_LESSEQ, n1, NodeOperations.createNumberNode(arr.length()));
 Node n6 = NodeOperations.createStatementNode(NodeOperations.createAssignmentNode(n1, NodeOperations.createAdditionNode(null, n1, NodeOperations.createNumberNode(1))));
 Node n7 = NodeOperations.createBlockNode("", Lists.newArrayList(n6));
 Node n8 = NodeOperations.createForNode(null, n1, n2, n5, n7);
 Node parent = NodeOperations.createBlockNode("", Lists.newArrayList());
 checkSideEffects.visit(null, parent, null);
 parent.addChildToBack(n8);
 checkSideEffects.visit(null, parent, null);
 assertFalse("No warnings expected", checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testProtectSideEffects() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createBlockNode("", Lists.newArrayList());
 Node n1 = NodeOperations.createNameNode("x", true);
 Node n2 = NodeOperations.createNumberNode(1);
 Node assign = NodeOperations.createAssignmentNode(n1, n2);
 Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("protectorFn", true), Lists.newArrayList(assign));
 root.addChildToBack(functionCall);
 checkSideEffects.protectSideEffects();
 Node extern = checkSideEffects.problemNodes.get(0);
 assertEquals("JSCOMPILER_PRESERVE(x = 1)", extern.toString());
 }

 @Test
 public void testStripProtection() {
 Node externs = NodeOperations.createRootNode();
 Node root = NodeOperations.createBlockNode("", Lists.newArrayList());
 Node n1 = NodeOperations.createNameNode("x", true);
 Node n2 = NodeOperations.createNumberNode(1);
 Node assign = NodeOperations.createAssignmentNode(n1, n2);
 Node functionCall = NodeOperations.createCallNode(NodeOperations.createNameNode("protectorFn", true), Lists.newArrayList(assign));
 root.addChildToBack(functionCall);
 CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
 stripProtection.process(externs, root);
 assertEquals(assign, root.getLastChild());
 }
}