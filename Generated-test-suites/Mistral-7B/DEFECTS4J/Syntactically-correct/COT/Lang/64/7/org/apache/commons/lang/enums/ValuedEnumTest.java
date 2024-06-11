package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 1) {
            // empty implementation
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 2) {
            // empty implementation
        };

        Method getEnumMethod = enumClass.getMethod("getEnum", String.class);
        assertEquals(javaVersion1_0, getEnumMethod.invoke(null, "JavaVersion1_0"));

        Method getEnumMethodInt = enumClass.getMethod("getEnum", int.class);
        assertEquals(javaVersion1_0, getEnumMethodInt.invoke(null, 1));

        Method getEnumMapMethod = enumClass.getMethod("getEnumMap");
        List<ValuedEnum> enumList = (List<ValuedEnum>) getEnumMapMethod.invoke(null);
        assertEquals(Arrays.asList(javaVersion1_0, javaVersion1_1), enumList);

        Method getEnumListMethod = enumClass.getMethod("getEnumList");
        enumList = (List<ValuedEnum>) getEnumListMethod.invoke(null);
        assertEquals(Arrays.asList(javaVersion1_0, javaVersion1_1), enumList);

        Method iteratorMethod = enumClass.getMethod("iterator");
        Iterator<ValuedEnum> iterator = (Iterator<ValuedEnum>) iteratorMethod.invoke(null);
        assertTrue(iterator.hasNext());
        assertSame(javaVersion1_0, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(javaVersion1_1, iterator.next());
        assertFalse(iterator.hasNext());

        Method getEnumByNameNullMethod = enumClass.getMethod("getEnum", (Class<String>) null);
        assertNull(getEnumByNameNullMethod.invoke(null));

        Method getEnumByNameEmptyStringMethod = enumClass.getMethod("getEnum", String.class, (Class<String>[]) new Class[] { String.class });
        assertNull(getEnumByNameEmptyStringMethod.invoke(null, ""));

        Method getEnumByValueNegativeIntMethod = enumClass.getMethod("getEnum", int.class, (Class<ValuedEnum>[]) new Class[] { ValuedEnum.class });
        assertNull(getEnumByValueNegativeIntMethod.invoke(null, -1));

        Method getEnumByValueNonExistentIntMethod = enumClass.getMethod("getEnum", int.class, (Class<ValuedEnum>[]) new Class[] { ValuedEnum.class });
        assertNull(getEnumByValueNonExistentIntMethod.invoke(null, 3));
    }

    @Test
    public void testCompareTo() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum value1 = new ValuedEnum("ValuedEnum1", 1) {
            // empty implementation
        };
        ValuedEnum value2 = new ValuedEnum("ValuedEnum2", 2) {
            // empty implementation
        };
        ValuedEnum value3 = new ValuedEnum("ValuedEnum3", 3) {
            // empty implementation
        };

        List<ValuedEnum> enumList = Arrays.asList(value1, value2, value3);
        Collections.sort(enumList);

        int comparisonResult1 = value1.compareTo(value1);
        assertEquals(0, comparisonResult1);

        int comparisonResult12 = value1.compareTo(value2);
        assertTrue(comparisonResult12 < 0);

        int comparisonResult21 = value2.compareTo(value1);
        assertTrue(comparisonResult21 > 0);

        int comparisonResult13 = value1.compareTo(value3);
        assertTrue(comparisonResult13 < 0);

        int comparisonResult31 = value3.compareTo(value1);
        assertTrue(comparisonResult31 > 0);
    }
}