import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newFor(Token.FOR, new Node[] {
      Node.newIdentifier("i"),
      Node.newExpression(Node.newNumber(0)),
      Node.newExpression(Node.newIdentifier("arr"))
    });
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression(Node.newFunction(Token.FUNCTION, null, new Node[] {
      Node.newIdentifier("x"), Node.newExpression(Node.newNumber(5))
    }));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression(Node.newNumber(5));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Token.FUNCTION, new Node[] {
        Node.newIdentifier("add"),
        Node.newIdentifier("a"),
        Node.newIdentifier("b")
    }, Node.newBlock(new Node[] {
        Node.newExpression(Node.newBinop(Token.PLUS, Node.newExpression(Node.newIdentifier("a")), Node.newExpression(Node.newIdentifier("b"))))
    }));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(Node.newFunction(Token.FUNCTION, null, new Node[] {
        Node.newIdentifier("x"), Node.newExpression(Node.newNumber(5))
    }));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(Token.FUNCTION, null, new Node[] {
        Node.newIdentifier("add")
    }, Node.newBlock(new Node[] {
        Node.newExpression(Node.newIdentifier("console.log"))
    }));
    Node scriptNode = Node.newBlock(new Node[] {
        nodeHoistedFunctionDeclaration
    });
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(Token.FUNCTION, null, new Node[] {
        Node.newIdentifier("add")
    }, Node.newBlock(new Node[] {
        Node.newExpression(Node.newIdentifier("console.log"))
    }));
    Node functionNode = Node.newExpression(Token.FUNCTION, null, new Node[] {
        Node.newIdentifier("x"), Node.newExpression(Node.newNumber(5))
    });
    Node blockNode = Node.newBlock(new Node[] {
        functionNode
    });
    Node scriptWithFunctionNode = Node.newBlock(new Node[] {
        blockNode
    });
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration)); // should be false since it's not in the scope root
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression.getParent()));
    assertFalse(isHoistedFunctionDeclaration(scriptWithFunctionNode));
  }
}