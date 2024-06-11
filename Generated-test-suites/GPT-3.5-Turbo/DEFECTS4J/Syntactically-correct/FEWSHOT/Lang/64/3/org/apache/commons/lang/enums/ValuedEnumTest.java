package org.apache.commons.lang.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals(100, java1_0.getValue());

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        assertEquals(110, java1_1.getValue());

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals(120, java1_2.getValue());

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);

        assertEquals(-1, java1_0.compareTo(java1_1));
        assertEquals(0, java1_1.compareTo(java1_1));
        assertEquals(1, java1_1.compareTo(java1_0));
        assertEquals(-1, java1_2.compareTo(java1_3));
    }

    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
    }

}