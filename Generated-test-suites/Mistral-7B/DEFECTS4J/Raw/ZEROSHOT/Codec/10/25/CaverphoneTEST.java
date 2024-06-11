import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String encoded = caverphone.encode(null);
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testEmptyInput() {
 String encoded = caverphone.encode("");
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testLowerCaseInput() {
 String input = "hello world";
 String encoded = caverphone.encode(input);
 String decoded = caverphone.decode(encoded);
 assertEquals(input, decoded);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "123hello4world";
 String encoded = caverphone.encode(input);
 assertEquals("hllo", encoded);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "enough";
 String encoded = caverphone.encode(input);
 assertEquals("enou2f", encoded);
 }

 @Test
 public void testStartOptions() {
 String input = "rough cough enough";
 String encoded = caverphone.encode(input);
 assertEquals("rou2fcou2fenou2f", encoded);
 }

 @Test
 public void testReplaceSequences() {
 String input = "tough enough tch qi cq ci cy tch tio tia dg d d ph b sh zz y3 why wh y j j y j j w3 w w w h h r3 r r l3 l l l j y y y 3";
 String encoded = caverphone.encode(input);
 assertEquals("tou2ftou2fenou2ftw2tw2tkhh22kkh2tkfhpkkkkpks3", encoded);
 }

 @Test
 public void testRemovals() {
 String input = "23gh3gh2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g2g";
 String encoded = caverphone.encode(input);
 assertEquals("", encoded);
 }

 @Test
 public void testPutTenOnEnd() {
 String input = "hello";
 String encoded = caverphone.encode(input);
 String decoded = caverphone.decode(encoded + "111111" + "1111");
 assertEquals(input, decoded);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeNullInput() {
 StringDecoder decoder = new StringDecoder();
 decoder.decode(null);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeEmptyInput() {
 StringDecoder decoder = new StringDecoder();
 decoder.decode("");
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidInput() {
 StringDecoder decoder = new StringDecoder();
 String encoded = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
 decoder.decode(encoded);
 }

 @Test
 public void testEquals() {
 String input1 = "hello world";
 String input2 = "hello world";
 boolean result = caverphone.isCaverphoneEqual(input1, input2);
 assertTrue(result);
 }

 @Test
 public void testNotEquals() {
 String input1 = "hello world";
 String input2 = "hello world!";
 boolean result = caverphone.isCaverphoneEqual(input1, input2);
 assertFalse(result);
 }

}