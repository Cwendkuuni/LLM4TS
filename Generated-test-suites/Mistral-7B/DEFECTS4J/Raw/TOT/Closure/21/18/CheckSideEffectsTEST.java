import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckSideEffects checker;
  private CompilerPassManager passManager;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    level = CheckLevel.WARNING;
    compiler = mock(Compiler.class);
    CheckerFactory checkerFactory = mock(CheckerFactory.class);
    when(checkerFactory.getCheck(CheckSideEffects.class.getName())).thenReturn(checker = new CheckSideEffects(compiler, level, false));
    when(compiler.getCheckFactory()).thenReturn(checkerFactory);
    when(compiler.getIntermediateCode()).thenReturn(IR.createEmptyAST());
    passManager = new CompilerPassManager(compiler);
    traversal = new NodeTraversal(passManager, null);
  }

  @Test
  public void testEmptyStatement() {
    Node root = IR.block();
    Node emptyStatement = IR.exprStatement(IR.noopExpression());
    root.addChildToBack(emptyStatement);

    checker.process(null, root);
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testSimpleStatementWithSideEffect() {
    Node root = IR.block();
    Node variableDeclaration = IR.variableDeclarationList(
        IR.variableDeclaration("int", "x", null, IR.numberLiteral(5)),
        IR.variableDeclaration("int", "y", null, IR.numberLiteral(10))
    );
    Node assignment = IR.assignment(IR.name("x"), IR.name("y"));
    root.addChildToBack(variableDeclaration);
    root.addChildToBack(assignment);

    checker.process(null, root);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals("Assignment expression 'x = y' has side-effects.",
        checker.compiler.getError(problemNode).getMessage());
  }

  @Test
  public void testExpressionStatementWithSideEffect() {
    Node root = IR.block();
    Node expressionStatement = IR.exprStatement(IR.logicalNotExpression(IR.numberLiteral(false)));
    root.addChildToBack(expressionStatement);

    checker.process(null, root);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals("Logical NOT expression lacks side-effects.",
        checker.compiler.getError(problemNode).getMessage());
  }

  @Test
  public void testStatementWithSideEffectInsideIfStatement() {
    Node root = IR.block();
    Node ifStatement = IR.ifStatement(IR.literal(true), IR.block(), IR.block());
    Node assignment = IR.assignment(IR.name("x"), IR.numberLiteral(5));
    ifStatement.getThenPart().addChildToBack(assignment);
    root.addChildToBack(ifStatement);

    checker.process(null, root);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals("Assignment expression 'x = 5' has side-effects.",
        checker.compiler.getError(problemNode).getMessage());
  }

  @Test
  public void testExpressionWithSideEffect() {
    Node root = IR.block();
    Node expression = IR.prefixExpression("!", IR.numberLiteral(true));
    root.addChildToBack(expression);

    checker.process(null, root);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals("Logical NOT expression lacks side-effects.",
        checker.compiler.getError(problemNode).getMessage());
  }

  @Test
  public void testStatementWithoutSideEffect() {
    Node root = IR.block();
    Node variableDeclaration = IR.variableDeclarationList(
        IR.variableDeclaration("int", "x", null, IR.numberLiteral(5)),
        IR.variableDeclaration("int", "y", null, IR.numberLiteral(10))
    );
    Node assignment = IR.assignment(IR.name("x"), IR.name("y"));
    Node ifStatement = IR.ifStatement(IR.literal(false), IR.block(), IR.block());
    Node ifStatementAssignment = IR.assignment(IR.name("y"), IR.numberLiteral(20));
    ifStatement.getElsePart().addChildToBack(ifStatementAssignment);
    root.addChildToBack(variableDeclaration);
    root.addChildToBack(assignment);
    root.addChildToBack(ifStatement);

    checker.process(null, root);
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testStatementWithSideEffectProtected() {
    Node root = IR.block();
    Node variableDeclaration = IR.variableDeclarationList(
        IR.variableDeclaration("int", "x", null, IR.numberLiteral(5)),
        IR.variableDeclaration("int", "y", null, IR.numberLiteral(10))
    );
    Node assignment = IR.assignment(IR.name("x"), IR.name("y"));
    Node callToProtectorFunction = IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.list(assignment));
    root.addChildToBack(variableDeclaration);
    root.addChildToBack(callToProtectorFunction);

    CheckSideEffects checker = new CheckSideEffects(compiler, level, true);
    checker.process(null, root);
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testStatementWithSideEffectProtectedAndReported() {
    Node root = IR.block();
    Node variableDeclaration = IR.variableDeclarationList(
        IR.variableDeclaration("int", "x", null, IR.numberLiteral(5)),
        IR.variableDeclaration("int", "y", null, IR.numberLiteral(10))
    );
    Node assignment = IR.assignment(IR.name("x"), IR.name("y"));
    Node callToProtectorFunction = IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.list(assignment));
    root.addChildToBack(variableDeclaration);
    root.addChildToBack(assignment);
    root.addChildToBack(callToProtectorFunction);

    checker.process(null, root);
    assertEquals(0, checker.problemNodes.size());

    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, root);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals("Assignment expression 'x = y' has side-effects.",
        checker.compiler.getError(problemNode).getMessage());
  }
}