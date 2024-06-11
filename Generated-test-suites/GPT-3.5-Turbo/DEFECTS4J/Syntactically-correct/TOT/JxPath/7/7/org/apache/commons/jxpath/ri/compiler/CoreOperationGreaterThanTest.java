import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;


public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_typicalCase() {
        // Typical case where both arguments are valid and arg1 > arg2
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(5);
        arg2.setValue(3);
        CoreOperationGreaterThan operator = new CoreOperationGreaterThan(arg1, arg2);
        Object result = operator.computeValue(null);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_equalArguments() {
        // Case where both arguments are equal
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(5);
        arg2.setValue(5);
        CoreOperationGreaterThan operator = new CoreOperationGreaterThan(arg1, arg2);
        Object result = operator.computeValue(null);
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_arg1LessThanArg2() {
        // Case where arg1 < arg2
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(3);
        arg2.setValue(5);
        CoreOperationGreaterThan operator = new CoreOperationGreaterThan(arg1, arg2);
        Object result = operator.computeValue(null);
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_arg1Null() {
        // Case where arg1 is null
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg2.setValue(5);
        CoreOperationGreaterThan operator = new CoreOperationGreaterThan(arg1, arg2);
        Object result = operator.computeValue(null);
        assertNull(result);
    }
    
    @Test
    public void testComputeValue_arg2Null() {
        // Case where arg2 is null
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(5);
        CoreOperationGreaterThan operator = new CoreOperationGreaterThan(arg1, arg2);
        Object result = operator.computeValue(null);
        assertNull(result);
    }
}