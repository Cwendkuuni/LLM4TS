import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValuedEnumTest {

    // Test cases for constructor
    @Test
    public void testConstructor() {
        ValuedEnum enumValue = new JavaVersionEnum("Java 1.0", 100);
        Assertions.assertEquals("Java 1.0", enumValue.getName());
        Assertions.assertEquals(100, enumValue.getValue());
    }

    // Test cases for getEnum()
    @Test
    public void testGetEnumWithValidString() {
        ValuedEnum enumValue = JavaVersionEnum.getEnum("Java 1.0");
        Assertions.assertEquals("Java 1.0", enumValue.getName());
        Assertions.assertEquals(100, enumValue.getValue());
    }

    @Test
    public void testGetEnumWithValidInt() {
        ValuedEnum enumValue = JavaVersionEnum.getEnum(100);
        Assertions.assertEquals("Java 1.0", enumValue.getName());
        Assertions.assertEquals(100, enumValue.getValue());
    }

    @Test
    public void testGetEnumWithInvalidString() {
        ValuedEnum enumValue = JavaVersionEnum.getEnum("Invalid Version");
        Assertions.assertNull(enumValue);
    }

    // Test cases for getEnumMap()
    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        Assertions.assertEquals(4, enumMap.size());
        Assertions.assertTrue(enumMap.containsKey("Java 1.0"));
        Assertions.assertTrue(enumMap.containsValue(100));
    }

    // Test cases for getEnumList()
    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        Assertions.assertEquals(4, enumList.size());
        Assertions.assertEquals("Java 1.0", enumList.get(0).getName());
        Assertions.assertEquals(100, enumList.get(0).getValue());
    }

    // Test cases for iterator()
    @Test
    public void testIterator() {
        Iterator enumIterator = JavaVersionEnum.iterator();
        Assertions.assertTrue(enumIterator.hasNext());
        Assertions.assertEquals("Java 1.0", enumIterator.next().getName());
        Assertions.assertEquals(100, enumIterator.next().getValue());
    }

    // Test case for compareTo()
    @Test
    public void testCompareTo() {
        ValuedEnum enumValue1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enumValue2 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum enumValue3 = new JavaVersionEnum("Java 1.2", 120);

        Assertions.assertTrue(enumValue1.compareTo(enumValue2) < 0);
        Assertions.assertTrue(enumValue2.compareTo(enumValue3) < 0);
        Assertions.assertTrue(enumValue3.compareTo(enumValue1) > 0);
        Assertions.assertTrue(enumValue1.compareTo(enumValue1) == 0);
    }

    // Test case for toString()
    @Test
    public void testToString() {
        ValuedEnum enumValue = new JavaVersionEnum("Java 1.0", 100);
        Assertions.assertEquals("JavaVersionEnum[Java 1.0=100]", enumValue.toString());
    }
}