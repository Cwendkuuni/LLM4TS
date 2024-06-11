import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.FLAG_FORCE_TRAVERSAL;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.Parser;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    String code = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "}";
    Parser parser = new Parser();
    rootNode = parser.parse(code, null, null);
  }

  @Test
  public void testUnreachableCodeRemoval() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator);
    traversal.traverse();
    assertEquals(0, eliminator.cfgStack.size());
    assertNull(eliminator.curCfg);
    Node expected = createNode(Token.RETURN, null, null);
    Node actual = eliminator.curCfg.getDirectedGraphNode(rootNode).getValue();
    assertEquals(expected, actual);
  }

  @Test
  public void testNoOpStatementRemoval() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    Node rootNodeWithNoOp = createNode(Token.BLOCK, null, null);
    rootNodeWithNoOp.addChild(createNode(Token.EMPTY, null, null));
    rootNodeWithNoOp.addChild(createNode(Token.RETURN, null, null));
    NodeTraversal traversal = new NodeTraversal(rootNodeWithNoOp, eliminator);
    traversal.traverse();
    assertEquals(0, eliminator.cfgStack.size());
    assertNull(eliminator.curCfg);
    Node expected = createNode(Token.RETURN, null, null);
    Node actual = eliminator.curCfg.getDirectedGraphNode(rootNodeWithNoOp).getValue();
    assertEquals(expected, actual);
  }

  @Test
  public void testFunctionWithDeadCode() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    String code = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "  return; // dead code" +
        "}";
    Parser parser = new Parser();
    rootNode = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, FLAG_FORCE_TRAVERSAL);
    traversal.traverse();

    assertEquals(1, eliminator.cfgStack.size());
    DiGraphNode<Node, Branch> cfgNode = eliminator.curCfg.getDirectedGraphNode(rootNode);
    assertNotNull(cfgNode);
    assertEquals(1, cfgNode.getOutEdges().size());
    Node expectedExit = eliminator.curCfg.getDirectedGraphNode(cfgNode.getOutEdges().get(0).getDestination().getValue()).getValue();
    Node actualExit = cfgNode.getValue();
    assertEquals(expectedExit, actualExit);
  }

  @Test
  public void testFunctionWithDeadBranch() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    String code = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "  return; // dead code" +
        "  if (false) {" +
        "    return;" +
        "  }" +
        "}";
    Parser parser = new Parser();
    rootNode = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, FLAG_FORCE_TRAVERSAL);
    traversal.traverse();

    assertEquals(1, eliminator.cfgStack.size());
    DiGraphNode<Node, Branch> cfgNode = eliminator.curCfg.getDirectedGraphNode(rootNode);
    assertNotNull(cfgNode);
    assertEquals(2, cfgNode.getOutEdges().size());
    Node expectedExit1 = eliminator.curCfg.getDirectedGraphNode(cfgNode.getOutEdges().get(0).getDestination().getValue()).getValue();
    Node actualExit1 = cfgNode.getValue();
    assertEquals(expectedExit1, actualExit1);

    Node expectedExit2 = eliminator.curCfg.getDirectedGraphNode(cfgNode.getOutEdges().get(1).getDestination().getValue()).getValue();
    Node actualExit2 = cfgNode.getValue();
    assertEquals(expectedExit2, actualExit2);
  }

  @Test
  public void testLoopWithDeadCode() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    String code = "function foo() {" +
        "  for (; true;) {" +
        "    return;" +
        "    alert('unreachable');" +
        "  }" +
        "}";
    Parser parser = new Parser();
    rootNode = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, FLAG_FORCE_TRAVERSAL);
    traversal.traverse();

    assertEquals(1, eliminator.cfgStack.size());
    DiGraphNode<Node, Branch> cfgNode = eliminator.curCfg.getDirectedGraphNode(rootNode);
    assertNotNull(cfgNode);
    assertEquals(0, cfgNode.getOutEdges().size());
  }
}