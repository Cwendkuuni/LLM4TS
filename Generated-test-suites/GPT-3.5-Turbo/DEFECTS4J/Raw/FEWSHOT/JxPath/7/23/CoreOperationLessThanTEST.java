
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CoreOperationLessThanTest {
    
    private CoreOperationLessThan lessThan;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;
    
    @Mock
    private EvalContext evalContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        lessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValueLessThan() {
        // Mock values
        double value1 = 10.5;
        double value2 = 20.5;
        
        // Mock expression compute values
        when(arg1.computeValue(evalContext)).thenReturn(value1);
        when(arg2.computeValue(evalContext)).thenReturn(value2);

        // Compute value
        Object result = lessThan.computeValue(evalContext);

        // Verify result
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueNotLessThan() {
        // Mock values
        double value1 = 30.5;
        double value2 = 20.5;

        // Mock expression compute values
        when(arg1.computeValue(evalContext)).thenReturn(value1);
        when(arg2.computeValue(evalContext)).thenReturn(value2);

        // Compute value
        Object result = lessThan.computeValue(evalContext);

        // Verify result
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        // Get symbol
        String symbol = lessThan.getSymbol();

        // Verify symbol
        assertEquals("<", symbol);
    }
}
