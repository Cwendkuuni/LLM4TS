
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

  Metaphone metaphone = new Metaphone();

  @Test
  public void testMetaphone() {
    String result = metaphone.metaphone("apple");
    assertEquals("APL", result);
  }

  @Test
  public void testEncode() {
    String result = (String) metaphone.encode("banana");
    assertEquals("BN", result);
  }

  @Test
  public void testIsMetaphoneEqual() {
    boolean result = metaphone.isMetaphoneEqual("dog", "dug");
    assertEquals(true, result);
  }

  @Test
  public void testGetMaxCodeLen() {
    int result = metaphone.getMaxCodeLen();
    assertEquals(4, result);
  }

  @Test
  public void testSetMaxCodeLen() {
    metaphone.setMaxCodeLen(2);
    int result = metaphone.getMaxCodeLen();
    assertEquals(2, result);
  }
}
