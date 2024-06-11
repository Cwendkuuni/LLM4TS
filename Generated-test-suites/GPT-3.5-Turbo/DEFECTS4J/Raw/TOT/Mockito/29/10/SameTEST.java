import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;

public class SameTest {

    @Test
    public void testMatches_withSameObject_shouldReturnTrue(){
        Object obj = new Object();
        Same same = new Same(obj);
        
        assertTrue(same.matches(obj));
    }
    
    @Test
    public void testMatches_withDifferentObject_shouldReturnFalse(){
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        
        assertFalse(same.matches(obj2));
    }
    
    @Test
    public void testDescribeTo_withString_shouldAppendQuoting(){
        String str = "Test String";
        Same same = new Same(str);
        Description description = new Description();
        
        same.describeTo(description);
        
        assertEquals("same(\"Test String\")", description.toString());
    }
    
    @Test
    public void testDescribeTo_withCharacter_shouldAppendQuoting(){
        char ch = 'A';
        Same same = new Same(ch);
        Description description = new Description();
        
        same.describeTo(description);
        
        assertEquals("same('A')", description.toString());
    }
    
    @Test
    public void testDescribeTo_withNonStringAndNonCharacter_shouldNotAppendQuoting(){
        Integer num = 10;
        Same same = new Same(num);
        Description description = new Description();
        
        same.describeTo(description);
        
        assertEquals("same(10)", description.toString());
    }
}