package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum java1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum java1_2 = new JavaVersionEnum("Java 1.2", 120);

        assertEquals(java1_0, ValuedEnum.getEnum(VALUED_ENUM_CLASS, "Java 1.0"));
        assertEquals(java1_0, ValuedEnum.getEnum(VALUED_ENUM_CLASS, 100));
        assertNull(ValuedEnum.getEnum(VALUED_ENUM_CLASS, null));
        assertNull(ValuedEnum.getEnum(VALUED_ENUM_CLASS, "Unknown"));

        List<ValuedEnum> javaVersions = Arrays.asList(java1_0, java1_1, java1_2);
        for (ValuedEnum version : javaVersions) {
            assertEquals(version, ValuedEnum.getEnum(VALUED_ENUM_CLASS, version.getName()));
            assertEquals(version, ValuedEnum.getEnum(VALUED_ENUM_CLASS, version.getValue()));
        }
    }

    @Test
    public void testGetEnum_Method() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getEnumMethod = VALUED_ENUM_CLASS.getDeclaredMethod("getEnum", String.class);
        getEnumMethod.setAccessible(true);

        ValuedEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum java1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum java1_2 = new JavaVersionEnum("Java 1.2", 120);

        assertEquals(java1_0, getEnumMethod.invoke(null, "Java 1.0"));
        assertEquals(java1_0, getEnumMethod.invoke(null, 100));
        assertNull(getEnumMethod.invoke(null, null));
        assertNull(getEnumMethod.invoke(null, "Unknown"));

        List<ValuedEnum> javaVersions = Arrays.asList(java1_0, java1_1, java1_2);
        for (ValuedEnum version : javaVersions) {
            assertEquals(version, getEnumMethod.invoke(null, version.getName()));
            assertEquals(version, getEnumMethod.invoke(null, version.getValue()));
        }
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
        JavaVersionEnum java1_1 = new JavaVersionEnum("Java 1.1", 110);
        JavaVersionEnum java1_2 = new JavaVersionEnum("Java 1.2", 120);

        assertEquals(-1, java1_0.compareTo(java1_1));
        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(1, java1_1.compareTo(java1_0));
        assertEquals(1, java1_1.compareTo(java1_1)); // reflexive
        assertEquals(0, java1_1.compareTo(java1_1.getValue()));
        assertEquals(1, java1_2.compareTo(java1_1));
        assertEquals(1, java1_1.compareTo(java1_2.getValue()));
        assertNull(java1_1.compareTo(null));

        JavaVersionEnum java1p5 = new JavaVersionEnum("Java 1.5", 105);
        assertEquals(-2, java1_1.compareTo(java1p5));
    }

    @Test
    public void testCompareTo_DifferentClassLoader() {
        JavaVersionEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
        MyVersionEnum myVersion1_0 = new MyVersionEnum("My 1.0", 100);

        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(0, myVersion1_0.compareTo(myVersion1_0));
        assertEquals(-1, java1_0.compareTo(myVersion1_0));
        assertEquals(1, myVersion1_0.compareTo(java1_0));
    }

    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
    }
}

class JavaVersionEnum extends ValuedEnum {
    JavaVersionEnum(String name, int value) {
        super(name, value);
    }
}

class MyVersionEnum extends ValuedEnum {
    MyVersionEnum(String name, int value) {
        super(name, value);
    }
}