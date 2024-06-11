import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_GlobalContext_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = check.shouldTraverse(new NodeTraversal(), new Node(Token.EMPTY), new Node(Token.SCRIPT));
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ConstructorFunction_ReturnsFalse() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        functionNode.setJSDocInfo(jsDoc);
        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, new Node(Token.SCRIPT));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType("MyObject");
        functionNode.setJSDocInfo(jsDoc);
        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, new Node(Token.SCRIPT));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionsWithNoLogicalPlaceForThisAnnotation_ReturnsFalse() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.EMPTY);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideBlock_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node blockNode = new Node(Token.BLOCK);
        Node parentNode = new Node(Token.EMPTY).addChildToBack(blockNode).addChildToBack(functionNode);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideScript_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node parentNode = new Node(Token.EMPTY).addChildToBack(scriptNode).addChildToBack(functionNode);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideName_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        Node parentNode = new Node(Token.EMPTY).addChildToBack(nameNode).addChildToBack(functionNode);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideAssign_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node assignNode = new Node(Token.ASSIGN);
        Node parentNode = new Node(Token.EMPTY).addChildToBack(assignNode).addChildToBack(functionNode);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideStringKey_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node stringNode = new Node(Token.STRING);
        Node parentNode = new Node(Token.EMPTY).addChildToBack(stringNode).addChildToBack(functionNode);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideNumberKey_ReturnsTrue() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node functionNode = new Node(Token.FUNCTION);
        Node numberNode = new Node(Token.NUMBER);
        Node parentNode = new Node(Token.EMPTY).addChildToBack(numberNode).addChildToBack(functionNode);

        boolean result = check.shouldTraverse(new NodeTraversal(), functionNode, parentNode);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisNode_ShouldReportError() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        check.visit(new NodeTraversal(), thisNode, parent);
        
        // Check that an error message was reported
        // TODO: Add assertion
    }

    @Test
    public void testVisit_ThisNodeInsideAssignLhsChild_DoNotReportError() {
        CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.EMPTY);

        check.assignLhsChild = new Node(Token.THIS);

        check.visit(new NodeTraversal(), thisNode, parent);
        
        // Check that no error message was reported
        // TODO: Add assertion
    }
}