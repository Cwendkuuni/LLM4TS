package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        // Test when actual is equal to wanted
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertTrue(same.matches(new String("test")));

        // Test when actual is not equal to wanted
        assertFalse(same.matches("other"));
        assertFalse(same.matches(new String("other")));
    }

    @Test
    public void testDescribeTo() {
        Description description = new NullDescription();
        Same same = new Same("test");
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    private class NullDescription implements Description {

        @Override
        public Description appendText(String text) {
            return null;
        }

        @Override
        public Description appendDescriptionOf(Object value) {
            return null;
        }

        @Override
        public Description appendValue(Object value) {
            return null;
        }

        @Override
        public <T> T as(Class<T> descriptionType) {
            return null;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<?> values) {
            return null;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
            return null;
        }

        @Override
        public Description appendText(StringBuilder newDescription) {
            return null;
        }

        @Override
        public Description appendNewLine() {
            return null;
        }
    }

}