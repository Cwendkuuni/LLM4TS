package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SameTest {

    private Same same;
    
    @Before
    public void setup() {
        same = new Same("hello");
    }

    @Test
    public void testMatches() {
        // Typical use case: same object instance is passed
        Object actual = "hello";
        assertTrue(same.matches(actual));

        // Error scenario: different object instance is passed
        actual = new String("hello");
        assertFalse(same.matches(actual));
        
        // Edge case: null object is passed
        actual = null;
        assertFalse(same.matches(actual));
    }

    @Test
    public void testDescribeTo() {
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
            public Description appendNewLine() {
                stringBuilder.append("\n");
                return this;
            }
            
            @Override
            public Description appendValue(Object o) {
                stringBuilder.append(o);
                return this;
            }
            
            @Override
            public <T> Description appendValueList(String s, String s1, String s2, T... ts) {
                stringBuilder.append("[");
                for (int i = 0; i < ts.length; i++) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    appendValue(ts[i]);
                }
                stringBuilder.append("]");
                return this;
            }
            
            @Override
            public <T> Description appendValueList(String s, String s1, String s2, Iterable<T> iterable) {
                stringBuilder.append("[");
                int i = 0;
                for (T item : iterable) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    appendValue(item);
                    i++;
                }
                stringBuilder.append("]");
                return this;
            }
            
            @Override
            public Description appendList(String s, String s1, String s2, Iterable<? extends SelfDescribing> iterable) {
                stringBuilder.append("[");
                int i = 0;
                for (SelfDescribing item : iterable) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    appendDescriptionOf(item);
                    i++;
                }
                stringBuilder.append("]");
                return this;
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

        // Typical use case: Same object
        same.describeTo(description);
        assertEquals("same(\"hello\")", description.toString());

        // Edge case: Same null object
        same = new Same(null);
        description = new Description() {
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
            public Description appendNewLine() {
                stringBuilder.append("\n");
                return this;
            }
            
            @Override
            public Description appendValue(Object o) {
                stringBuilder.append(o);
                return this;
            }
            
            @Override
            public <T> Description appendValueList(String s, String s1, String s2, T... ts) {
                stringBuilder.append("[");
                for (int i = 0; i < ts.length; i++) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    appendValue(ts[i]);
                }
                stringBuilder.append("]");
                return this;
            }
            
            @Override
            public <T> Description appendValueList(String s, String s1, String s2, Iterable<T> iterable) {
                stringBuilder.append("[");
                int i = 0;
                for (T item : iterable) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    appendValue(item);
                    i++;
                }
                stringBuilder.append("]");
                return this;
            }
            
            @Override
            public Description appendList(String s, String s1, String s2, Iterable<? extends SelfDescribing> iterable) {
                stringBuilder.append("[");
                int i = 0;
                for (SelfDescribing item : iterable) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }
                    appendDescriptionOf(item);
                    i++;
                }
                stringBuilder.append("]");
                return this;
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
        same.describeTo(description);
        assertEquals("same(null)", description.toString());
    }
}