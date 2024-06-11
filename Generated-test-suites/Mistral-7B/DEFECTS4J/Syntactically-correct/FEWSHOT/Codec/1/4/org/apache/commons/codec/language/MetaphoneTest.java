package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

 Metaphone metaphone = new Metaphone();

 @Test
 public void testMetaphone() {
 String[] testCases = new String[]{"apple", "appel", "apfel", "appl", "applie", "applebee", "appleton", "apprentice", "apparatus", "apothecary", "apotheosis"};
 for (String s : testCases) {
 String code = metaphone.metaphone(s);
 System.out.println(s + " -> " + code);
 assertEquals(code, metaphone.metaphone(s));
 }
 }

 @Test
 public void testEncode() throws EncoderException {
 String[] testCases = new String[]{"apple", "appel", "apfel", "appl", "applie", "applebee", "appleton", "apprentice", "apparatus", "apothecary", "apotheosis"};
 for (String s : testCases) {
 Object encoded = metaphone.encode(s);
 assertEquals(encoded.toString(), metaphone.metaphone(s));
 }
 }

 @Test
 public void testIsMetaphoneEqual() {
 String[] testCases = new String[]{"apple", "appel", "apfel", "appl", "applie", "applebee", "appleton", "apprentice", "apparatus", "apothecary", "apotheosis", "apples"};
 for (int i = 0; i < testCases.length; i++) {
 for (int j = 0; j < testCases.length; j++) {
 if (i != j) {
 boolean result = metaphone.isMetaphoneEqual(testCases[i], testCases[j]);
 assertFalse(result);
 }
 }
 }
 }

 @Test
 public void testMaxCodeLen() {
 int maxCodeLen = metaphone.getMaxCodeLen();
 assertEquals(maxCodeLen, 4);
 metaphone.setMaxCodeLen(5);
 maxCodeLen = metaphone.getMaxCodeLen();
 assertEquals(maxCodeLen, 5);
 }
}