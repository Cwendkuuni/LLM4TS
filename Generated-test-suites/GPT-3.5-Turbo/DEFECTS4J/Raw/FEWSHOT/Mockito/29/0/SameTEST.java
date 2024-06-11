package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testMatchesEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        Same sameMatcher = new Same(obj1);
        assertTrue(sameMatcher.matches(obj2));
    }

    @Test
    public void testMatchesDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Same sameMatcher = new Same(obj1);
        assertFalse(sameMatcher.matches(obj2));
    }

    @Test
    public void testDescribeTo() {
        Object obj1 = new Object();

        Same sameMatcher = new Same(obj1);
        Description description = new Description();

        sameMatcher.describeTo(description);

        assertEquals("same(\"" + obj1.toString() + "\")", description.toString());
    }
    
    @Test
    public void testDescribeToWithCharacter() {
        Character character = 'a';

        Same sameMatcher = new Same(character);
        Description description = new Description();

        sameMatcher.describeTo(description);

        assertEquals("same('" + character.toString() + "')", description.toString());
    }
}