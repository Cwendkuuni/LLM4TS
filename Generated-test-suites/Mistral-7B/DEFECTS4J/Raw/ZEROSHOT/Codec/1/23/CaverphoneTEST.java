import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneInstanceCreated() {
 assertNotNull(caverphone);
 }

 @Test
 public void testEncodeNullInput() {
 assertEquals("1111111111", caverphone.encode(null));
 }

 @Test
 public void testEncodeEmptyInput() {
 assertEquals("", caverphone.encode(""));
 }

 @Test
 public void testEncodeSingleLetter() {
 assertEquals("A", caverphone.encode("a"));
 }

 @Test
 public void testEncodeMultipleLetters() {
 assertEquals("AF22F22F2", caverphone.encode("Apple"));
 }

 @Test
 public void testEncodeSpecialCases() {
 assertEquals("A", caverphone.encode("e"));
 assertEquals("A", caverphone.encode("enough"));
 assertEquals("A", caverphone.encode("tough"));
 assertEquals("A", caverphone.encode("trough"));
 assertEquals("A", caverphone.encode("gn"));
 assertEquals("M", caverphone.encode("mb"));
 }

 @Test
 public void testEncodeComplexString() {
 assertEquals("K2Fs2S2T2F2F2F2K2Fs2S2T2F2F2F2K2Fs2S2T2F2F2F2", caverphone.encode("The quick brown fox jumps over the lazy dog"));
 }

 @Test (expected = EncoderException.class)
 public void testEncodeNonStringInput() {
 caverphone.encode(new Object());
 }

 @Test
 public void testIsCaverphoneEqualSameStrings() {
 assertTrue(caverphone.isCaverphoneEqual("Apple", "Apple"));
 }

 @Test
 public void testIsCaverphoneEqualDifferentStrings() {
 assertFalse(caverphone.isCaverphoneEqual("Apple", "Orange"));
 }

}