import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // Create a mock object
        Object mock = new Object();
        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expected, emptyValues.answer(InvocationOnMock.builder()
                .mock(mock)
                .method(mock.getClass().getDeclaredMethod("toString"))
                .build()));
    }

    @Test
    public void testAnswer_compareToMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        // Create a mock object
        Object mock = new Object();
        Object argument = new Object();
        assertEquals(0, emptyValues.answer(InvocationOnMock.builder()
                .mock(mock)
                .method(mock.getClass().getDeclaredMethod("compareTo", Object.class))
                .arguments(new Object[]{mock})
                .build()));

        assertNotEquals(0, emptyValues.answer(InvocationOnMock.builder()
                .mock(mock)
                .method(mock.getClass().getDeclaredMethod("compareTo", Object.class))
                .arguments(new Object[]{argument})
                .build()));
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(0, emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getInt"))
                .build()));

        assertEquals(0L, emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getLong"))
                .build()));

        assertEquals(0.0f, emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getFloat"))
                .build()), 0.0f);

        assertEquals(0.0, emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getDouble"))
                .build()), 0.0);

        assertEquals('\0', emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getChar"))
                .build()));

        assertFalse(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getBoolean"))
                .build()));
    }

    @Test
    public void testAnswer_wrapperClassReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(new Integer(0), emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getInteger"))
                .build()));

        assertEquals(new Long(0L), emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getLongObject"))
                .build()));

        assertEquals(new Float(0.0f), emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getFloatObject"))
                .build()));

        assertEquals(new Double(0.0), emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getDoubleObject"))
                .build()));

        assertEquals(new Character('\0'), emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getCharacter"))
                .build()));

        assertEquals(Boolean.FALSE, emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getBooleanObject"))
                .build()));
    }

    @Test
    public void testAnswer_collectionReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getCollection"))
                .build()) instanceof List);

        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getSet"))
                .build()) instanceof Set);

        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getSortedSet"))
                .build()) instanceof SortedSet);

        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getLinkedHashSet"))
                .build()) instanceof LinkedHashSet);
    }

    @Test
    public void testAnswer_mapReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getMap"))
                .build()) instanceof Map);

        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getSortedMap"))
                .build()) instanceof SortedMap);

        assertTrue(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getLinkedHashMap"))
                .build()) instanceof LinkedHashMap);
    }

    @Test
    public void testAnswer_otherReturnType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertNull(emptyValues.answer(InvocationOnMock.builder()
                .method(ExampleClass.class.getDeclaredMethod("getObject"))
                .build()));
    }

    private static class ExampleClass {
        public int getInt() {
            return 0;
        }

        public long getLong() {
            return 0L;
        }

        public float getFloat() {
            return 0.0f;
        }

        public double getDouble() {
            return 0.0;
        }

        public char getChar() {
            return '\0';
        }

        public boolean getBoolean() {
            return false;
        }

        public Integer getInteger() {
            return new Integer(0);
        }

        public Long getLongObject() {
            return new Long(0L);
        }

        public Float getFloatObject() {
            return new Float(0.0f);
        }

        public Double getDoubleObject() {
            return new Double(0.0);
        }

        public Character getCharacter() {
            return new Character('\0');
        }

        public Boolean getBooleanObject() {
            return Boolean.FALSE;
        }

        public Collection<Object> getCollection() {
            return new LinkedList<Object>();
        }

        public Set<Object> getSet() {
            return new HashSet<Object>();
        }

        public SortedSet<Object> getSortedSet() {
            return new TreeSet<Object>();
        }

        public LinkedHashSet<Object> getLinkedHashSet() {
            return new LinkedHashSet<Object>();
        }

        public Map<Object, Object> getMap() {
            return new HashMap<Object, Object>();
        }

        public SortedMap<Object, Object> getSortedMap() {
            return new TreeMap<Object, Object>();
        }

        public LinkedHashMap<Object, Object> getLinkedHashMap() {
            return new LinkedHashMap<Object, Object>();
        }

        public Object getObject() {
            return null;
        }
    }
}