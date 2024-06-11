
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;


public class SameTest {

    private static final Object TEST_OBJECT = new Object();

    @Test
    public void testMatchesWithSameObject() {
        Same sameMatcher = new Same(TEST_OBJECT);
        Assert.assertTrue(sameMatcher.matches(TEST_OBJECT));
    }

    @Test
    public void testMatchesWithDifferentObject() {
        Same sameMatcher = new Same(TEST_OBJECT);
        Assert.assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Same sameMatcher = new Same(TEST_OBJECT);
        Description description = new DescriptionStub();
        sameMatcher.describeTo(description);
        Assert.assertEquals("same(\"" + TEST_OBJECT.toString() + "\")", description.toString());
    }

    private class DescriptionStub implements Description {
        private String description = "";

        @Override
        public Description appendText(String text) {
            description += text;
            return this;
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
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            return null;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            return null;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends Description> values) {
            return null;
        }

        @Override
        public Description appendList(String start, String separator, String end, Description... values) {
            return null;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, Object... values) {
            return null;
        }

        @Override
        public Description appendNewLine() {
            return null;
        }

        @Override
        public Description appendText(char c) {
            return null;
        }

        @Override
        public Description appendText(CharSequence sequence) {
            return null;
        }

        @Override
        public Description appendText(StringBuffer str) {
            return null;
        }

        @Override
        public Description appendText(Throwable throwable) {
            return null;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
