import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {
  
  @Mock
  private AbstractCompiler compiler;
  
  private PeepholeOptimizationsPass peepholeOptimizationsPass;
  
  private AbstractPeepholeOptimization mockOptimization;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    
    mockOptimization = Mockito.mock(AbstractPeepholeOptimization.class);
    
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, mockOptimization);
  }
  
  @Test
  public void process_ShouldSetChangeHandlerAndThenRemoveChangeHandler_WhenCalled() {
    Node externs = createMockNode();
    Node root = createMockNode();
    CodeChangeHandler mockHandler = Mockito.mock(CodeChangeHandler.class);
    
    Mockito.when(compiler.getChangeHandlers()).thenReturn(Lists.newArrayList(mockHandler));
    
    peepholeOptimizationsPass.process(externs, root);
    
    Mockito.verify(compiler).addChangeHandler(mockHandler);
    Mockito.verify(compiler).removeChangeHandler(mockHandler);
  }
  
  @Test
  public void process_ShouldCallBeginTraversal_WhenCalled() {
    Node externs = createMockNode();
    Node root = createMockNode();
    
    peepholeOptimizationsPass.process(externs, root);
    
    Mockito.verify(mockOptimization).beginTraversal(compiler);
  }
  
  @Test
  public void process_ShouldCallTraverseAndEndTraversal_WhenCalled() {
    Node externs = createMockNode();
    Node root = createMockNode();
    
    peepholeOptimizationsPass.process(externs, root);
    
    Mockito.verify(peepholeOptimizationsPass).traverse(root);
    Mockito.verify(mockOptimization).endTraversal(compiler);
  }
  
  @Test
  public void beginTraversal_ShouldCallBeginTraversalOnAllOptimizations_WhenCalled() {
    AbstractPeepholeOptimization mockOptimization1 = Mockito.mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization mockOptimization2 = Mockito.mock(AbstractPeepholeOptimization.class);
    
    PeepholeOptimizationsPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, 
                                                                                    mockOptimization1, 
                                                                                    mockOptimization2);
    
    peepholeOptimizationsPass.beginTraversal();
    
    Mockito.verify(mockOptimization1).beginTraversal(compiler);
    Mockito.verify(mockOptimization2).beginTraversal(compiler);
  }
  
  @Test
  public void endTraversal_ShouldCallEndTraversalOnAllOptimizations_WhenCalled() {
    AbstractPeepholeOptimization mockOptimization1 = Mockito.mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization mockOptimization2 = Mockito.mock(AbstractPeepholeOptimization.class);
    
    PeepholeOptimizationsPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, 
                                                                                    mockOptimization1, 
                                                                                    mockOptimization2);
    
    peepholeOptimizationsPass.endTraversal();
    
    Mockito.verify(mockOptimization1).endTraversal(compiler);
    Mockito.verify(mockOptimization2).endTraversal(compiler);
  }
  
  @Test
  public void traverse_ShouldNotCallVisitOrExitNode_WhenShouldVisitIsFalse() {
    Node mockNode = createMockNode();
    
    Mockito.when(mockNode.isFunction()).thenReturn(true);
    Mockito.when(peepholeOptimizationsPass.shouldVisit(mockNode)).thenReturn(false);
    
    peepholeOptimizationsPass.traverse(mockNode);
    
    Mockito.verify(peepholeOptimizationsPass, Mockito.never()).visit(mockNode);
    Mockito.verify(peepholeOptimizationsPass, Mockito.never()).exitNode(mockNode);
  }
  
  @Test
  public void traverse_ShouldCallVisitAndExitNode_WhenShouldVisitIsTrueAndShouldRetraverseIsFalse() {
    Node mockNode = createMockNode();
    
    Mockito.when(mockNode.isFunction()).thenReturn(true);
    Mockito.when(peepholeOptimizationsPass.shouldVisit(mockNode)).thenReturn(true);
    Mockito.when(peepholeOptimizationsPass.shouldRetraverse(mockNode)).thenReturn(false);
    
    peepholeOptimizationsPass.traverse(mockNode);
    
    Mockito.verify(peepholeOptimizationsPass).visit(mockNode);
    Mockito.verify(peepholeOptimizationsPass).exitNode(mockNode);
  }
  
  @Test
  public void traverse_ShouldCallVisitSeveralTimesAndExitNode_WhenShouldVisitIsTrueAndShouldRetraverseIsTrue() {
    Node mockNode = createMockNode();
    Node childNode1 = createMockNode();
    Node childNode2 = createMockNode();
    
    Mockito.when(mockNode.isFunction()).thenReturn(true);
    Mockito.when(peepholeOptimizationsPass.shouldVisit(mockNode)).thenReturn(true);
    Mockito.when(peepholeOptimizationsPass.shouldRetraverse(mockNode)).thenReturn(true, true, false);
    
    mockNode.addChildToFront(childNode1);
    mockNode.addChildToFront(childNode2);
    
    peepholeOptimizationsPass.traverse(mockNode);
    
    Mockito.verify(peepholeOptimizationsPass, Mockito.times(3)).visit(Mockito.any(Node.class));
    Mockito.verify(peepholeOptimizationsPass, Mockito.times(1)).visit(childNode1);
    Mockito.verify(peepholeOptimizationsPass, Mockito.times(1)).visit(childNode2);
    Mockito.verify(peepholeOptimizationsPass, Mockito.times(3)).exitNode(Mockito.any(Node.class));
    Mockito.verify(peepholeOptimizationsPass, Mockito.times(1)).exitNode(childNode1);
    Mockito.verify(peepholeOptimizationsPass, Mockito.times(1)).exitNode(childNode2);
  }
  
  @Test
  public void visit_ShouldCallOptimizeSubtreeOnAllOptimizationsUntilNoChangeOccurs_WhenCalled() {
    Node mockNode = createMockNode();
    Node childNode1 = createMockNode();
    Node childNode2 = createMockNode();
    Node childNode3 = createMockNode();
    
    Mockito.when(mockOptimization.optimizeSubtree(mockNode)).thenReturn(childNode1, childNode2, childNode3, mockNode);
    
    peepholeOptimizationsPass.visit(mockNode);
    
    Mockito.verify(mockOptimization).optimizeSubtree(mockNode);
    Mockito.verify(mockOptimization).optimizeSubtree(childNode1);
    Mockito.verify(mockOptimization).optimizeSubtree(childNode2);
    Mockito.verify(mockOptimization).optimizeSubtree(childNode3);
  }
  
  private Node createMockNode() {
    return Mockito.mock(Node.class);
  }
}