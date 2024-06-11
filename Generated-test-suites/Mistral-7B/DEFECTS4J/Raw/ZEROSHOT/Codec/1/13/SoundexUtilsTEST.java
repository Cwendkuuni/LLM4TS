import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Language;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SoundexUtilsTest {

 private Soundex soundex;
 private StringEncoder encoder;

 @Before
 public void setUp() {
 soundex = new Soundex();
 encoder = new StringEncoder();
 }

 @After
 public void tearDown() {
 soundex = null;
 encoder = null;
 }

 @Test
 public void testClean() {
 // Test null input
 assertEquals("", SoundexUtils.clean(null));

 // Test empty string
 assertEquals("", SoundexUtils.clean(""));

 // Test single letter
 assertEquals("A", SoundexUtils.clean("A"));
 assertEquals("A", SoundexUtils.clean("a"));

 // Test multiple letters
 assertEquals("BAKER", SoundexUtils.clean("bAkEr"));
 assertEquals("BARN", SoundexUtils.clean("BaRn"));
 assertEquals("BIG", SoundxUtils.clean("bIg"));
 }

 @Test
 public void testDifference() throws EncoderException {
 // Test null inputs
 assertEquals(0, SoundexUtils.difference(encoder, null, "test"));
 assertEquals(0, SoundexUtils.difference(encoder, "test", null));

 // Test empty strings
 assertEquals(4, SoundexUtils.difference(encoder, "", ""));

 // Test identical strings
 assertEquals(4, SoundexUtils.difference(encoder, "test", "test"));

 // Test Soundex encoded strings with some similarity
 String s1 = soundex.encode("Apple");
 String s2 = soundex.encode("Orange");
 assertEquals(2, SoundexUtils.differenceEncoded(s1, s2));

 // Test Soundex encoded strings with no similarity
 s1 = soundex.encode("Apple");
 s2 = soundex.encode("Banana");
 assertEquals(0, SoundexUtils.differenceEncoded(s1, s2));
 }
}