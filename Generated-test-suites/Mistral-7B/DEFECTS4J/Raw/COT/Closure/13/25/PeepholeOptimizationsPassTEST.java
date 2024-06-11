import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest extends CompilerPassTestBase {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private AbstractPeepholeOptimization firstOptimization;

  @Mock
  private AbstractPeepholeOptimization secondOptimization;

  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(compiler.addChangeHandler(any())).thenReturn(compiler);
    when(compiler.removeChangeHandler(any())).thenReturn(compiler);

    pass = new PeepholeOptimizationsPass(compiler, firstOptimization, secondOptimization);
  }

  @Test
  public void testProcess_rootNodeIsVisited() {
    Node rootNode = createTestNode();

    pass.process(externs, rootNode);

    verify(compiler).addChangeHandler(pass.new PeepholeChangeHandler());
    verify(firstOptimization).beginTraversal(compiler);
    verify(secondOptimization).beginTraversal(compiler);

    pass.traverse(rootNode);

    verify(firstOptimization).optimizeSubtree(rootNode);
    verify(secondOptimization).optimizeSubtree(rootNode);

    pass.exitNode(rootNode);

    verify(firstOptimization).endTraversal(compiler);
    verify(secondOptimization).endTraversal(compiler);

    verify(compiler, times(1)).removeChangeHandler(pass.new PeepholeChangeHandler());
  }

  @Test
  public void testProcess_shouldNotRetraverseAfterOptimization() {
    Node rootNode = createTestNode();

    doReturn(true).when(firstOptimization).shouldRetraverse(rootNode);
    doReturn(true).when(secondOptimization).shouldRetraverse(rootNode);

    pass.process(externs, rootNode);

    verify(compiler).addChangeHandler(pass.new PeepholeChangeHandler());
    verify(firstOptimization).beginTraversal(compiler);
    verify(secondOptimization).beginTraversal(compiler);

    pass.traverse(rootNode);

    verify(firstOptimization).optimizeSubtree(rootNode);
    verify(secondOptimization).optimizeSubtree(rootNode);

    pass.exitNode(rootNode);

    verify(firstOptimization, never()).beginTraversal(compiler);
    verify(secondOptimization, never()).beginTraversal(compiler);

    verify(compiler).removeChangeHandler(pass.new PeepholeChangeHandler());
  }

  @Test
  public void testProcess_shouldVisitChildScopes() {
    Node rootNode = createTestNode();

    doReturn(true).when(firstOptimization).shouldRetraverse(rootNode);
    doReturn(true).when(secondOptimization).shouldRetraverse(rootNode);
    doReturn(true).when(firstOptimization).shouldVisit(any());
    doReturn(true).when(secondOptimization).shouldVisit(any());

    pass.process(externs, rootNode);

    verify(compiler).addChangeHandler(pass.new PeepholeChangeHandler());
    verify(firstOptimization).beginTraversal(compiler);
    verify(secondOptimization).beginTraversal(compiler);

    pass.traverse(rootNode);

    Node childNode = createTestNode();
    pass.traverse(childNode);

    verify(firstOptimization, times(2)).optimizeSubtree(any());
    verify(secondOptimization, times(2)).optimizeSubtree(any());

    verify(firstOptimization).endTraversal(compiler);
    verify(secondOptimization).endTraversal(compiler);

    verify(compiler).removeChangeHandler(pass.new PeepholeChangeHandler());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_shouldThrowException() {
    Node rootNode = createTestNode();

    doThrow(new RuntimeException("test exception")).when(firstOptimization).optimizeSubtree(rootNode);

    pass.process(externs, rootNode);

    verify(compiler).addChangeHandler(pass.new PeepholeChangeHandler());
    verify(firstOptimization).beginTraversal(compiler);
    verify(secondOptimization).beginTraversal(compiler);

    pass.traverse(rootNode);

    pass.exitNode(rootNode);

    verify(firstOptimization).endTraversal(compiler);
    verify(secondOptimization).endTraversal(compiler);

    verify(compiler, times(1)).removeChangeHandler(pass.new PeepholeChangeHandler());

    pass.traverse(rootNode);
  }

  @Test
  public void testShouldVisit_functionNodeIsVisited() {
    Node rootNode = createTestFunctionNode();

    doReturn(true).when(pass).shouldRetraverse(rootNode);

    pass.shouldVisit(rootNode);

    assertTrue(pass.shouldVisit(rootNode));
  }

  @Test
  public void testShouldVisit_scriptNodeIsVisited() {
    Node rootNode = createTestScriptNode();

    doReturn(true).when(pass).shouldRetraverse(rootNode);

    pass.shouldVisit(rootNode);

    assertTrue(pass.shouldVisit(rootNode));
  }

  @Test
  public void testShouldRetraverse_functionNodeIsRetraversed() {
    Node rootNode = createTestFunctionNode();

    doReturn(true).when(pass).shouldVisit(rootNode);
    doReturn(true).when(pass).shouldRetraverse(rootNode);

    pass.shouldRetraverse(rootNode);

    assertTrue(pass.shouldRetraverse(rootNode));
  }

  @Test
  public void testShouldRetraverse_scriptNodeIsRetraversed() {
    Node rootNode = createTestScriptNode();

    doReturn(true).when(pass).shouldVisit(rootNode);
    doReturn(true).when(pass).shouldRetraverse(rootNode);

    pass.shouldRetraverse(rootNode);

    assertTrue(pass.shouldRetraverse(rootNode));
  }

  @Test
  public void testShouldRetraverse_functionNodeIsNotRetraversed() {
    Node rootNode = createTestFunctionNode();

    doReturn(true).when(pass).shouldVisit(rootNode);
    doReturn(false).when(pass).shouldRetraverse(rootNode);

    pass.shouldRetraverse(rootNode);

    assertFalse(pass.shouldRetraverse(rootNode));
  }

  @Test
  public void testShouldRetraverse_scriptNodeIsNotRetraversed() {
    Node rootNode = createTestScriptNode();

    doReturn(true).when(pass).shouldVisit(rootNode);
    doReturn(false).when(pass).shouldRetraverse(rootNode);

    pass.shouldRetraverse(rootNode);

    assertFalse(pass.shouldRetraverse(rootNode));
  }

  @Test
  public void testExitNode_functionNodeIsVisited() {
    Node rootNode = createTestFunctionNode();

    pass.exitNode(rootNode);

    verifyNoMoreInteractions();
  }

  @Test
  public void testExitNode_scriptNodeIsVisited() {
    Node rootNode = createTestScriptNode();

    pass.exitNode(rootNode);

    verifyNoMoreInteractions();
  }

  @Test
  public void testVisit_functionNodeIsVisited() {
    Node rootNode = createTestFunctionNode();

    doReturn(true).when(pass).shouldVisit(rootNode);
    doReturn(true).when(pass).shouldRetraverse(rootNode);

    pass.visit(rootNode);

    verify(pass.peepholeOptimizations[0]).optimizeSubtree(rootNode);
    verify(pass.peepholeOptimizations[1]).optimizeSubtree(rootNode);

    verifyNoMoreInteractions();
  }

  @Test
  public void testVisit_scriptNodeIsVisited() {
    Node rootNode = createTestScriptNode();

    doReturn(true).when(pass).shouldVisit(rootNode);
    doReturn(true).when(pass).shouldRetraverse(rootNode);

    pass.visit(rootNode);

    verify(pass.peepholeOptimizations[0]).optimizeSubtree(rootNode);
    verify(pass.peepholeOptimizations[1]).optimizeSubtree(rootNode);

    verifyNoMoreInteractions();
  }

  private Node createTestNode() {
    Node testNode = new Node();
    testNode.setType(Node.NOSUCHNODE);
    testNode.setLine(1);
    testNode.setColumn(1);
    return testNode;
  }

  private Node createTestFunctionNode() {
    Node testNode = new Node();
    testNode.setType(Node.FUNCTION);
    testNode.setLine(1);
    testNode.setColumn(1);
    testNode.setScript(compileTestSource("function test() {}"));
    return testNode;
  }

  private Node createTestScriptNode() {
    Node testNode = new Node();
    testNode.setType(Node.SCRIPT);
    testNode.setLine(1);
    testNode.setColumn(1);
    testNode.setScript(compileTestSource("test()"));
    return testNode;
  }

  private String compileTestSource(String source) {
    Node externs = new Node();
    Node root = new Node();
    pass.process(externs, root);
    compiler.compile(externs, source, root);
    return root.toString();
  }
}