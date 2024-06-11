import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;
    private NodeTraversal.Callback callback;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        callback = checkGlobalThis;
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionThis_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionOverride_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isOverride()).thenReturn(true);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionBlock_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.BLOCK);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionScript_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.SCRIPT);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionName_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.NAME);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_FunctionAssign_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_AssignLeftSide_ReturnsTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        Node assignLhsChild = mock(Node.class);
        checkGlobalThis.assignLhsChild = assignLhsChild;
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_GetPropPrototype_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        Node lhs = mock(Node.class);
        Node getLastChild = mock(Node.class);
        Node llhs = mock(Node.class);
        Node getLastChild2 = mock(Node.class);
        
        when(n.getType()).thenReturn(Token.GETPROP);
        when(n.getLastChild()).thenReturn(getLastChild);
        when(getLastChild.getString()).thenReturn("prototype");
        when(n.getFirstChild()).thenReturn(lhs);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild()).thenReturn(getLastChild2);
        when(getLastChild2.getString()).thenReturn("prototype");
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_GetPropNoPrototype_ReturnsTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        Node lhs = mock(Node.class);
        Node getLastChild = mock(Node.class);
        Node llhs = mock(Node.class);
        Node getLastChild2 = mock(Node.class);
        
        when(n.getType()).thenReturn(Token.GETPROP);
        when(n.getLastChild()).thenReturn(getLastChild);
        when(getLastChild.getString()).thenReturn("property");
        when(n.getFirstChild()).thenReturn(lhs);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild()).thenReturn(getLastChild2);
        when(getLastChild2.getString()).thenReturn("prototype");
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testVisit_This_ReturnsTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        checkGlobalThis.assignLhsChild = null;
        boolean shouldReportThis = true;
        
        checkGlobalThis.visit(t, n, parent);
        
        verify(compiler).report(any());
        assertEquals(null, checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testVisit_This_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        
        checkGlobalThis.assignLhsChild = null;
        boolean shouldReportThis = false;
        
        checkGlobalThis.visit(t, n, parent);
        
        verify(compiler, never()).report(any());
        assertEquals(null, checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_HasJsDoc_ReturnsJsDoc() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node gramps = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        
        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(jsDoc);
        when(parent.getParent()).thenReturn(gramps);
        when(gramps.getType()).thenReturn(Token.VAR);
        when(gramps.getJSDocInfo()).thenReturn(null);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_ReturnsNull() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node gramps = mock(Node.class);
        
        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(null);
        when(parent.getParent()).thenReturn(gramps);
        when(gramps.getType()).thenReturn(Token.VAR);
        when(gramps.getJSDocInfo()).thenReturn(null);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(null, result);
    }
    
}