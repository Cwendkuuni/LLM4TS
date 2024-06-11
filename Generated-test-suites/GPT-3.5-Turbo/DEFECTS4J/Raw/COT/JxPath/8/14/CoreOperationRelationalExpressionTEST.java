import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        // Test the constructor functionality
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertNotNull(operation);
    }

}