import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {

	@Test
	public void testClean() {
		String input = "InputString";
		String expectedOutput = "INPUTSTRING";
		String actualOutput = SoundexUtils.clean(input);
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testCleanNullString() {
		String input = null;
		String expectedOutput = null;
		String actualOutput = SoundexUtils.clean(input);
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testCleanEmptyString() {
		String input = "";
		String expectedOutput = "";
		String actualOutput = SoundexUtils.clean(input);
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testDifference() throws EncoderException {
		StringEncoder encoder = new StringEncoder();
		String s1 = "String1";
		String s2 = "String2";
		int expectedOutput = 0; // Test case specific expected output
		int actualOutput = SoundexUtils.difference(encoder, s1, s2);
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testDifferenceEncoded() {
		String es1 = "EncodedString1";
		String es2 = "EncodedString2";
		int expectedOutput = 0; // Test case specific expected output
		int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
		assertEquals(expectedOutput, actualOutput);
	}

}