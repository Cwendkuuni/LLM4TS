import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            public void execute() {
                // implementation not needed for testing
            }
        };
        
        int expectedPrecedence = 3;
        int actualPrecedence = expression.getPrecedence();
        
        assertEquals(expectedPrecedence, actualPrecedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            public void execute() {
                // implementation not needed for testing
            }
        };
        
        boolean expectedSymmetric = false;
        boolean actualSymmetric = expression.isSymmetric();
        
        assertEquals(expectedSymmetric, actualSymmetric);
    }
}