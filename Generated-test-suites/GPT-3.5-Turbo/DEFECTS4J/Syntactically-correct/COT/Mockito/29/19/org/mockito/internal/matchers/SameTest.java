import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;

public class SameTest {
    
    @Test
    public void testMatches() {
        Same same = new Same("test");
        String actual = "test";
        Assert.assertTrue(same.matches(actual));
    }
    
    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        DescriptionMock descriptionMock = new DescriptionMock();
        same.describeTo(descriptionMock);
        Assert.assertEquals("same(\"test\")", descriptionMock.getDescription());
    }
    
    @Test
    public void testAppendQuotingString() {
        Same same = new Same("test");
        DescriptionMock descriptionMock = new DescriptionMock();
        same.appendQuoting(descriptionMock);
        Assert.assertEquals("\"", descriptionMock.getDescription());
    }
    
    @Test
    public void testAppendQuotingCharacter() {
        Same same = new Same('c');
        DescriptionMock descriptionMock = new DescriptionMock();
        same.appendQuoting(descriptionMock);
        Assert.assertEquals("'", descriptionMock.getDescription());
    }
    
    @Test
    public void testAppendQuotingObject() {
        Same same = new Same(123);
        DescriptionMock descriptionMock = new DescriptionMock();
        same.appendQuoting(descriptionMock);
        Assert.assertEquals("", descriptionMock.getDescription());
    }
    
    class DescriptionMock implements Description {
        private String description;
        
        @Override
        public Description appendText(String text) {
            description += text;
            return this;
        }
        
        @Override
        public Description appendDescriptionOf(SelfDescribing value) {
            return null;
        }
        
        @Override
        public Description appendValue(Object value) {
            return null;
        }
        
        @Override
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            return null;
        }
        
        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            return null;
        }
        
        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            return null;
        }
        
        @Override
        public Description appendText(String text, String charPrefix, String charSuffix) {
            return null;
        }
        
        @Override
        public Description appendNewLine() {
            return null;
        }
        
        @Override
        public Description appendNull() {
            return null;
        }
        
        @Override
        public Description appendValue(Object value, String prefix, String suffix) {
            return null;
        }
        
        @Override
        public <T> Description appendValueList(String start, String separator, String end, T value, T... values) {
            return null;
        }
        
        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values, T value) {
            return null;
        }
        
        @Override
        public Description appendList(String start, String separator, String end, Object... values) {
            return null;
        }
        
        @Override
        public Description appendList(String start, String separator, String end, Object value, Object... values) {
            return null;
        }
        
        @Override
        public Description appendList(String start, String separator, String end, Iterable<?> values) {
            return null;
        }
        
        @Override
        public Description appendList(String start, String separator, String end, Object value) {
            return null;
        }
        
        @Override
        public Description appendList(String start, String separator, String end, Object value, Object value2, Object... others) {
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