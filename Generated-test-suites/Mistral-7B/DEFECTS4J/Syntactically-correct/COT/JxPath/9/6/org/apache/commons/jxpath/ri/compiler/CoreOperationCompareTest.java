import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private EvalContext context;

    @Before
    public void setUp() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testEqual_whenBothAreInitialContext_thenReturnFalse() {
        coreOperationCompare.equal(context, coreOperationCompare.getArg(0), coreOperationCompare.getArg(1));
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreSelfContext_thenReturnFalse() {
        coreOperationCompare.equal(context, new SelfContext(), new SelfContext());
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreCollections_thenReturnTrueIfContainSameElements() {
        List<Object> list1 = new ArrayList<>();
        list1.add(new Object());
        List<Object> list2 = new ArrayList<>();
        list2.add(new Object());
        coreOperationCompare.equal(context, list1, list2);
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreCollections_thenReturnFalseIfNotContainSameElements() {
        List<Object> list1 = new ArrayList<>();
        list1.add(new Object());
        List<Object> list2 = new ArrayList<>();
        list2.add(new Object());
        list2.add(new Object());
        coreOperationCompare.equal(context, list1, list2);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreIterators_thenReturnTrueIfContainSameElements() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        coreOperationCompare.equal(context, iterator1, iterator2);
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreIterators_thenReturnFalseIfNotContainSameElements() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        iterator2.next();
        coreOperationCompare.equal(context, iterator1, iterator2);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothArePointers_thenReturnTrueIfPointToSameObject() {
        Pointer pointer1 = new PointerMock();
        Pointer pointer2 = new PointerMock();
        pointer1.setValue(new Object());
        pointer2.setValue(new Object());
        pointer1.setValue(pointer2.getValue());
        coreOperationCompare.equal(context, coreOperationCompare.getArg(0), coreOperationCompare.getArg(1));
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreStrings_thenReturnTrueIfStringsAreEqual() {
        coreOperationCompare.equal(context, "string1", "string1");
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreNumbers_thenReturnTrueIfNumbersAreEqual() {
        coreOperationCompare.equal(context, 1.0, 1.0);
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenBothAreBooleans_thenReturnTrueIfValuesAreEqual() {
        coreOperationCompare.equal(context, true, true);
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenLeftIsPointerAndRightIsString_thenReturnFalse() {
        Pointer pointer = new PointerMock();
        coreOperationCompare.equal(context, pointer, "string");
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testEqual_whenLeftIsStringAndRightIsPointer_thenReturnFalse() {
        String string = "string";
        Pointer pointer = new PointerMock();
        coreOperationCompare.equal(context, string, pointer);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_whenLeftIsNullAndRightIsNotNull_thenThrowIllegalArgumentException() {
        coreOperationCompare.equal(context, null, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_whenLeftIsNotNullAndRightIsNull_thenThrowIllegalArgumentException() {
        coreOperationCompare.equal(context, new Object(), null);
    }

    @Test
    public void testContains_whenIteratorContainsSingleElement_thenReturnTrue() {
        Iterator<Object> iterator = new Iterator<Object>() {
            private Object element;

            @Override
            public boolean hasNext() {
                return element != null;
            }

            @Override
            public Object next() {
                Object temp = element;
                element = null;
                return temp;
            }
        };
        iterator.next();
        iterator.next();
        coreOperationCompare.contains(context, iterator, new Object());
        assertFalse(coreOperationCompare.getResult());

        coreOperationCompare.contains(context, iterator, new Object()); // second call with the same iterator should return the same result
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testContains_whenIteratorContainsMultipleElements_thenReturnFalse() {
        Iterator<Object> iterator = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        coreOperationCompare.contains(context, iterator, iterator2);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testFindMatch_whenIteratorsContainSameElements_thenReturnTrue() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        coreOperationCompare.findMatch(iterator1, iterator2);
        assertTrue(coreOperationCompare.getResult());
    }

    @Test
    public void testFindMatch_whenIteratorsDoNotContainSameElements_thenReturnFalse() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        coreOperationCompare.findMatch(iterator1, iterator2);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testFindMatch_whenFirstIteratorIsEmpty_thenReturnFalse() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new RuntimeException("Should not be called");
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        coreOperationCompare.findMatch(iterator1, iterator2);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testFindMatch_whenSecondIteratorIsEmpty_thenReturnFalse() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new RuntimeException("Should not be called");
            }
        };
        coreOperationCompare.findMatch(iterator1, iterator2);
        assertFalse(coreOperationCompare.getResult());
    }

    @Test
    public void testFindMatch_whenFirstIteratorHasMoreElementsThanSecondIterator_thenReturnFalse() {
        Iterator<Object> iterator1 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object(), new Object(), new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        Iterator<Object> iterator2 = new Iterator<Object>() {
            private Object[] elements = new Object[] { new Object() };
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                Object temp = elements[index];
                index++;
                return temp;
            }
        };
        coreOperationCompare.findMatch(iterator1, iterator2);
        assertFalse(coreOperationCompare.getResult());
    }
}

class ExpressionMock {
    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}

class PointerMock implements Pointer {
    private Object value;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}