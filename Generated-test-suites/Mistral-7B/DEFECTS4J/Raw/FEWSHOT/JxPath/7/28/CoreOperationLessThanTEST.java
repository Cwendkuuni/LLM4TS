package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtil;

import java.math.BigDecimal;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan op = new CoreOperationLessThan(TestUtil.createNumberExpression(new BigDecimal(2.0)),
                TestUtil.createNumberExpression(new BigDecimal(3.0)));

        assertTrue(op.computeValue(new EvalContext(TestUtil.createInfoSet())) instanceof Boolean);
        assertEquals(Boolean.FALSE, op.computeValue(new EvalContext(TestUtil.createInfoSet())));

        op = new CoreOperationLessThan(TestUtil.createNumberExpression(new BigDecimal(3.0)),
                TestUtil.createNumberExpression(new BigDecimal(2.0)));

        assertTrue(op.computeValue(new EvalContext(TestUtil.createInfoSet())) instanceof Boolean);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext(TestUtil.createInfoSet())));

        op = new CoreOperationLessThan(TestUtil.createStringExpression("3"), TestUtil.createStringExpression("2"));

        assertFalse(op.computeValue(new EvalContext(TestUtil.createInfoSet())) instanceof Boolean);

        op = new CoreOperationLessThan(TestUtil.createStringExpression("2"), TestUtil.createStringExpression("3"));

        assertFalse(op.computeValue(new EvalContext(TestUtil.createInfoSet())) instanceof Boolean);

        op = new CoreOperationLessThan(TestUtil.createStringExpression("2"), TestUtil.createStringExpression("2"));

        assertTrue(op.computeValue(new EvalContext(TestUtil.createInfoSet())) instanceof Boolean);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext(TestUtil.createInfoSet())));

        op = new CoreOperationLessThan(TestUtil.createNullExpression(), TestUtil.createNumberExpression(new BigDecimal(1.0)));

        assertNull(op.computeValue(new EvalContext(TestUtil.createInfoSet())));
    }
}