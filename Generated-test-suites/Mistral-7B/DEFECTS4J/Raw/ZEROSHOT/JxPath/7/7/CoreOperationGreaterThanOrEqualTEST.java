package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.VariableReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationGreaterThanOrEqualTest {

    @Mock
    private EvalContext context;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;

    @InjectMocks
    private CoreOperationGreaterThanOrEqual operation;

    private Path path;

    @Before
    public void setUp() {
        path = Mockito.spy(new Path(""));
        Mockito.when(context.getContextNode()).thenReturn(new NodeInfo(path, new InfoSet()));
        Mockito.when(context.getVariables()).thenReturn(Collections.emptyMap());
        Mockito.when(arg1.computeValue(context)).thenReturn(1.5);
        Mockito.when(arg2.computeValue(context)).thenReturn(1.0);
    }

    @Test
    public void testComputeValueWithDoubleValues() {
        Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(1.5);
        Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(1.0);

        Object result = operation.computeValue(context);

        verify(context).setResult(Mockito.isA(Boolean.class));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithIntegerValues() {
        Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(3.0);
        Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(2.0);

        Object result = operation.computeValue(context);

        verify(context).setResult(Mockito.isA(Boolean.class));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithSameDoubleValue() {
        Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(1.5);
        Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(1.5);

        Object result = operation.computeValue(context);

        verify(context).setResult(Mockito.isA(Boolean.class));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(1.5);
        Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(new VariableReference("x"));

        OperationException exception = Mockito.spy(new OperationException());

        try {
            operation.computeValue(context);
        } catch (OperationException e) {
            exception.setNextException(e);
        }

        verify(context).setException(exception);
        assertEquals("Comparison of double and VariableReference types is not supported.", exception.getMessage());
    }

    @Test
    public void testComputeValueWithNullValue() {
        Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(1.5);
        Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(null);

        OperationException exception = Mockito.spy(new OperationException());

        try {
            operation.computeValue(context);
        } catch (OperationException e) {
            exception.setNextException(e);
        }

        verify(context).setException(exception);
        assertEquals("Cannot compare null value to double.", exception.getMessage());
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();

        assertEquals(">=", symbol);
    }
}