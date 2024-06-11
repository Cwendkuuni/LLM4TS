import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_WithNoSideEffects_ShouldNotReportErrors() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    checkSideEffects.process(null, null);
    
    Mockito.verify(compiler, Mockito.never()).report(Mockito.any(), Mockito.eq(level), Mockito.any(DiagnosticType.class), Mockito.any());
  }
  
  @Test
  public void testProcess_WithSideEffects_ShouldReportErrors() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    
    Node node = new Node();
    node.setType(Token.EXPR_RESULT);
    node.setJSDocInfo(null);
    
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    parent.addChildToBack(node);
    
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    checkSideEffects.visit(null, node, parent);
    
    Mockito.verify(compiler, Mockito.times(1)).report(Mockito.any(), Mockito.eq(level), Mockito.any(DiagnosticType.class), Mockito.any());
  }
  
  @Test
  public void testProtectSideEffects_WithProblemNodes_ShouldProtectSideEffects() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    
    Node node = new Node();
    node.setType(Token.ADD);
    
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    parent.addChildToBack(node);
    
    List<Node> problemNodes = new ArrayList<>();
    problemNodes.add(node);
    
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    checkSideEffects.setProblemNodes(problemNodes);
    
    checkSideEffects.protectSideEffects();
    
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    assertEquals(node.getNext(), node.getFirstChild());
  }
  
  @Test
  public void testProtectSideEffects_WithNoProblemNodes_ShouldNotProtectSideEffects() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    
    List<Node> problemNodes = new ArrayList<>();
    
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    checkSideEffects.setProblemNodes(problemNodes);
    
    checkSideEffects.protectSideEffects();
    
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }
  
  @Test
  public void testAddExtern_ShouldAddExtern() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.addExtern();
    
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
  }
  
  @Test
  public void testStripProtection_ShouldStripProtection() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    
    Node node = new Node();
    node.setType(Token.CALL);
    
    Node target = new Node();
    target.setType(Token.NAME);
    target.setString("JSCOMPILER_PRESERVE");
    
    Node expr = new Node();
    
    node.addChildToBack(target);
    node.addChildToBack(expr);
    
    Node parent = new Node();
    parent.addChildToBack(node);
    
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.visit(null, node, parent);
    
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    assertEquals(expr, parent.getFirstChild());
  }

}