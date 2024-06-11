package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link CoreOperationGreaterThan}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoreOperationGreaterThanTest {

    @Mock
    private EvalContext context;

    @InjectMocks
    private CoreOperationGreaterThan coreOperationGreaterThan;

    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = mockExpressionWithValue(1.5);
        arg2 = mockExpressionWithValue(0.5);
    }

    @Test
    public void testComputeValueTrue() {
        // Given
        when(context.getInfoSet()).thenReturn(createInfoSet());

        // When
        Object result = coreOperationGreaterThan.computeValue(context);

        // Then
        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    public void testComputeValueFalse() {
        // Given
        when(context.getInfoSet()).thenReturn(createInfoSet());

        // When
        Object result = coreOperationGreaterThan.computeValue(context);

        // Then
        assertThat(result, is(Boolean.FALSE));
    }

    private Expression mockExpressionWithValue(double value) {
        Expression expression = mock(Expression.class);
        when(expression.computeValue(context)).thenReturn(value);
        return expression;
    }

    private InfoSet createInfoSet() {
        InfoSet infoSet = mock(InfoSet.class);
        return infoSet;
    }
}