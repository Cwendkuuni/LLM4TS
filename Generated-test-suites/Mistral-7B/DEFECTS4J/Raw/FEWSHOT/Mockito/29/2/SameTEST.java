package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object obj = new Object();
        Matcher<Object> sameObj = new Same(obj);

        assertThat(sameObj, matches(obj));
    }

    @Test
    public void testDifferentInstances() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Matcher<Object> sameObj = new Same(obj1);

        assertThat(sameObj, matches(obj2)); // Should be false
    }

    @Test
    public void testSameString() {
        String str = "test";
        Matcher<String> sameStr = new Same(str);

        assertThat(sameStr, matches(str));
    }

    @Test
    public void testDifferentStrings() {
        String str1 = "test1";
        String str2 = "test2";
        Matcher<String> sameStr = new Same(str1);

        assertThat(sameStr, matches(str2)); // Should be false
    }

    @Test
    public void testSameCharacter() {
        Character chr = 'c';
        Matcher<Character> sameChr = new Same(chr);

        assertThat(sameChr, matches(chr));
    }

    @Test
    public void testDifferentCharacters() {
        Character chr1 = 'a';
        Character chr2 = 'b';
        Matcher<Character> sameChr = new Same(chr1);

        assertThat(sameChr, matches(chr2)); // Should be false
    }

    @Test
    public void testDescription() {
        Object obj = new Object();

        Matcher<Object> sameObj = new Same(obj);
        Description description = new Description();

        sameObj.describeTo(description);

        String expectedDescription = "same(" + obj.toString() + ")";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}