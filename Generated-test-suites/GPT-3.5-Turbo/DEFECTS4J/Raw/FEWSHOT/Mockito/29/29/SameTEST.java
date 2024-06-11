package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;


public class SameTest {
    @Test
    public void testMatches() {
        // Create an instance of Same with an object
        Same same = new Same("abc");

        // Test matches with different objects
        assertFalse(same.matches("def"));
        assertFalse(same.matches(123));

        // Test matches with the same object
        assertTrue(same.matches("abc"));
    }

    @Test
    public void testDescribeTo() {
        // Create an instance of Same with an object
        Same same = new Same("abc");

        // Create a Description object for testing
        Description description = new Description() {
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public Description appendText(CharSequence charSequence) {
                stringBuilder.append(charSequence);
                return this;
            }

            @Override
            public Description appendDescriptionOf(SelfDescribing selfDescribing) {
                selfDescribing.describeTo(this);
                return this;
            }

            @Override
            public Description appendValue(Object o) {
                stringBuilder.append(o.toString());
                return this;
            }

            @Override
            public <T> Description appendValueList(String s, String s1, String s2, T... ts) {
                return null;
            }

            @Override
            public <T> Description appendValueList(String s, String s1, String s2, Iterable<T> iterable) {
                return null;
            }

            @Override
            public Description appendList(String s, String s1, String s2, Iterable<? extends SelfDescribing> iterable) {
                return null;
            }

            @Override
            public Description appendList(String s, String s1, String s2, SelfDescribing... selfDescribings) {
                return null;
            }

            @Override
            public Description appendText(String s) {
                stringBuilder.append(s);
                return this;
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };

        // Test describeTo
        same.describeTo(description);
        assertEquals("same(\"abc\")", description.toString());
    }
}