package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        String str = "foo";
        Same same = new Same(str);
        assertTrue(same.matches(str));

        Integer num = 5;
        same = new Same(num);
        assertTrue(same.matches(num));

        Object obj = new Object();
        same = new Same(obj);
        assertTrue(same.matches(obj));

        assertNull(same.matches(null));
    }

    @Test
    public void testDescribeTo() {
        String str = "foo";
        Same same = new Same(str);

        Description description = new FakeDescription();
        same.describeTo(description);
        assertEquals("same(\"foo\")", description.toString());

        Integer num = 5;
        same = new Same(num);

        description = new FakeDescription();
        same.describeTo(description);
        assertEquals("same(5)", description.toString());

        Object obj = new Object();
        same = new Same(obj);

        description = new FakeDescription();
        same.describeTo(description);
        assertEquals("same(" + obj.toString() + ")", description.toString());
    }

    private class FakeDescription implements Description {
        private StringBuilder builder = new StringBuilder();

        public Description appendText(String text) {
            builder.append(text);
            return this;
        }

        public Description appendDescriptionOf(SelfDescribing value) {
            value.describeTo(this);
            return this;
        }

        public Description appendValue(Object value) {
            builder.append(value);
            return this;
        }

        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            return this;
        }

        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            return this;
        }

        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            return this;
        }

        public Description appendList(String start, String separator, String end, Object... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, boolean... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, byte... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, char... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, double... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, float... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, int... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, long... values) {
            return this;
        }

        public Description appendValueList(String start, String separator, String end, short... values) {
            return this;
        }

        @Override
        public Description appendText(CharSequence text) {
            return this;
        }

        @Override
        public Description appendDescriptionOf(Description value) {
            return this;
        }

        @Override
        public Description appendValue(Object value) {
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, T value, T... values) {
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Object... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, boolean value, boolean... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, byte value, byte... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, char value, char... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, double value, double... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, float value, float... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, int value, int... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, long value, long... values) {
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, short value, short... values) {
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Object value, Object... values) {
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, T[] values) {
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<? extends T> values) {
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Object value, Object... values) {
            return this;
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}