import com.google.common.base.CharMatcher;
import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_ContainsZero_ReturnsTrue() {
        CharMatcher charMatcher = CharMatcher.from(new BitSet(0), "test");
        assertTrue(charMatcher.matches('\0'));
    }

    @Test
    public void testMatches_ContainsZero_ReturnsFalse() {
        CharMatcher charMatcher = CharMatcher.from(new BitSet(0), "test");
        assertFalse(charMatcher.matches('A'));
    }

    @Test
    public void testMatches_NormalChar_ReturnsTrue() {
        BitSet chars = new BitSet();
        chars.set('A');
        CharMatcher charMatcher = CharMatcher.from(chars, "test");
        assertTrue(charMatcher.matches('A'));
    }

    @Test
    public void testMatches_NormalChar_ReturnsFalse() {
        BitSet chars = new BitSet();
        chars.set('A');
        CharMatcher charMatcher = CharMatcher.from(chars, "test");
        assertFalse(charMatcher.matches('B'));
    }

    // Add more JUnit 4 tests for the remaining methods in SmallCharMatcher class

    // ...

    // Test for edge cases and exception handling scenarios

    // ...

    // 
    // Merge all the individual test cases
    // ...
    // 
}