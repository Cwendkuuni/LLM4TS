import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class RandomStringUtilsTest {

	@Test
	public void testRandom() {
		String randomString = RandomStringUtils.random(10);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
	}

	@Test
	public void testRandomAscii() {
		String randomString = RandomStringUtils.randomAscii(10);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
		assertTrue(isAscii(randomString));
	}

	@Test
	public void testRandomAlphabetic() {
		String randomString = RandomStringUtils.randomAlphabetic(10);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
		assertTrue(isAlphabetic(randomString));
	}

	@Test
	public void testRandomAlphanumeric() {
		String randomString = RandomStringUtils.randomAlphanumeric(10);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
		assertTrue(isAlphanumeric(randomString));
	}

	@Test
	public void testRandomNumeric() {
		String randomString = RandomStringUtils.randomNumeric(10);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
		assertTrue(isNumeric(randomString));
	}

	@Test
	public void testRandomWithParameters() {
		String randomString = RandomStringUtils.random(10, true, true);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
		assertTrue(isAlphanumeric(randomString));
	}

	@Test
	public void testRandomWithCharSet() {
		char[] charSet = {'a', 'b', 'c'};
		String randomString = RandomStringUtils.random(10, charSet);
		assertNotNull(randomString);
		assertEquals(10, randomString.length());
		assertTrue(containsOnly(randomString, charSet));
	}

	private boolean isAscii(String str) {
		for(char c : str.toCharArray()) {
			if (c < 32 || c > 126) {
				return false;
			}
		}
		return true;
	}

	private boolean isAlphabetic(String str) {
		return str.chars().allMatch(Character::isLetter);
	}

	private boolean isAlphanumeric(String str) {
		return str.chars().allMatch(Character::isLetterOrDigit);
	}

	private boolean isNumeric(String str) {
		return str.chars().allMatch(Character::isDigit);
	}

	private boolean containsOnly(String str, char[] charSet) {
		for(char c : str.toCharArray()) {
			boolean found = false;
			for(char ch : charSet) {
				if(c == ch) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}

}