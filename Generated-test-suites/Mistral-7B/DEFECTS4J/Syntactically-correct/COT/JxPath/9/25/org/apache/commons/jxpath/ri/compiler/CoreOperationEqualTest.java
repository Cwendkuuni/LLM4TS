package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.EvalContext;
import org.apache.commons.jxpath.ri.util.TestUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test for CoreOperationEqual class.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class CoreOperationEqualTest extends TestCase {

    public static Test suite() {
        return new TestSuite(CoreOperationEqualTest.class);
    }

    private CoreOperationEqual op;
    private CoreOperationCompare arg1;
    private CoreOperationCompare arg2;
    private EvalContext context;

    @Override
    protected void setUp() throws Exception {
        arg1 = new CoreOperationCompare(new CoreConstant(1), new CoreConstant(1));
        arg2 = new CoreOperationCompare(new CoreConstant(1), new CoreConstant(2));
        op = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
    }

    // Test for constructor

    /**
     * Test constructor.
     */
    public void testConstructor() {
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        assertSame(CoreOperationEqual.class, op.getClass());
    }

    // Test for computeValue method

    /**
     * Test computeValue method with equal values.
     */
    public void testComputeValueWithEqualValues() {
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with not equal values.
     */
    public void testComputeValueWithNotEqualValues() {
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with null values.
     */
    public void testComputeValueWithNullValues() {
        arg1 = new CoreOperationCompare(new CoreConstant(null), new CoreConstant(null));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with different types of values.
     */
    public void testComputeValueWithDifferentTypes() {
        arg1 = new CoreConstant("1");
        Object result = op.computeValue(context);
        assertNull(result);
    }

    // Test for getSymbol method

    /**
     * Test getSymbol method.
     */
    public void testGetSymbol() {
        assertEquals("=", op.getSymbol());
    }

    // Edge cases and exception handling tests

    /**
     * Test computeValue method with null arguments.
     */
    public void testComputeValueWithNullArguments() {
        op = new CoreOperationEqual(null, arg2);
        Object result = op.computeValue(context);
        assertNull(result);

        op = new CoreOperationEqual(arg1, null);
        result = op.computeValue(context);
        assertNull(result);
    }

    /**
     * Test computeValue method with arguments of different types.
     */
    public void testComputeValueWithArgumentsOfDifferentTypes() {
        arg1 = new CoreConstant(1);
        arg2 = new CoreConstant("1");
        Object result = op.computeValue(context);
        assertNull(result);
    }

    /**
     * Test computeValue method with NaN values.
     */
    public void testComputeValueWithNaNValues() {
        arg1 = new CoreConstant(Double.NaN);
        arg2 = new CoreConstant(Double.NaN);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with Infinity values.
     */
    public void testComputeValueWithInfinityValues() {
        arg1 = new CoreConstant(Double.POSITIVE_INFINITY);
        arg2 = new CoreConstant(Double.POSITIVE_INFINITY);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstant(Double.NEGATIVE_INFINITY);
        arg2 = new CoreConstant(Double.NEGATIVE_INFINITY);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstant(Double.POSITIVE_INFINITY);
        arg2 = new CoreConstant(Double.NEGATIVE_INFINITY);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with empty string values.
     */
    public void testComputeValueWithEmptyStringValues() {
        arg1 = new CoreConstant("");
        arg2 = new CoreConstant("");
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstant(" ");
        arg2 = new CoreConstant(" ");
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstant("");
        arg2 = new CoreConstant(" ");
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with zero number values.
     */
    public void testComputeValueWithZeroNumberValues() {
        arg1 = new CoreConstant(0);
        arg2 = new CoreConstant(0);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstant(0.0);
        arg2 = new CoreConstant(0.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstant(0d);
        arg2 = new CoreConstant(0d);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}