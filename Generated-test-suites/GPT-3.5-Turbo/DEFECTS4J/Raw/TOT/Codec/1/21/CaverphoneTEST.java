import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

   @Test
   public void testCaverphone_emptyString_returns1111111111() {
      Caverphone caverphone = new Caverphone();
      String result = caverphone.caverphone("");
      Assert.assertEquals("1111111111", result);
   }

   @Test
   public void testCaverphone_lowercaseString_returnsCaverphoneCode() {
      Caverphone caverphone = new Caverphone();
      String result = caverphone.caverphone("example");
      Assert.assertEquals("7MPL111111", result);
   }

   @Test
   public void testEncode_emptyString_returns1111111111() {
      Caverphone caverphone = new Caverphone();
      String result = caverphone.encode("");
      Assert.assertEquals("1111111111", result);
   }

   @Test(expected=EncoderException.class)
   public void testEncode_nonStringObject_throwsEncoderException() throws EncoderException {
      Caverphone caverphone = new Caverphone();
      caverphone.encode(123);
   }

   @Test
   public void testEncode_nonEmptyString_returnsCaverphoneCode() {
      Caverphone caverphone = new Caverphone();
      String result = caverphone.encode("example");
      Assert.assertEquals("7MPL111111", result);
   }

   @Test
   public void testIsCaverphoneEqual_equalStrings_returnsTrue() {
      Caverphone caverphone = new Caverphone();
      boolean result = caverphone.isCaverphoneEqual("example", "example");
      Assert.assertTrue(result);
   }

   @Test
   public void testIsCaverphoneEqual_differentStrings_returnsFalse() {
      Caverphone caverphone = new Caverphone();
      boolean result = caverphone.isCaverphoneEqual("example1", "example2");
      Assert.assertFalse(result);
   }

   @Test
   public void testIsCaverphoneEqual_caseInsensitiveStrings_returnsTrue() {
      Caverphone caverphone = new Caverphone();
      boolean result = caverphone.isCaverphoneEqual("Example", "example");
      Assert.assertTrue(result);
   }

}