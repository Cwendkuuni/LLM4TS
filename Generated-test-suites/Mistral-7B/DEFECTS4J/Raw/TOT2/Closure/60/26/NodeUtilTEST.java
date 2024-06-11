import static org.junit.Assert.assertThat;
import static org.junit.matchers.jmock.TypeSafetyMatcher.isInstanceOf;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthMatcher.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import org.junit.Test;
import static org.junit.Assert.assertThatThrownBy;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node("for");
    Node forNodeWithThreeChildren = new Node("for", new Node("Identifier", "i"), new Node("Keyword", "in"), new Node("ExpressionStatement", new Node("Assignment", new Node("Identifier", "a"), new Node("Value", "1"))));

    TruthMatcher.assertThat(isForIn(forNode)).isFalse();
    TruthMatcher.assertThat(isForIn(forNodeWithThreeChildren)).isTrue();
  }

  @Test
  public void testIsForInNullNode() {
    TruthMatcher.assertThatThrownBy(() -> isForIn(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testIsForInNonFORNode() {
    Node nonFORNode = new Node("Identifier", "myVariable");
    TruthMatcher.assertThat(isForIn(nonFORNode)).isFalse();
  }

  @Test
  public void testIsForInNodeWithLessThanThreeChildren() {
    Node lessThanThreeChildrenNode = new Node("For", new Node("Identifier", "i"));
    TruthMatcher.assertThat(isForIn(lessThanThreeChildrenNode)).isFalse();
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node("PrintStatement", new Node("StringLiteral", "Hello, World!"));
    Node expressionNode = new Node("ExpressionStatement", new Node("Add", new Node("NumberLiteral", 5), new Node("NumberLiteral", 3)));

    TruthMatcher.assertThat(isStatement(statementNode)).isTrue();
    TruthMatcher.assertThat(isStatement(expressionNode)).isFalse();
  }

  @Test
  public void testIsStatementNullNode() {
    TruthMatcher.assertThatThrownBy(() -> isStatement(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testIsStatementNonStatementType() {
    Node nonStatementTypeNode = new Node("IfStatement");
    TruthMatcher.assertThat(isStatement(nonStatementTypeNode)).isFalse();
  }

  @Test
  public void testIsStatementNullParentNode() {
    Node nullParentNode = new Node("Identifier", "myVariable");
    TruthMatcher.assertThatThrownBy(() -> isStatement(nullParentNode)).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node("FunctionDeclaration", new Node("Identifier", "myFunction"), new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement"));
    Node functionExpressionNode = new Node("ExpressionStatement", new Node("FunctionExpression", new Node("Identifier", "myFunction"), new Node("Parameters", new Node("Identifier", "x")), new Node("Block", new Node("Statement", new Node("Identifier", "x").set("value", new Node("NumberLiteral", 5))), new Node("ReturnStatement", new Node("NumberLiteral", 10)))));

    TruthMatcher.assertThat(isFunctionDeclaration(functionDeclarationNode)).isTrue();
    TruthMatcher.assertThat(isFunctionDeclaration(functionExpressionNode)).isFalse();
  }

  @Test
  public void testIsFunctionDeclarationNullNode() {
    TruthMatcher.assertThatThrownBy(() -> isFunctionDeclaration(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testIsFunctionDeclarationNonFUNCTIONType() {
    Node nonFUNCTIONTypeNode = new Node("Identifier", "myVariable");
    TruthMatcher.assertThat(isFunctionDeclaration(nonFUNCTIONTypeNode)).isFalse();
  }

  @Test
  public void testIsFunctionDeclarationNoName() {
    Node noNameFunctionDeclarationNode = new Node("Function", new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement"));
    TruthMatcher.assertThat(isFunctionDeclaration(noNameFunctionDeclarationNode)).isFalse();
  }

  @Test
  public void testIsFunctionDeclarationNonStatementParentNode() {
    Node nonStatementParentNode = new Node("IfStatement", new Node("FunctionDeclaration", new Node("Identifier", "myFunction"), new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement")));
    TruthMatcher.assertThat(isFunctionDeclaration(nonStatementParentNode)).isFalse();
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node("FunctionDeclaration", new Node("Identifier", "myFunction"), new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement")).setParent(new Node("Script", new Node("Statement", hoistedFunctionDeclarationNode)));
    Node nonHoistedFunctionDeclarationNode = new Node("FunctionDeclaration", new Node("Identifier", "myFunction"), new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement")).setParent(new Node("ExpressionStatement", new Node("Assign", new Node("Identifier", "y"), new Node("NumberLiteral", 5))));

    TruthMatcher.assertThat(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode)).isTrue();
    TruthMatcher.assertThat(isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode)).isFalse();
  }

  @Test
  public void testIsHoistedFunctionDeclarationNullNode() {
    TruthMatcher.assertThatThrownBy(() -> isHoistedFunctionDeclaration(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testIsHoistedFunctionDeclarationNonFUNCTIONType() {
    Node nonFUNCTIONTypeNode = new Node("Identifier", "myVariable");
    TruthMatcher.assertThat(isHoistedFunctionDeclaration(nonFUNCTIONTypeNode)).isFalse();
  }

  @Test
  public void testIsHoistedFunctionDeclarationNoName() {
    Node noNameFunctionDeclarationNode = new Node("FunctionDeclaration", new Node("Identifier", "myFunction"), new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement")).setParent(new Node("ExpressionStatement", new Node("Assign", new Node("Identifier", "y"), new Node("NumberLiteral", 5))));
    TruthMatcher.assertThat(isHoistedFunctionDeclaration(noNameFunctionDeclarationNode)).isFalse();
  }

  @Test
  public void testIsHoistedFunctionDeclarationNonScriptOrFunctionParentNode() {
    Node nonScriptOrFunctionParentNode = new Node("PrintStatement", new Node("FunctionDeclaration", new Node("Identifier", "myFunction"), new Node("Block", new Node("Statement", new Node("Identifier", "x"))), new Node("EmptyStatement"))).setParent(new Node("Block", new Node("Statement", new Node("IfStatement"))));
    TruthMatcher.assertThat(isHoistedFunctionDeclaration(nonScriptOrFunctionParentNode)).isFalse();
  }
}