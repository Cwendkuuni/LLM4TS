import org.apache.commons.jxpath.ri.EvalContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        Expression arg1 = new CoreVariable();
        Expression arg2 = new CoreConstant();

        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueReturnsTrueWhenArgumentsAreEqual() {
        EvalContext context = new EvalContext();

        context.setVariable("var", "Hello");
        context.setConstant("const", "Hello");

        coreOperationEqual.getArguments()[0] = new CoreVariable("$var");
        coreOperationEqual.getArguments()[1] = new CoreConstant("const");

        Object result = coreOperationEqual.computeValue(context);

        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValueReturnsFalseWhenArgumentsAreNotEqual() {
        EvalContext context = new EvalContext();

        context.setVariable("var", 123);
        context.setConstant("const", "Hello");

        coreOperationEqual.getArguments()[0] = new CoreVariable("$var");
        coreOperationEqual.getArguments()[1] = new CoreConstant("const");

        Object result = coreOperationEqual.computeValue(context);

        Assert.assertFalse(result);
    }

    @Test
    public void testGetSymbolReturnsEqualSymbol() {
        String symbol = coreOperationEqual.getSymbol();

        Assert.assertEquals("=", symbol);
    }

}