package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CoreOperationLessThanOrEqualTest {

    @Mock
    private EvalContext evalContext;

    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue() {
        // Test when l <= r
        double l = 2.0;
        double r = 3.0;
        assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(evalContext));

        // Test when l > r
        l = 4.0;
        r = 3.0;
        assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(evalContext));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }
}