import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(1),
            new ConstantExpression(1)
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqualDifferentTypes() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression("Hello"),
            new ConstantExpression(1)
        );
        EvalContext context = new EvalContext();
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqualNull() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(null),
            new ConstantExpression(null)
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqualNullAndOther() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(null),
            new ConstantExpression("World")
        );
        EvalContext context = new EvalContext();
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqualDifferentInstances() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(1))
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());

        componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(2))
        );
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(1))
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());

        componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(new String("Hello")),
            new ConstantExpression(new String("Hello"))
        );
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());

        componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(new String("Hello")),
            new ConstantExpression(new String("World"))
        );
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqualIterators() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(Collections.singletonList(1).iterator()),
            new ConstantExpression(Collections.singletonList(1).iterator())
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());

        componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(Collections.singletonList(1).iterator()),
            new ConstantExpression(Collections.singletonList(2).iterator())
        );
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testContains() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(Collections.singletonList(1, 2, 3).iterator()),
            new ConstantExpression(new Integer(2))
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());

        componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(Collections.singletonList(1, 2, 3).iterator()),
            new ConstantExpression(new Integer(4))
        );
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(Collections.singletonList(1, 1, 2, 3).iterator()),
            new ConstantExpression(Collections.singletonList(1, 2, 3, 1).iterator())
        );
        EvalContext context = new EvalContext();
        assertTrue(componentUnderTest.evaluate(context).getBooleanValue());

        componentUnderTest = new CoreOperationCompare(
            new ConstantExpression(Collections.singletonList(1, 1, 2, 3).iterator()),
            new ConstantExpression(Collections.singletonList(2, 3, 4, 5).iterator())
        );
        assertFalse(componentUnderTest.evaluate(context).getBooleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOperation() {
        CoreOperationCompare componentUnderTest = new CoreOperationCompare(null, null);
    }
}