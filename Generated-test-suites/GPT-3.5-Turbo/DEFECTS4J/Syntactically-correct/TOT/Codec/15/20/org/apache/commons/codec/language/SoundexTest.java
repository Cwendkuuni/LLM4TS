import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

  @Test
  public void testSoundex() {
    Soundex soundex = new Soundex();
    Assert.assertEquals("A261", soundex.soundex("Apache"));
    Assert.assertEquals("A261", soundex.soundex("ApAche"));
    Assert.assertEquals("A261", soundex.soundex("APACHE"));

    Assert.assertEquals("B10", soundex.soundex("Bear"));
    Assert.assertEquals("B10", soundex.soundex("beEr"));
    Assert.assertEquals("B10", soundex.soundex("BEAR"));

    Assert.assertEquals("C400", soundex.soundex("cat"));
    Assert.assertEquals("C400", soundex.soundex("Cat"));
    Assert.assertEquals("C400", soundex.soundex("CAt"));

    Assert.assertEquals("D200", soundex.soundex("dog"));
    Assert.assertEquals("D200", soundex.soundex("Dog"));
    Assert.assertEquals("D200", soundex.soundex("dOg"));

    Assert.assertEquals("E525", soundex.soundex("elephant"));
    Assert.assertEquals("E525", soundex.soundex("Elephant"));
    Assert.assertEquals("E525", soundex.soundex("eLePhaNt"));
  }
  
  @Test
  public void testEncodeObject() throws EncoderException {
    Soundex soundex = new Soundex();
    Assert.assertEquals("A261", soundex.encode("Apache"));
    Assert.assertEquals("B10", soundex.encode("Bear"));
    Assert.assertEquals("C400", soundex.encode("cat"));
    Assert.assertEquals("D200", soundex.encode("dog"));
    Assert.assertEquals("E525", soundex.encode("elephant"));
  }
  
  @Test(expected = EncoderException.class)
  public void testEncodeObjectWithInvalidType() throws EncoderException {
    Soundex soundex = new Soundex();
    soundex.encode(123);
  }

  @Test
  public void testDifference() throws EncoderException {
    Soundex soundex = new Soundex();
    Assert.assertEquals(0, soundex.difference("Apache", "Cool"));
    Assert.assertEquals(2, soundex.difference("Cat", "Dog"));
    Assert.assertEquals(4, soundex.difference("elephant", "Elephant"));
    Assert.assertEquals(1, soundex.difference("Michael", "Mitchell"));
  }

  @Test(expected = EncoderException.class)
  public void testDifferenceWithInvalidString() throws EncoderException {
    Soundex soundex = new Soundex();
    soundex.difference(123, "Dog");
  }
  
  @Test(expected = EncoderException.class)
  public void testDifferenceWithInvalidType() throws EncoderException {
    Soundex soundex = new Soundex();
    soundex.difference("Cat", 123);
  }
}