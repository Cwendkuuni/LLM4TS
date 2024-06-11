import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testVisit_shouldReportThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        checker.visit(t, n, parent);
        
        // Assert that there is an error reported
        assertTrue(t.hasError());
    }
    
    @Test
    public void testVisit_shouldNotReportThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETELEM);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        checker.visit(t, n, parent);
        
        // Assert that there is no error reported
        assertFalse(t.hasError());
    }
    
    @Test
    public void testShouldTraverse_shouldTraverseFunctions() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETELEM);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checker.shouldTraverse(t, n, parent);
        
        // Assert that shouldTraverse returns true for functions
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldNotTraverseConstructors() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETELEM);
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checker.shouldTraverse(t, n, parent);
        
        // Assert that shouldTraverse returns false for constructor functions
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldNotTraverseFunctionsAnnotatedWithThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETELEM);
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checker.shouldTraverse(t, n, parent);
        
        // Assert that shouldTraverse returns false for functions annotated with this
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldNotTraverseNestedAssignmentsToPrototype() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.GETPROP);
        
        Node lhs = new Node(Token.GETPROP);
        Node lhsFirstChild = new Node(Token.NAME);
        lhsFirstChild.setString("prototype");
        lhs.addChildToBack(lhsFirstChild);
        
        parent.addChildToBack(lhs);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checker.shouldTraverse(t, n, parent);
        
        // Assert that shouldTraverse returns false for nested assignments to prototype properties
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldNotTraverseNestedAssignmentsToSubproperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.GETPROP);
        
        Node lhs = new Node(Token.GETPROP);
        Node lhsFirstChild = new Node(Token.NAME);
        lhsFirstChild.setString("prototype.subproperty");
        lhs.addChildToBack(lhsFirstChild);
        
        parent.addChildToBack(lhs);
        
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checker.shouldTraverse(t, n, parent);
        
        // Assert that shouldTraverse returns false for nested assignments to subproperties
        assertFalse(result);
    }
}