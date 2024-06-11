import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan coreOperation = new CoreOperationGreaterThan(new Expression("5"), new Expression("3"));
        EvaluContext context = new EvaluContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);

        coreOperation = new CoreOperationGreaterThan(new Expression("3"), new Expression("5"));
        result = coreOperation.computeValue(context);
        assertFalse((Boolean) result);

        // Edge cases
        coreOperation = new CoreOperationGreaterThan(new Expression("0.0000000001"), new Expression("0.0000000000001"));
        result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);

        coreOperation = new CoreOperationGreaterThan(new Expression("-1000"), new Expression("-100"));
        result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);

        // Exception handling scenarios?
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperation = new CoreOperationGreaterThan(new Expression("5"), new Expression("3"));
        assertEquals(">", coreOperation.getSymbol());
    }

}