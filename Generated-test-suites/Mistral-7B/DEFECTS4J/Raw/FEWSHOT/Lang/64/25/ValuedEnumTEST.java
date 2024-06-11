package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersion1_3 = JavaVersionEnum.JAVA1_3;
        JavaVersionEnum javaVersion1_4 = new JavaVersionEnum("Java 1.4", 104) {
            // override getEnum method to return this instance
            protected static Enum getEnum(Class enumClass, int value) {
                return JavaVersionEnum.JAVA1_4; // this instance
            }
        };

        assertEquals(javaVersion1_3, JavaVersionEnum.getEnum(JavaVersionEnum.class, 130));
        assertEquals(javaVersion1_4, JavaVersionEnum.getEnum(JavaVersionEnum.class, 104));
    }

    @Test
    public void testGetEnumByValue_Null() {
        assertNull(JavaVersionEnum.getEnum(JavaVersionEnum.class, Integer.MIN_VALUE));
    }

    @Test
    public void testGetEnumByValue_WrongClass() {
        assertNull(String.class.cast(JavaVersionEnum.getEnum(String.class, 130)));
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;

        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
        assertEquals(1, javaVersion1_1.compareTo(javaVersion1_0));

        JavaVersionEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);
        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_2));
        assertEquals(0, javaVersion1_2.compareTo(javaVersion1_2));
        assertEquals(1, javaVersion1_2.compareTo(javaVersion1_1));
    }

    @Test
    public void testCompareTo_SameClassLoader() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;

        JavaVersionEnum javaVersion1_3 = JavaVersionEnum.JAVA1_3;
        JavaVersionEnum javaVersion1_3_Alt = new JavaVersionEnum("Java 1.3", 130) {
            // override getEnum method to return this instance
            protected static Enum getEnum(Class enumClass, int value) {
                return JavaVersionEnum.JAVA1_3_Alt; // this instance
            }
        };

        JavaVersionEnum javaVersion1_4 = new JavaVersionEnum("Java 1.4", 104) {
            // override getEnum method to return this instance
            protected static Enum getEnum(Class enumClass, int value) {
                return JavaVersionEnum.JAVA1_4; // this instance
            }
        };

        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
        assertEquals(1, javaVersion1_1.compareTo(javaVersion1_0));
        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0.getClass().cast(javaVersion1_3)));
        assertEquals(-1, javaVersion1_3.compareTo(javaVersion1_0));
        assertEquals(0, javaVersion1_3.compareTo(javaVersion1_3));
        assertEquals(1, javaVersion1_3.compareTo(javaVersion1_1));
        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_4));
        assertEquals(-1, javaVersion1_4.compareTo(javaVersion1_0));
        assertEquals(0, javaVersion1_4.compareTo(javaVersion1_4));
        assertEquals(1, javaVersion1_4.compareTo(javaVersion1_1));
    }

    @Test
    public void testCompareTo_DifferentClassLoader() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> javaVersionEnumClass = Class.forName("org.apache.commons.lang.enums.JavaVersionEnum");
        JavaVersionEnum javaVersion1_0 = (JavaVersionEnum) javaVersionEnumClass.getEnumConstants()[0];
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;

        JavaVersionEnum javaVersion1_3_Alt = new JavaVersionEnum("Java 1.3", 130) {
            // override getEnum method to return this instance
            protected static Enum getEnum(Class enumClass, int value) {
                return JavaVersionEnum.JAVA1_3_Alt; // this instance
            }
        };

        Method compareToMethod = JavaVersionEnum.class.getDeclaredMethod("compareTo", Object.class);
        compareToMethod.setAccessible(true);

        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
        assertEquals(1, javaVersion1_1.compareTo(javaVersion1_0));

        JavaVersionEnum javaVersion1_3 = (JavaVersionEnum) javaVersionEnumClass.getEnumConstants()[2];

        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_3));
        assertEquals(0, javaVersion1_3.compareTo(javaVersion1_3.getClass().cast(javaVersion1_3_Alt)));
        assertEquals(1, javaVersion1_3_Alt.compareTo(javaVersion1_3));

        Object javaVersion1_3_AltObj = compareToMethod.invoke(javaVersion1_3_Alt, javaVersion1_3);
        assertEquals(1, javaVersion1_3.compareTo(javaVersion1_3_AltObj));
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;

        assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0.toString());
        assertEquals("JavaVersion[Java 1.1=110]", javaVersion1_1.toString());
    }
}