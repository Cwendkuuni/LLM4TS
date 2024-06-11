import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", 110);
        assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullName() {
        ValuedEnum.getEnum("JavaVersionEnum", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        ValuedEnum.getEnum("JavaVersionEnum", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullEnum() {
        ValuedEnum javaVersionEnum = new ValuedEnum("TestEnum", 0) {
            @Override
            public static Enum getEnum(Class enumClass, int value) {
                return null;
            }
        };
        ValuedEnum testEnum = ValuedEnum.getEnum(javaVersionEnum.getClass(), 0);
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum[] javaVersionEnums = JavaVersionEnum.values();
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(JavaVersionEnum.class);
        for (ValuedEnum javaVersionEnum : javaVersionEnums) {
            assertEquals(javaVersionEnum, enumMap.get(javaVersionEnum.getName()));
        }
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum[] javaVersionEnums = JavaVersionEnum.values();
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(JavaVersionEnum.class);
        for (int i = 0; i < javaVersionEnums.length; i++) {
            assertEquals(javaVersionEnums[i], enumList.get(i));
        }
    }

    @Test
    public void testIterator() {
        ValuedEnum[] javaVersionEnums = JavaVersionEnum.values();
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(JavaVersionEnum.class);
        for (ValuedEnum javaVersionEnum : javaVersionEnums) {
            assertTrue(iterator.hasNext());
            assertEquals(javaVersionEnum, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorWithNullClass() {
        ValuedEnum.iterator(null);
    }
}