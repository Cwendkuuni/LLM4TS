package org.apache.commons.lang.enums;

import org.apache.commons.lang.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.apache.commons.lang.enums.ValuedEnum.compareTo;
import static org.apache.commons.lang.enums.ValuedEnum.getValue;

/**
 * Test class for ValuedEnum.
 */
public class ValuedEnumTest {

    /**
     * Test getEnum method.
     */
    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(JavaVersionEnum.class, "Java1_2");
        Assert.assertNotNull(javaVersionEnum);
        Assert.assertEquals("Java 1.2", javaVersionEnum.getName());
        Assert.assertEquals(120, javaVersionEnum.getValue());
    }

    /**
     * Test getEnum method with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullArgument() {
        ValuedEnum.getEnum(null, "Java1_2");
    }

    /**
     * Test getEnum method with non-existent value.
     */
    @Test
    public void testGetEnumNonExistentValue() {
        Assert.assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 1000));
    }

    /**
     * Test getEnumMap method.
     */
    @Test
    public void testGetEnumMap() {
        Map<JavaVersionEnum, JavaVersionEnum> enumMap = ValuedEnum.getEnumMap(JavaVersionEnum.class);
        Assert.assertNotNull(enumMap);
        JavaVersionEnum javaVersion1_2 = enumMap.get(JavaVersionEnum.JAVA1_2);
        Assert.assertNotNull(javaVersion1_2);
        Assert.assertEquals("Java 1.2", javaVersion1_2.getName());
        Assert.assertEquals(120, javaVersion1_2.getValue());
    }

    /**
     * Test getEnumList method.
     */
    @Test
    public void testGetEnumList() {
        List<JavaVersionEnum> enumList = ValuedEnum.getEnumList(JavaVersionEnum.class);
        Assert.assertNotNull(enumList);
        JavaVersionEnum javaVersion1_2 = enumList.get(1);
        Assert.assertNotNull(javaVersion1_2);
        Assert.assertEquals("Java 1.2", javaVersion1_2.getName());
        Assert.assertEquals(120, javaVersion1_2.getValue());
    }

    /**
     * Test getEnumList method with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumListNullArgument() {
        ValuedEnum.getEnumList(null);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        Iterator<JavaVersionEnum> iterator = ValuedEnum.iterator(JavaVersionEnum.class);
        int index = 0;
        while (iterator.hasNext()) {
            JavaVersionEnum javaVersionEnum = iterator.next();
            Assert.assertNotNull(javaVersionEnum);
            Assert.assertEquals("Java " + (index + 1), javaVersionEnum.getName());
            Assert.assertEquals(100 + index, javaVersionEnum.getValue());
            index++;
        }
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum javaVersion1_2 = JavaVersionEnum.JAVA1_2;

        int result = compareTo(javaVersion1_0, javaVersion1_1);
        Assert.assertTrue(result < 0);

        result = compareTo(javaVersion1_1, javaVersion1_0);
        Assert.assertTrue(result > 0);

        result = compareTo(javaVersion1_0, javaVersion1_0);
        Assert.assertEquals(0, result);

        result = compareTo(javaVersion1_1, javaVersion1_2);
        Assert.assertTrue(result < 0);

        result = compareTo(javaVersion1_2, javaVersion1_1);
        Assert.assertTrue(result > 0);

        result = compareTo(javaVersion1_2, javaVersion1_2);
        Assert.assertEquals(0, result);
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum javaVersion1_2 = JavaVersionEnum.JAVA1_2;

        int javaVersion1_0Value = getValue(javaVersion1_0);
        Assert.assertEquals(100, javaVersion1_0Value);

        int javaVersion1_1Value = getValue(javaVersion1_1);
        Assert.assertEquals(110, javaVersion1_1Value);

        int javaVersion1_2Value = getValue(javaVersion1_2);
        Assert.assertEquals(120, javaVersion1_2Value);
    }

    /**
     * Test getValue method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetValueNullArgument() {
        getValue(null);
    }

    /**
     * Test getEnum method with null class argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClassArgument() {
        ValuedEnum.getEnum(null, "Java1_2");
    }

    /**
     * Test getEnum method with non-existent enum class.
     */
    @Test
    public void testGetEnumNonExistentEnumClass() {
        Assert.assertNull(ValuedEnum.getEnum(InvalidEnum.class, "InvalidEnum"));
    }

    /**
     * Test getEnum method with non-existent enum name.
     */
    @Test
    public void testGetEnumNonExistentEnumName() {
        Assert.assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, "InvalidEnum"));
    }

    /**
     * Test getEnumList method with null class argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumListNullClassArgument() {
        ValuedEnum.getEnumList(null);
    }

    /**
     * Test getEnumList method with invalid class.
     */
    @Test
    public void testGetEnumListInvalidClass() {
        Assert.assertNotNull(ValuedEnum.getEnumList(InvalidEnum.class));
        Assert.assertTrue(ValuedEnum.getEnumList(InvalidEnum.class).isEmpty());
    }

    /**
     * Test getEnumMap method with null class argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumMapNullClassArgument() {
        ValuedEnum.getEnumMap(null);
    }

    /**
     * Test getEnumMap method with invalid class.
     */
    @Test
    public void testGetEnumMapInvalidClass() {
        Assert.assertNotNull(ValuedEnum.getEnumMap(InvalidEnum.class));
        Assert.assertTrue(ValuedEnum.getEnumMap(InvalidEnum.class).isEmpty());
    }

    /**
     * Test iterator method with null class argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorNullClassArgument() {
        ValuedEnum.iterator(null);
    }

    /**
     * Test iterator method with invalid class.
     */
    @Test
    public void testIteratorInvalidClass() {
        Iterator<Object> iterator = ValuedEnum.iterator(InvalidEnum.class);
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test compareTo method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testCompareToNullArgument() {
        compareTo(null, JavaVersionEnum.JAVA1_1);
    }

    /**
     * Test compareTo method with non-existent enum.
     */
    @Test
    public void testCompareToNonExistentEnum() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        Object nonExistentEnum = new Object();

        int result = compareTo(javaVersion1_0, nonExistentEnum);
        Assert.assertEquals(-1, result);

        try {
            Method compareToMethod = nonExistentEnum.getClass().getMethod("compareTo", Object.class);
            result = ((Integer) compareToMethod.invoke(nonExistentEnum, javaVersion1_0)).intValue();
            Assert.fail("compareTo method should not be found or should throw an exception");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // expected
        }
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum javaVersion1_2 = JavaVersionEnum.JAVA1_2;

        String javaVersion1_0ToString = javaVersion1_0.toString();
        Assert.assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0ToString);

        String javaVersion1_1ToString = javaVersion1_1.toString();
        Assert.assertEquals("JavaVersion[Java 1.1=110]", javaVersion1_1ToString);

        String javaVersion1_2ToString = javaVersion1_2.toString();
        Assert.assertEquals("JavaVersion[Java 1.2=120]", javaVersion1_2ToString);
    }

    /**
     * Test toString method with invalid enum.
     */
    @Test
    public void testToStringInvalidEnum() {
        Object invalidEnum = new Object();
        String invalidEnumToString = invalidEnum.toString();
        Assert.assertNotEquals("JavaVersion[...]", invalidEnumToString);
    }

    /**
     * Test equals method with same object.
     */
    @Test
    public void testEqualsSameObject() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0Copy = new JavaVersionEnum("Java 1.0", 100);

        Assert.assertEquals(javaVersion1_0, javaVersion1_0);
        Assert.assertEquals(javaVersion1_0, javaVersion1_0Copy);
    }

    /**
     * Test equals method with different objects.
     */
    @Test
    public void testEqualsDifferentObjects() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;

        Assert.assertNotEquals(javaVersion1_0, javaVersion1_1);
    }

    /**
     * Test equals method with null object.
     */
    @Test
    public void testEqualsNullObject() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;

        Assert.assertNotEquals(javaVersion1_0, null);
    }

    /**
     * Test equals method with different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        Object differentClassObject = new Object();

        Assert.assertNotEquals(javaVersion1_0, differentClassObject);
    }

    /**
     * Test equals method with same value but different name.
     */
    @Test
    public void testEqualsSameValueDifferentName() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0NameDifferent = new JavaVersionEnum("Java 1.0_name_different", 100);

        Assert.assertNotEquals(javaVersion1_0, javaVersion1_0NameDifferent);
    }

    /**
     * Test equals method with same name but different value.
     */
    @Test
    public void testEqualsSameNameDifferentValue() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum javaVersion1_1ValueDifferent = new JavaVersionEnum("Java 1.1", 120);

        Assert.assertNotEquals(javaVersion1_0, javaVersion1_1);
        Assert.assertNotEquals(javaVersion1_0, javaVersion1_1ValueDifferent);
    }

    /**
     * Test hashCode method with same object.
     */
    @Test
    public void testHashCodeSameObject() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0Copy = new JavaVersionEnum("Java 1.0", 100);

        Assert.assertEquals(javaVersion1_0.hashCode(), javaVersion1_0Copy.hashCode());
    }

    /**
     * Test hashCode method with different objects.
     */
    @Test
    public void testHashCodeDifferentObjects() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;

        Assert.assertNotEquals(javaVersion1_0.hashCode(), javaVersion1_1.hashCode());
    }

    /**
     * Test hashCode method with null object.
     */
    @Test
    public void testHashCodeNullObject() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;

        Assert.assertNotEquals(javaVersion1_0.hashCode(), null.hashCode());
    }

    /**
     * Test hashCode method with different class.
     */
    @Test
    public void testHashCodeDifferentClass() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        Object differentClassObject = new Object();

        Assert.assertNotEquals(javaVersion1_0.hashCode(), differentClassObject.hashCode());
    }

    /**
     * Test hashCode method with same value but different name.
     */
    @Test
    public void testHashCodeSameValueDifferentName() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_0NameDifferent = new JavaVersionEnum("Java 1.0_name_different", 100);

        Assert.assertNotEquals(javaVersion1_0.hashCode(), javaVersion1_0NameDifferent.hashCode());
    }

    /**
     * Test hashCode method with same name but different value.
     */
    @Test
    public void testHashCodeSameNameDifferentValue() {
        JavaVersionEnum javaVersion1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum javaVersion1_1ValueDifferent = new JavaVersionEnum("Java 1.1", 120);

        Assert.assertNotEquals(javaVersion1_0.hashCode(), javaVersion1_1.hashCode());
        Assert.assertNotEquals(javaVersion1_0.hashCode(), javaVersion1_1ValueDifferent.hashCode());
    }

    private static class JavaVersionEnum extends ValuedEnum {
        public static final JavaVersionEnum JAVA1_0 = new JavaVersionEnum("Java 1.0", 100);
        public static final JavaVersionEnum JAVA1_1 = new JavaVersionEnum("Java 1.1", 110);
        public static final JavaVersionEnum JAVA1_2 = new JavaVersionEnum("Java 1.2", 120);

        private JavaVersionEnum(String name, int value) {
            super(name, value);
        }
    }

    private static class InvalidEnum {
    }
}