import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertEquals(100, valuedEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum valuedEnum1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum valuedEnum2 = new JavaVersionEnum("Java 1.1", 110);
        assertTrue(valuedEnum1.compareTo(valuedEnum2) < 0);
        assertTrue(valuedEnum2.compareTo(valuedEnum1) > 0);
        assertTrue(valuedEnum1.compareTo(valuedEnum1) == 0);
    }

    @Test
    public void testToString() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("JavaVersion[Java 1.0=100]", valuedEnum.toString());
    }
}