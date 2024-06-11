import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneWithNullInput() {
     String result = caverphone.caverphone(null);
     assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneWithEmptyInput() {
     String result = caverphone.caverphone("");
     assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneWithLowercaseInput() {
     String input = "hello world";
     String result = caverphone.caverphone(input);
     String expected = "h2l2o2w2r2l2d2";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithOnlyAtoZ() {
     String input = "AppleBananaCherry";
     String result = caverphone.caverphone(input);
     String expected = "Appl2Ban2Cherr";
     assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneWithInvalidInput() {
     String input = "Apple#BananaCherry";
     caverphone.caverphone(input);
 }

 @Test
 public void testCaverphoneWithStartOptions() {
     String input = "CoughEnoughTough";
     String result = caverphone.caverphone(input);
     String expected = "cou2fenou2ftou2f";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithHandledReplacements() {
     String input = "CaQtion";
     String result = caverphone.caverphone(input);
     String expected = "2qsi3on";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithHandledRemovals() {
     String input = "TwoTwoThreeTwoTwoThree";
     String result = caverphone.caverphone(input);
     String expected = "222233";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithHandledEndings() {
     String input = "AppleApple";
     String result = caverphone.caverphone(input);
     String expected = "Appl2Appl2";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithHandledMultipleReplacements() {
     String input = "AppleCaQtionApple";
     String result = caverphone.caverphone(input);
     String expected = "Appl22qsi3OnAppl2";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithHandledMultipleStartOptions() {
     String input = "CoughToughEnoughCough";
     String result = caverphone.caverphone(input);
     String expected = "cou2ftou2fenou2fcou2f";
     assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithHandledMultipleHandledReplacements() {
     String input = "AppleCaQtionAppleCaQtionApple";
     String result = caverphone.caverphone(input);
     String expected = "Appl22qsi3OnAppl22qsi3OnAppl2";
     assertEquals(expected, result);
 }

}