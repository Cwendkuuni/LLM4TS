import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverseWithFunctionConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverseWithFunctionThisAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverseWithRegularFunction() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
        assertTrue(result);
    }
    
    @Test
    public void shouldTraverseWithAssignmentOnLeftSide() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node parentNode = new Node(Token.ASSIGN);
        Node lhsChild = new Node(Token.NAME);
        Node rhsChild = new Node(Token.THIS);
        parentNode.addChildToBack(lhsChild);
        parentNode.addChildToBack(rhsChild);
        Node n = lhsChild;
        Node parent = parentNode;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }
    
    @Test
    public void shouldNotTraverseWithAssignmentOnRightSide() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node parentNode = new Node(Token.ASSIGN);
        Node lhsChild = new Node(Token.NAME);
        Node rhsChild = new Node(Token.THIS);
        parentNode.addChildToBack(lhsChild);
        parentNode.addChildToBack(rhsChild);
        Node n = rhsChild;
        Node parent = parentNode;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void shouldNotTraverseWithAssignmentToPrototypeProperty() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node parentNode = new Node(Token.ASSIGN);
        Node lhsChild = new Node(Token.GETPROP);
        lhsChild.addChildToBack(new Node(Token.NAME, "this"));
        lhsChild.addChildToBack(new Node(Token.STRING, "prototype"));
        Node rhsChild = new Node(Token.THIS);
        parentNode.addChildToBack(lhsChild);
        parentNode.addChildToBack(rhsChild);
        Node n = rhsChild;
        Node parent = parentNode;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void shouldNotTraverseWithAssignmentToPrototypeSubproperty() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node parentNode = new Node(Token.ASSIGN);
        Node lhsChild = new Node(Token.GETPROP);
        lhsChild.addChildToBack(new Node(Token.NAME, "this"));
        lhsChild.addChildToBack(new Node(Token.STRING, "prototype.subproperty"));
        Node rhsChild = new Node(Token.THIS);
        parentNode.addChildToBack(lhsChild);
        parentNode.addChildToBack(rhsChild);
        Node n = rhsChild;
        Node parent = parentNode;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void shouldReportThisIfAssignLhsChildIsNotNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }
    
    @Test
    public void shouldNotReportThisIfAssignLhsChildIsNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = null;
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }
    
    @Test
    public void shouldReportThisIfAssignLhsChildNotNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }
    
    @Test
    public void shouldNotReportThisIfAssignLhsChildIsNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = null;
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testVisitShouldReportThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EMPTY);
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(t, n, parent);
        // assert that a report was made
    }
    
    @Test
    public void testVisitShouldNotReportThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EMPTY);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        // assert that no report was made
    }
    
    @Test
    public void testVisitSetsAssignLhsChildToNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EMPTY);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void shouldGetFunctionJsDocInfoWithJSDocOnFunction() {
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        functionNode.setJSDocInfo(jsDoc);
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void shouldGetFunctionJsDocInfoWithJSDocOnParentName() {
        Node functionNode = new Node(Token.NAME);
        functionNode.addChildToFront(new Node(Token.FUNCTION));
        JSDocInfo jsDoc = new JSDocInfo();
        functionNode.setJSDocInfo(jsDoc);
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void shouldGetFunctionJsDocInfoWithJSDocOnParentAssign() {
        Node functionNode = new Node(Token.EMPTY);
        Node assignNode = new Node(Token.ASSIGN);
        Node nameNode = new Node(Token.NAME);
        assignNode.addChildToBack(nameNode);
        assignNode.addChildToBack(functionNode);
        JSDocInfo jsDoc = new JSDocInfo();
        nameNode.setJSDocInfo(jsDoc);
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void shouldGetFunctionJsDocInfoWithJSDocOnParentVar() {
        Node functionNode = new Node(Token.EMPTY);
        Node assignNode = new Node(Token.ASSIGN);
        Node nameNode = new Node(Token.NAME);
        assignNode.addChildToBack(nameNode);
        assignNode.addChildToBack(functionNode);
        Node varNode = new Node(Token.VAR, assignNode);
        JSDocInfo jsDoc = new JSDocInfo();
        varNode.setJSDocInfo(jsDoc);
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void shouldReturnNullIfNoJSDocFound() {
        Node functionNode = new Node(Token.FUNCTION);
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertNull(result);
    }
}