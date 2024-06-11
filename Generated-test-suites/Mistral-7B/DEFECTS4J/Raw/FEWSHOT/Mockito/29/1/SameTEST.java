package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object obj = new Object();
        Matcher<Object> sameObject = new Same(obj);
        assertThat(sameObject, sameObject);
        assertThat(obj, sameObject);
    }

    @Test
    public void testSameString() {
        String str = "foo";
        Matcher<String> sameString = new Same(str);
        assertThat(sameString, sameString);
        assertThat(str, sameString);
    }

    @Test
    public void testSameCharacter() {
        Character ch = 'a';
        Matcher<Character> sameCharacter = new Same(ch);
        assertThat(sameCharacter, sameCharacter);
        assertThat(ch, sameCharacter);
    }

    @Test
    public void testDifferentInstance() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Matcher<Object> sameObject = new Same(obj1);
        assertFalse(sameObject.matches(obj2));
    }

    @Test
    public void testDifferentString() {
        String str1 = "foo";
        String str2 = "bar";
        Matcher<String> sameString = new Same(str1);
        assertFalse(sameString.matches(str2));
    }

    @Test
    public void testDifferentCharacter() {
        Character ch1 = 'a';
        Character ch2 = 'b';
        Matcher<Character> sameCharacter = new Same(ch1);
        assertFalse(sameCharacter.matches(ch2));
    }

    @Test
    public void testStringWithQuotes() {
        String str = "\"foo\"";
        Matcher<String> sameString = new Same(str);
        String expectedDescription = "same(\"foo\")";
        Description description = new Description();
        sameString.describeTo(description);
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testCharacterWithQuotes() {
        Character ch = '\'a\'';
        Matcher<Character> sameCharacter = new Same(ch);
        String expectedDescription = "same('a')";
        Description description = new Description();
        sameCharacter.describeTo(description);
        assertEquals(expectedDescription, description.toString());
    }
}