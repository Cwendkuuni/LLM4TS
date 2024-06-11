import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Test case 1: Non side effecting statement
    Compiler compiler1 = new Compiler();
    CheckLevel level1 = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode1 = false;
    CheckSideEffects cse1 = new CheckSideEffects(compiler1, level1, protectSideEffectFreeCode1);
    Node externs1 = new Node(Token.EXTERN_NAME);
    Node root1 = new Node(Token.ROOT);

    Node varNode1 = new Node(Token.VAR);
    Node lhs1 = IR.name("x");
    Node rhs1 = IR.number(5);
    varNode1.addChildToBack(lhs1);
    lhs1.addChildToBack(rhs1);

    root1.addChildToFront(varNode1);

    cse1.process(externs1, root1);

    assertEquals(0, compiler1.getErrors().length);
    assertEquals(0, compiler1.getWarnings().length);
    assertEquals(0, compiler1.getInjected());

    // Test case 2: Non side effecting statement with missing '+'
    Compiler compiler2 = new Compiler();
    CheckLevel level2 = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode2 = false;
    CheckSideEffects cse2 = new CheckSideEffects(compiler2, level2, protectSideEffectFreeCode2);
    Node externs2 = new Node(Token.EXTERN_NAME);
    Node root2 = new Node(Token.ROOT);

    Node varNode2 = new Node(Token.VAR);
    Node lhs2 = IR.name("s");
    Node rhs2 = IR.string("this string is ")
                      .addChildToBack(IR.string("continued on the next line but you forgot the +"));
    varNode2.addChildToBack(lhs2);
    lhs2.addChildToBack(rhs2);

    root2.addChildToFront(varNode2);

    cse2.process(externs2, root2);

    assertEquals(0, compiler2.getErrors().length);
    assertEquals(1, compiler2.getWarnings().length);
    assertEquals(0, compiler2.getInjected());

    // Test case 3: Non side effecting simple operator statement
    Compiler compiler3 = new Compiler();
    CheckLevel level3 = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode3 = false;
    CheckSideEffects cse3 = new CheckSideEffects(compiler3, level3, protectSideEffectFreeCode3);
    Node externs3 = new Node(Token.EXTERN_NAME);
    Node root3 = new Node(Token.ROOT);

    Node exprNode3 = IR.name("x");
    Node opNode3 = new Node(Token.EQ);
    Node lhs3 = IR.name("x");
    Node rhs3 = IR.number(5);
    exprNode3.addChildToFront(opNode3);
    opNode3.addChildToFront(lhs3);
    opNode3.addChildToBack(rhs3);

    root3.addChildToFront(exprNode3);

    cse3.process(externs3, root3);

    assertEquals(0, compiler3.getErrors().length);
    assertEquals(1, compiler3.getWarnings().length);
    assertEquals(0, compiler3.getInjected());
  }

  @Test
  public void testHotSwapScript() {
    // Test case 1: Non side effecting statement
    Compiler compiler1 = new Compiler();
    CheckLevel level1 = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode1 = false;
    CheckSideEffects cse1 = new CheckSideEffects(compiler1, level1, protectSideEffectFreeCode1);
    Node scriptRoot1 = new Node(Token.ROOT);
    Node originalRoot1 = new Node(Token.ROOT);

    Node varNode1 = new Node(Token.VAR);
    Node lhs1 = IR.name("x");
    Node rhs1 = IR.number(5);
    varNode1.addChildToBack(lhs1);
    lhs1.addChildToBack(rhs1);

    scriptRoot1.addChildToFront(varNode1);

    cse1.hotSwapScript(scriptRoot1, originalRoot1);

    assertEquals(0, compiler1.getErrors().length);
    assertEquals(0, compiler1.getWarnings().length);
    assertEquals(0, compiler1.getInjected());

    // Test case 2: Non side effecting statement with missing '+'
    Compiler compiler2 = new Compiler();
    CheckLevel level2 = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode2 = false;
    CheckSideEffects cse2 = new CheckSideEffects(compiler2, level2, protectSideEffectFreeCode2);
    Node scriptRoot2 = new Node(Token.ROOT);
    Node originalRoot2 = new Node(Token.ROOT);

    Node varNode2 = new Node(Token.VAR);
    Node lhs2 = IR.name("s");
    Node rhs2 = IR.string("this string is ")
                      .addChildToBack(IR.string("continued on the next line but you forgot the +"));
    varNode2.addChildToBack(lhs2);
    lhs2.addChildToBack(rhs2);

    scriptRoot2.addChildToFront(varNode2);

    cse2.hotSwapScript(scriptRoot2, originalRoot2);

    assertEquals(0, compiler2.getErrors().length);
    assertEquals(1, compiler2.getWarnings().length);
    assertEquals(0, compiler2.getInjected());

    // Test case 3: Non side effecting simple operator statement
    Compiler compiler3 = new Compiler();
    CheckLevel level3 = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode3 = false;
    CheckSideEffects cse3 = new CheckSideEffects(compiler3, level3, protectSideEffectFreeCode3);
    Node scriptRoot3 = new Node(Token.ROOT);
    Node originalRoot3 = new Node(Token.ROOT);

    Node exprNode3 = IR.name("x");
    Node opNode3 = new Node(Token.EQ);
    Node lhs3 = IR.name("x");
    Node rhs3 = IR.number(5);
    exprNode3.addChildToFront(opNode3);
    opNode3.addChildToFront(lhs3);
    opNode3.addChildToBack(rhs3);

    scriptRoot3.addChildToFront(exprNode3);

    cse3.hotSwapScript(scriptRoot3, originalRoot3);

    assertEquals(0, compiler3.getErrors().length);
    assertEquals(1, compiler3.getWarnings().length);
    assertEquals(0, compiler3.getInjected());
  }

}