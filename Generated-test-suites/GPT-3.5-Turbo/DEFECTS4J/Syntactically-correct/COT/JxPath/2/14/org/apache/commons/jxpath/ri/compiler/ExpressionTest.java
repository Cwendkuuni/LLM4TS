import static org.junit.Assert.*;

import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };

        assertTrue(expression.isContextDependent());
    }
}