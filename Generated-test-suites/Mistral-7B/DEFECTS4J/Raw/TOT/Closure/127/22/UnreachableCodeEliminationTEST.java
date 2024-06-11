import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testSimpleReturn() {
    String input = "function foo() { return; alert('unreachable'); }";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
    String output = compiler.printTree(root);
    assertEquals("function foo() { }", output);
  }

  @Test
  public void testDeadCodeAfterReturn() {
    String input = "function foo() { if (x) { return; alert('unreachable'); } }";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
    String output = compiler.printTree(root);
    assertEquals("function foo() { if (x) { return; } }", output);
  }

  @Test
  public void testDeadCodeWithoutSideEffects() {
    String input = "function foo() { a.b.MyClass.prototype.propertyName; }";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
    String output = compiler.printTree(root);
    assertEquals("function foo() { }", output);
  }

  @Test
  public void testDeadCodeWithSideEffects() {
    String input = "function foo() { document.write('Hello, World!'); return; }";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
    String output = compiler.printTree(root);
    assertEquals("function foo() { document.write('Hello, World!'); }", output);
  }

  @Test
  public void testEmptyFunction() {
    String input = "function foo() {}";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
    String output = compiler.printTree(root);
    assertEquals("function foo() { }", output);
  }

  @Test (expected = AssertionError.class)
  public void testNullNode() {
    String input = null;
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
  }

  @Test
  public void testControlFlowGraph() {
    String input = "function foo() { if (x > 5) { return 42; } if (y > 10) { return 7; } }";
    Compiler compiler = new Compiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    Node root = compiler.parseTree(input, null, null);
    pass.process(null, root);
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    // Check that the control flow graph has the expected structure.
    // This assumes that the UnreachableCodeElimination pass correctly
    // computed the control flow graph.
    DiGraphNode<Node, Branch> entryNode = cfg.getEntry();
    DiGraphNode<Node, Branch> ifXNode = cfg.getNodeForStatement(0);
    DiGraphNode<Node, Branch> ifYNode = cfg.getNodeForStatement(1);
    DiGraphNode<Node, Branch> returnNode = cfg.getNodeForStatement(2);
    // The entry node should have no incoming edges, except for the
    // implicit entry edge.
    assertEquals(1, entryNode.getIncomingEdges().size());
    assertEquals(Branch.ENTRY, entryNode.getIncomingEdges().get(0).getValue());
    // The ifX node should have one incoming edge from the entry node.
    assertEquals(1, ifXNode.getIncomingEdges().size());
    assertEquals(Branch.UNCOND, ifXNode.getIncomingEdges().get(0).getValue());
    // The ifY node should have one incoming edge from ifX node.
    assertEquals(1, ifYNode.getIncomingEdges().size());
    assertEquals(Branch.UNCOND, ifYNode.getIncomingEdges().get(0).getValue());
    // The return node should have one outgoing edge to the entry node.
    assertEquals(1, returnNode.getOutgoingEdges().size());
    assertEquals(Branch.UNCOND, returnNode.getOutgoingEdges().get(0).getValue());
    // The ifX node should have two outgoing edges, one to ifY and one to return.
    assertEquals(2, ifXNode.getOutgoingEdges().size());
    assertEquals(Branch.UNCOND, ifXNode.getOutgoingEdges().get(0).getValue());
    assertEquals(Branch.UNCOND, ifXNode.getOutgoingEdges().get(1).getValue());
  }
}