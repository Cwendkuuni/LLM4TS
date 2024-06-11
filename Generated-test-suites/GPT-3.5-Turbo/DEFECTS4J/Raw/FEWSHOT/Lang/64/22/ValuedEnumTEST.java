package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetEnum() {
        assertEquals(JavaVersionEnum.JAVA1_0, ValuedEnum.getEnum(JavaVersionEnum.class, JavaVersionEnum.JAVA1_0_VALUE));
        assertEquals(JavaVersionEnum.JAVA1_1, ValuedEnum.getEnum(JavaVersionEnum.class, JavaVersionEnum.JAVA1_1_VALUE));
        assertEquals(JavaVersionEnum.JAVA1_2, ValuedEnum.getEnum(JavaVersionEnum.class, JavaVersionEnum.JAVA1_2_VALUE));
        assertEquals(JavaVersionEnum.JAVA1_3, ValuedEnum.getEnum(JavaVersionEnum.class, JavaVersionEnum.JAVA1_3_VALUE));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 0));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test
    public void testGetValue() {
        assertEquals(JavaVersionEnum.JAVA1_0_VALUE, JavaVersionEnum.JAVA1_0.getValue());
        assertEquals(JavaVersionEnum.JAVA1_1_VALUE, JavaVersionEnum.JAVA1_1.getValue());
        assertEquals(JavaVersionEnum.JAVA1_2_VALUE, JavaVersionEnum.JAVA1_2.getValue());
        assertEquals(JavaVersionEnum.JAVA1_3_VALUE, JavaVersionEnum.JAVA1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_0));
        assertEquals(-10, JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_1));
        assertEquals(10, JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_0));
    }

    @Test(expected=ClassCastException.class)
    public void testCompareToDifferentClass() {
        JavaVersionEnum.JAVA1_0.compareTo(Util.stripLeadingHyphens(""));
    }

    @Test
    public void testToString() {
        assertEquals("JavaVersion[Java 1.0=100]", JavaVersionEnum.JAVA1_0.toString());
        assertEquals("JavaVersion[Java 1.1=110]", JavaVersionEnum.JAVA1_1.toString());
        assertEquals("JavaVersion[Java 1.2=120]", JavaVersionEnum.JAVA1_2.toString());
        assertEquals("JavaVersion[Java 1.3=130]", JavaVersionEnum.JAVA1_3.toString());
    }
}