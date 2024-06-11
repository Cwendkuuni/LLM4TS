import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChildren( new Node[]{ new Node(Token.IDENTIFIER, "forVariable"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENTIFIER, "collection") } );
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.FOR);
    nodeNotForIn.setChildren( new Node[]{ new Node(Token.IDENTIFIER, "forVariable"), new Node(Token.KEYWORD, "if"), new Node(Token.BOOLEAN_LITERAL, true)} );
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF);
    Node parentScript = new Node(Token.SCRIPT);
    parentScript.setChildren( new Node[]{ nodeStatement } );
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, null, new Node[]{ new Node(Token.IDENTIFIER, "myFunction") });
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunctionName", new Node[]{ new Node(Token.IDENTIFIER, "arg1"), new Node(Token.IDENTIFIER, "arg2") } );
    nodeFunctionDeclaration.setParent( new Node(Token.BLOCK) );
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, new Node[]{ new Node(Token.IDENTIFIER, "myFunctionName"), new Node(Token.IDENTIFIER, "arg1") } );
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunctionName", new Node[]{ new Node(Token.IDENTIFIER, "arg1"), new Node(Token.IDENTIFIER, "arg2") } );
    nodeFunctionDeclaration.setParent( new Node(Token.SCRIPT) );
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "outerFunction", new Node[]{ new Node(Token.BLOCK, new Node[]{ nodeFunctionDeclaration }) } );
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}