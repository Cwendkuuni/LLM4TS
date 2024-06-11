import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        context.setContextNodePointer(nodePointer);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        int precedence = operation.getPrecedence();
        
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        boolean symmetric = operation.isSymmetric();
        
        assertFalse(symmetric);
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        boolean result = operation.evaluateCompare(compare);
        
        assertTrue(result);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        boolean result = operation.compute(leftObject, rightObject);
        
        assertTrue(result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        Object result = operation.reduce(object);
        
        assertNotNull(result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        boolean result = operation.containsMatch(iterator, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = {expression1, expression2}; 
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        
        boolean result = operation.findMatch(leftIterator, rightIterator);
        
        assertFalse(result);
    }

}