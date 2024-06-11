import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testConstructorAndGetters() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        assertEquals("Enum1", enum1.getName());
        assertEquals(1, enum1.getValue());
        
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        assertEquals("Enum2", enum2.getName());
        assertEquals(2, enum2.getValue());
        
        ValuedEnum enum3 = new ValuedEnumImpl("Enum3", 3);
        assertEquals("Enum3", enum3.getName());
        assertEquals(3, enum3.getValue());
    }

    @Test
    public void testGetEnumWithValidValue() {
        ValuedEnum enum1 = ValuedEnum.getEnum(ValuedEnumImpl.class, 1);
        assertEquals("Enum1", enum1.getName());
        assertEquals(1, enum1.getValue());
        
        ValuedEnum enum2 = ValuedEnum.getEnum(ValuedEnumImpl.class, 2);
        assertEquals("Enum2", enum2.getName());
        assertEquals(2, enum2.getValue());
        
        ValuedEnum enum3 = ValuedEnum.getEnum(ValuedEnumImpl.class, 3);
        assertEquals("Enum3", enum3.getName());
        assertEquals(3, enum3.getValue());
    }

    @Test
    public void testGetEnumWithInvalidValue() {
        ValuedEnum enum1 = ValuedEnum.getEnum(ValuedEnumImpl.class, 4);
        assertNull(enum1);
        
        ValuedEnum enum2 = ValuedEnum.getEnum(ValuedEnumImpl.class, 5);
        assertNull(enum2);
        
        ValuedEnum enum3 = ValuedEnum.getEnum(ValuedEnumImpl.class, 6);
        assertNull(enum3);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        ValuedEnum enum3 = new ValuedEnumImpl("Enum3", 3);

        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(1, enum2.compareTo(enum1));
        assertEquals(-1, enum2.compareTo(enum3));
        assertEquals(1, enum3.compareTo(enum2));
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        assertEquals("ValuedEnumImpl[Enum1=1]", enum1.toString());

        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        assertEquals("ValuedEnumImpl[Enum2=2]", enum2.toString());

        ValuedEnum enum3 = new ValuedEnumImpl("Enum3", 3);
        assertEquals("ValuedEnumImpl[Enum3=3]", enum3.toString());
    }
}

class ValuedEnumImpl extends ValuedEnum {
    
    public ValuedEnumImpl(String name, int value) {
        super(name, value);
    }
}