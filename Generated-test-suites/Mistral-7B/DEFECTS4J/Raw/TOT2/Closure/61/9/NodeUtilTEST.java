import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node("for in");
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_DifferentType() {
        Node node = new Node("while loop");
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_DifferentNumberOfChildren() {
        Node node = new Node("for loop with 4 children");
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node("statement");
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_PartOfExpression() {
        Node node = new Node("expression statement");
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_ParentIsNull() {
        Node node = new Node("node without a parent");
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node("function declaration");
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node node = new Node("if statement");
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_ParentIsNotStatement() {
        Node functionNode = new Node("function declaration");
        Node expressionNode = new Node("expression node");
        expressionNode.addChild(functionNode);
        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node("function declaration in the root scope");
        Node scriptNode = new Node("script node");
        scriptNode.addChild(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node functionNode = new Node("function declaration inside a function");
        Node functionParentNode = new Node("function node");
        functionParentNode.addChild(functionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_ParentIsNotScriptOrFunction() {
        Node functionNode = new Node("function declaration in the root scope");
        Node blockNode = new Node("block node");
        blockNode.addChild(functionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}