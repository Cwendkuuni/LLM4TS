import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    }
    
    @Test
    public void testShouldTraverse_globalContext_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_functionConstructor_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionThisAnnotation_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionGlobalContext_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        jsDoc.recordGlobal();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_nonFunctionContext_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_nestedAssignment_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node();
        lhs.setType(Token.GETPROP);
        parent.addChildToFront(lhs);
        Node rhs = new Node();
        parent.addChildToBack(rhs);
        n.setJSDocInfo(null);
        CheckGlobalThis spy = spyOnCheckGlobalThis();
        spy.shouldTraverse(t, n, parent);
        
        boolean result = spy.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testVisit_thisNode_shouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        CheckGlobalThis spy = spyOnCheckGlobalThis();
        
        spy.visit(t, n, parent);
        
        // assert report method called
    }
    
    @Test
    public void testVisit_notThisNode_shouldNotReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        CheckGlobalThis spy = spyOnCheckGlobalThis();
        
        spy.visit(t, n, parent);
        
        // assert report method not called
    }
    
    private CheckGlobalThis spyOnCheckGlobalThis() {
        return spy(checkGlobalThis);
    }
}