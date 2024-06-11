package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.TestSupport;

/**
 * Test class for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @org.junit.Test
    public void testCoreOperationGreaterThan() {
        // Test with valid arguments
        Expression arg1 = new CoreNumber(Double.parseDouble("5.5"));
        Expression arg2 = new CoreNumber(Double.parseDouble("3.3"));
        CoreOperationGreaterThan cogn = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestSupport.createContext();
        Boolean result = (Boolean) cogn.computeValue(context);
        assertTrue(result);

        // Test with reverse arguments
        arg1 = new CoreNumber(Double.parseDouble("3.3"));
        arg2 = new CoreNumber(Double.parseDouble("5.5"));
        cogn = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) cogn.computeValue(context);
        assertFalse(result);

        // Test with same arguments
        arg1 = arg2 = new CoreNumber(Double.parseDouble("5.5"));
        cogn = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) cogn.computeValue(context);
        assertFalse(result);

        // Test with null arguments
        arg1 = new CoreNull();
        arg2 = new CoreNull();
        cogn = new CoreOperationGreaterThan(arg1, arg2);
        result = cogn.computeValue(context);
        assertFalse(result);

        // Test with one of the arguments as null
        arg1 = new CoreNull();
        arg2 = new CoreNumber(Double.parseDouble("5.5"));
        cogn = new CoreOperationGreaterThan(arg1, arg2);
        result = cogn.computeValue(context);
        assertFalse(result);

        arg1 = new CoreNumber(Double.parseDouble("5.5"));
        arg2 = new CoreNull();
        cogn = new CoreOperationGreaterThan(arg1, arg2);
        result = cogn.computeValue(context);
        assertFalse(result);
    }
}