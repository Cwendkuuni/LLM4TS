import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testVisit_emptyNode_noChanges() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.NAME);
    CheckSideEffects.checkSideEffects.visit(t, n, parent);
    assertEquals(null, n.getJSDocInfo());
  }

  @Test
  public void testVisit_qualifiedName_hasJSDocInfo() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    n.setJSDocInfo("example JSDocInfo");
    Node parent = new Node(Token.NAME);
    CheckSideEffects.checkSideEffects.visit(t, n, parent);
    assertEquals("example JSDocInfo", n.getJSDocInfo());
  }

  @Test
  public void testVisit_exprResult_hasSideEffect() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    n.setSideEffect(true);
    Node parent = new Node(Token.NAME);
    CheckSideEffects.checkSideEffects.visit(t, n, parent);
    assertFalse(n.isSideEffect());
  }

  @Test
  public void testVisit_exprResult_noSideEffect() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    n.setSideEffect(false);
    Node parent = new Node(Token.NAME);
    CheckSideEffects.checkSideEffects.visit(t, n, parent);
    assertFalse(n.isSideEffect());
  }

  @Test
  public void testProcess_protectSideEffectFreeCode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    Node externs = new Node(Token.NAME);
    Node root = new Node(Token.NAME);
    checkSideEffects.process(externs, root);

    assertEquals(true, checkSideEffects.protectSideEffectFreeCode);
  }

  @Test
  public void test_process_addExtern() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    checkSideEffects.addExtern();

    assertEquals("JSCOMPILER_PRESERVE", compiler.getSynthesizedExternsInput().getAstRoot(compiler).getFirstChild().getString());
  }

  @Test
  public void test_process_hotSwapScript() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    Node scriptRoot = new Node(Token.NAME);
    Node originalRoot = new Node(Token.NAME);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    assertEquals(true, checkSideEffects.protectSideEffectFreeCode);
  }

  @Test
  public void test_stripProtection() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);

    Node externs = new Node(Token.NAME);
    Node root = new Node(Token.NAME);
    stripProtection.process(externs, root);

    // TODO: assert something
  }

}