import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private FunctionRewriter functionRewriter;
  private Node rootNode;

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = new Compiler();
    functionRewriter = new FunctionRewriter(compiler);

    rootNode = compiler.parseJs("class C {\n" +
        "  getA = function() { return this.a_ };\n" +
        "  setA = function(newValue) { this.a_ = newValue };\n" +
        "}", null);
  }

  @org.junit.Test
  public void testProcess() {
    functionRewriter.process(null, rootNode);

    List<Node> getA = NodeUtil.getChildren(rootNode, "C.prototype.getA");
    assertSame(NodeUtil.getChildren(rootNode, "JSCompiler_get")
        .get(0), getA.get(0));

    List<Node> setA = NodeUtil.getChildren(rootNode, "C.prototype.setA");
    assertSame(NodeUtil.getChildren(rootNode, "JSCompiler_set")
        .get(0), setA.get(0));
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node emptyFunction = NodeUtil.getChildren(rootNode, "C.prototype.empty")
        .get(0);
    assertNull(emptyFunction);

    functionRewriter.process(null, rootNode);

    List<Node> emptyFunctionNodeList = NodeUtil.getChildren(rootNode,
        "JSCompiler_emptyFn");
    assertSame(emptyFunctionNodeList.get(0),
        NodeUtil.getChildren(rootNode, "C.prototype.empty")
            .get(0));
  }

  @org.junit.Test
  public void testIdentityReducer() {
    Node identityFunction = NodeUtil.getChildren(rootNode, "C.prototype.identity")
        .get(0);
    assertNull(identityFunction);

    functionRewriter.process(null, rootNode);

    List<Node> identityFunctionNodeList = NodeUtil.getChildren(rootNode,
        "JSCompiler_identityFn");
    assertSame(identityFunctionNodeList.get(0),
        NodeUtil.getChildren(rootNode, "C.prototype.identity")
            .get(0));

    Node identityFunctionCall = NodeUtil.getChildren(rootNode,
        "C.prototype.identity").get(0).getLastChild();
    assertSame(identityFunctionCall,
        NodeUtil.getChildren(rootNode, "JSCompiler_identityFn")
            .get(0).getLastChild());
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node getA = NodeUtil.getChildren(rootNode, "C.prototype.getA").get(0);
    assertSame(Token.FUNCTION, getA.getType());

    functionRewriter.process(null, rootNode);

    List<Node> getAFunctionList = NodeUtil.getChildren(rootNode,
        "JSCompiler_get");
    assertSame(getAFunctionList.get(0), NodeUtil.getChildren(rootNode,
        "C.prototype.getA").get(0));

    List<Node> getANodeList = NodeUtil.getChildren(getAFunctionList.get(0),
        "JSCompiler_get");
    assertSame(NodeUtil.getChildren(rootNode, "C.prototype.getA")
        .get(0), getANodeList.get(1));
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node setA = NodeUtil.getChildren(rootNode, "C.prototype.setA").get(0);
    assertSame(Token.FUNCTION, setA.getType());

    functionRewriter.process(null, rootNode);

    List<Node> setAFunctionList = NodeUtil.getChildren(rootNode,
        "JSCompiler_set");
    assertSame(setAFunctionList.get(0), NodeUtil.getChildren(rootNode,
        "C.prototype.setA").get(0));

    List<Node> setANodeList = NodeUtil.getChildren(setAFunctionList.get(0),
        "JSCompiler_set");
    assertSame(NodeUtil.getChildren(rootNode, "C.prototype.setA")
        .get(0), setANodeList.get(1));
  }

  @org.junit.Test
  public void testReductionGatherer() {
    Node rootNodeCopy = rootNode.cloneTree();

    NodeTraversal nodeTraversal = new NodeTraversal(rootNodeCopy, new ReductionGatherer(
        ImmutableList.of(new IdentityReducer(), new GetterReducer(), new SetterReducer(),
            new EmptyFunctionReducer())));
    nodeTraversal.traverse();

    functionRewriter.process(null, rootNodeCopy);
  }

  private static class ReductionGatherer implements NodeTraversal.Callback {

    private final List<Reducer> reducers;

    ReductionGatherer(List<Reducer> reducers) {
      this.reducers = reducers;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
      for (Reducer reducer : reducers) {
        Node replacement = reducer.reduce(node);
        if (replacement != node) {
          nodeTraversal.addReduction(new FunctionRewriter.Reduction(parent, node, replacement));
          return false;
        }
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
    }
  }
}