import com.google.common.base.SmallCharMatcher;
import org.junit.Test;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 97; // 'a'
        int result = SmallCharMatcher.smear(hashCode);
        // Add assertion here
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{}, 1L, true, "Test");
        boolean result = smallCharMatcher.checkFilter(97); // 'a'
        // Add assertion here
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        // Add assertion here
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet(8);
        chars.set(97); // 'a'
        String description = "Test CharMatcher";
        SmallCharMatcher smallCharMatcher = SmallCharMatcher.from(chars, description);
        // Add assertion here
    }

    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 1L, true, "Test");
        boolean result = smallCharMatcher.matches('a');
        // Add assertion here
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 1L, true, "Test");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        // Add assertion here
    }

}