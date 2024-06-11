import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    // Test for the getPrecedence method
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        int precedence = expression.getPrecedence();
        
        // Typical use case: expected precedence is 3
        assertEquals(3, precedence);
    }
    
    // Test for the isSymmetric method
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        boolean isSymmetric = expression.isSymmetric();
        
        // Typical use case: expected isSymmetric is false
        assertFalse(isSymmetric);
    }
    
}