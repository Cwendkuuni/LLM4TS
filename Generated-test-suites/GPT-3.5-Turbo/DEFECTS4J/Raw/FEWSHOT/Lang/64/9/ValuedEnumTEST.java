package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetEnum() {
        assertEquals(JavaVersionEnum.JAVA1_0, ValuedEnum.getEnum(JavaVersionEnum.class, 100));
        assertEquals(JavaVersionEnum.JAVA1_1, ValuedEnum.getEnum(JavaVersionEnum.class, 110));
        assertEquals(JavaVersionEnum.JAVA1_2, ValuedEnum.getEnum(JavaVersionEnum.class, 120));
        assertEquals(JavaVersionEnum.JAVA1_3, ValuedEnum.getEnum(JavaVersionEnum.class, 130));
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 200));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, 100);
    }
}