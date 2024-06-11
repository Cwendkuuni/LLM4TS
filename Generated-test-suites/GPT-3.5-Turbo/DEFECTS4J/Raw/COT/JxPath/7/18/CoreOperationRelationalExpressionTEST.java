import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[]{new Expression()};
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertNotNull(operation);
    }

    @Test
    public void testConstructor_emptyArgs() {
        Expression[] args = new Expression[]{};
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertNotNull(operation);
    }

    @Test
    public void testConstructor_multipleArgs() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertNotNull(operation);
    }
}