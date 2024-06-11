package org.mockito.internal.matchers.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObjectMatcher() {
        Same sameMatcher = new Same(new Object());
        Object actualObject = new Object();
        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testDifferentObjectsMatcher() {
        Same sameMatcher = new Same(new Object());
        Object differentObject = new String("different");
        assertFalse(sameMatcher.matches(differentObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWantedObject() {
        new Same(null);
    }

    @Test
    public void testStringMatcher() {
        Same sameMatcher = new Same("test");
        String actualString = "test";
        assertTrue(sameMatcher.matches(actualString));
    }

    @Test
    public void testDifferentStringMatcher() {
        Same sameMatcher = new Same("test");
        String differentString = "different";
        assertFalse(sameMatcher.matches(differentString));
    }

    @Test
    public void testCharacterMatcher() {
        Same sameMatcher = new Same('a');
        Character actualCharacter = 'a';
        assertTrue(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testDifferentCharacterMatcher() {
        Same sameMatcher = new Same('a');
        Character differentCharacter = 'b';
        assertFalse(sameMatcher.matches(differentCharacter));
    }

    @Test
    public void testSerializableObjectMatcher() {
        Same sameMatcher = new Same(new SerializableObject());
        SerializableObject actualObject = new SerializableObject();
        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testDifferentSerializableObjectMatcher() {
        Same sameMatcher = new Same(new SerializableObject());
        SerializableObject differentObject = new SerializableObject();
        assertNotSame(sameMatcher, differentObject);
        assertFalse(sameMatcher.matches(differentObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializableDifferentTypes() {
        Same sameMatcher = new Same(new Object());
        new SerializableObject();
    }

    @Test
    public void testDescribeTo() {
        Object wantedObject = new Object();
        Description description = new Description();
        Same sameMatcher = new Same(wantedObject);
        sameMatcher.describeTo(description);
        Matcher expectedMatcher = CoreMatchers.same(wantedObject);
        Matcher actualMatcher = new Same(wantedObject);
        assertEquals(expectedMatcher.description().toString(), description.toString());
    }
}