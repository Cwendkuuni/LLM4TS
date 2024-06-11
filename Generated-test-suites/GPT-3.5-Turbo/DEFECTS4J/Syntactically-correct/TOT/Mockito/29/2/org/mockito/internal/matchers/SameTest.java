import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    private Same matcher;

    @Before
    public void setup() {
        matcher = new Same("test");
    }

    @Test
    public void testMatches_WithSameString_ReturnsTrue() {
        Object obj = "test";
        
        assertTrue(matcher.matches(obj));
    }

    @Test
    public void testMatches_WithDifferentString_ReturnsFalse() {
        Object obj = "different";
        
        assertFalse(matcher.matches(obj));
    }

    @Test
    public void testMatches_WithSameChar_ReturnsTrue() {
        Object obj = 't';
        
        assertTrue(matcher.matches(obj));
    }

    @Test
    public void testMatches_WithDifferentChar_ReturnsFalse() {
        Object obj = 'd';

        assertFalse(matcher.matches(obj));
    }

    @Test
    public void testDescribeTo_WithStringDescription_AppendsQuoting() {
        Description description = new StringDescription();

        matcher.describeTo(description);
        
        assertThat(description.toString(), is("same(\"test\")"));
    }

    @Test
    public void testDescribeTo_WithCharacterDescription_AppendsQuoting() {
        matcher = new Same('t');
        Description description = new StringDescription();

        matcher.describeTo(description);
        
        assertThat(description.toString(), is("same('t')"));
    }
}