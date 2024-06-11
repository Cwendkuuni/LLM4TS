import org.junit.Test;
import org.mockito.Description;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SameTest {

    @Test
    public void sameMatchesWithSameObject() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }
    
    @Test
    public void sameDoesNotMatchWithDifferentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void describeToAppendsQuotingForString() {
        String str = "test";
        Same same = new Same(str);
        Description description = Mockito.mock(Description.class);
        same.describeTo(description);
        Mockito.verify(description).appendText("same(\"test\")");
    }
    
    @Test
    public void describeToAppendsQuotingForCharacter() {
        Character ch = 'a';
        Same same = new Same(ch);
        Description description = Mockito.mock(Description.class);
        same.describeTo(description);
        Mockito.verify(description).appendText("same('a')");
    }
    
    @Test
    public void describeToDoesNotAppendQuotingForOtherTypes() {
        Integer num = 10;
        Same same = new Same(num);
        Description description = Mockito.mock(Description.class);
        same.describeTo(description);
        Mockito.verify(description).appendText("same(10)");
    }
}