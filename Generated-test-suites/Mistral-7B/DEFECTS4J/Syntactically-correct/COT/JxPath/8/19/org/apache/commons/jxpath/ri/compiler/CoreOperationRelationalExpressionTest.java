import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationRelationalExpressionTest {

    private EvalContext context;
    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        context = mock(EvalContext.class);
        expression = new CoreOperationRelationalExpression(new CoreExpression[] {new CoreConstantExpression(mock(Object.class)), new CoreConstantExpression(mock(Object.class))}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        Object left = new Integer(1);
        Object right = new Integer(2);

        when(context.getSingleNodePointer()).thenReturn(left);
        when(context.getSingleNodePointer()).thenReturn(right);

        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Assert.assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Assert.assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testComputeWithInitialContext() {
        Object left = mock(InitialContext.class);
        Object right = mock(InitialContext.class);

        when(left.reset()).thenReturn(left);
        when(right.reset()).thenReturn(right);

        Assert.assertEquals(Boolean.FALSE, expression.compute(left, right));
    }

    @Test
    public void testComputeWithSelfContext() {
        Object left = new SelfContext();
        Object right = new SelfContext();

        Assert.assertEquals(Boolean.FALSE, expression.compute(left, right));
    }

    @Test
    public void testComputeWithCollection() {
        Object left = new ArrayList<>();
        Object right = new Integer(1);

        when(context.getSingleNodePointer()).thenReturn(left);
        when(context.getSingleNodePointer()).thenReturn(right);

        Assert.assertEquals(Boolean.FALSE, expression.compute(left, right));
    }

    @Test
    public void testComputeWithIterators() {
        Object left = new ArrayList<>(Collections.singletonList(new Integer(1))).iterator();
        Object right = new ArrayList<>(Collections.singletonList(new Integer(1))).iterator();

        Assert.assertEquals(Boolean.TRUE, expression.compute(left, right));
    }

    @Test(expected = ClassCastException.class)
    public void testComputeWithMismatchedTypes() {
        Object left = new Double(1.0);
        Object right = new Integer(1);

        when(context.getSingleNodePointer()).thenReturn(left);
        when(context.getSingleNodePointer()).thenReturn(right);

        expression.compute(left, right);
    }

    @Test
    public void testDoubleComparison() {
        Object left = new Double(1.0);
        Object right = new Double(1.0);

        Assert.assertEquals(Boolean.TRUE, expression.compute(left, right));
    }

    @Test
    public void testDoubleComparisonWithDifferentValues() {
        Object left = new Double(1.0);
        Object right = new Double(2.0);

        Assert.assertEquals(Boolean.FALSE, expression.compute(left, right));
    }

    @Test
    public void testDoubleComparisonWithEqualValuesButDifferentSigns() {
        Object left = new Double(-1.0);
        Object right = new Double(1.0);

        Assert.assertEquals(Boolean.FALSE, expression.compute(left, right));
    }

    @Test
    public void testDoubleComparisonWithEqualValuesButDifferentOrders() {
        Object left = new Double(1.0);
        Object right = new Double(-1.0);

        Assert.assertEquals(Boolean.FALSE, expression.compute(left, right));
    }
}