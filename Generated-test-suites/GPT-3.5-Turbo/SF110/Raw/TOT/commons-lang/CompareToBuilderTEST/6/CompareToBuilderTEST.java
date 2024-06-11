package org.apache.commons.lang3.builder;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareToBuilderTest {

    private static final Object OBJECT1 = new Object();
    private static final Object OBJECT2 = new Object();
    private static final Object OBJECT3 = new Object();

    // appendSuper() test cases
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();

        // Append different comparison value
        builder.appendSuper(1);
        Assert.assertEquals(1, builder.build().intValue());

        // Append same comparison value
        builder.appendSuper(1);
        Assert.assertEquals(1, builder.build().intValue());
    }

    // append() test cases
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        // Append two different Objects
        builder.append(OBJECT1, OBJECT2);
        Assert.assertEquals(-1, builder.build().intValue());

        // Append two same Objects
        builder.append(OBJECT1, OBJECT1);
        Assert.assertEquals(0, builder.build().intValue());
    }

    // append() with Comparator test cases
    @Test
    public void testAppendWithComparator() {
        CompareToBuilder builder = new CompareToBuilder();

        // Define a Comparator for String
        Comparator<String> stringComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        };

        // Append two different Strings with comparator
        builder.append("abc", "abcd", stringComparator);

        // Expected result: Length of "abc" - Length of "abcd" = -1
        Assert.assertEquals(-1, builder.build().intValue());

        // Append two same Strings with comparator
        builder.append("abc", "abc", stringComparator);

        // Expected result: Length of "abc" - Length of "abc" = 0
        Assert.assertEquals(0, builder.build().intValue());
    }

    // append() test cases for different data types

    // append() test cases for arrays

    // append() test cases for primitive data types

    // append() test cases for primitive wrapper classes

    // reflectionCompare() test cases

    // reflectionAppend() test cases

    // toComparison() test cases
    @Test
    public void testToComparison() {
        CompareToBuilder builder = new CompareToBuilder();

        // Result is 0 by default
        Assert.assertEquals(0, builder.toComparison());

        // Change comparison value
        builder.append(1, 2);
        Assert.assertEquals(-1, builder.toComparison());
    }

}