package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class MetaphoneTest {

 private final Metaphone metaphone = new Metaphone();

 @Test
 public void testEncodeEmptyString() {
 String encoded = metaphone.encode("");
 assertNull(encoded);
 }

 @Test
 public void testEncodeNullString() {
 assertThrows(EncoderException.class, () -> metaphone.encode(null));
 }

 @Test
 public void testEncodeSingleCharacter() {
 String encoded = metaphone.encode("A");
 assertEquals("A", encoded);
 }

 @Test
 public void testEncodeDoubleCharactersKN() {
 String encoded = metaphone.encode("KN");
 assertEquals("K", encoded);
 }

 @Test
 public void testEncodeDoubleCharactersAE() {
 String encoded = metaphone.encode("AE");
 assertEquals("A", encoded);
 }

 @Test
 public void testEncodeDoubleCharactersWR() {
 String encoded = metaphone.encode("WR");
 assertEquals("W", encoded);
 }

 @Test
 public void testEncodeDoubleCharactersWH() {
 String encoded = metaphone.encode("WH");
 assertEquals("W", encoded);
 }

 @Test
 public void testEncodeInitialX() {
 String encoded = metaphone.encode("X");
 assertEquals("S", encoded);
 }

 @Test
 public void testEncodeSingleCharacterVowel() {
 String encoded = metaphone.encode("E");
 assertEquals("E", encoded);
 }

 @Test
 public void testEncodeStringWithDuplicateLetters() {
 String encoded = metaphone.encode("APPLE");
 assertEquals("PPL", encoded);
 }

 @Test
 public void testEncodeStringWithConsecutiveVowels() {
 String encoded = metaphone.encode("AEEIOU");
 assertEquals("EIO", encoded);
 }

 @Test
 public void testEncodeStringWithConsonants() {
 String encoded = metaphone.encode("BANANA");
 assertEquals("BNG", encoded);
 }

 @Test
 public void testEncodeStringWithVowelsAndConsonants() {
 String encoded = metaphone.encode("APPLEE");
 assertEquals("PPL", encoded);
 }

 @Test
 public void testEncodeStringWithInitialGh() {
 String encoded = metaphone.encode("GHASTLY");
 assertEquals("F", encoded);
 }

 @Test
 public void testEncodeStringWithFinalGh() {
 String encoded = metaphone.encode("BIGHAM");
 assertEquals("B", encoded);
 }

 @Test
 public void testEncodeStringWithFinalSilentG() {
 String encoded = metaphone.encode("BINGO");
 assertEquals("B", encoded);
 }

 @Test
 public void testEncodeStringWithInitialDge() {
 String encoded = metaphone.encode("DGEEGEE");
 assertEquals("J", encoded);
 }

 @Test
 public void testEncodeStringWithInitialDgi() {
 String encoded = metaphone.encode("DGIIGI");
 assertEquals("J", encoded);
 }

 @Test
 public void testEncodeStringWithInitialDgY() {
 String encoded = metaphone.encode("DGYIGY");
 assertEquals("J", encoded);
 }

 @Test
 public void testEncodeStringWithDoubleLetterC() {
 String encoded = metaphone.encode("CITY");
 assertEquals("S", encoded);
 }

 @Test
 public void testEncodeStringWithTripleLetterCCC() {
 String encoded = metaphone.encode("CCC");
 assertEquals("C", encoded);
 }

 @Test
 public void testEncodeStringWithConsecutiveSilentG() {
 String encoded = metaphone.encode("GHINGHAM");
 assertEquals("F", encoded);
 }

 @Test
 public void testEncodeStringWithVowelFollowedByConsonant() {
 String encoded = metaphone.encode("HELLHO");
 assertEquals("H", encoded);
 }

 @Test
 public void testEncodeStringWithConsonantFollowedByVowel() {
 String encoded = metaphone.encode("BREAD");
 assertEquals("B", encoded);
 }

 @Test
 public void testEncodeStringWithConsecutiveVowelsFollowedByConsonant() {
 String encoded = metaphone.encode("AEEIOUIA");
 assertEquals("EIO", encoded);
 }

 @Test
 public void testEncodeStringWithConsecutiveConsonants() {
 String encoded = metaphone.encode("BSPTG");
 assertEquals("S", encoded);
 }

 @Test
 public void testEncodeStringWithConsecutiveSilentGh() { 
 String encoded = metaphone.encode("GHINGHAMGHINGHAM");
 assertEquals("F", encoded);
 }

 @Test
 public void testEncodeStringWithConsecutiveVowels() {
 String encoded = metaphone.encode("AEEIOUAEIOU");
 assertEquals("EIO", encoded);
 }

 @Test
 public void testEncodeStringWithVowelsAndConsonantsAndDigits() {
 String encoded = metaphone.encode("APPLE123");
 assertEquals("PPL", encoded);
 }

 @Test
 public void testEncodeStringWithUppercaseLetters() {
 String encoded = metaphone.encode("HELLO");
 assertEquals("H", encoded);
 }

 @Test
 public void testEncodeStringWithCapitalLettersAndDigits() {
 String encoded = metaphone.encode("APPLE123");
 assertEquals("PPL", encoded);
 }

 @Test
 public void testEncodeStringWithEmptyStringAfter() {
 String encoded = metaphone.encode("APPLE");
 assertEquals("PPL", encoded);
 encoded += "";
 assertEquals("PPL", encoded);
 }

 @Test
 public void testEncodeStringWithSingleCharacterAfter() {
 String encoded = metaphone.encode("APPLE");
 assertEquals("PPL", encoded);
 encoded += 'A';
 assertEquals("PPL", encoded);
 }

 @Test
 public void testEncodeStringWithLongerStringAfter() {
 String encoded = metaphone.encode("APPLE");
 assertEquals("PPL", encoded);
 encoded += "BANANA";
 assertEquals("PPLBNG", encoded);
 }

 @Test
 public void testEncodeStringWithStringBefore() {
 String encoded = "APPLE";
 encoded += metaphone.encode("BANANA");
 assertEquals("PPBLNG", encoded);
 }

 @Test
 public void testEncodeStringWithLongerStringBefore() {
 String encoded = metaphone.encode("BANANA");
 encoded += "APPLE";
 assertEquals("BNGPPPL", encoded);
 }

 @Test
 public void testEncodeStringWithLongerStringBeforeAndAfter() {
 String encoded = metaphone.encode("BANANA");
 encoded += "APPLE";
 encoded += "ORANGE";
 assertEquals("BNGPPPLORNG", encoded);
 }

 @Test
 public void testEncodeStringWithNullAfter() {
 String encoded = metaphone.encode("APPLE");
 assertEquals("PPL", encoded);
 encoded += null;
 assertNull(encoded);
 }

 @Test
 public void testEncodeStringWithNullBefore() {
 String encoded = null;
 encoded += metaphone.encode("APPLE");
 assertEquals(null, encoded);
 }

 @Test
 public void testEncodeStringWithNullBeforeAndAfter() {
 String encoded = null;
 encoded += null;
 assertEquals(null, encoded);
 }

 @Test
 public void testEncodeLongString() {
 String encoded = metaphone.encode("This is a test of the Metaphone encoder. This is a test of the Metaphone encoder.");
 assertEquals("STHSISATTESTTHTESTTHMTFTPNGDGR", encoded);
 }
}