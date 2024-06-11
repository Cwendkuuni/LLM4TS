import static org.junit.Assert.*;

import java.util.Map;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    private static final int JAVA1_0_VALUE = 100;
    private static final int JAVA1_1_VALUE = 110;
    private static final int JAVA1_2_VALUE = 120;
    private static final int JAVA1_3_VALUE = 130;

    private static final ValuedEnum JAVA1_0 = new ValuedEnumImpl("Java 1.0", JAVA1_0_VALUE);
    private static final ValuedEnum JAVA1_1 = new ValuedEnumImpl("Java 1.1", JAVA1_1_VALUE);
    private static final ValuedEnum JAVA1_2 = new ValuedEnumImpl("Java 1.2", JAVA1_2_VALUE);
    private static final ValuedEnum JAVA1_3 = new ValuedEnumImpl("Java 1.3", JAVA1_3_VALUE);

    private ValuedEnumImpl enumUnderTest;

    @Before
    public void setUp() {
        enumUnderTest = new ValuedEnumImpl("TestEnum", 999);
    }

    @Test
    public void testGetValue() {
        assertEquals(999, enumUnderTest.getValue());
    }

    @Test
    public void testGetEnum_ExistingValue_ReturnsEnum() {
        ValuedEnum result = ValuedEnum.getEnum(ValuedEnumImpl.class, 999);
        assertSame(enumUnderTest, result);
    }

    @Test
    public void testGetEnum_NonExistingValue_ReturnsNull() {
        ValuedEnum result = ValuedEnum.getEnum(ValuedEnumImpl.class, 123);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnum_NullEnumClass_ThrowsIllegalArgumentException() {
        ValuedEnum.getEnum(null, 999);
    }

    @Test
    public void testCompareTo_LessThan_ReturnsNegativeValue() {
        int result = enumUnderTest.compareTo(JAVA1_0);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareTo_GreaterThan_ReturnsPositiveValue() {
        int result = enumUnderTest.compareTo(JAVA1_2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareTo_SameValue_ReturnsZero() {
        int result = enumUnderTest.compareTo(new ValuedEnumImpl("TestEnum2", 999));
        assertEquals(0, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_ObjectOfDifferentType_ThrowsClassCastException() {
        enumUnderTest.compareTo("Not an enum");
    }

    @Test
    public void testToString_ReturnsEnumDescription() {
        String result = enumUnderTest.toString();
        assertEquals("ValuedEnumImpl[TestEnum=999]", result);
    }

    private static class ValuedEnumImpl extends ValuedEnum {

        private static final Map<String, ValuedEnum> ENUM_MAP;
        private static final List<ValuedEnum> ENUM_LIST;

        public ValuedEnumImpl(String name, int value) {
            super(name, value);
        }

        public static ValuedEnum getEnum(String testEnum) {
            return (ValuedEnum) getEnum(ValuedEnumImpl.class, testEnum);
        }

        public static ValuedEnum getEnum(int testEnum) {
            return (ValuedEnum) getEnum(ValuedEnumImpl.class, testEnum);
        }

        public static Map<String, ValuedEnum> getEnumMap() {
            return getEnumMap(ValuedEnumImpl.class);
        }

        public static List<ValuedEnum> getEnumList() {
            return getEnumList(ValuedEnumImpl.class);
        }

        public static Iterator<ValuedEnum> iterator() {
            return iterator(ValuedEnumImpl.class);
        }

        static {
            ENUM_MAP = getEnumMap(ValuedEnumImpl.class);
            ENUM_LIST = getEnumList(ValuedEnumImpl.class);
        }
    }
}