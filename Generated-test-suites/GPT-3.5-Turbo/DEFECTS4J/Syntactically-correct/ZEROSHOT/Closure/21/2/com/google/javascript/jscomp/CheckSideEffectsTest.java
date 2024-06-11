import com.google.javascript.jscomp.*;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node externs = new Node(Token.EXTERN, new Node(Token.EMPTY));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    checkSideEffects.process(externs, root);
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node originalRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);
    checkSideEffects.visit(traversal, n, parent);
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    checkSideEffects.protectSideEffects();
  }

  @Test
  public void testAddExtern() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    checkSideEffects.addExtern();
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    Node externs = new Node(Token.EXTERN, new Node(Token.EMPTY));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    stripProtection.process(externs, root);
  }
}