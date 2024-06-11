import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare instanceof CoreOperationCompare);
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
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual_sameObjects() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Object left = new Object();
        Object right = left;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertTrue(coreOperationCompare.equal(context, NullExpression.INSTANCE, NullExpression.INSTANCE));
        assertTrue(coreOperationCompare.equal(context, NullExpression.INSTANCE, NullExpression.INSTANCE));
        assertTrue(coreOperationCompare.equal(context, left, right));
        assertTrue(coreOperationCompare.equal(context, right, left));
    }

    @Test
    public void testEqual_booleanValues() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertTrue(coreOperationCompare.equal(context, BooleanValueExpression.TRUE, BooleanValueExpression.TRUE));
        assertTrue(coreOperationCompare.equal(context, BooleanValueExpression.TRUE, BooleanValueExpression.TRUE));
        assertTrue(coreOperationCompare.equal(context, BooleanValueExpression.FALSE, BooleanValueExpression.FALSE));
        assertTrue(coreOperationCompare.equal(context, BooleanValueExpression.FALSE, BooleanValueExpression.FALSE));
        assertFalse(coreOperationCompare.equal(context, BooleanValueExpression.TRUE, BooleanValueExpression.FALSE));
        assertFalse(coreOperationCompare.equal(context, BooleanValueExpression.FALSE, BooleanValueExpression.TRUE));
    }

    @Test
    public void testEqual_numbers() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertTrue(coreOperationCompare.equal(context, DoubleValueExpression.valueOf(1.0), DoubleValueExpression.valueOf(1.0)));
        assertTrue(coreOperationCompare.equal(context, DoubleValueExpression.valueOf(1.0), DoubleValueExpression.valueOf(1.0)));
        assertFalse(coreOperationCompare.equal(context, DoubleValueExpression.valueOf(1.0), DoubleValueExpression.valueOf(2.0)));
        assertFalse(coreOperationCompare.equal(context, DoubleValueExpression.valueOf(2.0), DoubleValueExpression.valueOf(1.0)));
    }

    @Test
    public void testEqual_strings() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertTrue(coreOperationCompare.equal(context, StringConstantExpression.INSTANCE("hello"), StringConstantExpression.INSTANCE("hello")));
        assertTrue(coreOperationCompare.equal(context, StringConstantExpression.INSTANCE("hello"), StringConstantExpression.INSTANCE("hello")));
        assertFalse(coreOperationCompare.equal(context, StringConstantExpression.INSTANCE("hello"), StringConstantExpression.INSTANCE("world")));
        assertFalse(coreOperationCompare.equal(context, StringConstantExpression.INSTANCE("world"), StringConstantExpression.INSTANCE("hello")));
    }

    @Test
    public void testEqual_pointers() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Pointer left = Mockito.mock(Pointer.class);
        Pointer right = Mockito.mock(Pointer.class);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        PowerMockito.when(left.getValue()).thenReturn("value1");
        PowerMockito.when(right.getValue()).thenReturn("value1");
        assertTrue(coreOperationCompare.equal(context, left, right));
        assertTrue(coreOperationCompare.equal(context, right, left));
    }

    @Test
    public void testContains_emptyCollection_and_value() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Iterator it = Collections.emptyIterator();
        Object value = "value";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testContains_collection_and_value() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Iterator it = Arrays.asList("value1", "value2", "value3").iterator();
        Object value = "value2";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertTrue(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch_emptySet_and_iterator() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Collection left = new HashSet();
        Iterator lit = Collections.emptyIterator();
        Iterator rit = Arrays.asList("value1", "value2", "value3").iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testFindMatch_set_and_iterator() {
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Collection left = new HashSet(Arrays.asList("value1", "value2", "value3"));
        Iterator lit = left.iterator();
        Iterator rit = Arrays.asList("value2", "value1", "value3").iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(NullExpression.INSTANCE, NullExpression.INSTANCE);
        assertTrue(coreOperationCompare.findMatch(lit, rit));
    }

}