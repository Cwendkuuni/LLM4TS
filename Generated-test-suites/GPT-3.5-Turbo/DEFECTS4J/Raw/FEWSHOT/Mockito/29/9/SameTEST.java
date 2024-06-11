package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        Object object1 = new Object();
        Object object2 = object1;
        Object object3 = new Object();
        
        Same sameMatcher = new Same(object1);
        
        assertTrue(sameMatcher.matches(object2));
        assertTrue(!sameMatcher.matches(object3));
    }
    
    @Test
    public void testDescribeTo() {
        Object object = new Object();
        
        Same sameMatcher = new Same(object);
        
        Description description = new Description() {
            private StringBuilder stringBuilder = new StringBuilder();
            
            @Override
            public Description appendText(String text) {
                stringBuilder.append(text);
                return this;
            }
            
            @Override
            public Description appendText(CharSequence text) {
                stringBuilder.append(text);
                return this;
            }
            
            @Override
            public Description appendDescriptionOf(ArgumentMatcher<?> matcher) {
                return null;
            }
            
            @Override
            public Description appendValue(Object value) {
                return null;
            }
            
            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                return null;
            }
            
            @Override
            public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }
            
            @Override
            public Description appendList(String start, String separator, String end, Iterable<? extends Description> list) {
                return null;
            }
            
            @Override
            public Description appendList(String start, String separator, String end, Description... list) {
                return null;
            }
            
            @Override
            public Description appendValue(Object value, String[] excludeFields) {
                return null;
            }
            
            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };
        
        sameMatcher.describeTo(description);
        
        assertTrue(description.toString().equals("same(" + object.toString() + ")"));
    }
}