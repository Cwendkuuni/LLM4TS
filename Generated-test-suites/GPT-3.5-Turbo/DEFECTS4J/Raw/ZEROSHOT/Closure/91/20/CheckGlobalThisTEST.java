import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, new Node()));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.addThisType();
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, new Node()));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, new Node()));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideName() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithStringKey() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithNumberKey() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithoutLogicalPlaceForThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LET);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_NonFunctionNode() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ADD);
        Node parent = new Node(Token.BLOCK);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_AssignLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_AssignLhsChildNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.LET);
        checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testVisit_ThisShouldReportThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        checkGlobalThis.visit(t, n, parent);
        // Assert error report
    }
    
    @Test
    public void testVisit_AssignLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        checkGlobalThis.shouldTraverse(t, n, parent);
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }
    
    private JSDocInfo createFunctionJsDocInfo(boolean isConstructor, boolean hasThisType, boolean isOverride) {
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(isConstructor);
        if (hasThisType) {
            jsDoc.addThisType();
        }
        jsDoc.setOverride(isOverride);
        return jsDoc;
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionNameHasJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_VariableDeclarationHasJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LET);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        Node gramps = new Node(Token.VAR);
        parent.setParent(gramps);
        assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testShouldReportThis_AssignLhsChildNotNull() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        checkGlobalThis.shouldTraverse(new NodeTraversal(), n, parent);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }
    
    @Test
    public void testShouldReportThis_AssignLhsChildNull() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.LET);
        checkGlobalThis.shouldTraverse(new NodeTraversal(), n, parent);
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    }
    
    @Test
    public void testConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        assertNotNull(checkGlobalThis);
        assertEquals(compiler, checkGlobalThis.compiler);
        assertEquals(level, checkGlobalThis.level);
    }
}