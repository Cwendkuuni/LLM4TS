import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        Description description = new Description();
        
        boolean result = same.matches("test");
        
        assert(result == true);
    }

    @Test
    public void testDescribeTo_String() {
        Same same = new Same("test");
        Description description = new Description();
        
        same.describeTo(description);
        
        assert(description.toString().equals("same(\"test\")"));
    }
    
    @Test
    public void testDescribeTo_Character() {
        Same same = new Same('c');
        Description description = new Description();
        
        same.describeTo(description);
        
        assert(description.toString().equals("same('c')"));
    }
}