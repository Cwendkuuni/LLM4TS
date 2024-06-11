import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Given
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression("5"), new CoreExpression("10"));
        EvalContext context = new EvalContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}