package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java1_0.getValue());

        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, java1_1.getValue());

        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals(120, java1_2.getValue());

        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals(130, java1_3.getValue());
    }
}