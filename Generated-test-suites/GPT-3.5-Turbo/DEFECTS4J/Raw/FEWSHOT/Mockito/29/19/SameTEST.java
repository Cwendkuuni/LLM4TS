package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class SameTest {

    @Test
    public void testMatches() {
        ArgumentMatcher<Object> same = new Same("test");
        Assert.assertTrue(same.matches("test"));
        Assert.assertFalse(same.matches("not test"));
        Assert.assertTrue(same.matches(null));

        same = new Same(123);
        Assert.assertTrue(same.matches(123));
        Assert.assertFalse(same.matches(456));
        Assert.assertTrue(same.matches(null));

        same = new Same(true);
        Assert.assertTrue(same.matches(true));
        Assert.assertFalse(same.matches(false));
        Assert.assertTrue(same.matches(null));
    }

    @Test
    public void testDescribeTo() {
        Description description = new TestDescription();
        ArgumentMatcher<Object> same = new Same("test");
        same.describeTo(description);
        Assert.assertEquals("same(\"test\")", description.toString());

        description = new TestDescription();
        same = new Same(123);
        same.describeTo(description);
        Assert.assertEquals("same(123)", description.toString());

        description = new TestDescription();
        same = new Same(true);
        same.describeTo(description);
        Assert.assertEquals("same(true)", description.toString());
    }

    private class TestDescription extends Description {
        private String desc = "";

        @Override
        public Description appendText(String text) {
            desc += text;
            return this;
        }

        @Override
        public Description appendDescriptionOf(ArgumentMatcher<?> arg0) {
            return null;
        }

        @Override
        public Description appendMismatchOf(ArgumentMatcher<?> arg0, Object arg1) {
            return null;
        }

        @Override
        public Description appendList(String arg0, String arg1, String arg2, ArgumentMatcher<?>... arg3) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0) {
            return null;
        }

        @Override
        public Description appendValueList(String arg0, String arg1, String arg2, Object... arg3) {
            return null;
        }

        @Override
        public Description appendText(CharSequence arg0) {
            return null;
        }

        @Override
        public Description appendNewLine() {
            return null;
        }

        @Override
        public String toString() {
            return desc;
        }
    }
}