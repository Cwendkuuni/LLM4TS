import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_equal() {
        EvalContext context = new EvalContext();
        context.setValue("arg1", "value");
        context.setValue("arg2", "value");
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_notEqual() {
        EvalContext context = new EvalContext();
        context.setValue("arg1", "value1");
        context.setValue("arg2", "value2");
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Assert.assertEquals("!=", operation.getSymbol());
    }

}