import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

	@Test
	public void testClean() {
		assertEquals("ABC", SoundexUtils.clean("a!B#C"));
		assertEquals("", SoundexUtils.clean(null));
		assertEquals("", SoundexUtils.clean(""));
	}

	@Test
	public void testDifference() {
		try {
			assertEquals(4, SoundexUtils.difference(new Soundex(), "ABC", "ABCD"));
			assertEquals(0, SoundexUtils.difference(new Soundex(), "ABC", "XYZ"));
		} catch (EncoderException e) {
			fail("EncoderException occurred");
		}
	}

	@Test
	public void testDifferenceEncoded() {
		assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
		assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "XYZW"));
		assertEquals(1, SoundexUtils.differenceEncoded("ABCD", "AXCW"));
	}

}