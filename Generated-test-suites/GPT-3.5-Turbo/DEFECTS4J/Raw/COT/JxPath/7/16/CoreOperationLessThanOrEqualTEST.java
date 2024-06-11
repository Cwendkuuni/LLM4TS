import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNodeSet(arg1);
        context.setCurrentNode(arg2);

        // Test when arg1 and arg2 values are equal
        arg1.setValue(1);
        arg2.setValue(1);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Test when arg1 value is less than arg2 value
        arg1.setValue(1);
        arg2.setValue(2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Test when arg1 value is greater than arg2 value
        arg1.setValue(2);
        arg2.setValue(1);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertEquals("<=", operation.getSymbol());
    }
}