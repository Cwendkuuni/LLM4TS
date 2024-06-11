
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
  
  @Test
  public void testClean() {
    String str = "abc123!@#$%ABC";
    String cleanedStr = SoundexUtils.clean(str);
    assertEquals("ABCABC", cleanedStr);
  }
  
  @Test
  public void testDifference() {
    StringEncoder encoder = new SomeEncoder();
    String s1 = "abc";
    String s2 = "def";
    int diff = SoundexUtils.difference(encoder, s1, s2);
    assertEquals(0, diff);
  }
  
  @Test
  public void testDifferenceEncoded() {
    String es1 = "123";
    String es2 = "456";
    int diff = SoundexUtils.differenceEncoded(es1, es2);
    assertEquals(0, diff);
  }
}
