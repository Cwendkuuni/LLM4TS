import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        Assert.assertTrue(same.matches(obj)); // typical use case: object is the same
        Assert.assertFalse(same.matches(new Object())); // typical use case: object is not the same
    }

    @Test
    public void testDescribeTo() {
        Same sameString = new Same("test");
        Same sameChar = new Same('a');
        DescriptionMock description = new DescriptionMock();
        sameString.describeTo(description);
        Assert.assertEquals("same(\"test\")", description.getResult()); // typical use case: string object
        description.clear();
        sameChar.describeTo(description);
        Assert.assertEquals("same('a')", description.getResult()); // typical use case: char object
    }

    @Test
    public void testDescribeToEdgeCases() {
        Same sameEmptyString = new Same("");
        Same sameNull = new Same(null);
        DescriptionMock description = new DescriptionMock();
        sameEmptyString.describeTo(description);
        Assert.assertEquals("same(\"\")", description.getResult()); // edge case: empty string
        description.clear();
        sameNull.describeTo(description);
        Assert.assertEquals("same(null)", description.getResult()); // edge case: null object
    }

    @Test
    public void testAppendQuoting() {
        Same sameString = new Same("test");
        Same sameChar = new Same('a');
        DescriptionMock description = new DescriptionMock();
        sameString.appendQuoting(description);
        Assert.assertEquals("\"", description.getResult()); // typical use case: string object
        description.clear();
        sameChar.appendQuoting(description);
        Assert.assertEquals("'", description.getResult()); // typical use case: char object
    }

    @Test
    public void testAppendQuotingEdgeCases() {
        Same sameInteger = new Same(123);
        Same sameBoolean = new Same(true);
        DescriptionMock description = new DescriptionMock();
        sameInteger.appendQuoting(description);
        Assert.assertEquals("", description.getResult()); // edge case: non-quoteable object (integer)
        description.clear();
        sameBoolean.appendQuoting(description);
        Assert.assertEquals("", description.getResult()); // edge case: non-quoteable object (boolean)
    }

    @Test
    public void testSerializable() {
        Same same = new Same("test");
        Assert.assertTrue(same instanceof Serializable); // error scenario: class should implement Serializable
    }

    private class DescriptionMock implements Description {
        private StringBuilder result;

        DescriptionMock() {
            result = new StringBuilder();
        }

        @Override
        public Description appendText(String text) {
            result.append(text);
            return this;
        }

        @Override
        public Description appendDescriptionOf(SelfDescribing value) {
            result.append(value.toString());
            return this;
        }

        @Override
        public Description appendValue(Object value) {
            result.append(value.toString());
            return this;
        }

        @Override
        public Description appendValueList(String start, String separator, String end, Object... values) {
            result.append(start);
            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    result.append(separator);
                }
                result.append(values[i]);
            }
            result.append(end);
            return this;
        }

        @Override
        public Description appendDescriptionOf(Describable value) {
            result.append(value.getDescription().toString());
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            result.append(start);
            int i = 0;
            for (SelfDescribing value : values) {
                if (i++ > 0) {
                    result.append(separator);
                }
                result.append(value.toString());
            }
            result.append(end);
            return this;
        }

        @Override
        public Description appendValue(Object value, String prefix, String suffix) {
            result.append(prefix).append(value.toString()).append(suffix);
            return this;
        }

        @Override
        public String toString() {
            return result.toString();
        }

        public String getResult() {
            return result.toString();
        }

        public void clear() {
            result.setLength(0);
        }
    }
}