import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ValuedEnumTest {

    private static final int JAVA1_0_VALUE = 100;
    private static final int JAVA1_1_VALUE = 110;
    private static final int JAVA1_2_VALUE = 120;
    private static final int JAVA1_3_VALUE = 130;

    private static final ValuedEnum JAVA1_0 = new ValuedEnum("Java 1.0", JAVA1_0_VALUE) {
        @Override
        public Class getEnumClass() {
            return null;
        }

        @Override
        public int compareTo(Object other) {
            return 0;
        }

        @Override
        public String toString() {
            return null;
        }
    };

    private static final ValuedEnum JAVA1_1 = new ValuedEnum("Java 1.1", JAVA1_1_VALUE) {
        @Override
        public Class getEnumClass() {
            return null;
        }

        @Override
        public int compareTo(Object other) {
            return 0;
        }

        @Override
        public String toString() {
            return null;
        }
    };

    private static final ValuedEnum JAVA1_2 = new ValuedEnum("Java 1.2", JAVA1_2_VALUE) {
        @Override
        public Class getEnumClass() {
            return null;
        }

        @Override
        public int compareTo(Object other) {
            return 0;
        }

        @Override
        public String toString() {
            return null;
        }
    };

    private static final ValuedEnum JAVA1_3 = new ValuedEnum("Java 1.3", JAVA1_3_VALUE) {
        @Override
        public Class getEnumClass() {
            return null;
        }

        @Override
        public int compareTo(Object other) {
            return 0;
        }

        @Override
        public String toString() {
            return null;
        }
    };

    @Before
    public void setUp() {
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = (ValuedEnum) ValuedEnum.getEnum(ValuedEnum.class, JAVA1_0_VALUE);
        ValuedEnum enum2 = (ValuedEnum) ValuedEnum.getEnum(ValuedEnum.class, JAVA1_2_VALUE);

        assertEquals(JAVA1_0, enum1);
        assertEquals(JAVA1_2, enum2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, JAVA1_0_VALUE);
    }

    @Test
    public void testGetValue() {
        assertEquals(JAVA1_0_VALUE, JAVA1_0.getValue());
        assertEquals(JAVA1_2_VALUE, JAVA1_2.getValue());
    }

    @Test
    public void testCompareTo() {
        assertTrue(JAVA1_0.compareTo(JAVA1_1) < 0);
        assertTrue(JAVA1_2.compareTo(JAVA1_1) > 0);
        assertTrue(JAVA1_3.compareTo(JAVA1_2) > 0);
    }

    @Test
    public void testToString() {
        assertEquals("ValuedEnum[Java 1.0=100]", JAVA1_0.toString());
        assertEquals("ValuedEnum[Java 1.2=120]", JAVA1_2.toString());
    }
    
    @Test
    public void testGetEnumMap() {
        Map enumMap = ValuedEnum.getEnumMap();
        assertNotNull(enumMap);
        assertFalse(enumMap.isEmpty());
    }
    
    @Test
    public void testGetEnumList() {
        List enumList = ValuedEnum.getEnumList();
        assertNotNull(enumList);
        assertFalse(enumList.isEmpty());
    }

    @Test
    public void testIterator() {
        Iterator iterator = ValuedEnum.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }
}