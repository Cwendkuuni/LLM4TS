import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Given
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result.equals(Boolean.TRUE));
    }
}