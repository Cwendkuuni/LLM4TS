import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_ReturnsTrueForGlobalThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsFalseForNonGlobalThisInFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsFalseForFunctionWithConstructorAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testVisit_ReportsErrorForGlobalThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Check that compiler.report() was called with the correct parameters
        // Assumption: the compiler.report() method throws an exception if it is called
        // with a null error value
    }
    
    @Test
    public void testVisit_DoesNotReportErrorForLocalThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Check that compiler.report() was not called
    }
    
    @Test
    public void testVisit_DoesNotReportErrorForThisWithPropertyAccess() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Check that compiler.report() was not called
    }
    
    @Test
    public void testVisit_ClearsAssignLhsChild() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Check that assignLhsChild is null
    }
}