import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        ValuedEnum valEnum = new ValuedEnumImpl("Test", 10);
        assertNotNull(valEnum);
        assertEquals("Test", valEnum.getName());
        assertEquals(10, valEnum.getValue());
    }

    // Test case for getEnum()
    @Test
    public void testGetEnum() {
        ValuedEnum valEnum = new ValuedEnumImpl("Test", 10);
        assertEquals(valEnum, ValuedEnum.getEnum(ValuedEnumImpl.class, 10));
    }

    // Test case for getEnum() - invalid case, enum does not exist
    @Test
    public void testGetEnumInvalid() {
        assertNull(ValuedEnum.getEnum(ValuedEnumImpl.class, 20));
    }

    // Test case for getValue()
    @Test
    public void testGetValue() {
        ValuedEnum valEnum = new ValuedEnumImpl("Test", 10);
        assertEquals(10, valEnum.getValue());
    }

    // Test case for compareTo() - equal values
    @Test
    public void testCompareToEqual() {
        ValuedEnum valEnum1 = new ValuedEnumImpl("Test1", 10);
        ValuedEnum valEnum2 = new ValuedEnumImpl("Test2", 10);
        assertEquals(0, valEnum1.compareTo(valEnum2));
    }

    // Test case for compareTo() - this value is less than other
    @Test
    public void testCompareToLess() {
        ValuedEnum valEnum1 = new ValuedEnumImpl("Test1", 10);
        ValuedEnum valEnum2 = new ValuedEnumImpl("Test2", 20);
        assertTrue(valEnum1.compareTo(valEnum2) < 0);
    }

    // Test case for compareTo() - this value is greater than other
    @Test
    public void testCompareToGreater() {
        ValuedEnum valEnum1 = new ValuedEnumImpl("Test1", 20);
        ValuedEnum valEnum2 = new ValuedEnumImpl("Test2", 10);
        assertTrue(valEnum1.compareTo(valEnum2) > 0);
    }

    // Test case for toString()
    @Test
    public void testToString() {
        ValuedEnum valEnum = new ValuedEnumImpl("Test", 10);
        assertEquals("ValuedEnumImpl[Test=10]", valEnum.toString());
    }

    // Test case for getEnumList()
    @Test
    public void testGetEnumList() {
        assertNotNull(ValuedEnum.getEnumList(ValuedEnumImpl.class));
        assertEquals(1, ValuedEnum.getEnumList(ValuedEnumImpl.class).size());
    }

    // Test case for iterator()
    @Test
    public void testIterator() {
        assertNotNull(ValuedEnum.iterator(ValuedEnumImpl.class));
        assertTrue(ValuedEnum.iterator(ValuedEnumImpl.class).hasNext());
    }

}

class ValuedEnumImpl extends ValuedEnum {
    public ValuedEnumImpl(String name, int value) {
        super(name, value);
    }
}