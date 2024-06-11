import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {
	
	@Test
	public void testClean() {
		// Testing clean method with null input
		String result = SoundexUtils.clean(null);
		assertEquals(null, result);
		
		// Testing clean method with empty string input
		result = SoundexUtils.clean("");
		assertEquals("", result);
		
		// Testing clean method with valid string input
		result = SoundexUtils.clean("abc123Def");
		assertEquals("ABCDEF", result);
	}
	
	@Test
	public void testDifferenceEncoded() {
		// Testing differenceEncoded method with null input
		int result = SoundexUtils.differenceEncoded(null, null);
		assertEquals(0, result);
		
		// Testing differenceEncoded method with empty string input
		result = SoundexUtils.differenceEncoded("", "");
		assertEquals(0, result);
		
		// Testing differenceEncoded method with valid input
		result = SoundexUtils.differenceEncoded("F1234", "F5678");
		assertEquals(1, result);
	}
	
	@Test
	public void testDifference() throws EncoderException {
		// Testing difference method with null input
		int result = SoundexUtils.difference(null, null, null);
		assertEquals(0, result);
		
		// Testing difference method with empty string input
		result = SoundexUtils.difference(null, "", "");
		assertEquals(0, result);
		
		// Testing difference method with valid input
		result = SoundexUtils.difference(null, "abc123Def", "abcDEF123");
		assertEquals(4, result);
	}

}