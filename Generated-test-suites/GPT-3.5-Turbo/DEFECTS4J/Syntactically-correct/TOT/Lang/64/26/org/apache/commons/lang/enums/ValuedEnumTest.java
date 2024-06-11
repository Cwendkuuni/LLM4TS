import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ValuedEnumTest {

    private Enum enum1;
    private Enum enum2;
    private Enum enum3;

    @Before
    public void setUp() {
        enum1 = new JavaVersionEnum("Java 1.0", 100);
        enum2 = new JavaVersionEnum("Java 1.1", 110);
        enum3 = new JavaVersionEnum("Java 1.2", 120);
    }

    @Test
    public void testConstructor() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.3", 130);
        assertEquals("Java 1.3", valuedEnum.getName());
        assertEquals(130, valuedEnum.getValue());
    }

    @Test
    public void testGetEnumByClassAndValue() {
        assertEquals(enum1, ValuedEnum.getEnum(JavaVersionEnum.class, 100));
        assertEquals(enum2, ValuedEnum.getEnum(JavaVersionEnum.class, 110));
        assertEquals(enum3, ValuedEnum.getEnum(JavaVersionEnum.class, 120));
    }

    @Test
    public void testGetEnumByClassAndValueReturnsNull() {
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 90));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 150));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 200));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test
    public void testGetValue() {
        assertEquals(100, enum1.getValue());
        assertEquals(110, enum2.getValue());
        assertEquals(120, enum3.getValue());
    }

    @Test
    public void testCompareTo() {
        assertTrue(enum1.compareTo(enum1) == 0);
        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum1) > 0);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentType() {
        enum1.compareTo(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToWithNull() {
        enum1.compareTo(null);
    }

    @Test
    public void testToString() {
        assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());
        assertEquals("JavaVersion[Java 1.1=110]", enum2.toString());
        assertEquals("JavaVersion[Java 1.2=120]", enum3.toString());
    }
}