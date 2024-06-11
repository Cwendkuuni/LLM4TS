import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    }
    
    @Test
    public void shouldTraverse_shouldReturnTrueWhenNodeIsNotGlobalContext() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void shouldTraverse_shouldReturnFalseWhenNodeIsConstructorFunction() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = createFunctionNodeWithJSDoc(true, false);
        Node parent = new Node(Token.EXPR_RESULT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverse_shouldReturnFalseWhenNodeIsFunctionWithThisAnnotation() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = createFunctionNodeWithJSDoc(false, true);
        Node parent = new Node(Token.EXPR_RESULT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverse_shouldReturnFalseWhenNodeIsAssignmentToPrototypeProperty() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = createAssignmentToPrototypePropertyNode();
        Node parent = new Node(Token.EXPR_RESULT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverse_shouldReturnFalseWhenNodeIsAssignmentToSubproperty() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = createAssignmentToSubpropertyNode();
        Node parent = new Node(Token.EXPR_RESULT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverse_shouldReturnTrueWhenNodeIsAssignmentToNonPrototypeProperty() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = createAssignmentToNonPrototypePropertyNode();
        Node parent = new Node(Token.EXPR_RESULT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void visit_shouldReportErrorWhenNodeIsGlobalThis() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        DiagnosticType level = DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");
        
        checkGlobalThis.visit(t, n, parent);
        
        verify(t).makeError(n, level);
    }
    
    @Test
    public void visit_shouldNotReportErrorWhenNodeIsNotGlobalThis() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        
        checkGlobalThis.visit(t, n, parent);
        
        verify(t, never()).makeError(any(), any(), any());
    }
    
    @Test
    public void shouldReportThis_shouldReturnTrueWhenAssignLhsChildIsNotNull() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void shouldReportThis_shouldReturnFalseWhenAssignLhsChildIsNull() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = null;
        
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnJsDocInfoOfFunctionNode() {
        Node n = createFunctionNodeWithJSDoc(false, false);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals("some doc", result.getOriginalCommentString());
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnJsDocInfoOfParentNodeWhenFunctionNodeHasNoJsDocInfo() {
        Node n = createFunctionNodeWithJSDoc(false, false);
        n.setJSDocInfo(null);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(createJsDocInfo("parent doc"));
        n.setParent(parent);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals("parent doc", result.getOriginalCommentString());
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnJsDocInfoOfParentNodeWhenFunctionNodeIsAssignedToNameNodeWithJsDocInfo() {
        Node n = createFunctionNodeWithJSDoc(false, false);
        n.setJSDocInfo(null);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(createJsDocInfo("parent doc"));
        Node grandparent = new Node(Token.ASSIGN);
        grandparent.setFirstChild(parent);
        n.setParent(parent);
        parent.setParent(grandparent);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals("parent doc", result.getOriginalCommentString());
    }
    
    @Test
    public void getFunctionJsDocInfo_shouldReturnNullWhenFunctionNodeHasNoJsDocInfoAndParentNodeHasNoJsDocInfo() {
        Node n = createFunctionNodeWithJSDoc(false, false);
        n.setJSDocInfo(null);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(createJsDocInfo(null));
        n.setParent(parent);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertNull(result);
    }
    
    private Node createFunctionNodeWithJSDoc(boolean isConstructor, boolean hasThis) {
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(createJsDocInfo("some doc"));
        if (isConstructor) {
            JSTypeExpression jsTypeExpr = mock(JSTypeExpression.class);
            when(jsTypeExpr.isValidConstructor()).thenReturn(true);
            when(jsTypeExpr.isInterfaceDefinition()).thenReturn(false);
            n.getJSDocInfo().setConstructorType(jsTypeExpr);
        }
        if (hasThis) {
            JSTypeExpression jsTypeExpr = mock(JSTypeExpression.class);
            when(jsTypeExpr.isThisType()).thenReturn(true);
            n.getJSDocInfo().setThisType(jsTypeExpr);
        }
        return n;
    }
    
    private Node createAssignmentToPrototypePropertyNode() {
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "SomeClass"));
        lhs.addChildToBack(new Node(Token.STRING, "prototype"));
        Node rhs = new Node(Token.FUNCTION);
        Node assign = new Node(Token.ASSIGN);
        assign.addChildToBack(lhs);
        assign.addChildToBack(rhs);
        return assign;
    }
    
    private Node createAssignmentToSubpropertyNode() {
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "SomeClass"));
        lhs.addChildToBack(new Node(Token.STRING, "prototype.property"));
        Node rhs = new Node(Token.FUNCTION);
        Node assign = new Node(Token.ASSIGN);
        assign.addChildToBack(lhs);
        assign.addChildToBack(rhs);
        return assign;
    }
    
    private Node createAssignmentToNonPrototypePropertyNode() {
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "SomeClass"));
        lhs.addChildToBack(new Node(Token.STRING, "property"));
        Node rhs = new Node(Token.FUNCTION);
        Node assign = new Node(Token.ASSIGN);
        assign.addChildToBack(lhs);
        assign.addChildToBack(rhs);
        return assign;
    }
    
    private JSDocInfo createJsDocInfo(String comment) {
        JSDocInfo jsDocInfo = new JSDocInfo(factory);
        jsDocInfo.setOriginalCommentString(comment);
        return jsDocInfo;
    }
}