import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;

public class SameTest {

    @Test
    public void testMatches_WhenEquals_ReturnsTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        Same same = new Same(obj1);

        assertTrue(same.matches(obj2));
    }

    @Test
    public void testMatches_WhenNotEquals_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Same same = new Same(obj1);

        assertFalse(same.matches(obj2));
    }

    @Test
    public void testMatches_WhenNull_ReturnsFalse() {
        Object obj1 = new Object();

        Same same = new Same(obj1);

        assertFalse(same.matches(null));
    }


    @Test
    public void testDescribeTo_WhenStringArgument_QuotesStringWithDoubleQuotes() {
        String str = "test";

        Same same = new Same(str);
        Description description = new Description() {
            private StringBuilder desc = new StringBuilder();

            @Override
            public Description appendText(String text) {
                desc.append(text);
                return this;
            }

            @Override
            public String toString() {
                return desc.toString();
            }
        };

        same.describeTo(description);

        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_WhenCharacterArgument_QuotesCharacterWithSingleQuotes() {
        Character ch = 'a';

        Same same = new Same(ch);
        Description description = new Description() {
            private StringBuilder desc = new StringBuilder();

            @Override
            public Description appendText(String text) {
                desc.append(text);
                return this;
            }

            @Override
            public String toString() {
                return desc.toString();
            }
        };

        same.describeTo(description);

        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeTo_WhenOtherArgument_QuotesObjectToString() {
        Object obj = new Object();

        Same same = new Same(obj);
        Description description = new Description() {
            private StringBuilder desc = new StringBuilder();

            @Override
            public Description appendText(String text) {
                desc.append(text);
                return this;
            }

            @Override
            public String toString() {
                return desc.toString();
            }
        };

        same.describeTo(description);

        assertEquals("same(" + obj.toString() + ")", description.toString());
    }
}