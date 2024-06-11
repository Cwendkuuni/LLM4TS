
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

	@Test
	public void testRandom() {
		int count = 5;
		String result = RandomStringUtils.random(count);
		assertEquals(count, result.length());
	}

	@Test
	public void testRandomAscii() {
		int count = 5;
		String result = RandomStringUtils.randomAscii(count);
		assertEquals(count, result.length());
	}

	@Test
	public void testRandomAlphabetic() {
		int count = 5;
		String result = RandomStringUtils.randomAlphabetic(count);
		assertEquals(count, result.length());
		assertTrue(result.chars().allMatch(Character::isLetter));
	}

	@Test
	public void testRandomAlphanumeric() {
		int count = 5;
		String result = RandomStringUtils.randomAlphanumeric(count);
		assertEquals(count, result.length());
		assertTrue(result.chars().allMatch(Character::isLetterOrDigit));
	}

	@Test
	public void testRandomNumeric() {
		int count = 5;
		String result = RandomStringUtils.randomNumeric(count);
		assertEquals(count, result.length());
		assertTrue(result.chars().allMatch(Character::isDigit));
	}

	@Test
	public void testRandomWithLettersAndNumbers() {
		int count = 5;
		boolean letters = true;
		boolean numbers = true;
		String result = RandomStringUtils.random(count, letters, numbers);
		assertEquals(count, result.length());
		assertTrue(result.chars().allMatch(Character::isLetterOrDigit));
	}

	@Test
	public void testRandomWithStartAndEnd() {
		int count = 5;
		int start = 65;
		int end = 90;
		boolean letters = true;
		boolean numbers = false;
		String result = RandomStringUtils.random(count, start, end, letters, numbers);
		assertEquals(count, result.length());
		assertTrue(result.chars().allMatch(ch -> ch >= start && ch <= end));
	}

	@Test
	public void testRandomWithCharsArray() {
		int count = 5;
		char[] chars = {'A', 'B', 'C'};
		String result = RandomStringUtils.random(count, chars);
		assertEquals(count, result.length());
		assertTrue(result.chars().allMatch(ch -> new String(chars).contains(String.valueOf((char) ch))));
	}
}
