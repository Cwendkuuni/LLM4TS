package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.mockito.internal.matchers.Same.serialVersionUID;

public class SameTest {

    @Test
    public void testSameMatchesSameObject() {
        Object wanted = new Object();
        Object actual = wanted;

        Matcher<Object> sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMatchesNullWithNull() {
        Object wanted = null;
        Object actual = null;

        Matcher<Object> sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameDoesNotMatchDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        Matcher<Object> sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameDescribesCorrectly() {
        Object wanted = new Object();

        Matcher<Object> sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(" + wanted.toString() + ")";
        String actualDescription = description.toString();

        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testSameSerializesAndDeserializesCorrectly() {
        Object wanted = new Object();

        Matcher<Object> sameMatcher = new Same(wanted);

        // Serialize
        byte[] serialized = TestUtil.serialize(sameMatcher);

        // Deserialize
        Matcher<Object> deserializedSameMatcher = TestUtil.deserialize(serialized);

        // Compare with original matcher
        Assert.assertEquals(sameMatcher, deserializedSameMatcher);
    }
}