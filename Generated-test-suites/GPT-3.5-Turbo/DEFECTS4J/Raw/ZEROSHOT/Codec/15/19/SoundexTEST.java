import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

	@Test
	public void testEncode_Object_String() throws EncoderException {
		Soundex soundex = new Soundex();
		Assert.assertEquals("S530", soundex.encode("Smith"));
	}

	@Test
	public void testEncode_String_String() {
		Soundex soundex = new Soundex();
		Assert.assertEquals("S530", soundex.encode("Smith"));
	}

	@Test(expected = EncoderException.class)
	public void testEncode_Object_Exception() throws EncoderException {
		Soundex soundex = new Soundex();
		soundex.encode(123);
	}

	@Test
	public void testDifference() throws EncoderException {
		Soundex soundex = new Soundex();
		Assert.assertEquals(2, soundex.difference("Smith", "Smite"));
	}

	@Test(expected = EncoderException.class)
	public void testDifference_Exception() throws EncoderException {
		Soundex soundex = new Soundex();
		soundex.difference(123, "Smite");
	}

}