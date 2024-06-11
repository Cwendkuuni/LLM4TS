import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

/**
 * Test class for CheckSideEffects.
 */
public class CheckSideEffectsTest {

  @org.junit.Test
  public void testCheckSideEffects() {
    // Set up mocks
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test empty tree
    testEmptyTree(checkSideEffects);

    // Test simple statements
    testSimpleStatements(checkSideEffects);

    // Test expressions and operators
    testExpressionsAndOperators(checkSideEffects);

    // Test for statements within for loops
    testForLoopStatements(checkSideEffects);

    // Test protectSideEffectFreeCode
    testProtectSideEffectFreeCode(checkSideEffects);

    // Test remove side-effect sync functions
    testRemoveSideEffectSyncFunctions(checkSideEffects);
  }

  private void testEmptyTree(CheckSideEffects checkSideEffects) {
    CompilerInput input = new CompilerInput(null, null, null);
    Node root = null;
    checkSideEffects.process(input, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  private void testSimpleStatements(CheckSideEffects checkSideEffects) {
    CompilerInput input = new CompilerInput(null, null, null);
    Node root = IR.block(
        IR.assign(IR.name("x"), IR.literal(1)),
        IR.assign(IR.name("y"), IR.literal(2))
    );
    checkSideEffects.process(input, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  private void testExpressionsAndOperators(CheckSideEffects checkSideEffects) {
    CompilerInput input = new CompilerInput(null, null, null);
    Node root = IR.block(
        IR.assign(IR.name("x"), IR.binaryOp(Token.PLUS, IR.literal(1), IR.literal(2))),
        IR.assign(IR.name("y"), IR.binaryOp(Token.MINUS, IR.name("x"), IR.literal(1)))
    );
    checkSideEffects.process(input, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  private void testForLoopStatements(CheckSideEffects checkSideEffects) {
    CompilerInput input = new CompilerInput(null, null, null);
    Node root = IR.block(
        IR.forInit(IR.var(IR.name("i")), IR.literal(0), IR.literal(5), null),
        IR.stmtList(
            IR.assign(IR.name("x"), IR.binaryOp(Token.PLUS, IR.name("i"), IR.literal(1))),
            IR.assign(IR.name("y"), IR.binaryOp(Token.MINUS, IR.name("x"), IR.literal(1)))
        ),
        IR.stmtList()
    );
    checkSideEffects.process(input, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  private void testProtectSideEffectFreeCode(CheckSideEffects checkSideEffects) {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffectsWithProtectSideEffectFreeCode = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    CompilerInput input = new CompilerInput(null, null, null);
    Node root = IR.block(
        IR.assign(IR.name("x"), IR.binaryOp(Token.PLUS, IR.literal(1), IR.literal(2)))
    );
    checkSideEffectsWithProtectSideEffectFreeCode.process(input, root);
    assertEquals(1, checkSideEffectsWithProtectSideEffectFreeCode.problemNodes.size());
    checkSideEffectsWithProtectSideEffectFreeCode.protectSideEffects();
    assertEquals(0, checkSideEffectsWithProtectSideEffectFreeCode.problemNodes.size());
  }

  private void testRemoveSideEffectSyncFunctions(CheckSideEffects checkSideEffects) {
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(mock(AbstractCompiler.class));
    CompilerInput input = new CompilerInput(null, null, null);
    Node root = IR.block(
        IR.assign(IR.name("x"), IR.call(IR.name("goog.reflect.sinkValue"), IR.name("x"), IR.literal(42))),
        IR.assign(IR.name("y"), IR.literal(5))
    );
    stripProtection.process(input, root);
    assertEquals(1, stripProtection.problemNodes.size());
    stripProtection.process(input, root);
    assertEquals(0, stripProtection.problemNodes.size());
  }
}