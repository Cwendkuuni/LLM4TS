import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

	@Test
	public void testClean() {
		String str = "Hello World!";
		String cleanStr = SoundexUtils.clean(str);
		assertEquals("HELLOWORLD", cleanStr);
	}

	@Test
	public void testCleanWithEmptyString() {
		String str = "";
		String cleanStr = SoundexUtils.clean(str);
		assertEquals("", cleanStr);
	}

	@Test
	public void testCleanWithNullString() {
		String str = null;
		String cleanStr = SoundexUtils.clean(str);
		assertEquals(null, cleanStr);
	}

	@Test
	public void testCleanWithMixedCase() {
		String str = "HeLlO";
		String cleanStr = SoundexUtils.clean(str);
		assertEquals("HELLO", cleanStr);
	}

	@Test
	public void testDifference() throws EncoderException {
		StringEncoder encoder = new Soundex();
		String s1 = "Hello";
		String s2 = "Halo";
		int diff = SoundexUtils.difference(encoder, s1, s2);
		assertEquals(3, diff);
	}

	@Test
	public void testDifferenceWithEmptyStrings() throws EncoderException {
		StringEncoder encoder = new Soundex();
		String s1 = "";
		String s2 = "";
		int diff = SoundexUtils.difference(encoder, s1, s2);
		assertEquals(0, diff);
	}

	@Test
	public void testDifferenceWithNullStrings() throws EncoderException {
		StringEncoder encoder = new Soundex();
		String s1 = null;
		String s2 = null;
		int diff = SoundexUtils.difference(encoder, s1, s2);
		assertEquals(0, diff);
	}

	@Test
	public void testDifferenceEncoded() {
		String es1 = "H400";
		String es2 = "H401";
		int diff = SoundexUtils.differenceEncoded(es1, es2);
		assertEquals(2, diff);
	}

	@Test
	public void testDifferenceEncodedWithEmptyStrings() {
		String es1 = "";
		String es2 = "";
		int diff = SoundexUtils.differenceEncoded(es1, es2);
		assertEquals(0, diff);
	}

	@Test
	public void testDifferenceEncodedWithNullStrings() {
		String es1 = null;
		String es2 = null;
		int diff = SoundexUtils.differenceEncoded(es1, es2);
		assertEquals(0, diff);
	}
}