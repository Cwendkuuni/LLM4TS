import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.encoding.BaseEncoding;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MetaphoneTest {

 private static final Metaphone metaphone = new Metaphone();
 private static final BaseEncoding base64 = new BaseEncoding();

 @Test
 public void testEmptyString() {
 String original = "";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("", decoded);
 }

 @Test
 public void testSingleCharacter() {
 String original = "A";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("A", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncoding() {
 String original = "apple";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("appl", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialK() {
 String original = "kitten";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("ktin", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialX() {
 String original = "xylophone";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("ksxtphn", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentH() {
 String original = "rhythm";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("rhthym", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithDoubleLetter() {
 String original = "butter";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("buhtr", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentG() {
 String original = "gymnasium";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("jmnsm", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithMultipleInitialSilentG() {
 String original = "gymnastics";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("jmntcs", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentC() {
 String original = "scissors";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("skrs", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentS() {
 String original = "silence";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("slns", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentSAndInitialSilentC() {
 String original = "cssc";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("sk", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentCAndMultipleInitialSilentG() {
 String original = "cgymnastics";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("skjmntcs", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithInitialSilentGAndMultipleInitialSilentC() {
 String original = "gcgcc";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("jskk", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithDoubleInitialSilentC() {
 String original = "cc";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("k", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithDifferentCases() {
 String original = "Apple";
 String encoded = metaphone.metaphone(original);
 String decoded = "";
 try {
 decoded = base64.decode(encoded);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("appl", decoded);
 assertEquals(encoded, metaphone.metaphone(decoded));
 }

 @Test
 public void testMetaphoneEncodingWithEmptyStringAsArgument() {
 assertEquals("", metaphone.metaphone(""));
 }

 @Test(expected = NullPointerException.class)
 public void testMetaphoneEncodingWithNullArgument() {
 metaphone.metaphone(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMetaphoneEncodingWithLongString() {
 String original = new String(new char[256 * 1024]);
 assertEquals("Error: String too long: 655360", metaphone.metaphone(original));
 }

 @Test
 public void testIsMetaphoneEqual() {
 String str1 = "apple";
 String str2 = "apple";
 assertTrue(metaphone.isMetaphoneEqual(str1, str2));
 String str3 = "orange";
 assertFalse(metaphone.isMetaphoneEqual(str1, str3));
 }

 @Test
 public void testEncodingWithSetMaxCodeLen() {
 Metaphone metaphoneShort = new Metaphone();
 metaphoneShort.setMaxCodeLen(2);
 String originalShort = "apple";
 String encodedShort = metaphoneShort.metaphone(originalShort);
 String decodedShort = "";
 try {
 decodedShort = base64.decode(encodedShort);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("appl", decodedShort);
 Metaphone metaphoneLong = new Metaphone();
 String originalLong = "apple";
 String encodedLong = metaphoneLong.metaphone(originalLong);
 String decodedLong = "";
 try {
 decodedLong = base64.decode(encodedLong);
 } catch (DecoderException e) {
 e.printStackTrace();
 }
 assertEquals("appl", decodedLong);
 assertEquals(encodedLong, encodedShort);
 }

}