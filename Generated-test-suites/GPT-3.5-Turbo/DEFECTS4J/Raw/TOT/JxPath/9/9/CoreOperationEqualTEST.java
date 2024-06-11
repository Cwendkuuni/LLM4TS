import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual coreOperationEqual;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;
    
    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue_equalValues_returnTrue() {
        arg1.setValue(10);
        arg2.setValue(10);
        Assert.assertTrue(coreOperationEqual.computeValue(context));
    }
    
    @Test
    public void testComputeValue_notEqualValues_returnFalse() {
        arg1.setValue("hello");
        arg2.setValue("world");
        Assert.assertFalse(coreOperationEqual.computeValue(context));
    }
    
    @Test
    public void testComputeValue_differentTypes_returnFalse() {
        arg1.setValue(true);
        arg2.setValue(1);
        Assert.assertFalse(coreOperationEqual.computeValue(context));
    }
    
    @Test
    public void testGetSymbol_validSymbol_returnEqualSymbol() {
        Assert.assertEquals("=", coreOperationEqual.getSymbol());
    }
}