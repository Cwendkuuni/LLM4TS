import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CaverphoneTest {

 private Caverphone encoder = new Caverphone();

 @Test
 public void testNullInput() {
 String encoded = encoder.caverphone(null);
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testEmptyInput() {
 String encoded = encoder.caverphone("");
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testCaverphoneEncoding() {
 String input = "Caversham Project";
 String expected = "C2p1e2j3o1g2h3a2m";
 String encoded = encoder.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneEncodingWithStartOptions() {
 String input = "Caverphone Algorithm";
 String expected = "C2p1a1l3g3o4r3i5t3h5m3o2t";
 String encoded = encoder.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneEncodingWithReplacements() {
 String input = "University of Otago";
 String expected = "U1n1v3r5s3i1t3y1u3o2t3a3g3o";
 String encoded = encoder.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneEncodingWithHandles() {
 String input = "Apache Software Foundation";
 String expected = "A1p2c3h3e4s5w3o1r3d3f3o1n3d4i3a3t3i3o3r3d3s3w3o3f3o3n3d";
 String encoded = encoder.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneEncodingWithRemovals() {
 String input = "Creates an instance of the Caverphone encoder";
 String expected = "C3r3t3s3 4n3 0f1 t2 C4v3r3p0h3n3 3n3c0d3r";
 String encoded = encoder.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneEncodingWithHandlesAndReplacements() {
 String input = "This is an algorithm created the Caversham Project at the University of Otago";
 String expected = "T1h3 1s3 4n3 4l3g3o3r3h3t3 1t1 h3 U1n1v3r5s3i1t3y1u3o2t3a3g3o";
 String encoded = encoder.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testEqualsStringsWithSameCaverphone() {
 String str1 = "Caversham Project";
 String str2 = "Caverphone Algorithm";
 boolean result = encoder.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testEqualsStringsWithDifferentCaverphone() {
 String str1 = "Caversham Project";
 String str2 = "University of Otago";
 boolean result = encoder.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonString() {
 Integer number = new Integer(10);
 encoder.encode(number);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidCaverphone() {
 String invalidCaverphone = "111111111112";
 encoder.decode(invalidCaverphone);
 }

}