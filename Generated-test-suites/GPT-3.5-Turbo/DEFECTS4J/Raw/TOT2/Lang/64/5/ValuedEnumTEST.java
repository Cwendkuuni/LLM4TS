import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testConstructor() {
        ValuedEnum valuedEnum = new JavaVersionEnum("TestEnum", 200);
        assertEquals("TestEnum", valuedEnum.getName());
        assertEquals(200, valuedEnum.getValue());
    }
    
    @Test
    public void testGetEnumByValue() {
        ValuedEnum enumValue = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", enumValue.getName());
        assertEquals(120, enumValue.getValue());
    }
    
    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum(100);
        ValuedEnum enum2 = JavaVersionEnum.getEnum(110);
        
        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum1) > 0);
        assertEquals(0, enum1.compareTo(JavaVersionEnum.getEnum(100)));
    }
    
    @Test
    public void testToString() {
        ValuedEnum valuedEnum = JavaVersionEnum.getEnum(130);
        assertEquals("JavaVersionEnum[Java 1.3=130]", valuedEnum.toString());
    }
}