import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    @Mock
    private EvalContext context;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;

    @Mock
    private Pointer pointerValue1;

    @Mock
    private Pointer pointerValue2;

    @Mock
    private Iterator iterator1;

    @Mock
    private Iterator iterator2;

    @Mock
    private Collection collection;

    private CoreOperationCompare coreOperationCompare;

    @Test
    public void testEqual() {
        Mockito.when(arg1.compute(context)).thenReturn(pointerValue1);
        Mockito.when(arg2.compute(context)).thenReturn(pointerValue2);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");
        Mockito.when(pointerValue2.getValue()).thenReturn("value1");

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_Null_Values() {
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(arg1.compute(context)).thenReturn(null);
        Mockito.when(arg2.compute(context)).thenReturn(null);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_NonEqual_Values() {
        Mockito.when(arg1.compute(context)).thenReturn(pointerValue1);
        Mockito.when(arg2.compute(context)).thenReturn(pointerValue2);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");
        Mockito.when(pointerValue2.getValue()).thenReturn("value2");

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_Iterator_And_Value() {
        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn("value2");

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(iterator1.hasNext()).thenReturn(true);
        Mockito.when(iterator1.next()).thenReturn(pointerValue1);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_Value_And_Iterator() {
        Mockito.when(arg1.compute(context)).thenReturn("value1");
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(iterator2.hasNext()).thenReturn(true);
        Mockito.when(iterator2.next()).thenReturn(pointerValue2);
        Mockito.when(pointerValue2.getValue()).thenReturn("value2");

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_Iterator_And_Iterator() {
        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(iterator1.hasNext()).thenReturn(true);
        Mockito.when(iterator1.next()).thenReturn(pointerValue1);
        Mockito.when(iterator2.hasNext()).thenReturn(true);
        Mockito.when(iterator2.next()).thenReturn(pointerValue2);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");
        Mockito.when(pointerValue2.getValue()).thenReturn("value1");

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        Mockito.when(arg1.compute(context)).thenReturn(collection);
        Mockito.when(arg2.compute(context)).thenReturn("value2");

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(collection.iterator()).thenReturn(iterator1);
        Mockito.when(iterator1.hasNext()).thenReturn(true);
        Mockito.when(iterator1.next()).thenReturn(pointerValue1);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");

        assertFalse(coreOperationCompare.contains(context, arg1, arg2));
    }

    @Test
    public void testContains_Empty_Collection() {
        Mockito.when(arg1.compute(context)).thenReturn(collection);
        Mockito.when(arg2.compute(context)).thenReturn("value2");

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(collection.iterator()).thenReturn(iterator1);
        Mockito.when(iterator1.hasNext()).thenReturn(false);

        assertFalse(coreOperationCompare.contains(context, arg1, arg2));
    }

    @Test
    public void testContains_Values_Matching_Collection_Element() {
        Mockito.when(arg1.compute(context)).thenReturn(collection);
        Mockito.when(arg2.compute(context)).thenReturn("value1");

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(collection.iterator()).thenReturn(iterator1);
        Mockito.when(iterator1.hasNext()).thenReturn(true);
        Mockito.when(iterator1.next()).thenReturn(pointerValue1);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");

        assertTrue(coreOperationCompare.contains(context, arg1, arg2));
    }

    @Test
    public void testFindMatch() {
        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(iterator1.hasNext()).thenReturn(true);
        Mockito.when(iterator1.next()).thenReturn(pointerValue1);
        Mockito.when(iterator2.hasNext()).thenReturn(true);
        Mockito.when(iterator2.next()).thenReturn(pointerValue2);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");
        Mockito.when(pointerValue2.getValue()).thenReturn("value1");

        assertTrue(coreOperationCompare.findMatch(context, arg1, arg2));
    }

    @Test
    public void testFindMatch_No_Matching_Values() {
        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Mockito.when(iterator1.hasNext()).thenReturn(true);
        Mockito.when(iterator1.next()).thenReturn(pointerValue1);
        Mockito.when(iterator2.hasNext()).thenReturn(true);
        Mockito.when(iterator2.next()).thenReturn(pointerValue2);
        Mockito.when(pointerValue1.getValue()).thenReturn("value1");
        Mockito.when(pointerValue2.getValue()).thenReturn("value2");

        assertFalse(coreOperationCompare.findMatch(context, arg1, arg2));
    }
}