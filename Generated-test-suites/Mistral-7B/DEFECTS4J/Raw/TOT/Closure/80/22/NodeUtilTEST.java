import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newFor();
    Node forNodeWithThreeChildren = Node.newFor(Node.newIdentifier("i"), Node.newExpression(Node.newNumber(0)), Node.newStatementList());
    Node notForNode = Node.newVariable("notFor");
    Node notForNodeWithThreeChildren = Node.newExpression(Node.newIdentifier("notFor"), Node.newExpression(Node.newNumber(1)), Node.newStatementList());

    // Test valid FOR-IN loops
    assertTrue(isForIn(forNodeWithThreeChildren));
    assertTrue(isForIn(forNode));

    // Test invalid FOR-IN loops
    assertFalse(isForIn(notForNode));
    assertFalse(isForIn(notForNodeWithThreeChildren));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statement = Node.newExpression(Node.newIdentifier("x"), Node.newExpression(Node.newNumber(10)), Node.newExpression(Node.newIdentifier("y")));
    Node notStatement = Node.newExpression(Node.newIdentifier("x"), Node.newNumber(10), Node.newNumber(5));
    Node parentOfStatement = Node.newBlock(new Node[] {statement});
    Node parentOfNotStatement = Node.newExpression(Node.newIdentifier("x"), Node.newNumber(10), Node.newNumber(5));

    // Test valid statements
    assertTrue(isStatement(statement));
    assertTrue(isStatement(parentOfStatement));

    // Test invalid statements
    assertFalse(isStatement(notStatement));
    assertFalse(isStatement(parentOfNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction("myFunction", new Node[]{}, Node.newBlock(new Node[]{}));
    Node functionExpression = Node.newExpression(Node.newIdentifier("myFunction"), Node.newFunction(Node.newIdentifier("arg"), new Node[]{}, Node.newBlock(new Node[]{})));

    // Test valid function declarations
    assertTrue(isFunctionDeclaration(functionDeclaration));

    // Test invalid function declarations
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationInRootScope = Node.newFunction("myFunction", new Node[]{}, Node.newBlock(new Node[]{}), Node.newScript());
    Node functionDeclarationInFunctionScope = Node.newFunction("myFunction", new Node[]{}, Node.newBlock(new Node[]{}), Node.newExpression(Node.newIdentifier("myFunction"), Node.newFunction(Node.newIdentifier("arg"), new Node[]{}, Node.newBlock(new Node[]{}))));

    // Test valid hoisted function declarations
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInRootScope));

    // Test invalid hoisted function declarations
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunctionScope));
  }
}