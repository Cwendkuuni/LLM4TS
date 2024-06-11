import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Enum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = new ValuedEnumForTest("JavaVersion", "JavaVersionEnum") {
            @Override
            public int getValue() {
                return super.getValue();
            }
        };

        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;

        assertEquals(java1_0, ValuedEnum.getEnum(JavaVersionEnum.class, "JavaVersionEnum.JAVA1_0"));
        assertEquals(java1_0, ValuedEnum.getEnum(JavaVersionEnum.class, 100));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, "NonExistentJavaVersion"));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 400));
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum javaVersionEnum = new ValuedEnumForTest("JavaVersion", "JavaVersionEnum") {
            @Override
            public int getValue() {
                return super.getValue();
            }
        };

        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;

        List<JavaVersionEnum> javaVersionEnumList = ValuedEnum.getEnumList(JavaVersionEnum.class);
        assertEquals(4, javaVersionEnumList.size());

        javaVersionEnumList.remove(0);
        javaVersionEnumList.remove(2);

        Map<Integer, JavaVersionEnum> javaVersionEnumMap = ValuedEnum.getEnumMap(JavaVersionEnum.class);
        assertEquals(4, javaVersionEnumMap.size());

        assertEquals(java1_0, javaVersionEnumMap.get(java1_0.getValue()));
        assertEquals(java1_1, javaVersionEnumMap.get(java1_1.getValue()));
        assertEquals(java1_2, javaVersionEnumMap.get(java1_2.getValue()));
        assertEquals(java1_3, javaVersionEnumMap.get(java1_3.getValue()));
    }

    @Test
    public void testCompareTo() {
        ValuedEnum javaVersionEnum = new ValuedEnumForTest("JavaVersion", "JavaVersionEnum") {
            @Override
            public int getValue() {
                return super.getValue();
            }
        };

        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;

        assertEquals(-2, java1_1.compareTo(java1_0));
        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(2, java1_0.compareTo(java1_1));
        assertEquals(1, java1_1.compareTo(java1_2));
        assertEquals(-1, java1_2.compareTo(java1_1));

        ValuedEnum otherValuedEnum = new ValuedEnumForTest("OtherVersion", "OtherVersionEnum") {
            @Override
            public int getValue() {
                return super.getValue();
            }
        };

        assertNull(java1_0.compareTo(otherValuedEnum));
    }
}

abstract class ValuedEnumForTest extends ValuedEnum {
    private String name;
    private String enumClassName;

    ValuedEnumForTest(String name, String enumClassName) {
        super(name, Integer.parseInt(name.replaceAll("[^0-9]", "")));
        this.name = name;
        this.enumClassName = enumClassName;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEnumClass() {
        try {
            return Class.forName(enumClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}