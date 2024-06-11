package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

 @Test
 public void testUSEnglishSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input = "Apple, Banana, Cat, Dog, Elephant, Hello, Igloo, Jamboree, Kangaroo, Lemon, Mango";
 String expected = "A120, A120, C126, D220, E113, H000, I000, J161, K155, L110, M141, M141";
 String[] inputs = input.split(", ");
 String[] outputs = expected.split(", ");
 for (int i = 0; i < inputs.length; i++) {
 String result = soundex.soundex(inputs[i]);
 assertEquals(outputs[i], result);
 }
 }

 @Test (expected = EncoderException.class)
 public void testInvalidCharacter() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 soundex.encode("apple&banana");
 }

 @Test
 public void testEmptyString() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String result = soundex.soundex("");
 assertNull(result);
 }

 @Test
 public void testNullString() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String result = soundex.soundex(null);
 assertNull(result);
 }

}