package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Signature;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThan;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new CoreStringLiteralExpression("5.0");
        Expression arg2 = new CoreNumberLiteralExpression("6.0");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
        assertEquals(2, coreOperationLessThan.getArity());
        assertEquals(CoreOperationLessThan.class, coreOperationLessThan.getClass());
        assertEquals("5.0 < 6.0", coreOperationLessThan.getSymbol());
    }

    /**
     * Test computeValue method with double values.
     */
    @Test
    public void testComputeValueDoubleValues() {
        Expression arg1 = new CoreNumberLiteralExpression("5.0");
        Expression arg2 = new CoreNumberLiteralExpression("6.0");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public NodeInfo getNodeByKey(String key) {
                return null;
            }
        });
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse("Expected false", result instanceof Boolean || result instanceof Boolean.FALSE);
        assertNull("Expected null", result);

        arg1 = new CoreNumberLiteralExpression("3.0");
        arg2 = new CoreNumberLiteralExpression("4.0");
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean || result instanceof Boolean.TRUE);
    }

    /**
     * Test computeValue method with different types of values.
     */
    @Test(expected = ClassCastException.class)
    public void testComputeValueDifferentTypes() {
        Expression arg1 = new CoreNumberLiteralExpression("5.0");
        Expression arg2 = new CoreStringLiteralExpression("6.0");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public NodeInfo getNodeByKey(String key) {
                return null;
            }
        });
        coreOperationLessThan.computeValue(context);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreNumberLiteralExpression("5.0");
        Expression arg2 = new CoreNumberLiteralExpression("6.0");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", coreOperationLessThan.getSymbol());
    }

    /**
     * Test signature.
     */
    @Test
    public void testSignature() {
        Signature signature = new Signature();
        signature.setName("lt");
        signature.setReturnType(Boolean.TYPE);
        signature.setParameterTypes(new Class[] { Double.TYPE, Double.TYPE });
        Expression arg1 = new CoreNumberLiteralExpression("5.0");
        Expression arg2 = new CoreNumberLiteralExpression("6.0");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals(signature, coreOperationLessThan.getSignature());
    }
}