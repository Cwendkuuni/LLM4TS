import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.List;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertNotNull(valuedEnum);
        assertEquals("Java 1.0", valuedEnum.getName());
        assertEquals(100, valuedEnum.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum(100);
        assertNotNull(enum1);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());

        ValuedEnum enum2 = JavaVersionEnum.getEnum(999); // Non-existent value
        assertNull(enum2);
    }

    @Test
    public void testGetValue() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertEquals(100, valuedEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);

        assertEquals(-10, enum1.compareTo(enum2));
        assertEquals(10, enum2.compareTo(enum1));
        assertEquals(0, enum1.compareTo(enum1));
    }

    @Test
    public void testToString() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("JavaVersion[Java 1.0=100]", valuedEnum.toString());
    }

}