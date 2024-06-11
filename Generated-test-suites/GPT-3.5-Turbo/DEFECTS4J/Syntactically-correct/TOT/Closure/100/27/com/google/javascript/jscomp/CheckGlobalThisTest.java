import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }
    
    @Test
    public void testVisit_ThisOnLeftSideOfAssign_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        
        boolean shouldReportError = checkGlobalThis.shouldReportThis(n, parent);
        
        assertTrue(shouldReportError);
    }
    
    @Test
    public void testVisit_ThisWithPropertyAccess_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        boolean shouldReportError = checkGlobalThis.shouldReportThis(n, parent);
        
        assertTrue(shouldReportError);
    }
    
    @Test
    public void testVisit_ThisOnLeftSideOfAssign_ShouldNotReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        boolean shouldReportError = checkGlobalThis.shouldReportThis(n, parent);
        
        assertFalse(shouldReportError);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        parent.getJSDocInfo().setConstructor(true);
        
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        parent.getJSDocInfo().setHasThisType(true);
        
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_FunctionNotConstructorOrThisAnnotation_ShouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_AssignNodeWithLhsChild_ShouldNotOverrideAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        
        checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(n, checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testShouldTraverse_AssignNodeWithRhsChild_ShouldNotTraverseRightSide() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.NAME));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.addChildToBack(n);
        
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_NullParent_ShouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = null;
        
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(shouldTraverse);
    }
    
    @Test
    public void testVisit_ThisNode_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = null;
        
        checkGlobalThis.visit(t, n, parent);
        
        ErrorReport error = compiler.getReportedErrors().get(0);
        assertEquals(CheckGlobalThis.GLOBAL_THIS, error.getType());
    }
    
    @Test
    public void testVisit_NodeNotAssignLhsChild_ShouldSetAssignLhsChildNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = null;
        checkGlobalThis.assignLhsChild = n;
        
        checkGlobalThis.visit(t, n, parent);
        
        assertNull(checkGlobalThis.assignLhsChild);
    }
}