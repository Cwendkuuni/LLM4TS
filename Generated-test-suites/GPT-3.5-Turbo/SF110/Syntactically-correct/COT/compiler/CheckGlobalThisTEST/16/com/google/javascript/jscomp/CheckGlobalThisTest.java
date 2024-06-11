import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    }
    
    @Test
    public void testShouldTraverse_functionConstructor() {
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }
    
    @Test
    public void testShouldTraverse_functionThisAnnotation() {
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }
    
    @Test
    public void testShouldTraverse_functionOverrideAnnotation() {
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }
    
    @Test
    public void testShouldTraverse_nonGlobalContext() {
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }
    
    @Test
    public void testShouldTraverse_assignLhsChildNotNull() {
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }
    
    @Test
    public void testShouldTraverse_assignLhsChildNull() {
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }
    
    @Test
    public void testVisit_thisOnLeftSideOfAssignment_ShouldReportError() {
        // create a fake NodeTraversal and AbstractCompiler object
        NodeTraversal t = new NodeTraversal(new AbstractCompiler());
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);
        // Call the visit method
        checkGlobalThis.visit(t, n, parent);
        // Assert that the error was reported
        // TODO: Properly assert the error message
    }
    
    @Test
    public void testVisit_thisOnRightSideOfAssignment_ShouldNotReportError() {
        // create a fake NodeTraversal and AbstractCompiler object
        NodeTraversal t = new NodeTraversal(new AbstractCompiler());
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);
        // Call the visit method
        checkGlobalThis.visit(t, n, parent);
        // Assert that no error was reported
        
    }
    
    @Test
    public void testShouldReportThis_assignLhsChildNotNull_ReturnTrue() {
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));
    }
    
    @Test
    public void testShouldReportThis_assignLhsChildNull_ReturnFalse() {
        assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));
    }
}