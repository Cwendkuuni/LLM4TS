package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Object actual = wanted;
        Assert.assertTrue(same.matches(actual));
        Assert.assertEquals(wanted.toString(), same.description().toString());
    }

    @Test
    public void testSameReference() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Object actual = wanted;
        Assert.assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Object actual = new Object();
        Assert.assertFalse(same.matches(actual));
    }

    @Test
    public void testDifferentTypes() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Integer actual = 1;
        Assert.assertFalse(same.matches(actual));
    }

    @Test
    public void testString() {
        String wanted = "test";
        Same same = new Same(wanted);

        String actual = "test";
        Assert.assertTrue(same.matches(actual));
        Assert.assertEquals("same(\"test\")", same.description().toString());
    }

    @Test
    public void testChar() {
        Character wanted = 'a';
        Same same = new Same(wanted);

        Character actual = 'a';
        Assert.assertTrue(same.matches(actual));
        Assert.assertEquals("same('a')", same.description().toString());
    }

    @Test
    public void testMockitoMock() {
        Object wanted = mock(Object.class);
        Same same = new Same(wanted);

        Object actual = mock(Object.class);
        when(actual.toString()).thenReturn("mocked");

        Assert.assertFalse(same.matches(actual));
        Assert.assertEquals("same(<mocked Object>) [diff between wanted and actual]", same.description().toString());
    }

    @Test
    public void testSerializable() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        // Serialize and deserialize
        byte[] byteArray = new JavaSerializabilityTester().serialize(same);
        Same deserializedSame = (Same) new JavaSerializabilityTester().deserialize(byteArray);

        Object actual = wanted;
        Assert.assertTrue(deserializedSame.matches(actual));
    }
}