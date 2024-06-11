import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenBothArgsAreSameInstance_thenShouldReturnTrue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreEqual_thenShouldReturnTrue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreDifferentInstancesButHaveEqualValues_thenShouldReturnTrue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNull_thenShouldReturnFalse() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsNullAndSecondArgIsNull_thenShouldThrowIllegalArgumentException() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNotNullAndSecondArgIsNull_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new Integer(1));
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNotInstanceOfNumberAndSecondArgIsInstanceOfNumber_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new String("1"));
        Expression arg2 = new Expression(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfNumberAndSecondArgIsNotInstanceOfNumber_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new Integer(1));
        Expression arg2 = new Expression(new String("1"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfBooleanAndSecondArgIsInstanceOfBoolean_thenShouldReturnTrue() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfBooleanAndSecondArgIsFalse_thenShouldReturnFalse() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsFalseAndSecondArgIsInstanceOfBoolean_thenShouldReturnFalse() {
        Expression arg1 = new Expression(Boolean.FALSE);
        Expression arg2 = new Expression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreInstanceOfList_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>());
        Expression arg2 = new Expression(new java.util.ArrayList<Object>());
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreInstanceOfMap_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new java.util.HashMap<Object, Object>());
        Expression arg2 = new Expression(new java.util.HashMap<Object, Object>());
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfListAndSecondArgIsInstanceOfMap_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>());
        Expression arg2 = new Expression(new java.util.HashMap<Object, Object>());
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfMapAndSecondArgIsInstanceOfList_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new java.util.HashMap<Object, Object>());
        Expression arg2 = new Expression(new java.util.ArrayList<Object>());
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfListAndSecondArgIsNull_thenShouldThrowIllegalArgumentException() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>());
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfMapAndSecondArgIsNull_thenShouldThrowIllegalArgumentException() {
        Expression arg1 = new Expression(new java.util.HashMap<Object, Object>());
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfListAndSecondArgIsAnotherListWithDifferentSize_thenShouldThrowIllegalArgumentException() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>());
        Expression arg2 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1, 2)));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfMapAndSecondArgIsAnotherMapWithDifferentSize_thenShouldThrowIllegalArgumentException() {
        Expression arg1 = new Expression(new java.util.HashMap<Object, Object>());
        Expression arg2 = new Expression(new java.util.HashMap<Object, Object>(Arrays.asList(new NetscapeUtil.MapEntry("k1", "v1"), new NetscapeUtil.MapEntry("k2", "v2"))));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreCollectionsWithSameElements_thenShouldReturnTrue() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1, 2, 3)));
        Expression arg2 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1, 2, 3)));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreCollectionsWithDifferentOrderOfElementsButSameElements_thenShouldReturnTrue() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1, 3, 2)));
        Expression arg2 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(2, 3, 1)));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsCollectionAndSecondArgIsSingleElementCollection_thenShouldReturnTrue() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1, 2, 3)));
        Expression arg2 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1)));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsSingleElementCollectionAndSecondArgIsCollection_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1)));
        Expression arg2 = new Expression(new java.util.ArrayList<Object>(Arrays.asList(1, 2, 3)));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreInstanceOfString_thenShouldReturnTrue() {
        Expression arg1 = new Expression("hello");
        Expression arg2 = new Expression("hello");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreDifferentInstancesOfStringButHaveEqualValues_thenShouldReturnTrue() {
        Expression arg1 = new Expression("hello");
        Expression arg2 = new Expression(new String("hello"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfStringAndSecondArgIsInstanceOfNumber_thenShouldReturnFalse() {
        Expression arg1 = new Expression("hello");
        Expression arg2 = new Expression(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfNumberAndSecondArgIsInstanceOfString_thenShouldReturnFalse() {
        Expression arg1 = new Expression(new Integer(1));
        Expression arg2 = new Expression("hello");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}