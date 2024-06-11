import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testValidInput() {
 String result = caverphone.caverphone("hello");
 Assert.assertEquals("h2l2o2o2l2o2o2l2o", result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test
 public void testStringWithDigits() {
 String result = caverphone.caverphone("hello123");
 Assert.assertEquals("h2l2o2o2l2o2o2l2o123", result);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String result = caverphone.caverphone("hello@world");
 Assert.assertEquals("h2l2o2o2l2o2o2l2oA2w2l2d", result);
 }

 @Test
 public void testUpperCaseString() {
 String result = caverphone.caverphone("HELLO");
 Assert.assertEquals("H2L2O2O2L2O2O2L2O", result);
 }

 @Test
 public void testStringWithLongLength() {
 String longString = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
 String result = caverphone.caverphone(longString);
 Assert.assertEquals("T3h3 q3u3i3c3 k3i3n3 g3r3 32x3 f3o3 x3p3s3 3o3v33r3 3h3 t3h3 l3a3z3y3 d3o3g.", result);
 }

 @Test
 public void testLeadingSpecialChars() {
 String result = caverphone.caverphone("@hello");
 Assert.assertEquals("A2h2l2o2o2l2o2o2l2o", result);
 }

 @Test
 public void testTrailingSpecialChars() {
 String result = caverphone.caverphone("hello@");
 Assert.assertEquals("h2l2o2o2l2o2o2l2oA", result);
 }

 @Test
 public void testTrailingSpecialChars_2() {
 String result = caverphone.caverphone("hello@world");
 Assert.assertEquals("h2l2o2o2l2o2o2l2oA2w2l2d", result);
 }

 @Test
 public void testStartsWithCough() {
 String result = caverphone.caverphone("coughhello");
 Assert.assertEquals("cou2fh2l2o2o2l2o2o2l2o", result);
 }

 @Test
 public void testStartsWithRough() {
 String result = caverphone.caverphone("roughhello");
 Assert.assertEquals("rou2fh2l2o2o2l2o2o2l2o", result);
 }

 @Test
 public void testStartsWithTough() {
 String result = caverphone.caverphone("toughhello");
 Assert.assertEquals("tou2fh2l2o2o2l2o2o2l2o", result);
 }

 @Test
 public void testStartsWithEnough() {
 String result = caverphone.caverphone("enoughhello");
 Assert.assertEquals("enou2fh2l2o2o2l2o2o2l2o", result);
 }

 @Test
 public void testStartsWithTrough() {
 String result = caverphone.caverphone("troughhello");
 Assert.assertEquals("trou2fh2l2o2o2l2o2o2l2o", result);
 }

 @Test
 public void testStartsWithGn() {
 String result = caverphone.caverphone("gnhello");
 Assert.assertEquals("2nhello", result);
 }

}