package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetValue() {
        JavaVersionEnum java10 = JavaVersionEnum.getEnum(100);
        assertEquals(100, java10.getValue());

        JavaVersionEnum java11 = JavaVersionEnum.getEnum(110);
        assertEquals(110, java11.getValue());

        JavaVersionEnum java12 = JavaVersionEnum.getEnum(120);
        assertEquals(120, java12.getValue());

        JavaVersionEnum java13 = JavaVersionEnum.getEnum(130);
        assertEquals(130, java13.getValue());
    }

    @Test
    public void testToString() {
        JavaVersionEnum java10 = JavaVersionEnum.getEnum(100);
        assertEquals("JavaVersion[Java 1.0=100]", java10.toString());

        JavaVersionEnum java11 = JavaVersionEnum.getEnum(110);
        assertEquals("JavaVersion[Java 1.1=110]", java11.toString());

        JavaVersionEnum java12 = JavaVersionEnum.getEnum(120);
        assertEquals("JavaVersion[Java 1.2=120]", java12.toString());

        JavaVersionEnum java13 = JavaVersionEnum.getEnum(130);
        assertEquals("JavaVersion[Java 1.3=130]", java13.toString());
    }
}