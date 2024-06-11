package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSame() {
        Object wanted = new Object();

        Same sameMatcher = new Same(wanted);

        Object actual = new Object();

        // Test matches method
        assertTrue(sameMatcher.matches(wanted));
        assertFalse(sameMatcher.matches(actual));

        // Test describeTo method
        Description description = new Description();
        sameMatcher.describeTo(description);
        String descriptionText = description.toString();
        assertThat(descriptionText, is("same(" + wanted.toString() + ")"));

        // Test serialization
        Same serializedSameMatcher = (Same) Mockito.mock(Same.class);
        Mockito.when(serializedSameMatcher.matches((Object) Mockito.any())).thenCallRealMethod();
        Serializable serialize = Mockito.spy(sameMatcher);
        Mockito.doReturn(serializedSameMatcher).when(serialize).serialize();
        Object deserialized = Mockito.when(serializedSameMatcher.deserialize((Serializable) Mockito.any())).thenReturn(sameMatcher).getMock();
        assertTrue(sameMatcher.matches(wanted));
        assertTrue(deserialized.matches(wanted));
    }

    @Test
    public void testSameWithString() {
        String wanted = "testString";

        Same sameMatcher = new Same(wanted);

        String actual = "testString";

        assertTrue(sameMatcher.matches(wanted));
        assertTrue(sameMatcher.matches(actual));

        Description description = new Description();
        sameMatcher.describeTo(description);
        String descriptionText = description.toString();
        assertThat(descriptionText, is("same(\"testString\")"));
    }

    @Test
    public void testSameWithCharacter() {
        char wanted = 'a';

        Same sameMatcher = new Same(wanted);

        char actual = 'a';

        assertTrue(sameMatcher.matches(wanted));
        assertTrue(sameMatcher.matches(actual));

        Description description = new Description();
        sameMatcher.describeTo(description);
        String descriptionText = description.toString();
        assertThat(descriptionText, is("same('a')"));
    }

    private boolean assertFalse(boolean condition) {
        if (condition) {
            fail("Expected assertion to fail");
        }
        return condition;
    }

    private boolean assertTrue(boolean condition) {
        if (!condition) {
            fail("Expected assertion to pass");
        }
        return condition;
    }
}