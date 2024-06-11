import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnumByName() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "Java 1.0");
        assertNotNull(java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumByNameNull() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNameInvalidEnumName() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", "InvalidEnumName");
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", 100);
        assertNotNull(java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByValueInvalidValue() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", 200);
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        assertNotNull(enumMap);
        assertEquals(4, enumMap.size());
        ValuedEnum java1_0 = enumMap.get("Java 1.0");
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        assertNotNull(enumList);
        assertEquals(4, enumList.size());
        ValuedEnum java1_0 = enumList.get(0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testIterator() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        int count = 0;
        while (iterator.hasNext()) {
            ValuedEnum next = iterator.next();
            assertEquals(next.getClass(), ValuedEnum.class);
            count++;
        }
        assertEquals(4, count);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumByValueNullEnumClass() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(null, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNameNullEnumName() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByValueNegativeNumber() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", -1);
    }
}