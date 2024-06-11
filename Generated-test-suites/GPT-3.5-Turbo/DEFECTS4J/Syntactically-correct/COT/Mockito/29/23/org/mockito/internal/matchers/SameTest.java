
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_positive() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
    }
    
    @Test
    public void testMatches_negative() {
        Same same = new Same("test");
        assertFalse(same.matches("different"));
    }
    
    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = new Description() {
            private StringBuilder stringBuilder = new StringBuilder();
            
            @Override
            public Description appendText(String text) {
                stringBuilder.append(text);
                return this;
            }
            
            @Override
            public Description appendDescriptionOf(ArgumentMatcher<?> arg0) {
                return null;
            }
            
            @Override
            public Description appendMismatchOf(ArgumentMatcher<?> arg0, Object arg1)
                    throws RuntimeException {
                return null;
            }
            
            @Override
            public Description appendValue(Object arg0) {
                return null;
            }
            
            @Override
            public Description appendValueList(String arg0, String arg1, String arg2,
                    Object... arg3) {
                return null;
            }
            
            @Override
            public Description appendValueList(String arg0, String arg1, String arg2,
                    Iterable<?> arg3) {
                return null;
            }
            
            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }
}

