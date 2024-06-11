import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Map;
import java.util.List;
import org.junit.Test;
import org.apache.commons.lang.enums.EnumUtils;

public class ValuedEnumTest {

	@Test
	public void testGetEnum() {
        ValuedEnum enumValue = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(ValuedEnum.JAVA1_0, enumValue);
        
        enumValue = JavaVersionEnum.getEnum(100);
        assertEquals(ValuedEnum.JAVA1_0, enumValue);
        
        enumValue = JavaVersionEnum.getEnum("nonexistent");
        assertNull(enumValue);
    }
	
	@Test
	public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        assertEquals(4, enumMap.size());
        assertTrue(enumMap.containsKey("Java 1.0"));
        assertTrue(enumMap.containsValue(100));
    }
	
	@Test
	public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        assertEquals(4, enumList.size());
        assertTrue(enumList.contains(ValuedEnum.JAVA1_0));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_1));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_2));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_3));
    }

	@Test
	public void testIterator() {
        Iterator enumIterator = JavaVersionEnum.iterator();
        assertTrue(enumIterator.hasNext());
        assertEquals(ValuedEnum.JAVA1_0, enumIterator.next());
        assertTrue(enumIterator.hasNext());
        assertEquals(ValuedEnum.JAVA1_1, enumIterator.next());
        assertTrue(enumIterator.hasNext());
        assertEquals(ValuedEnum.JAVA1_2, enumIterator.next());
        assertTrue(enumIterator.hasNext());
        assertEquals(ValuedEnum.JAVA1_3, enumIterator.next());
        assertFalse(enumIterator.hasNext());
    }
	
	@Test
	public void testCompareTo() {
        assertTrue(ValuedEnum.JAVA1_0.compareTo(ValuedEnum.JAVA1_1) < 0);
        assertTrue(ValuedEnum.JAVA1_1.compareTo(ValuedEnum.JAVA1_0) > 0);
        assertTrue(ValuedEnum.JAVA1_1.compareTo(ValuedEnum.JAVA1_1) == 0);
    }
	
	@Test
	public void testToString() {
        String expectedToString = "JavaVersionEnum[Java 1.0=100]";
        assertEquals(expectedToString, ValuedEnum.JAVA1_0.toString());
    }
	
	@Test
	public void testGetEnumWithNullClass() {
        try {
            ValuedEnum.getEnum(null, 100);
            fail("IllegalArgumentException should be thrown when class is null.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}