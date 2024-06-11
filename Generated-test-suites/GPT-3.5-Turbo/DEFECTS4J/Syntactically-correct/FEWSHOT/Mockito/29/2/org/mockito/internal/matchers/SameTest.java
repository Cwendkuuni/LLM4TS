package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;


public class SameTest {
    @Test
    public void testMatchesWithSameObject() {
        Object object = new Object();
        Same same = new Same(object);

        // Verify that the Same.matches() method returns true when the actual object is the same as the wanted object
        assertTrue(same.matches(object));
    }

    @Test
    public void testMatchesWithDifferentObject() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        // Verify that the Same.matches() method returns false when the actual object is different from the wanted object
        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeToWithString() {
        String string = "test";
        Same same = new Same(string);
        Description description = new StringDescription();

        // Verify that the Same.describeTo() method appends the string surrounded by double quotes to the description
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeToWithCharacter() {
        Character character = 'a';
        Same same = new Same(character);
        Description description = new StringDescription();

        // Verify that the Same.describeTo() method appends the character surrounded by single quotes to the description
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }
}