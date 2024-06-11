import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operationCompare = new ConcreteCoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        // Test case: Both values are null
        context.setContextNodePointer(null);
        left.setResult(null);
        right.setResult(null);
        assertEquals(true, operationCompare.equal(context, left, right));

        // Test case: Both values are not null, but are equal
        context.setContextNodePointer(new Pointer());
        left.setResult("test");
        right.setResult("test");
        assertEquals(true, operationCompare.equal(context, left, right));

        // Test case: Both values are not null and are not equal
        context.setContextNodePointer(new Pointer());
        left.setResult("test1");
        right.setResult("test2");
        assertEquals(false, operationCompare.equal(context, left, right));

        // Additional test cases...

    }

    // Additional test methods for other public methods...

}