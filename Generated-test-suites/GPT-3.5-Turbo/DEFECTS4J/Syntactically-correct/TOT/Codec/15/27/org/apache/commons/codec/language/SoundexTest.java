import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference_SameWord_ShouldReturn4() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("TEST", "TEST");
        Assert.assertEquals(4, difference);
    }
  
    @Test
    public void testDifference_DifferentWords_ShouldReturn0() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("TEST", "ROOM");
        Assert.assertEquals(0, difference);
    }
  
    @Test
    public void testDifference_PartialMatch_ShouldReturn2() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("TEST", "TEA");
        Assert.assertEquals(2, difference);
    }
  
    @Test(expected = EncoderException.class)
    public void testDifference_NullString_ShouldThrowEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(null, "TEST");
    }
  
    @Test
    public void testEncode_ValidString_ShouldReturnSoundexCode() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("TEST");
        Assert.assertEquals("T230", encoded);
    }
  
    @Test(expected = EncoderException.class)
    public void testEncode_InvalidObject_ShouldThrowEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }
  
    @Test
    public void testGetMaxLength_ShouldReturn4() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        Assert.assertEquals(4, maxLength);
    }
  
    @Test
    public void testGetSoundexMapping_ShouldReturnMappingArray() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        char[] expectedMapping = {'0', '1', '2', '3', '0', '1', '2', '2', '4', '5', '5', '0', '1', '2', '2', '6', '2', '3', '0', '1', '0', '2', '0', '2', '0', '2'};
        Assert.assertArrayEquals(expectedMapping, mapping);
    }
  
    @Test
    public void testSetMaxLength_ValidLength_ShouldSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(3);
        int maxLength = soundex.getMaxLength();
        Assert.assertEquals(3, maxLength);
    }

}