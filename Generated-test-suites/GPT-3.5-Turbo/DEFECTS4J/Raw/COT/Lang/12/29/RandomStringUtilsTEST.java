import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        assert result.length() == count;
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        assert result.length() == count;
        for (char c : result.toCharArray()) {
            assert c >= 32 && c <= 127;
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        assert result.length() == count;
        for (char c : result.toCharArray()) {
            assert Character.isLetter(c);
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        assert result.length() == count;
        for (char c : result.toCharArray()) {
            assert Character.isLetterOrDigit(c);
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        assert result.length() == count;
        for (char c : result.toCharArray()) {
            assert Character.isDigit(c);
        }
    }

    @Test
    public void testRandomSpecific() {
        int count = 10;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = false;
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        String result = RandomStringUtils.random(count, start, end, letters, numbers, chars);
        assert result.length() == count;
        for (char c : result.toCharArray()) {
            assert c >= start && c <= end && (letters && Character.isLetter(c) || numbers && Character.isDigit(c));
        }
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        int count = -1;
        RandomStringUtils.random(count);
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        int count = 10;
        char[] chars = {};
        RandomStringUtils.random(count, chars);
    }
  
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfBound() {
        int count = 10;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = false;
        char[] chars = {'A'};
        RandomStringUtils.random(count, start, end, letters, numbers, chars);
    }

    @Test
    public void testRandomWithSeed() {
        int count = 10;
        String result1 = RandomStringUtils.random(count, 0, 0, false, false, null, new java.util.Random(123L));
        String result2 = RandomStringUtils.random(count, 0, 0, false, false, null, new java.util.Random(123L));
        assert result1.equals(result2);
    }
}