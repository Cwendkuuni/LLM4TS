import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetaphoneTest {

 private Metaphone metaphone = new Metaphone();

 @Test
 public void testEmptyString() {
 String result = metaphone.metaphone("");
 assertEquals("", result);
 }

 @Test
 public void testSingleCharacter() {
 String result = metaphone.metaphone("A");
 assertEquals("A", result);
 }

 @Test
 public void testInitialK() {
 String result = metaphone.metaphone("Knife");
 assertEquals("K251", result);
 }

 @Test
 public void testInitialC() {
 String result = metaphone.metaphone("Cat");
 assertEquals("C123", result);
 }

 @Test
 public void testInitialX() {
 String result = metaphone.metaphone("Xylophone");
 assertEquals("S000", result);
 }

 @Test
 public void testInitialH() {
 String result = metaphone.metaphone("House");
 assertEquals("H250", result);
 }

 @Test
 public void testInitialWh() {
 String result = metaphone.metaphone("Whisky");
 assertEquals("W500", result);
 }

 @Test
 public void testInitialWr() {
 String result = metaphone.metaphone("Wrong");
 assertEquals("R255", result);
 }

 @Test
 public void testInitialCh() {
 String result = metaphone.metaphone("Church");
 assertEquals("K524", result);
 }

 @Test
 public void testInitialTh() {
 String result = metaphone.metaphone("Thought");
 assertEquals("T254", result);
 }

 @Test
 public void testDoubleLetter() {
 String result = metaphone.metaphone("Bitter");
 assertEquals("T255", result);
 }

 @Test
 public void testThreeLetterWord() {
 String result = metaphone.metaphone("Banana");
 assertEquals("B256", result);
 }

 @Test
 public void testFourLetterWord() {
 String result = metaphone.metaphone("Elephant");
 assertEquals("E325", result);
 }

 @Test
 public void testEncodeObject() throws EncoderException {
 Object result = metaphone.encode("Apple");
 assertNotNull(result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() {
 Object result = metaphone.encode(new Integer(1));
 }

 @Test
 public void testIsMetaphoneEqual() {
 String str1 = "Apple";
 String str2 = "Apple";
 boolean result = metaphone.isMetaphoneEqual(str1, str2);
 assertEquals(true, result);
 }

 @Test
 public void testIsMetaphoneEqualDifferentStrings() {
 String str1 = "Apple";
 String str2 = "Banana";
 boolean result = metaphone.isMetaphoneEqual(str1, str2);
 assertEquals(false, result);
 }

 @Test
 public void testGetMaxCodeLen() {
 int result = metaphone.getMaxCodeLen();
 assertEquals(4, result);
 }

 @Test
 public void testSetMaxCodeLen() {
 int newMaxCodeLen = 5;
 metaphone.setMaxCodeLen(newMaxCodeLen);
 int result = metaphone.getMaxCodeLen();
 assertEquals(newMaxCodeLen, result);
 }
}