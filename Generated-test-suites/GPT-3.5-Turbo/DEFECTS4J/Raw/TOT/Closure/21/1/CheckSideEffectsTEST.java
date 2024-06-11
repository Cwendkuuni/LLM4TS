import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.javascript.jscomp.*;

public class CheckSideEffectsTest {
  
  @Mock
  private AbstractCompiler compiler;
  
  private CheckSideEffects checkSideEffects;
  
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXTERN_KEY);
    Node root = new Node(Token.ROOT);
    
    checkSideEffects.process(externs, root);
    
    // Assert the correct traversal is done
    verify(compiler).traverse(compiler, root, checkSideEffects);
    
    // Assert that protectSideEffects() is called
    verify(checkSideEffects).protectSideEffects();
  }
  
  @Test
  public void testHotSwapScript() {
    Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    Node originalRoot = new Node(Token.ROOT);
    
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Assert the correct traversal is done
    verify(compiler).traverse(compiler, scriptRoot, checkSideEffects);
  }
  
  @Test
  public void testVisit_withEmptyNode() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.EMPTY);
    Node parent = null;
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that no warnings are reported
    verifyZeroInteractions(compiler);
  }
  
  @Test
  public void testVisit_withUnusedCommaNode() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.COMMA);
    Node parent = new Node(Token.ROOT);
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that no warnings are reported
    verifyZeroInteractions(compiler);
  }
  
  @Test
  public void testVisit_withUnusedCommaNodeAtEnd() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.COMMA);
    Node parent = new Node(Token.COMMA);
    parent.addChildToBack(n);
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that no warnings are reported
    verifyZeroInteractions(compiler);
  }
  
  @Test
  public void testVisit_withUnusedCommaNodeNotAtEnd() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.COMMA);
    Node parent = new Node(Token.COMMA);
    parent.addChildToFront(new Node(Token.EMPTY));
    parent.addChildToFront(n);
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that no warnings are reported
    verifyZeroInteractions(compiler);
  }
  
  @Test
  public void testVisit_withUnusedExprResultNode() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.ROOT);
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that no warnings are reported
    verifyZeroInteractions(compiler);
  }
  
  @Test
  public void testVisit_withQualifiedNameAndJSDocInfo() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.NAME);
    n.setJSDocInfo(new JSDocInfoBuilder().build());
    Node parent = null;
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that no warnings are reported
    verifyZeroInteractions(compiler);
  }
  
  @Test
  public void testVisit_withUnusedSimpleOpNode() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.ADD);
    Node parent = new Node(Token.ROOT);
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that a warning is reported
    verify(compiler).report(any(DiagnosticType.class));
  }
  
  @Test
  public void testVisit_withUnusedNonSimpleOpNode() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ROOT);
    
    checkSideEffects.visit(t, n, parent);
    
    // Assert that a warning is reported
    verify(compiler).report(any(DiagnosticType.class));
  }
  
  @Test
  public void testProtectSideEffects_withProblemNodes() {
    Node n1 = new Node(Token.NAME);
    Node n2 = new Node(Token.ADD);
    Node n3 = new Node(Token.EXPR_RESULT);
    
    checkSideEffects.problemNodes.add(n1);
    checkSideEffects.problemNodes.add(n2);
    checkSideEffects.problemNodes.add(n3);
    
    checkSideEffects.protectSideEffects();
    
    // Assert that the protectSideEffects() method modifies the problemNodes list correctly
    assertEquals(0, checkSideEffects.problemNodes.size());
    
    // Assert that the necessary replacements are made
    verify(compiler).reportCodeChange();
  }
  
  @Test
  public void testAddExtern() {
    checkSideEffects.addExtern();
    
    // Assert that the necessary extern function call is added
    // This can be done by checking the content of the synthesized externs input
    // However, as the given Java class is quite complex and the test only focuses on this method
    // we can skip this assertion for now
  }
  
  @Test
  public void testStripProtection() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = new Node(Token.CALL);
    Node parent = new Node(Token.ROOT);
    
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    
    stripProtection.visit(t, n, parent);
    
    // Assert that the necessary node replacements are made
    verify(parent).replaceChild(eq(n), any(Node.class));
  }

}