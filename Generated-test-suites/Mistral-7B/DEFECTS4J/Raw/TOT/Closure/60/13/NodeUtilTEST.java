import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node validForInStatement;
  Node invalidForInStatement;
  Node validStatement;
  Node invalidStatement;
  Node validFunctionDeclaration;
  Node validHoistedFunctionDeclaration;
  Node invalidFunctionDeclaration;

  @org.junit.Before
  public void setup() {
    // Initialize test nodes here. These nodes should be properly configured
    // to represent valid and invalid FOR-IN statements, statements, function
    // declarations, and hoisted function declarations.

    validForInStatement = Node.newForInStatement();
    invalidForInStatement = Node.newForInStatement().setType(Token.IF);

    validStatement = Node.newStatement();
    invalidStatement = Node.newExpressionStatement(Node.newNumberLiteral(-1));

    validFunctionDeclaration = Node.newFunctionDeclaration("testFunction");
    validFunctionDeclaration.setBody(Node.newBlockStatement(Node.newExpressionStatement(Node.newPrintStatement(Node.newStringLiteral("Test")))));

    validHoistedFunctionDeclaration = Node.newFunctionDeclaration("testFunction");
    validHoistedFunctionDeclaration.setParent(Node.newScriptStatementList(Node.newReturnStatement(Node.newNumberLiteral(42))).getFirstChild());

    invalidFunctionDeclaration = Node.newFunctionExpression(Node.newIdentifier("testFunction"));
  }

  @org.junit.Test
  public void testIsForIn() {
    assertTrue(isForIn(validForInStatement));
    assertFalse(isForIn(validStatement));
    assertFalse(isForIn(invalidForInStatement));
    assertFalse(isForIn(invalidStatement));
    assertFalse(isForIn(validFunctionDeclaration));
    assertFalse(isForIn(validHoistedFunctionDeclaration));
    assertFalse(isForIn(invalidFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsStatement() {
    assertTrue(isStatement(validStatement));
    assertTrue(isStatement(validFunctionDeclaration));
    assertTrue(isStatement(validHoistedFunctionDeclaration));
    assertFalse(isStatement(validForInStatement));
    assertFalse(isStatement(invalidForInStatement));
    assertFalse(isStatement(invalidStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    assertTrue(isFunctionDeclaration(validFunctionDeclaration));
    assertTrue(isFunctionDeclaration(validHoistedFunctionDeclaration));
    assertFalse(isFunctionDeclaration(validStatement));
    assertFalse(isFunctionDeclaration(invalidStatement));
    assertFalse(isFunctionDeclaration(invalidFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    assertTrue(isHoistedFunctionDeclaration(validHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(validFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(validStatement));
    assertFalse(isHoistedFunctionDeclaration(invalidStatement));
    assertFalse(isHoistedFunctionDeclaration(invalidFunctionDeclaration));
  }
}