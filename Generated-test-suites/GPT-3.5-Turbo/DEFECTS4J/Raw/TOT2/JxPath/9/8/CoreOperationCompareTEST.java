import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertTrue(result);
    }

    // Add more test cases for other methods as per the requirements
    // Remember to cover typical use cases, edge cases, and error scenarios

}

class ExpressionMock extends Expression {
    @Override
    Object compute(EvalContext context) {
        return new Object();
    }
}