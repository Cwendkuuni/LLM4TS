
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {
	
	@Test
	public void testClean() {
		String str = "abc";
		String cleanStr = SoundexUtils.clean(str);
		Assert.assertEquals("ABC", cleanStr);
	}
	
	@Test
	public void testClean_NullString() {
		String str = null;
		String cleanStr = SoundexUtils.clean(str);
		Assert.assertNull(cleanStr);
	}
	
	@Test
	public void testDifference() throws EncoderException {
		StringEncoder encoder = null; // Provide the necessary encoder
		String s1 = "abc";
		String s2 = "abd";
		int diff = SoundexUtils.difference(encoder, s1, s2);
		Assert.assertEquals(2, diff);
	}
	
	@Test
	public void testDifference_NullEncoder() throws EncoderException {
		StringEncoder encoder = null;
		String s1 = "abc";
		String s2 = "abd";
		int diff = SoundexUtils.difference(encoder, s1, s2);
		Assert.assertEquals(0, diff);
	}
	
	@Test
	public void testDifference_NullStrings() throws EncoderException {
		StringEncoder encoder = null; // Provide the necessary encoder
		String s1 = null;
		String s2 = null;
		int diff = SoundexUtils.difference(encoder, s1, s2);
		Assert.assertEquals(0, diff);
	}
}
