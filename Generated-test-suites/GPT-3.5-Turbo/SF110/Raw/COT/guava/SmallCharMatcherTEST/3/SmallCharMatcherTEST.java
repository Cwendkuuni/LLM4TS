
import com.google.common.base.CharMatcher;
import org.junit.Assert;
import org.junit.Test;

public class SmallCharMatcherTest {

@Test
public void testMatches_ContainsZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "");
    Assert.assertTrue(matcher.matches('\0'));
}

@Test
public void testMatches_CheckFilter_True() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0b1000000000000000L, false, "");
    Assert.assertTrue(matcher.matches((char) 15));
}

@Test
public void testMatches_CheckFilter_False() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "");
    Assert.assertFalse(matcher.matches((char) 15));
    }

@Test
public void testSetBits() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', '\0', 'c'}, 0b100000001L, true, "");
    BitSet table = new BitSet();
    matcher.setBits(table);
    Assert.assertTrue(table.get(0));
    Assert.assertTrue(table.get('a'));
    Assert.assertTrue(table.get('b'));
    Assert.assertFalse(table.get('c'));
}

}
