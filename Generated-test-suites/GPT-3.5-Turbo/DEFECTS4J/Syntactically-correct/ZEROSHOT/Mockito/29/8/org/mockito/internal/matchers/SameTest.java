import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        boolean result = same.matches(obj);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testMatches_False() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        boolean result = same.matches(obj2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testDescribeTo_String() {
        String str = "test";
        Same same = new Same(str);
        Description description = new DescriptionStub();
        
        same.describeTo(description);
        
        Assert.assertEquals("same(\"test\")", description.toString());
    }
    
    @Test
    public void testDescribeTo_Character() {
        Character c = 'c';
        Same same = new Same(c);
        Description description = new DescriptionStub();
        
        same.describeTo(description);
        
        Assert.assertEquals("same('c')", description.toString());
    }
    
    private class DescriptionStub implements Description {
        private StringBuilder stringBuilder = new StringBuilder();
        
        public Description appendText(String text) {
            stringBuilder.append(text);
            return this;
        }
        
        public String toString() {
            return stringBuilder.toString();
        }

        // Implement other methods required by the Description interface
    }
}