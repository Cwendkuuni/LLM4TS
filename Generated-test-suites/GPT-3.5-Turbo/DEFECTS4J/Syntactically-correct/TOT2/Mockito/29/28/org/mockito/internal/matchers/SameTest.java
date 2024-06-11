import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.internal.matchers.Same.same;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = new Description() {
            @Override
            public Description appendText(CharSequence charSequence) {
                return null;
            }

            @Override
            public Description appendDescriptionOf(org.hamcrest.SelfDescribing selfDescribing) {
                return null;
            }

            @Override
            public Description appendValue(Object o) {
                return null;
            }

            @Override
            public <T> Description appendValueList(String s, String s1, String s2, T... ts) {
                return null;
            }

            @Override
            public <T> Description appendValueList(String s, String s1, String s2, Iterable<? extends T> iterable) {
                return null;
            }

            @Override
            public <T> Description appendList(String s, String s1, String s2, Iterable<T> iterable) {
                return null;
            }

            @Override
            public Description appendText(String s) {
                return null;
            }

            @Override
            public String toString() {
                return null;
            }
        };
        same.describeTo(description);
    }

    // Edge case: Object input is null
    @Test
    public void testMatchesNullObject() {
        Same same = new Same(null);
        assertTrue(same.matches(null));
    }

    // Edge case: Character input
    @Test
    public void testMatchesCharacterObject() {
        Same same = new Same('a');
        assertFalse(same.matches('b'));
    }

    // Edge case: Different string inputs
    @Test
    public void testMatchesDifferentStringInputs() {
        Same same = new Same("hello");
        assertFalse(same.matches("world"));
    }

    // Error scenario: Object comparison error
    @Test
    public void testMatchesObjectComparisonError() {
        Same same = new Same(new Object());
        assertFalse(same.matches(new Object()));
    }

    // Error scenario: Non-string/non-character object with describeTo
    @Test
    public void testDescribeToGenericObject() {
        Same same = new Same(123);
        Description description = new Description() {
            @Override
            public Description appendText(CharSequence charSequence) {
                return null;
            }

            @Override
            public Description appendDescriptionOf(org.hamcrest.SelfDescribing selfDescribing) {
                return null;
            }

            @Override
            public Description appendValue(Object o) {
                return null;
            }

            @Override
            public <T> Description appendValueList(String s, String s1, String s2, T... ts) {
                return null;
            }

            @Override
            public <T> Description appendValueList(String s, String s1, String s2, Iterable<? extends T> iterable) {
                return null;
            }

            @Override
            public <T> Description appendList(String s, String s1, String s2, Iterable<T> iterable) {
                return null;
            }

            @Override
            public Description appendText(String s) {
                return null;
            }

            @Override
            public String toString() {
                return null;
            }
        };
        same.describeTo(description);
    }
}