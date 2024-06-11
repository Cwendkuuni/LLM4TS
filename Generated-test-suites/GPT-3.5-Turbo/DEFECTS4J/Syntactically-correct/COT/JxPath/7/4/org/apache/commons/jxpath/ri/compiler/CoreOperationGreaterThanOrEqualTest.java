
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();

        // Edge case: Check when arg1 is equal to arg2
        // Expected: The result should be true
        arg1.setValue(5);
        arg2.setValue(5);
        assert coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE);

        // Edge case: Check when arg1 is greater than arg2
        // Expected: The result should be true
        arg1.setValue(10);
        arg2.setValue(5);
        assert coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE);

        // Edge case: Check when arg1 is less than arg2
        // Expected: The result should be false
        arg1.setValue(5);
        arg2.setValue(10);
        assert coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.FALSE);
    }
}
