package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        Same same = new Same("foo");
        assertTrue(same.matches("foo"));
        assertFalse(same.matches("bar"));
    }
    
    @Test
    public void testDescribeTo() {
        Same same1 = new Same("foo");
        Same same2 = new Same('f');
        Same same3 = new Same(10);
        
        DescriptionMock descriptionMock1 = new DescriptionMock();
        DescriptionMock descriptionMock2 = new DescriptionMock();
        DescriptionMock descriptionMock3 = new DescriptionMock();
        
        same1.describeTo(descriptionMock1);
        same2.describeTo(descriptionMock2);
        same3.describeTo(descriptionMock3);
        
        assertEquals("same(\"foo\")", descriptionMock1.getDescription());
        assertEquals("same('f')", descriptionMock2.getDescription());
        assertEquals("same(10)", descriptionMock3.getDescription());
    }
    
    private class DescriptionMock implements Description {
        private String description;
        
        @Override
        public Description appendText(String arg0) {
            description += arg0;
            return this;
        }

        @Override
        public Description appendDescriptionOf(SelfDescribing arg0) {
            // Not needed for this test
            return null;
        }

        @Override
        public Description appendValue(Object arg0) {
            description += arg0.toString();
            return this;
        }

        @Override
        public <T> Description appendValueList(String arg0, String arg1, String arg2, T... arg3) {
            // Not needed for this test
            return null;
        }

        @Override
        public <T> Description appendValueList(String arg0, String arg1, String arg2, Iterable<T> arg3) {
            // Not needed for this test
            return null;
        }

        @Override
        public Description appendList(String arg0, String arg1, String arg2, Iterable<? extends SelfDescribing> arg3) {
            // Not needed for this test
            return null;
        }

        @Override
        public Description appendText(Description arg0) {
            // Not needed for this test
            return null;
        }

        @Override
        public String toString() {
            return description;
        }

        public String getDescription() {
            return description;
        }
    }
}