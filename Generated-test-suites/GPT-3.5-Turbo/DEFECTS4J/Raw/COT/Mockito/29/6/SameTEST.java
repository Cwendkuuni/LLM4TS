
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object wanted = new Object();
        Same matcher = new Same(wanted);
        
        assertTrue(matcher.matches(wanted));
        assertFalse(matcher.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same matcher = new Same(wanted);
        Description description = new Description() {
            StringBuilder stringBuilder = new StringBuilder();

            @Override
            public Description appendText(CharSequence charSequence) {
                stringBuilder.append(charSequence);
                return this;
            }

            @Override
            public Description appendText(String s) {
                stringBuilder.append(s);
                return this;
            }

            @Override
            public Description appendDescriptionOf(SelfDescribing selfDescribing) {
                return appendText(selfDescribing.toString());
            }

            @Override
            public Description appendValue(Object o) {
                return appendText(String.valueOf(o));
            }

            @Override
            public Description appendValueList(String s, String s1, String s2, Object... objects) {
                String separator = "";
                appendText(s);
                for (Object o : objects) {
                    appendText(separator);
                    appendValue(o);
                    separator = s2;
                }
                return appendText(s1);
            }

            @Override
            public Description appendList(String s, String s1, String s2, Iterable<?> iterable) {
                String separator = "";
                appendText(s);
                for (Object o : iterable) {
                    appendText(separator);
                    appendValue(o);
                    separator = s2;
                }
                return appendText(s1);
            }

            @Override
            public Description appendTextList(String s, String s1, String s2, Iterable<? extends CharSequence> iterable) {
                String separator = "";
                appendText(s);
                for (CharSequence charSequence : iterable) {
                    appendText(separator);
                    appendText(charSequence);
                    separator = s2;
                }
                return appendText(s1);
            }

            @Override
            public Description appendValueList(String s, String s1, String s2, Object[] objects) {
                return appendValueList(s, s1, s2, Arrays.asList(objects));
            }

            @Override
            public Description appendNewLine() {
                return appendText("\n");
            }

            @Override
            public Description appendTextAndNewLine(String s) {
                appendText(s);
                return appendNewLine();
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };

        matcher.describeTo(description);

        assertEquals("same(" + wanted.toString() + ")", description.toString());
    }
}

