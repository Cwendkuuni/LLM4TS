import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

	@Test
	public void testClean() {
		Assert.assertEquals("ABC", SoundexUtils.clean("AbC123"));
		Assert.assertEquals("", SoundexUtils.clean(""));
		Assert.assertEquals(null, SoundexUtils.clean(null));
	}

	@Test
	public void testDifference() throws EncoderException {
		Assert.assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "heloo"));
		Assert.assertEquals(0, SoundexUtils.difference(new Soundex(), "", ""));
		Assert.assertEquals(0, SoundexUtils.difference(new Soundex(), null, "abc"));
	}

	@Test
	public void testDifferenceEncoded() {
		Assert.assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
		Assert.assertEquals(0, SoundexUtils.differenceEncoded("", ""));
		Assert.assertEquals(0, SoundexUtils.differenceEncoded(null, "abc"));
	}

}