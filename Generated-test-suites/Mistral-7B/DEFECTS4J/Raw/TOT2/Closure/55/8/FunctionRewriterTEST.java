import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {

 @Test
 public void testEmptyFunctionReduction() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 Node rootNode = new Node(Token.BLOCK, 1, 1);
 Node emptyFunction = new Node(Token.FUNCTION, 1, 1, null, null, null, null);
 emptyFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1));
 rootNode.addChildToBack(emptyFunction);

 FunctionRewriter.Reducer reducer = new FunctionRewriter.EmptyFunctionReducer();
 NodeTraversal traversal = new NodeTraversal(functionRewriter, rootNode);
 traversal.traverse(new FunctionRewriter.ReductionGatherer(
 ImmutableList.of(reducer), null));
 traversal.traverse(new NodeTraversal.Callback() {
 @Override
 public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
 return reducer.reduce(node) != node;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {}
 });

 Node expectedResult = functionRewriter.parseHelperCode(reducer);
 Node actualResult = rootNode.getLastChild();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIdentityFunctionReduction() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 Node rootNode = new Node(Token.BLOCK, 1, 1);
 Node identityFunction = new Node(Token.FUNCTION, 1, 1, null, null, null, null);
 identityFunction.addChildrenToBack(new Node(Token.ARGUMENTS, 1, 1));
 identityFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1,
 new Node(Token.NAME, 1, 1, "a"), null));
 rootNode.addChildToBack(identityFunction);

 FunctionRewriter.Reducer reducer = new FunctionRewriter.IdentityReducer();
 NodeTraversal traversal = new NodeTraversal(functionRewriter, rootNode);
 traversal.traverse(new FunctionRewriter.ReductionGatherer(
 ImmutableList.of(reducer), null));
 traversal.traverse(new NodeTraversal.Callback() {
 @Override
 public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
 return reducer.reduce(node) != node;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {}
 });

 Node expectedResult = functionRewriter.parseHelperCode(reducer);
 Node actualResult = rootNode.getLastChild();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testReturnConstantReduction() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 Node rootNode = new Node(Token.BLOCK, 1, 1);
 Node returnConstantFunction = new Node(Token.FUNCTION, 1, 1, null, null, null, null);
 returnConstantFunction.addChildrenToBack(new Node(Token.ARGUMENTS, 1, 1));
 returnConstantFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1,
 new Node(Token.NUM, 1, 1, 10), null));
 rootNode.addChildToBack(returnConstantFunction);

 FunctionRewriter.Reducer reducer = new FunctionRewriter.ReturnConstantReducer();
 NodeTraversal traversal = new NodeTraversal(functionRewriter, rootNode);
 traversal.traverse(new FunctionRewriter.ReductionGatherer(
 ImmutableList.of(reducer), null));
 traversal.traverse(new NodeTraversal.Callback() {
 @Override
 public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
 return reducer.reduce(node) != node;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {}
 });

 Node expectedResult = functionRewriter.parseHelperCode(reducer);
 Node actualResult = rootNode.getLastChild();
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidReduction() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 Node rootNode = new Node(Token.BLOCK, 1, 1);
 Node invalidFunction = new Node(Token.FUNCTION, 1, 1, null, null, null, null);
 // This function doesn't match any reducer pattern
 invalidFunction.addChildrenToBack(new Node(Token.ARGUMENTS, 1, 1));
 invalidFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1,
 new Node(Token.NUM, 1, 1, 10), null));
 rootNode.addChildToBack(invalidFunction);

 FunctionRewriter.Reducer reducer = new FunctionRewriter.EmptyFunctionReducer();
 NodeTraversal traversal = new NodeTraversal(functionRewriter, rootNode);
 traversal.traverse(new FunctionRewriter.ReductionGatherer(
 ImmutableList.of(reducer), null));
 traversal.traverse(new NodeTraversal.Callback() {
 @Override
 public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
 return reducer.reduce(node) != node;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {}
 });
 }
}