import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

	@Test
	public void testMetaphone() {
		Metaphone metaphone = new Metaphone();

		// Test 1: Empty String
		String empty = metaphone.metaphone("");
		Assert.assertEquals("", empty);

		// Test 2: Single character
		String singleChar = metaphone.metaphone("a");
		Assert.assertEquals("A", singleChar);

		// Test 3: Single character (vowel)
		String singleVowelChar = metaphone.metaphone("e");
		Assert.assertEquals("E", singleVowelChar);

		// Test 4: Two characters
		String twoChars = metaphone.metaphone("ab");
		Assert.assertEquals("AB", twoChars);

		// Test 5: Two characters (vowel)
		String twoVowelChars = metaphone.metaphone("ae");
		Assert.assertEquals("AE", twoVowelChars);

		// Test 6: Three characters
		String threeChars = metaphone.metaphone("abc");
		Assert.assertEquals("ABK", threeChars);

		// Test 7: Three characters (vowel)
		String threeVowelChars = metaphone.metaphone("aee");
		Assert.assertEquals("AE", threeVowelChars);

		// Test 8: Multiple characters
		String multipleChars = metaphone.metaphone("abcde");
		Assert.assertEquals("ABKT", multipleChars);
	}

	@Test
	public void testEncode() throws EncoderException{
		Metaphone metaphone = new Metaphone();

		// Test 1: Empty string
		Object empty = metaphone.encode("");
		Assert.assertEquals("", empty);

		// Test 2: Non-string object
		try {
			metaphone.encode(123);
			Assert.fail("Should throw EncoderException");
		} catch (EncoderException e) {
			Assert.assertTrue(true);
		}

		// Test 3: Normal string
		String normalString = metaphone.encode("hello");
		Assert.assertEquals("HL", normalString);
	}

	@Test
	public void testIsMetaphoneEqual() {
		Metaphone metaphone = new Metaphone();

		// Test 1: Equal strings
		boolean equalStrings = metaphone.isMetaphoneEqual("hello", "hello");
		Assert.assertTrue(equalStrings);

		// Test 2: Non-equal strings
		boolean nonEqualStrings = metaphone.isMetaphoneEqual("hello", "world");
		Assert.assertFalse(nonEqualStrings);
	}

}