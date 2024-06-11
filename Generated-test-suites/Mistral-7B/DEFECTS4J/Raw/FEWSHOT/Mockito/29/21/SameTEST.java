package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.serialVERSIONUID;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class SameTest {

    private Object wanted = new Object();

    @Test
    public void testSameMatchesSameObject() {
        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(wanted));
    }

    @Test
    public void testSameDoesNotMatchDifferentObjects() {
        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testDescribeToWithString() {
        ArgumentMatcher<Object> sameMatcher = new Same("wantedString");
        Description description = new Description();
        sameMatcher.describeTo(description);
        String expectedDescription = "same(wantedString)";
        String actualDescription = description.toString();
        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testDescribeToWithCharacter() {
        ArgumentMatcher<Object> sameMatcher = new Same('wantedChar');
        Description description = new Description();
        sameMatcher.describeTo(description);
        String expectedDescription = "same('wantedChar')";
        String actualDescription = description.toString();
        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testDescribeToWithNull() {
        ArgumentMatcher<Object> sameMatcher = new Same(null);
        Description description = new Description();
        sameMatcher.describeTo(description);
        String expectedDescription = "same(null)";
        String actualDescription = description.toString();
        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testSerialization() {
        ArgumentMatcher<Object> sameMatcher = new Same(wanted);
        Same serializedSameMatcher = (Same) SerializationHelper.serializeAndDeserialize(sameMatcher);
        ArgumentMatcher<Object> deserializedSameMatcher = new Same(wanted);
        assertTrue(serializedSameMatcher.matches(deserializedSameMatcher.wanted));
    }
}