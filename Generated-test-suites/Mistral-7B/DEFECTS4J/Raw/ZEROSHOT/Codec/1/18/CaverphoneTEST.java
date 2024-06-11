import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone encoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String caverphoneCode = encoder.caverphone("");
 assertEquals("1111111111", caverphoneCode);
 }

 @Test
 public void testNullString() {
 try {
 encoder.caverphone(null);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testBasicCaverphone() {
 String input = "Caversham";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "C2v1sh2m";
 assertEquals(expected, caverphoneCode);
 }

 @Test
 public void testCaverphoneWithStartOptions() {
 String input = "CoughCaversham";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "Cou2f2v1sh2m";
 assertEquals(expected, caverphoneCode);
 }

 @Test
 public void testCaverphoneWithReplacements() {
 String input = "CaverphoneProject";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "C2v1ph2r1j3p2o2ct";
 assertEquals(expected, caverphoneCode);
 }

 @Test
 public void testCaverphoneWithHandlings() {
 String input = "Caverphonehandling";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "C2v1ph2h2n2d2l2n2g";
 assertEquals(expected, caverphoneCode);
 }

 @Test
 public void testCaverphoneWithRemovals() {
 String input = "12345Caverphone12345";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "25C2v1ph2";
 assertEquals(expected, caverphoneCode);
 }

 @Test
 public void testCaverphoneWithHandlingsAndReplacements() {
 String input = "CaverphonehandlingReplacements";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "C2v1ph2h2n2d2l2n2gR3p2l3a3c3e3s3";
 assertEquals(expected, caverphoneCode);
 }

 @Test
 public void testCaverphoneWithHandlingsReplacementsAndRemovals() {
 String input = "123CaverphonehandlingReplacements123";
 String caverphoneCode = encoder.caverphone(input);
 String expected = "23C2v1ph2h2n2d2l2n2gR3p2l3";
 assertEquals(expected, caverphoneCode);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeNonString() {
 encoder.decode(new Object() {});
 }

 @Test(expected = DecoderException.class)
 public void testDecodeNullString() {
 try {
 encoder.decode(null);
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeEmptyString() {
 String decodedString = encoder.decode("");
 assertEquals("", decodedString);
 }

 @Test
 public void testDecodeBasicCaverphone() {
 String encodedString = encoder.caverphone("Caversham");
 String decodedString = encoder.decode(encodedString);
 assertEquals("Caversham", decodedString);
 }

 @Test
 public void testDecodeStartOptions() {
 String encodedString = encoder.caverphone("CoughCaversham");
 String decodedString = encoder.decode(encodedString);
 assertEquals("CoughCaversham", decodedString);
 }

 @Test
 public void testDecodeReplacements() {
 String encodedString = encoder.caverphone("CaverphoneProject");
 String decodedString = encoder.decode(encodedString);
 assertEquals("CaverphoneProject", decodedString);
 }

 @Test
 public void testDecodeHandlings() {
 String encodedString = encoder.caverphone("Caverphonehandling");
 String decodedString = encoder.decode(encodedString);
 assertEquals("Caverphonehandling", decodedString);
 }

 @Test
 public void testDecodeHandlingsReplacementsAndRemovals() {
 String encodedString = encoder.caverphone("CaverphonehandlingReplacements");
 String decodedString = encoder.decode(encodedString);
 assertEquals("CaverphonehandlingReplacements", decodedString);
 }

 @Test
 public void testDecodeHandlingsReplacementsAndRemovalsWithStartOptions() {
 String encodedString = encoder.caverphone("CoughCaverphonehandlingReplacements");
 String decodedString = encoder.decode(encodedString);
 assertEquals("CoughCaverphonehandlingReplacements", decodedString);
 }

}