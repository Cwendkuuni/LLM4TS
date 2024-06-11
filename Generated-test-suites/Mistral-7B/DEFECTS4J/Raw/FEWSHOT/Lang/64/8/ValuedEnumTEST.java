package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0Returned = (JavaVersionEnum) ValuedEnum.getEnum(javaVersionEnumClass, JavaVersionEnum.JAVA1_0_VALUE);
        assertEquals(javaVersion1_0, javaVersion1_0Returned);

        JavaVersionEnum javaVersionNull = null;
        assertNull(ValuedEnum.getEnum(javaVersionEnumClass, -1));
        assertNull(ValuedEnum.getEnum(javaVersionEnumClass, javaVersionNull));
    }

    @Test
    public void testGetEnumWithReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;
        Method getEnumMethod = ValuedEnum.class.getDeclaredMethod("getEnum", Class.class, int.class);
        getEnumMethod.setAccessible(true);

        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0Returned = (JavaVersionEnum) getEnumMethod.invoke(null, javaVersionEnumClass, JavaVersionEnum.JAVA1_0_VALUE);
        assertEquals(javaVersion1_0, javaVersion1_0Returned);

        JavaVersionEnum javaVersionNull = null;
        assertNull(getEnumMethod.invoke(null, javaVersionEnumClass, -1));
        assertNull(getEnumMethod.invoke(null, javaVersionEnumClass, javaVersionNull));
    }

    @Test
    public void testGetEnumMap() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;
        Map<Integer, JavaVersionEnum> enumMap = ValuedEnum.getEnumMap(javaVersionEnumClass);

        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(javaVersion1_0, enumMap.get(JavaVersionEnum.JAVA1_0_VALUE));

        JavaVersionEnum javaVersionNull = null;
        assertNull(enumMap.get(-1));
        assertNull(enumMap.get(javaVersionNull));
    }

    @Test
    public void testGetEnumList() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;
        List<JavaVersionEnum> enumList = ValuedEnum.getEnumList(javaVersionEnumClass);

        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0InList = findEnumInList(javaVersion1_0, enumList);
        assertEquals(javaVersion1_0, javaVersion1_0InList);

        JavaVersionEnum javaVersionNull = null;
        assertNull(findEnumInList(javaVersionNull, enumList));
    }

    @Test
    public void testIterator() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;
        Iterator<JavaVersionEnum> iterator = ValuedEnum.iterator(javaVersionEnumClass);

        int counter = 0;
        while (iterator.hasNext()) {
            JavaVersionEnum nextEnum = iterator.next();
            assertEquals(nextEnum.getValue(), ++counter);
        }
    }

    private JavaVersionEnum findEnumInList(JavaVersionEnum javaVersionEnum, List<JavaVersionEnum> enumList) {
        for (JavaVersionEnum enumItem : enumList) {
            if (enumItem.equals(javaVersionEnum)) {
                return enumItem;
            }
        }
        return null;
    }
}

class JavaVersionEnum extends ValuedEnum {
    public static final int JAVA1_0_VALUE = 100;
    public static final int JAVA1_1_VALUE = 110;
    public static final int JAVA1_2_VALUE = 120;

    public static final JavaVersionEnum JAVA1_0 = new JavaVersionEnum("Java 1.0", JAVA1_0_VALUE);
    public static final JavaVersionEnum JAVA1_1 = new JavaVersionEnum("Java 1.1", JAVA1_1_VALUE);
    public static final JavaVersionEnum JAVA1_2 = new JavaVersionEnum("Java 1.2", JAVA1_2_VALUE);

    private JavaVersionEnum(String name, int value) {
        super(name, value);
    }
}