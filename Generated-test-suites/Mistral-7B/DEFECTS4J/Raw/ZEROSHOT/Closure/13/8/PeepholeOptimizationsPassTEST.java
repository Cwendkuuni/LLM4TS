import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest extends CompilerPassTestCase {

  @SuppressWarnings("unchecked")
  private AbstractPeepholeOptimization[] createPeepholeOptimizationsArray() {
    return new AbstractPeepholeOptimization[] {mock(AbstractPeepholeOptimization.class), mock(AbstractPeepholeOptimization.class)};
  }

  private PeepholeOptimizationsPass createPass(AbstractCompiler compiler) {
    AbstractPeepholeOptimization[] optimizations = createPeepholeOptimizationsArray();
    return new PeepholeOptimizationsPass(compiler, optimizations);
  }

  @Override
  protected CompilerPass createPassUnderTest() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    return createPass(compiler);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = createPass(compiler);
    Node root = createNode("root");
    pass.process(null, root);
  }

  @org.junit.Test
  public void testBeginTraversal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = createPass(compiler);
    pass.beginTraversal();
  }

  @org.junit.Test
  public void testEndTraversal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = createPass(compiler);
    pass.endTraversal();
  }

  @org.junit.Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = createPass(compiler);

    Node inputNode = createNode("inputNode");
    Node expectedOutputNode = createNode("expectedOutputNode");

    // Spy the visit method to check its behavior
    PeepholeOptimizationsPass spyPass = spy(pass);

    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      when(optimization.optimizeSubtree(inputNode)).thenReturn(expectedOutputNode);
    }

    Node actualOutputNode = spyPass.visit(inputNode);
    assertNotNull(actualOutputNode);
    assertEquals(expectedOutputNode, actualOutputNode);
  }

  @org.junit.Test
  public void testShouldVisit_function() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node functionNode = createNode("functionNode");
    assertTrue(pass.shouldVisit(functionNode));
  }

  @org.junit.Test
  public void testShouldVisit_script() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node scriptNode = createNode("scriptNode");
    assertTrue(pass.shouldVisit(scriptNode));
  }

  @org.junit.Test
  public void testShouldVisit_other() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node otherNode = createNode("otherNode");
    assertFalse(pass.shouldVisit(otherNode));
  }

  @org.junit.Test
  public void testShouldRetraverse_function() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node functionNode = createNode("functionNode");
    assertFalse(pass.shouldRetraverse(functionNode));
  }

  @org.junit.Test
  public void testShouldRetraverse_script() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node scriptNode = createNode("scriptNode");
    assertFalse(pass.shouldRetraverse(scriptNode));
  }

  @org.junit.Test
  public void testShouldRetraverse_functionWithChangedChildren() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node functionNode = createNode("functionNode");
    Node childNode = createNode("childNode");
    pass.traversalState.push();
    pass.traversalState.peek().traverseChildScopes = false;
    pass.visit(childNode);
    assertTrue(pass.shouldRetraverse(functionNode));
  }

  @org.junit.Test
  public void testShouldRetraverse_scriptWithChangedChildren() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node scriptNode = createNode("scriptNode");
    Node childNode = createNode("childNode");
    pass.traversalState.push();
    pass.traversalState.peek().traverseChildScopes = false;
    pass.visit(childNode);
    assertTrue(pass.shouldRetraverse(scriptNode));
  }

  @org.junit.Test
  public void testExitNode() {
    PeepholeOptimizationsPass pass = createPass(null);
    Node node = createNode("node");
    pass.exitNode(node);
  }

  @org.junit.Test
  public void testBeginAndEndTraversal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = createPass(compiler);

    pass.beginTraversal();
    pass.endTraversal();
  }

  private Node createNode(String name) {
    Node node = new Node();
    node.setType(Node.NDEF);
    node.setName(name);
    return node;
  }

  private List<Node> createNodesList(String... names) {
    List<Node> nodes = new ArrayList<>();
    for (String name : names) {
      nodes.add(createNode(name));
    }
    return nodes;
  }
}