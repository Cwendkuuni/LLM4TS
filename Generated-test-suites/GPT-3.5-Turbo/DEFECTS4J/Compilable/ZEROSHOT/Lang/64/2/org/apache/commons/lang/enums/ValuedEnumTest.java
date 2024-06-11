package org.apache.commons.lang.enums;
import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new TestEnum("Enum 1", 1);
        assertEquals(1, enum1.getValue());

        ValuedEnum enum2 = new TestEnum("Enum 2", 2);
        assertEquals(2, enum2.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new TestEnum("Enum 1", 1);
        ValuedEnum enum2 = new TestEnum("Enum 2", 2);
        ValuedEnum enum3 = new TestEnum("Enum 3", 3);

        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum1) > 0);
        assertEquals(0, enum2.compareTo(enum2));
        assertTrue(enum3.compareTo(enum2) > 0);
        assertTrue(enum2.compareTo(enum3) < 0);
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new TestEnum("Enum 1", 1);
        assertEquals("TestEnum[Enum 1=1]", enum1.toString());

        ValuedEnum enum2 = new TestEnum("Enum 2", 2);
        assertEquals("TestEnum[Enum 2=2]", enum2.toString());
    }
    
    // Custom enum class used for testing
    public static final class TestEnum extends ValuedEnum {
        public static final TestEnum ENUM_1 = new TestEnum("Enum 1", 1);
        public static final TestEnum ENUM_2 = new TestEnum("Enum 2", 2);

        private TestEnum(String name, int value) {
            super(name, value);
        }
    }
}