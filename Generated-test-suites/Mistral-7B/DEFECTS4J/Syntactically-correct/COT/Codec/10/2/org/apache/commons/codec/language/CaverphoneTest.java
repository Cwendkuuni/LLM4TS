import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private String input;
 private String expectedOutput;

 @Test
 public void testCaverphoneNullInput() {
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone(null);
 assertEquals("1111111111", actualOutput);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone("");
 assertEquals("1111111111", actualOutput);
 }

 @Test
 public void testCaverphoneLowerCase() {
 input = "hello world";
 expectedOutput = "h3l3o2w2o2r2l2d3";
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test (expected = EncoderException.class)
 public void testCaverphoneInvalidInput() {
 input = "12345";
 Caverphone caverphone = new Caverphone();
 caverphone.encode(input); // throws EncoderException
 }

 @Test
 public void testCaverphoneStartOptions() {
 input = "cough";
 expectedOutput = "cou2f";
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneReplacements() {
 input = "quality";
 expectedOutput = "k2a3l3i2t3y";
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneRemovals() {
 input = "two threes";
 expectedOutput = "2t2";
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneHandling() {
 input = "caversham project at the university of otago";
 expectedOutput = "c2a2v3r3s3h4a2m3 p3r3j3e3ct2 t3h3 u3n3i3v3r3s3i3t3y";
 Caverphone caverphone = new Caverphone();
 String actualOutput = caverphone.caverphone(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneEncodeObject() {
 String str = "test string";
 Caverphone caverphone = new Caverphone();
 Object actualOutput = caverphone.encode(str);
 assertEquals(caverphone.caverphone(str), actualOutput.toString());
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 input = "hello world";
 Caverphone caverphone = new Caverphone();
 String code1 = caverphone.caverphone(input);
 String code2 = caverphone.caverphone(input);
 assertTrue(caverphone.isCaverphoneEqual(input, input));
 assertTrue(code1.equals(code2));
 }

 @Test
 public void testCaverphoneIsCaverphoneNotEqual() {
 input = "hello world";
 String differentInput = "hi there";
 Caverphone caverphone = new Caverphone();
 String code1 = caverphone.caverphone(input);
 String code2 = caverphone.caverphone(differentInput);
 assertFalse(caverphone.isCaverphoneEqual(input, differentInput));
 assertFalse(code1.equals(code2));
 }
}