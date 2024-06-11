import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class CheckSideEffectsTest {
  
  @Test
  public void testProcess_ValidInput_CorrectWarning() {
    StringBuilder warning = new StringBuilder();
    DiagnosticType expectedDiagnosticType = CheckSideEffects.USELESS_CODE_ERROR;
    CheckLevel expectedLevel = CheckLevel.WARNING;
    AbstractCompiler compiler = new Compiler();
    Node externs = new Node();
    Node root = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, expectedLevel, false);
    checkSideEffects.process(externs, root);
    for (Diagnostic warning : compiler.getWarnings()) {
      if (warning.getType().equals(expectedDiagnosticType)) {
        warning.appendToStringBuilder(warnings);
        break;
      }
    }
    assertTrue(warning.toString().contains("This code lacks side-effects."));
    assertEquals(expectedLevel, compiler.getWarningLevel(expectedDiagnosticType));
  }
  
  @Test
  public void testHotSwapScript_ValidInput_CorrectWarning() {
    StringBuilder warning = new StringBuilder();
    DiagnosticType expectedDiagnosticType = CheckSideEffects.USELESS_CODE_ERROR;
    CheckLevel expectedLevel = CheckLevel.WARNING;
    AbstractCompiler compiler = new Compiler();
    Node scriptRoot = new Node();
    Node originalRoot = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, expectedLevel, false);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    for (Diagnostic warning : compiler.getWarnings()) {
      if (warning.getType().equals(expectedDiagnosticType)) {
        warning.appendToStringBuilder(warnings);
        break;
      }
    }
    assertTrue(warning.toString().contains("This code lacks side-effects."));
    assertEquals(expectedLevel, compiler.getWarningLevel(expectedDiagnosticType));
  }
  
  @Test
  public void testVisit_ResultUsed_Ignore() {
    StringBuilder warning = new StringBuilder();
    DiagnosticType expectedDiagnosticType = CheckSideEffects.USELESS_CODE_ERROR;
    CheckLevel expectedLevel = CheckLevel.WARNING;
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal();
    Node n = new Node(Token.CALL);
    Node parent = new Node(Token.EXPR_RESULT);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, expectedLevel, false);
    checkSideEffects.visit(traversal, n, parent);
    for (Diagnostic warning : compiler.getWarnings()) {
      if (warning.getType().equals(expectedDiagnosticType)) {
        warning.appendToStringBuilder(warnings);
        break;
      }
    }
    assertFalse(warning.toString().contains("This code lacks side-effects."));
    assertEquals(expectedLevel, compiler.getWarningLevel(expectedDiagnosticType));
  }
  
  @Test
  public void testVisit_NotResultUsed_Warning() {
    StringBuilder warning = new StringBuilder();
    DiagnosticType expectedDiagnosticType = CheckSideEffects.USELESS_CODE_ERROR;
    CheckLevel expectedLevel = CheckLevel.WARNING;
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal();
    Node n = new Node(Token.NEG);
    n.addChildToFront(IR.number(5));
    Node parent = new Node(Token.FOR);
    parent.addChildToFront(n);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, expectedLevel, false);
    checkSideEffects.visit(traversal, n, parent);
    for (Diagnostic warning : compiler.getWarnings()) {
      if (warning.getType().equals(expectedDiagnosticType)) {
        warning.appendToStringBuilder(warnings);
        break;
      }
    }
    assertTrue(warning.toString().contains("The result of the 'neg' operator is not being used."));
    assertEquals(expectedLevel, compiler.getWarningLevel(expectedDiagnosticType));
  }
  
  @Test
  public void testVisit_NotSimpleOp_Warning() {
    StringBuilder warning = new StringBuilder();
    DiagnosticType expectedDiagnosticType = CheckSideEffects.USELESS_CODE_ERROR;
    CheckLevel expectedLevel = CheckLevel.WARNING;
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    n.addChildToFront(IR.name("obj"));
    n.addChildToFront(IR.string("property"));
    Node parent = new Node(Token.EXPR_RESULT);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, expectedLevel, false);
    checkSideEffects.visit(traversal, n, parent);
    for (Diagnostic warning : compiler.getWarnings()) {
      if (warning.getType().equals(expectedDiagnosticType)) {
        warning.appendToStringBuilder(warnings);
        break;
      }
    }
    assertTrue(warning.toString().contains("This code lacks side-effects."));
    assertEquals(expectedLevel, compiler.getWarningLevel(expectedDiagnosticType));
  }
  
  @Test
  public void testProtectSideEffects_NotEmptyList_ChangesMade() {
    List<Node> problemNodes = new ArrayList<>();
    Node n = IR.exprResult(IR.string("test"));
    problemNodes.add(n);
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.protectSideEffects();
    assertTrue(n.getParent() instanceof IR.call);
  }
  
  @Test
  public void testProtectSideEffects_EmptyList_NoChangesMade() {
    List<Node> problemNodes = new ArrayList<>();
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.protectSideEffects();
    assertFalse(problemNodes.contains(IR.call));
  }
  
  @Test
  public void testProtectSideEffects_NotEmptyList_CompilerReportsCodeChange() {
    List<Node> problemNodes = new ArrayList<>();
    problemNodes.add(IR.nonterminal(Token.ADD));
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.protectSideEffects();
    assertTrue(copiler.getReportCodeChange());
  }
  
  @Test
  public void testStripProtection_LongMethodName_CorrectRemoval() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal();
    Node n = IR.call(IR.name(CheckSideEffects.PROTECTOR_FN), IR.trueNode());
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);
    CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compiler);
    sp.visit(traversal, n, parent);
    assertEquals(IR.trueNode(), parent);
  }
  
  @Test
  public void testStripProtection_ShortMethodName_CorrectRemoval() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal();
    Node n = IR.call(IR.name("foo"), IR.trueNode());
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);
    CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compiler);
    sp.visit(traversal, n, parent);
    assertEquals(IR.call, parent);
  }
  
  @Test
  public void testStripProtection_NameDifferentFromProtector_CorrectRemoval() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal();
    Node n = IR.call(IR.name("bar"), IR.trueNode());
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);
    CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compiler);
    sp.visit(traversal, n, parent);
    assertEquals(IR.call, parent);
  }
}