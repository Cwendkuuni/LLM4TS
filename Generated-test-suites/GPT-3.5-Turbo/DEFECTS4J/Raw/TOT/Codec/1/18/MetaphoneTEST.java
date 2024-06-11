import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.language.Metaphone;

public class MetaphoneTest {

  @Test
  public void testMetaphone_validString() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone("John");
    Assert.assertEquals("JN", result);
  }

  @Test
  public void testMetaphone_singleCharacter() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone("A");
    Assert.assertEquals("A", result);
  }

  @Test
  public void testMetaphone_emptyString() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone("");
    Assert.assertEquals("", result);
  }

  @Test
  public void testMetaphone_nullString() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone(null);
    Assert.assertEquals("", result);
  }

  @Test
  public void testMetaphone_maxCodeLen() {
    Metaphone metaphone = new Metaphone();
    metaphone.setMaxCodeLen(2);
    String result = metaphone.metaphone("Chocolate");
    Assert.assertEquals("CH", result);
  }

  @Test
  public void testMetaphone_encode_validString() throws EncoderException {
    Metaphone metaphone = new Metaphone();
    Object result = metaphone.encode("John");
    Assert.assertEquals("JN", result);
  }

  @Test(expected = EncoderException.class)
  public void testMetaphone_encode_invalidObjectType() throws EncoderException {
    Metaphone metaphone = new Metaphone();
    Object result = metaphone.encode(123);
  }

  @Test
  public void testMetaphone_isMetaphoneEqual_equalStrings() {
    Metaphone metaphone = new Metaphone();
    boolean result = metaphone.isMetaphoneEqual("John", "John");
    Assert.assertTrue(result);
  }

  @Test
  public void testMetaphone_isMetaphoneEqual_differentStrings() {
    Metaphone metaphone = new Metaphone();
    boolean result = metaphone.isMetaphoneEqual("John", "Jane");
    Assert.assertFalse(result);
  }

  @Test
  public void testMetaphone_isMetaphoneEqual_emptyStrings() {
    Metaphone metaphone = new Metaphone();
    boolean result = metaphone.isMetaphoneEqual("", "");
    Assert.assertTrue(result);
  }

}