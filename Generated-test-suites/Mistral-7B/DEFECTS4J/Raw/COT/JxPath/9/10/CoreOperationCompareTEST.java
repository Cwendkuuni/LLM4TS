import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.*;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testEqual_BothNull() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(null), new ConstantExpression(null));
        assertTrue(result);
    }

    @Test
    public void testEqual_BothNotNullAndEqual() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(value), new ConstantExpression(value));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(value), new ConstantExpression(value));
        assertTrue(result);
    }

    @Test
    public void testEqual_BothNotNullAndNotEqual() {
        EvalContext context = new EvalContext();
        Object lValue = new Object();
        Object rValue = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(lValue), new ConstantExpression(rValue));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(lValue), new ConstantExpression(rValue));
        assertFalse(result);
    }

    @Test
    public void testEqual_OneIsPointer_OneIsValue() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(value), new VariableExpression("test"));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(value), new VariableExpression("test"));
        assertFalse(result);
    }

    @Test
    public void testEqual_BothPointer() {
        EvalContext context = new EvalContext();
        Object lValue = new Object();
        Pointer lPointer = new Pointer(new Object(), new Class[0], new int[0], new int[0], null);
        lPointer.setValue(lValue);

        Object rValue = new Object();
        Pointer rPointer = new Pointer(new Object(), new Class[0], new int[0], new int[0], null);
        rPointer.setValue(rValue);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(lPointer), new ConstantExpression(rPointer));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(lPointer), new ConstantExpression(rPointer));
        assertTrue(result);
    }

    @Test
    public void testEqual_BothPointerDifferentValues() {
        EvalContext context = new EvalContext();
        Object lValue1 = new Object();
        Pointer lPointer1 = new Pointer(new Object(), new Class[0], new int[0], new int[0], null);
        lPointer1.setValue(lValue1);

        Object lValue2 = new Object();
        Pointer lPointer2 = new Pointer(new Object(), new Class[0], new int[0], new int[0], null);
        lPointer2.setValue(lValue2);

        Object rValue = new Object();
        Pointer rPointer = new Pointer(new Object(), new Class[0], new int[0], new int[0], null);
        rPointer.setValue(rValue);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(lPointer1), new ConstantExpression(rPointer));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(lPointer1), new ConstantExpression(rPointer));
        assertFalse(result);
    }

    @Test
    public void testEqual_OneIsIterable_OtherIsValue() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(Collections.singletonList(value)), new ConstantExpression(value));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(Collections.singletonList(value)), new ConstantExpression(value));
        assertFalse(result);
    }

    @Test
    public void testEqual_BothIterable() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(Arrays.asList(value)), new ConstantExpression(Arrays.asList(value)));
        boolean result = coreOperationCompare.equal(context, new ConstantExpression(Arrays.asList(value)), new ConstantExpression(Arrays.asList(value)));
        assertTrue(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindMatch_BothIteratorsEmpty() {
        Iterator lit = Collections.emptyIterator();
        Iterator rit = Collections.emptyIterator();
        CoreOperationCompare.findMatch(lit, rit);
    }

    @Test
    public void testFindMatch_OneIteratorEmpty_OtherHasElements() {
        Iterator lit = Collections.emptyIterator();
        Iterator rit = Arrays.asList(new Object(), new Object()).iterator();
        assertFalse(CoreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testFindMatch_BothIteratorsHaveElements() {
        Iterator lit = Arrays.asList(new Object(), new Object()).iterator();
        Iterator rit = Arrays.asList(new Object(), new Object()).iterator();
        assertTrue(CoreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testFindMatch_OneIteratorHasMoreElements_OtherIteratorHasFewerElements() {
        Iterator lit = Arrays.asList(new Object(), new Object(), new Object()).iterator();
        Iterator rit = Arrays.asList(new Object(), new Object()).iterator();
        assertFalse(CoreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testContains_IteratorHasElement_OtherIsValue() {
        Object value = new Object();
        Iterator iterator = Arrays.asList(new Object(), value, new Object()).iterator();
        assertTrue(CoreOperationCompare.contains(iterator, value));
    }

    @Test
    public void testContains_IteratorDoesNotHaveElement_OtherIsValue() {
        Object value = new Object();
        Iterator iterator = Arrays.asList(new Object(), new Object()).iterator();
        assertFalse(CoreOperationCompare.contains(iterator, value));
    }

    @Test
    public void testContains_IteratorHasElements_OtherIsIterator() {
        Object value = new Object();
        Iterator iterator1 = Arrays.asList(new Object(), value, new Object()).iterator();
        Iterator iterator2 = Arrays.asList(new Object(), new Object(), new Object()).iterator();
        assertTrue(CoreOperationCompare.contains(iterator1, iterator2));
    }

    @Test
    public void testContains_IteratorDoesNotHaveElement_OtherIsIterator() {
        Object value = new Object();
        Iterator iterator1 = Arrays.asList(new Object(), new Object()).iterator();
        Iterator iterator2 = Arrays.asList(new Object(), new Object(), new Object()).iterator();
        assertFalse(CoreOperationCompare.contains(iterator1, iterator2));
    }

    @Test
    public void testContains_BothIteratorsEmpty() {
        Iterator iterator1 = Collections.emptyIterator();
        Iterator iterator2 = Collections.emptyIterator();
        assertFalse(CoreOperationCompare.contains(iterator1, iterator2));
    }

    @Test
    public void testContains_BothIteratorsHaveSomeElements_OneHasMore() {
        Iterator iterator1 = Arrays.asList(new Object(), new Object(), new Object()).iterator();
        Iterator iterator2 = Arrays.asList(new Object(), new Object()).iterator();
        assertFalse(CoreOperationCompare.contains(iterator1, iterator2));
    }

    @Test
    public void testContains_BothIteratorsHaveSomeElements_OneHasFewer() {
        Iterator iterator1 = Arrays.asList(new Object(), new Object()).iterator();
        Iterator iterator2 = Arrays.asList(new Object(), new Object(), new Object()).iterator();
        assertFalse(CoreOperationCompare.contains(iterator2, iterator1));
    }

    @Test
    public void testContains_BothIteratorsHaveSameElements() {
        Iterator iterator1 = Arrays.asList(new Object(), new Object()).iterator();
        Iterator iterator2 = Arrays.asList(new Object(), new Object()).iterator();
        assertTrue(CoreOperationCompare.contains(iterator1, iterator2));
        assertTrue(CoreOperationCompare.contains(iterator2, iterator1));
    }

}