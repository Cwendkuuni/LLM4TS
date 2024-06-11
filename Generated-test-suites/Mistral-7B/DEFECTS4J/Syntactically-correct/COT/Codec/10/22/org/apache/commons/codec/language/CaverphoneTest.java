import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNullString() {
 String encoded = caverphone.caverphone(null);
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testCaverphoneEmptyString() {
 String encoded = caverphone.caverphone("");
 assertEquals("1111111111", encoded);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneObjectNotString() {
 caverphone.encode(new Object());
 }

 @Test
 public void testCaverphoneSimpleString() {
 String txt = "hello";
 String encoded = caverphone.caverphone(txt);
 String expected = "h2h2l2l2o2o";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String txt = "CoughCoffee";
 String encoded = caverphone.caverphone(txt);
 String expected = "cou2fco2f";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneReplacements() {
 String txt = "Cecilcycles";
 String encoded = caverphone.caverphone(txt);
 String expected = "se2sil2cy2cles";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String txt = "TwoThrees";
 String encoded = caverphone.caverphone(txt);
 String expected = "T";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesMultipleReplacements() {
 String txt = "TwoThreesTwoThrees";
 String encoded = caverphone.caverphone(txt);
 String expected = "T";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesMultipleStartOptions() {
 String txt = "CoughCoffeeRoughRough";
 String encoded = caverphone.caverphone(txt);
 String expected = "cou2frou2f";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesMultipleReplacementsWithStartOptions() {
 String txt = "CecilcyclesCoughCoffee";
 String encoded = caverphone.caverphone(txt);
 String expected = "se2sil2cou2fco2f";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesFinalE() {
 String txt = "EnoughEnough";
 String encoded = caverphone.caverphone(txt);
 String expected = "enou2fenou2f";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesDifferentCases() {
 String txt = "HELLOHello";
 String encoded = caverphone.caverphone(txt);
 String expected = "h2h2l2l2o2o";
 assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesLongStrings() {
 String txt = "ThisIsASentenceThatIsReallyLongAndIsMeantToTestTheCaverphoneClassForStringsThatAreLongerThanTheAverage";
 String encoded = caverphone.caverphone(txt);
 int expectedLength = 34;
 assertEquals(expectedLength, encoded.length());
 }

}