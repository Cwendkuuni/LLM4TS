import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        assertEquals(1, enum1.getValue());
        
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 100);
        assertEquals(100, enum2.getValue());
    }
    
    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        
        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum1) > 0);
        assertTrue(enum1.compareTo(enum1) == 0);
    }
    
    @Test
    public void testToString() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        assertEquals("ValuedEnumImpl[Enum1=1]", enum1.toString());
        
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 100);
        assertEquals("ValuedEnumImpl[Enum2=100]", enum2.toString());
    }
    
    // Additional test cases:
    
    @Test
    public void testGetEnum_NullClass() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ValuedEnum.getEnum(null, 1);
        });
        
        String expectedMessage = "The Enum Class must not be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testGetEnum_InvalidValue() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 100);
        
        assertEquals(enum1, ValuedEnum.getEnum(ValuedEnumImpl.class, 1));
        assertEquals(enum2, ValuedEnum.getEnum(ValuedEnumImpl.class, 100));
        assertNull(ValuedEnum.getEnum(ValuedEnumImpl.class, 10));
    }
    
    private class ValuedEnumImpl extends ValuedEnum {
        
        public ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}