import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.collect.Lists.newArrayList;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {

 private AbstractCompiler compiler;
 private NodeFactory nodeFactory;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 Compiler compiler = new Compiler();
 this.compiler = (AbstractCompiler) compiler;
 this.nodeFactory = compiler.getFactory();
 }

 @Test
 public void testRemoveConstantExpressions() {
 // Given a simple expression with constant expressions
 Node root = createConstantExpressionsNode();

 // When the RemoveConstantExpressions pass is run
 CompilerPass removeConstantExpressions =
 new RemoveConstantExpressions(compiler);
 removeConstantExpressions.process(null, root);

 // Then the constant expressions have been removed
 Node simplifiedNode = compiler.getOutput();
 List<Node> expectedNodes = newArrayList(
 nodeFactory.literal(1), nodeFactory.identifier("foo"),
 nodeFactory.identifier("bar")
 );
 List<Node> actualNodes = NodeTraversal.deepTraverse(null, simplifiedNode,
 new GetNodeListCallback());

 assertTrue(expectedNodes.equals(actualNodes));
 }

 @Test
 public void testRemoveConstantExpressionsWithSideEffects() {
 // Given an expression with constant expressions and side effects
 Node root = createSideEffectsConstantExpressionsNode();

 // When the RemoveConstantExpressions pass is run
 CompilerPass removeConstantExpressions =
 new RemoveConstantExpressions(compiler);
 removeConstantExpressions.process(null, root);

 // Then the constant expressions have been replaced with side effect nodes
 Node simplifiedNode = compiler.getOutput();
 List<Node> expectedNodes = newArrayList(
 nodeFactory.identifier("foo"), nodeFactory.identifier("bar")
 );
 List<Node> actualNodes = NodeTraversal.deepTraverse(null, simplifiedNode,
 new GetNodeListCallback());

 for (int i = 0; i < actualNodes.size(); i++) {
 Node actualNode = actualNodes.get(i);
 if (actualNode.getType() == Token.IDENTIFIER) {
 assertEquals("foo", actualNode.getFirstChild().getIdentifier());
 assertEquals("bar", actualNodes.get(i + 1).getFirstChild().getIdentifier());
 i++;
 }
 }
 }

 @Test(expected = AssertionError.class)
 public void testRemoveConstantExpressionsWithNonConstantExpressions() {
 // Given an expression with non-constant expressions
 Node root = createNonConstantExpressionsNode();

 // When the RemoveConstantExpressions pass is run
 CompilerPass removeConstantExpressions =
 new RemoveConstantExpressions(compiler);
 removeConstantExpressions.process(null, root);

 // Then an error is thrown
 }

 private Node createConstantExpressionsNode() {
 return nodeFactory.literal(1)
 .add(Token.OP_PLUS)
 .add(nodeFactory.call(null, "foo", Lists.newArrayList()))
 .add(Token.OP_PLUS)
 .add(nodeFactory.call(null, "bar", Lists.newArrayList()));
 }

 private Node createSideEffectsConstantExpressionsNode() {
 return nodeFactory.literal(1)
 .add(Token.OP_PLUS)
 .add(nodeFactory.call(null, "foo", Lists.newArrayList()))
 .add(Token.ASSIGN, new Node(Token.IDENTIFIER, "x"))
 .add(Token.OP_PLUS)
 .add(nodeFactory.call(null, "bar", Lists.newArrayList()))
 .add(Token.ASSIGN, new Node(Token.IDENTIFIER, "y"));
 }

 private Node createNonConstantExpressionsNode() {
 return nodeFactory.literal(1)
 .add(Token.OP_PLUS)
 .add(nodeFactory.call(null, "foo", Lists.newArrayList()))
 .add(Token.OP_PLUS)
 .add(nodeFactory.identifier("x").add(Token.OP_PLUS));
 }

 private static class GetNodeListCallback extends AbstractPostOrderCallback {
 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (node != null) {
 getNodes().add(node);
 }
 }
 }
}