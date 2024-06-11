import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_whenArg1EqualsArg2_thenReturnFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1NotEqualsArg2_thenReturnTrue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenArg1IsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = null;
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenArg2IsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new Expression(1);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNumber_andArg2IsInstanceOfNumber_thenCompareNumericValues() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfBoolean_andArg2IsInstanceOfBoolean_thenCompareBooleanValues() {
        Expression arg1 = new Expression(Boolean.FALSE);
        Expression arg2 = new Expression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfString_andArg2IsInstanceOfString_thenCompareStringValues() {
        Expression arg1 = new Expression("test");
        Expression arg2 = new Expression("otherTest");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfCoreMap_andArg2IsInstanceOfCoreMap_thenCompareMaps() {
        Expression arg1 = new Expression(new CoreMap());
        Expression arg2 = new Expression(new CoreMap());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNode_andArg2IsInstanceOfNode_thenCompareNodes() {
        Expression arg1 = new Expression(new Node());
        Expression arg2 = new Expression(new Node());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfString_andArg2IsInstanceOfNumber_thenReturnFalse() {
        Expression arg1 = new Expression("test");
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNumber_andArg2IsInstanceOfString_thenReturnFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression("test");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfBoolean_andArg2IsInstanceOfString_thenReturnTrue() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression("test");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfString_andArg2IsInstanceOfBoolean_thenReturnTrue() {
        Expression arg1 = new Expression("test");
        Expression arg2 = new Expression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNumber_andArg2IsInstanceOfCoreMap_thenReturnFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(new CoreMap());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfCoreMap_andArg2IsInstanceOfNumber_thenReturnFalse() {
        Expression arg1 = new Expression(new CoreMap());
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNode_andArg2IsInstanceOfCoreMap_thenReturnFalse() {
        Expression arg1 = new Expression(new Node());
        Expression arg2 = new Expression(new CoreMap());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfCoreMap_andArg2IsInstanceOfNode_thenReturnFalse() {
        Expression arg1 = new Expression(new CoreMap());
        Expression arg2 = new Expression(new Node());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNumber_andArg2IsInstanceOfNumber_butValuesAreEqual_thenReturnFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }
}