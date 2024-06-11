import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

	@Test
	public void testClean() {
		assertEquals("ABC", SoundexUtils.clean("abc"));
		assertEquals("DEF", SoundexUtils.clean("def123"));
		assertEquals("", SoundexUtils.clean(""));
		assertNull(SoundexUtils.clean(null));
	}

	@Test
	public void testDifferenceEncoded() {
		assertEquals(0, SoundexUtils.differenceEncoded("abc", "def"));
		assertEquals(3, SoundexUtils.differenceEncoded("abc", "abcd"));
		assertEquals(4, SoundexUtils.differenceEncoded("abcd", "abcd"));
	}

	@Test
	public void testDifference() throws EncoderException {
		StringEncoder encoder = new Soundex();
		assertEquals(0, SoundexUtils.difference(encoder, "abc", "def"));
		assertEquals(3, SoundexUtils.difference(encoder, "abc", "abcd"));
		assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcd"));
	}

}