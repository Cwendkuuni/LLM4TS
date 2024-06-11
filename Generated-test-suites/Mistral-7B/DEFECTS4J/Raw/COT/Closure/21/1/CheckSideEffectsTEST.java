import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;

import java.util.List;

public class CheckSideEffectsTest {

  @org.junit.Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    AbstractCompiler compiler = new CompilerOptions().getCompiler();
    CompilerInput input = new CompilerInput(null, null, null, null, null, null);
    checkSideEffects.process(input, null);
  }

  @org.junit.Test
  public void testVisitVoidNode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node voidNode = IR.voidExpr();
    root.addChild(voidNode);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("Void node should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVisitNoOpStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node noOpStatement = IR.statement(IR.expressionStatement(IR.voidExpr()));
    root.addChild(noOpStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("No-op statement should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVisitStringConcatenation() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node string1 = IR.stringLiteral("this string is ");
    Node string2 = IR.stringLiteral("continued on the next line but you forgot the +");
    Node concatenation = IR.binaryOperator(Token.PLUS, string1, string2);
    root.addChild(concatenation);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertTrue("String concatenation should be flagged as a problem node", checkSideEffects.problemNodes.size() > 0);
  }

  @org.junit.Test
  public void testVisitAssignment() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node variable = IR.variable("x");
    Node assignment = IR.assignment(variable, IR.name("foo"));
    root.addChild(assignment);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("Assignment should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVisitFunctionCallWithEmptyArgumentList() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node functionCall = IR.call(IR.name("foo"));
    root.addChild(functionCall);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("Function call with empty argument list should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVisitFunctionCallWithSingleArgument() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node argument = IR.number(42);
    Node functionCall = IR.call(IR.name("foo"), argument);
    root.addChild(functionCall);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("Function call with single argument should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVisitSimpleOperator() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node leftOperand = IR.number(5);
    Node operator = IR.binaryOperator(Token.MINUS, IR.name("x"), leftOperand);
    root.addChild(operator);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("Simple operator should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVisitStatementInsideForLoop() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node root = IR.block();
    Node forStatement = IR.forInit(IR.list(IR.name("i"), IR.set("i", IR.number(0))),
        IR.block(IR.expressionStatement(IR.assignment(IR.name("i"), IR.number(1))),
            IR.statement(IR.expressionStatement(IR.voidExpr()))),
        IR.forCondition(IR.relation(Token.LESS_EQUAL, IR.name("i"), IR.number(3))),
        IR.forUpdate(IR.expressionStatement(IR.assignment(IR.name("i"), IR.binaryOperator(Token.INCREMENT, IR.name("i"))))));
    root.addChild(forStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null);
    nodeTraversal.traverse(checkSideEffects, root, null);

    assertFalse("Statement inside for loop should not be flagged as a problem node", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    checkSideEffects.process(null, null);

    Node root = checkSideEffects.problemNodes.get(0);
    assertNotNull("Protected node should not be null", root);
    Node replacement = checkSideEffects.protectSideEffects();
    assertNotNull("Replacement node should not be null", replacement);
  }

  @org.junit.Test
  public void testRemoveSideEffectsProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    checkSideEffects.process(null, null);

    StripProtection stripProtection = new StripProtection(null);
    stripProtection.process(null, null);

    Node root = checkSideEffects.problemNodes.get(0);
    assertNotNull("Protected node should not be null", root);
    Node replacement = IR.expressionStatement(root);
    assertNull("Replacement node should be null after stripping protection", replacement.getFirstChild());
  }
}