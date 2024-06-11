import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof String);
        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testAnswer_compareToMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof Integer);
        assertEquals(1, result);
    }

    @Test
    public void testAnswer_primitiveReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[0]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_primitiveWrapperReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[1]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_collectionReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[2]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_setReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[3]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_hashSetReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[4]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[5]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_treeSetReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[6]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[7]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_listReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[8]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedListReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[9]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[10]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }

    @Test
    public void testAnswer_mapReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[11]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_hashMapReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[12]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[13]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_treeMapReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[14]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[15]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_otherTypeReturningMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethods()[16]);

        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.answer(invocation);

        assertNull(result);
    }

    // Test Class for mocking purposes
    private static class TestClass {
        public int primitiveMethod() {
            return 0;
        }

        public Integer wrapperMethod() {
            return 0;
        }

        public Collection<?> collectionMethod() {
            return null;
        }

        public Set<?> setMethod() {
            return null;
        }

        public HashSet<?> hashSetMethod() {
            return null;
        }

        public SortedSet<?> sortedSetMethod() {
            return null;
        }

        public TreeSet<?> treeSetMethod() {
            return null;
        }

        public LinkedHashSet<?> linkedHashSetMethod() {
            return null;
        }

        public List<?> listMethod() {
            return null;
        }

        public LinkedList<?> linkedListMethod() {
            return null;
        }

        public ArrayList<?> arrayListMethod() {
            return null;
        }

        public Map<?, ?> mapMethod() {
            return null;
        }

        public HashMap<?, ?> hashMapMethod() {
            return null;
        }

        public SortedMap<?, ?> sortedMapMethod() {
            return null;
        }

        public TreeMap<?, ?> treeMapMethod() {
            return null;
        }

        public LinkedHashMap<?, ?> linkedHashMapMethod() {
            return null;
        }

        public String otherTypeMethod() {
            return null;
        }
    }

}