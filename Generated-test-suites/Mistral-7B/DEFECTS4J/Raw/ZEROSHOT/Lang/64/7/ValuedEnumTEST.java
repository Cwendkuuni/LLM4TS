package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.class.getEnum(ValuedEnum.JAVA1_0_VALUE);
        Assert.assertNotNull(javaVersionEnum);
        Assert.assertEquals("Java 1.0", javaVersionEnum.getName());
        Assert.assertEquals(100, javaVersionEnum.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidValue() {
        ValuedEnum.getEnum(ValuedEnum.class, 1000);
    }

    @Test
    public void testGetEnumByString() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersion1_0ByValue = ValuedEnum.getEnum(ValuedEnum.class, javaVersion1_0.getValue());
        Assert.assertEquals(javaVersion1_0, javaVersion1_0ByValue);
    }

    @Test
    public void testGetEnumByName() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersion1_0ByName = ValuedEnum.getEnum(ValuedEnum.class, javaVersion1_0.getName());
        Assert.assertEquals(javaVersion1_0, javaVersion1_0ByName);
    }

    @Test
    public void testGetEnumMap() {
        List enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Map enumMap = new HashMap();
        for (Iterator it = enumList.iterator(); it.hasNext();) {
            ValuedEnum valuedEnum = (ValuedEnum) it.next();
            enumMap.put(valuedEnum.getName(), valuedEnum);
        }
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumMap, "Java 1.0");
        Assert.assertEquals(javaVersion1_0, ValuedEnum.JAVA1_0);
    }

    @Test
    public void testGetEnumList() {
        List enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Assert.assertEquals(4, enumList.size());
        for (int i = 0; i < enumList.size(); i++) {
            ValuedEnum valuedEnum = (ValuedEnum) enumList.get(i);
            switch ((int) valuedEnum.getValue()) {
                case 100:
                    Assert.assertEquals("Java 1.0", valuedEnum.getName());
                    break;
                case 110:
                    Assert.assertEquals("Java 1.1", valuedEnum.getName());
                    break;
                case 120:
                    Assert.assertEquals("Java 1.2", valuedEnum.getName());
                    break;
                case 130:
                    Assert.assertEquals("Java 1.3", valuedEnum.getName());
                    break;
            }
        }
    }

    @Test
    public void testIterator() {
        List enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Iterator iterator = ValuedEnum.iterator(ValuedEnum.class);
        int index = 0;
        while (iterator.hasNext()) {
            ValuedEnum valuedEnum = (ValuedEnum) iterator.next();
            Assert.assertEquals(enumList.get(index), valuedEnum);
            index++;
        }
    }

    @Test
    public void testCompareTo() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersion1_1 = ValuedEnum.JAVA1_1;
        ValuedEnum javaVersion1_2 = ValuedEnum.JAVA1_2;
        ValuedEnum javaVersion1_3 = ValuedEnum.JAVA1_3;

        int compareResult = javaVersion1_0.compareTo(javaVersion1_1);
        Assert.assertTrue(compareResult < 0);

        compareResult = javaVersion1_1.compareTo(javaVersion1_0);
        Assert.assertTrue(compareResult > 0);

        compareResult = javaVersion1_0.compareTo(javaVersion1_0);
        Assert.assertEquals(0, compareResult);

        compareResult = javaVersion1_0.compareTo(javaVersion1_3);
        Assert.assertTrue(compareResult < 0);

        compareResult = javaVersion1_3.compareTo(javaVersion1_0);
        Assert.assertTrue(compareResult > 0);

        compareResult = javaVersion1_1.compareTo(javaVersion1_2);
        Assert.assertTrue(compareResult < 0);

        compareResult = javaVersion1_2.compareTo(javaVersion1_1);
        Assert.assertTrue(compareResult > 0);

        compareResult = javaVersion1_2.compareTo(javaVersion1_2);
        Assert.assertEquals(0, compareResult);
    }
}