package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {
    @Test
    public void testMatches() {
        // test cases where wanted is same as actual
        Object wanted = new Object();
        Same same = new Same(wanted);
        assertTrue(same.matches(wanted));
        
        String str = "Hello";
        Same sameStr = new Same(str);
        assertTrue(sameStr.matches(str));
        
        Integer num = 10;
        Same sameNum = new Same(num);
        assertTrue(sameNum.matches(num));
    }
    
    @Test
    public void testDescribeTo() {
        // test cases for describeTo method
        Description description = mock(Description.class);
        
        Object wanted = new Object();
        Same same = new Same(wanted);
        same.describeTo(description);
        
        verify(description).appendText("same(");
        verify(description, times(2)).appendText("\""); // assuming wanted is a string
        
        Description description2 = mock(Description.class);
        
        Integer num = 10;
        Same sameNum = new Same(num);
        sameNum.describeTo(description2);
        
        verify(description2).appendText("same(");
        verify(description2).appendText(num.toString());
    }
}