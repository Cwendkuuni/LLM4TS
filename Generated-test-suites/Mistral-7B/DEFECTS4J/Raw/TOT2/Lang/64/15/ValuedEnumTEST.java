package org.apache.commons.lang.enums.test;

import org.apache.commons.lang.EnumUtils;
import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        ValuedEnum enumInstance = ValuedEnum.JAVA1_0;
        int expectedValue = 100;
        int actualValue = enumInstance.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetEnumNullEnumClass() {
        Class<ValuedEnum> enumClass = null;
        int value = 100;
        Enum expectedEnum = ValuedEnum.JAVA1_0;
        Enum actualEnum = getEnum(enumClass, value);
        assertThat(actualEnum, is(nullValue()));
    }

    @Test
    public void testGetEnumNegativeValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        int value = -1;
        Enum expectedEnum = null;
        Enum actualEnum = getEnum(enumClass, value);
        assertThat(actualEnum, is(equalTo(expectedEnum)));
    }

    // Add test cases for other edge cases and exceptions here

    private Enum getEnum(Class enumClass, int value) {
        return EnumUtils.getEnum(enumClass, value);
    }
}