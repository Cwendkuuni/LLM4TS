import org.junit.Test;
import org.junit.Assert;

public class SoundexTest {

	@Test
	public void testEncode_Object() {
		Soundex soundex = new Soundex();
		String encoded = (String) soundex.encode("Test");
		Assert.assertEquals("T230", encoded);
	}

	@Test(expected = EncoderException.class)
	public void testEncode_Object_InvalidInput() {
		Soundex soundex = new Soundex();
		soundex.encode(123);
	}

	@Test
	public void testEncode_String() {
		Soundex soundex = new Soundex();
		String encoded = soundex.encode("Test");
		Assert.assertEquals("T230", encoded);
	}

	@Test
	public void testDifference() throws EncoderException {
		Soundex soundex = new Soundex();
		int difference = soundex.difference("Test", "Test");
		Assert.assertEquals(4, difference);
	}

	@Test
	public void testSoundex() {
		Soundex soundex = new Soundex();
		String encoded = soundex.soundex("Test");
		Assert.assertEquals("T230", encoded);
	}

	@Test
	public void testGetMaxLength() {
		Soundex soundex = new Soundex();
		int maxLength = soundex.getMaxLength();
		Assert.assertEquals(4, maxLength);
	}

	@Test
	public void testSetMaxLength() {
		Soundex soundex = new Soundex();
		soundex.setMaxLength(2);
		int maxLength = soundex.getMaxLength();
		Assert.assertEquals(2, maxLength);
	}

	@Test
	public void testConstructor_WithStringMapping() {
		String mapping = "01230120022455012623010202";
		Soundex soundex = new Soundex(mapping.toCharArray());
		Assert.assertArrayEquals(mapping.toCharArray(), soundex.getSoundexMapping());
	}

	@Test
	public void testConstructor_WithCharMapping() {
		String mapping = "01230120022455012623010202";
		Soundex soundex = new Soundex(mapping);
		Assert.assertArrayEquals(mapping.toCharArray(), soundex.getSoundexMapping());
	}

	@Test
	public void testConstructor_WithDefaultMapping() {
		String defaultMapping = Soundex.US_ENGLISH_MAPPING_STRING;
		Soundex soundex = new Soundex();
		Assert.assertArrayEquals(defaultMapping.toCharArray(), soundex.getSoundexMapping());
	}
}