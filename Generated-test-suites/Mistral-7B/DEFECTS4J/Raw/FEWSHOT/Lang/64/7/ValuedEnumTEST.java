package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    private static final Class<?> VALUED_ENUM_CLASS = ValuedEnum.class;

    @SuppressWarnings("unchecked")
    @Test
    public void testGetEnum() throws Exception {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_0");
        assertEquals("JavaVersionEnum_JAVA1_0", javaVersion1_0.toString());
        assertEquals(100, javaVersion1_0.getValue());

        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_1");
        assertEquals("JavaVersionEnum_JAVA1_1", javaVersion1_1.toString());
        assertEquals(110, javaVersion1_1.getValue());

        ValuedEnum javaVersion1_2 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_2");
        assertEquals("JavaVersionEnum_JAVA1_2", javaVersion1_2.toString());
        assertEquals(120, javaVersion1_2.getValue());

        assertNull(ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "INVALID"));
    }

    @Test
    public void testGetEnumWithInt() throws Exception {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 100);
        assertEquals("JavaVersionEnum_JAVA1_0", javaVersion1_0.toString());
        assertEquals(100, javaVersion1_0.getValue());

        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 110);
        assertEquals("JavaVersionEnum_JAVA1_1", javaVersion1_1.toString());
        assertEquals(110, javaVersion1_1.getValue());

        assertNull(ValuedEnum.getEnum(VALUED_ENUM_CLASS, 150));
    }

    @Test
    public void testGetEnumMap() throws Exception {
        List<ValuedEnum> javaVersions = Arrays.asList(
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_0"),
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_1"),
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_2")
        );

        Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);
        assertEquals(3, enumMap.size());

        for (ValuedEnum javaVersion : javaVersions) {
            assertEquals(javaVersion, enumMap.get(javaVersion.getValue()));
        }

        assertNull(enumMap.get(150));
    }

    @Test
    public void testGetEnumList() throws Exception {
        List<ValuedEnum> javaVersions = Arrays.asList(
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_0"),
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_1"),
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_2")
        );

        List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
        assertEquals(javaVersions, enumList);
    }

    @Test
    public void testIterator() throws Exception {
        List<ValuedEnum> javaVersions = Arrays.asList(
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_0"),
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_1"),
                ValuedEnum.getEnum(VALUED_ENUM_CLASS, "JavaVersionEnum", "JAVA1_2")
        );

        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(VALUED_ENUM_CLASS);
        List<ValuedEnum> enumList = new ArrayList<>();
        while (iterator.hasNext()) {
            enumList.add(iterator.next());
        }

        assertEquals(javaVersions, enumList);
    }
}