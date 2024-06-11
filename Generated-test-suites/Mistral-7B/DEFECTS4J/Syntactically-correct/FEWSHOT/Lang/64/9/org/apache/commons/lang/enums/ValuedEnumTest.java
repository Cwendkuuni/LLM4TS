package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumType = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum javaVersion1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);
        ValuedEnum javaVersion1_3 = new JavaVersionEnum("Java 1.3", 130);

        assertEquals(javaVersion1_0, ValuedEnum.getEnum(enumType, "Java 1.0"));
        assertEquals(javaVersion1_0, ValuedEnum.getEnum(enumType, 100));
        assertNull(ValuedEnum.getEnum(enumType, "Java 9.0"));
        assertNull(ValuedEnum.getEnum(enumType, 9));

        ValuedEnum[] values = ValuedEnumTest.values();
        for (ValuedEnum value : values) {
            assertEquals(value, ValuedEnum.getEnum(enumType, value.getName()));
            assertEquals(value, ValuedEnum.getEnum(enumType, value.getValue()));
        }
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumType = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum javaVersion1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);
        ValuedEnum javaVersion1_3 = new JavaVersionEnum("Java 1.3", 130);

        List<ValuedEnum> values = ValuedEnum.getEnumList(enumType);
        assertEquals(Arrays.asList(javaVersion1_0, javaVersion1_1, javaVersion1_2, javaVersion1_3), values);
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumType = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum javaVersion1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);
        ValuedEnum javaVersion1_3 = new JavaVersionEnum("Java 1.3", 130);

        Map<String, ValuedEnum> map = ValuedEnum.getEnumMap(enumType);
        assertEquals(javaVersion1_0, map.get("Java 1.0"));
        assertEquals(javaVersion1_1, map.get("Java 1.1"));
        assertEquals(javaVersion1_2, map.get("Java 1.2"));
        assertEquals(javaVersion1_3, map.get("Java 1.3"));
        assertNull(map.get("Java 9.0"));
    }

    @Test
    public void testIterator() {
        ValuedEnum enumType = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum javaVersion1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);
        ValuedEnum javaVersion1_3 = new JavaVersionEnum("Java 1.3", 130);

        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumType);
        List<ValuedEnum> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        assertEquals(Arrays.asList(javaVersion1_0, javaVersion1_1, javaVersion1_2, javaVersion1_3), values);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum javaVersion1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);
        ValuedEnum javaVersion1_3 = new JavaVersionEnum("Java 1.3", 130);

        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
        assertEquals(1, javaVersion1_1.compareTo(javaVersion1_0));
        assertEquals(1, javaVersion1_0.compareTo(javaVersion1_2));
        assertEquals(-1, javaVersion1_2.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_1.compareTo(javaVersion1_2));
        assertEquals(1, javaVersion1_2.compareTo(javaVersion1_1));
        assertEquals(-1, javaVersion1_3.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_3.compareTo(javaVersion1_1));
        assertEquals(-1, javaVersion1_3.compareTo(javaVersion1_2));
        assertEquals(0, javaVersion1_3.compareTo(javaVersion1_3));
    }
}

class JavaVersionEnum extends ValuedEnum {
    JavaVersionEnum(String name, int value) {
        super(name, value);
    }
}