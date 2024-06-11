package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Paths;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationLessThanTest {

    @Mock
    private EvalContext context;

    @Mock
    private InfoSet infoSet;

    private CoreOperationLessThan lessThan;

    @Before
    public void setup() {
        lessThan = new CoreOperationLessThan(Mockito.mock(Expression.class), Mockito.mock(Expression.class));
    }

    @Test
    public void testConstructor() {
        assertNotNull(lessThan);
    }

    @Test
    public void testComputeValue_double_double_whenFirstDoubleIsLessThanSecond_thenReturnsTrue() {
        // given
        double first = 1.5;
        double second = 2.5;
        Mockito.when(context.getInfoSet()).thenReturn(infoSet);
        Mockito.when(infoSet.getValue(Paths.STRING_PATH, 0)).thenReturn(first);
        Mockito.when(infoSet.getValue(Paths.STRING_PATH, 1)).thenReturn(second);
        Mockito.when(context.getEvalContextSupport()).thenReturn(new EvalContextSupport());
        EvalContextSupport evalContextSupport = Mockito.mock(EvalContextSupport.class);
        Mockito.when(context.getEvalContextSupport()).thenReturn(evalContextSupport);

        // when
        Object result = lessThan.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_double_double_whenFirstDoubleIsGreaterOrEqualThanSecond_thenReturnsFalse() {
        // given
        double first = 2.5;
        double second = 1.5;
        Mockito.when(context.getInfoSet()).thenReturn(infoSet);
        Mockito.when(infoSet.getValue(Paths.STRING_PATH, 0)).thenReturn(first);
        Mockito.when(infoSet.getValue(Paths.STRING_PATH, 1)).thenReturn(second);
        Mockito.when(context.getEvalContextSupport()).thenReturn(new EvalContextSupport());
        EvalContextSupport evalContextSupport = Mockito.mock(EvalContextSupport.class);
        Mockito.when(context.getEvalContextSupport()).thenReturn(evalContextSupport);

        // when
        Object result = lessThan.computeValue(context);

        // then
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_whenArgIsNotANumber_thenThrowsClassCastException() {
        // given
        Mockito.when(context.getInfoSet()).thenReturn(infoSet);
        Mockito.when(infoSet.getValue(Paths.STRING_PATH, 0)).thenReturn(Paths.BOOLEAN_TRUE);
        Mockito.when(infoSet.getValue(Paths.STRING_PATH, 1)).thenReturn(Paths.BOOLEAN_TRUE);

        // when & then
        lessThan.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        // given
        String symbol = "<";

        // when
        String result = lessThan.getSymbol();

        // then
        assertEquals(symbol, result);
    }
}