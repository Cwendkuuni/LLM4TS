package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.serialVersionUID;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Same sameObject = new Same(new Object());
        Object actual = new Object();
        assertTrue(sameObject.matches(actual));
    }

    @Test
    public void testNotSameInstance() {
        Same sameObject1 = new Same(new Object());
        Object actual1 = new Object();
        Object actual2 = new Object(); // different instance
        assertFalse(sameObject1.matches(actual2));
    }

    @Test
    public void testSameString() {
        Same sameString = new Same("test");
        String actual = "test";
        assertTrue(sameString.matches(actual));
    }

    @Test
    public void testNotSameString() {
        Same sameString = new Same("test");
        String actual = "different";
        assertFalse(sameString.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacter = new Same('a');
        char actual = 'a';
        assertTrue(sameCharacter.matches(actual));
    }

    @Test
    public void testNotSameCharacter() {
        Same sameCharacter = new Same('a');
        char actual = 'b';
        assertFalse(sameCharacter.matches(actual));
    }

    @Test
    public void testDescribeTo_Object() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);

        Description description = new Description();
        sameObject.describeTo(description);
        String expectedDescription = "same(" + wanted.toString() + ")";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeTo_String() {
        String wanted = "test";
        Same sameString = new Same(wanted);

        Description description = new Description();
        sameString.describeTo(description);
        String expectedDescription = "same(\"test\")";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        char wanted = 'a';
        Same sameCharacter = new Same(wanted);

        Description description = new Description();
        sameCharacter.describeTo(description);
        String expectedDescription = "same('a')";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testSerializable() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);

        // Serialize and deserialize
        byte[] serialized = serialize(sameObject);
        Same sameObjectDeserialized = deserialize(Same.class, serialized);

        // Check if the deserialized object is the same as the original
        assertTrue(sameObject.matches(sameObjectDeserialized.wanted));
    }
}