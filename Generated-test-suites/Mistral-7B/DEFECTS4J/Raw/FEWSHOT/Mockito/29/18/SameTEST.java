package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object obj1 = new Object();
        Same sameObj = new Same(obj1);

        Object obj2 = obj1;
        assertTrue(sameObj.matches(obj2));

        Object obj3 = new Object();
        assertFalse(sameObj.matches(obj3));
    }

    @Test
    public void testSameString() {
        String str1 = "test";
        Same sameStr = new Same(str1);

        String str2 = "test";
        assertTrue(sameStr.matches(str2));

        String str3 = "other";
        assertFalse(sameStr.matches(str3));
    }

    @Test
    public void testSameCharacter() {
        char ch1 = 'a';
        Same sameCh = new Same(ch1);

        char ch2 = 'a';
        assertTrue(sameCh.matches(ch2));

        char ch3 = 'b';
        assertFalse(sameCh.matches(ch3));
    }

    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Same sameObj = new Same(obj);

        Description description = new Description();
        sameObj.describeTo(description);

        String expectedDescription = "same(" + obj.toString() + ")";
        String actualDescription = description.toString();
        assertEquals(expectedDescription, actualDescription);
    }
}