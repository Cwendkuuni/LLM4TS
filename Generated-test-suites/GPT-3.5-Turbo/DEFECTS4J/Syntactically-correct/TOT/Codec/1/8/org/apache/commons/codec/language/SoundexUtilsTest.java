import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

	@Test
	public void testClean() {
		// Typical use case: string with only upper case letters
		String str1 = "ABCD";
		String expected1 = "ABCD";
		assertEquals(expected1, SoundexUtils.clean(str1));
		
		// Typical use case: string with upper and lower case letters
		String str2 = "aBcD";
		String expected2 = "ABCD";
		assertEquals(expected2, SoundexUtils.clean(str2));
		
		// Edge case: empty string
		String str3 = "";
		String expected3 = "";
		assertEquals(expected3, SoundexUtils.clean(str3));
		
		// Error scenario: null string
		String str4 = null;
		exception.expect(NullPointerException.class);
		SoundexUtils.clean(str4);
	}

	@Test
	public void testDifference() throws EncoderException {
		// Typical use case: same encoded strings
		StringEncoder encoder1 = new Soundex();
		String s11 = "ABCD";
		String s12 = "ABCD";
		int expected1 = 4;
		assertEquals(expected1, SoundexUtils.difference(encoder1, s11, s12));
		
		// Typical use case: different encoded strings
		StringEncoder encoder2 = new RefinedSoundex();
		String s21 = "ABCD";
		String s22 = "XYZW";
		int expected2 = 1;
		assertEquals(expected2, SoundexUtils.difference(encoder2, s21, s22));
		
		// Edge case: empty strings
		StringEncoder encoder3 = new Soundex();
		String s31 = "";
		String s32 = "";
		int expected3 = 0;
		assertEquals(expected3, SoundexUtils.difference(encoder3, s31, s32));
		
		// Error scenario: null strings
		StringEncoder encoder4 = new Soundex();
		String s41 = null;
		String s42 = null;
		exception.expect(NullPointerException.class);
		SoundexUtils.difference(encoder4, s41, s42);
	}

	@Test
	public void testDifferenceEncoded() {
		// Typical use case: same encoded strings
		String es1 = "1234";
		String es2 = "1234";
		int expected1 = 4;
		assertEquals(expected1, SoundexUtils.differenceEncoded(es1, es2));
		
		// Typical use case: different encoded strings
		String es3 = "1234";
		String es4 = "9876";
		int expected2 = 1;
		assertEquals(expected2, SoundexUtils.differenceEncoded(es3, es4));
		
		// Edge case: empty strings
		String es5 = "";
		String es6 = "";
		int expected3 = 0;
		assertEquals(expected3, SoundexUtils.differenceEncoded(es5, es6));
		
		// Error scenario: null strings
		String es7 = null;
		String es8 = null;
		int expected4 = 0;
		assertEquals(expected4, SoundexUtils.differenceEncoded(es7, es8));
	}

}