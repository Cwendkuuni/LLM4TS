import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Assert.assertTrue(same.matches(wanted));
        Assert.assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Description description = new Description() {
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public Description appendText(String text) {
                stringBuilder.append(text);
                return this;
            }

            @Override
            public Description appendDescriptionOf(SelfDescribing value) {
                value.describeTo(this);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                stringBuilder.append(value.toString());
                return this;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                stringBuilder.append(start);
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        stringBuilder.append(separator);
                    }
                    appendValue(values[i]);
                }
                stringBuilder.append(end);
                return this;
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };

        same.describeTo(description);

        Assert.assertEquals("same(" + wanted.toString() + ")", description.toString());
    }
}