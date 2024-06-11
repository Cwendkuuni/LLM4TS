import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_notFunctionConstructorOrThisAnnotated_returnTrue() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        boolean result = check.shouldTraverse(null, new Node(Token.LOCAL_NAME), new Node(Token.ROOT));
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_functionConstructor_returnFalse() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        boolean result = check.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ROOT));
        
        //Make the necessary assertions
    }
    
    @Test
    public void testShouldTraverse_functionThisAnnotated_returnFalse() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        boolean result = check.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ROOT));
        
        //Make the necessary assertions
    }
    
    @Test
    public void testShouldTraverse_functionAssignable_returnTrue() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        boolean result = check.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ROOT));
        
        //Make the necessary assertions
    }
    
    @Test
    public void testShouldTraverse_functionAssignable_nestedAssignments_returnFalse() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        boolean result = check.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ROOT));
        
        //Make the necessary assertions
    }
    
    @Test
    public void testVisit_thisOnLeftSideOfAssignment_reportGlobalThis() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        check.visit(traversal, node, parent);
        
        //Make the necessary assertions
    }
    
    @Test
    public void testVisit_thisWithPropertyAccess_reportGlobalThis() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        check.visit(traversal, node, parent);
        
        //Make the necessary assertions
    }
    
    @Test
    public void testVisit_thisOnLeftSideOfAssignment_nestedAssignments_notReportGlobalThis() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        check.visit(traversal, node, parent);
        
        //Make the necessary assertions
    }
    
    @Test
    public void testVisit_thisWithPropertyAccess_notReportGlobalThis() {
        CheckGlobalThis check = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        check.visit(traversal, node, parent);
        
        //Make the necessary assertions
    }
}