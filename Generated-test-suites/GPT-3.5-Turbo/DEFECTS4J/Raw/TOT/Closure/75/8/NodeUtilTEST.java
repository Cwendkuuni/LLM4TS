import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_typicalCase() {
        Node forNode = createForInNode();
        assertTrue(nodeUtil.isForIn(forNode));
    }

    @Test
    public void testIsForIn_notForInNode() {
        Node dummyNode = createDummyNode();
        assertFalse(nodeUtil.isForIn(dummyNode));
    }

    @Test
    public void testIsForIn_incompleteForNode() {
        Node incompleteForNode = createIncompleteForNode();
        assertFalse(nodeUtil.isForIn(incompleteForNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = createStatementNode();
        assertTrue(nodeUtil.isStatement(statementNode));
    }

    @Test
    public void testIsStatement_notStatementNode() {
        Node expressionNode = createExpressionNode();
        assertFalse(nodeUtil.isStatement(expressionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclNode = createFunctionDeclarationNode();
        assertTrue(nodeUtil.isFunctionDeclaration(functionDeclNode));
    }

    @Test
    public void testIsFunctionDeclaration_notFunctionDeclNode() {
        Node expressionNode = createExpressionNode();
        assertFalse(nodeUtil.isFunctionDeclaration(expressionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclNode = createHoistedFunctionDeclarationNode();
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclNode() {
        Node functionDeclNode = createFunctionDeclarationNode();
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(functionDeclNode));
    }

    private Node createForInNode() {
        Node forNode = new Node(Token.FOR);
        Node child1 = new Node(Token.ADD);
        Node child2 = new Node(Token.SUB);
        Node child3 = new Node(Token.MUL);
        forNode.addChildToBack(child1);
        forNode.addChildToBack(child2);
        forNode.addChildToBack(child3);
        return forNode;
    }

    private Node createDummyNode() {
        Node dummyNode = new Node(Token.VAR);
        return dummyNode;
    }

    private Node createIncompleteForNode() {
        Node forNode = new Node(Token.FOR);
        Node child1 = new Node(Token.ADD);
        Node child2 = new Node(Token.SUB);
        forNode.addChildToBack(child1);
        forNode.addChildToBack(child2);
        return forNode;
    }

    private Node createStatementNode() {
        Node statementNode = new Node(Token.SCRIPT);
        Node child = new Node(Token.ADD);
        statementNode.addChildToBack(child);
        return statementNode;
    }

    private Node createExpressionNode() {
        Node expressionNode = new Node(Token.BLOCK);
        Node child = new Node(Token.ADD);
        expressionNode.addChildToBack(child);
        return expressionNode;
    }

    private Node createFunctionDeclarationNode() {
        Node functionDeclNode = new Node(Token.FUNCTION);
        Node child = new Node(Token.ADD);
        functionDeclNode.addChildToBack(child);
        return functionDeclNode;
    }

    private Node createHoistedFunctionDeclarationNode() {
        Node functionDeclNode = new Node(Token.FUNCTION);
        Node child = new Node(Token.ADD);
        functionDeclNode.addChildToBack(child);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionDeclNode);
        return scriptNode;
    }
}