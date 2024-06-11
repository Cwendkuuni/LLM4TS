import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

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
 public void testLowercaseInput() {
 String input = "hello world";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }

 @Test
 public void testHandleVariousStartOptions() {
 String input = "CoughCAT";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "RoughDOG";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "ToughTOAD";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "EnoughMONKEY";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "TroughBANANA";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }

 @Test
 public void testHandleReplacements() {
 String input = "QuackQUACK";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "CalfCAT";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "CeilCEILING";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "CycleCYCLE";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "TchatTCHAT";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "CatCAT";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "CakeCAKE";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "CrapCRAP";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "BounceBOUNCE";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "BabyBABY";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "BrightBRIGHT";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "BitterBITTER";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "BuzzBUZZ";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);

 input = "BubbleBUBBLE";
 encoded = caverphone.encode(input);
 decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }

 @Test
 public void testHandleRemovals() {
 String input = "TwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }

 @Test
 public void testHandleRemovals2() {
 String input = "TwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }

 @Test
 public void testHandleRemovals3() {
 String input = "TwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }

 @Test
 public void testHandleRemovals4() {
 String input = "TwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThreeTwoThree TwoThreeTwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree TwoThreeTwoThree";
 String encoded = caverphone.encode(input);
 String decoded = "";
 try {
 decoded = (String) caverphone.decode(encoded.getBytes());
 } catch (DecoderException e) {
 fail("decode failed: " + e.getMessage());
 }
 assertEquals(input, decoded);
 }
}