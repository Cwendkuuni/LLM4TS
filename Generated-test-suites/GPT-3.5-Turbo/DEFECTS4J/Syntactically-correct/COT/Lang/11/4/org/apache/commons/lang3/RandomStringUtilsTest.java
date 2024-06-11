import org.junit.Test;

public class RandomStringUtilsTest {

    // random(int count)
    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assert(result.length() == 10);
    }

    // randomAscii(int count)
    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert((int)c >= 32 && (int)c <= 126);
        }
    }
    
    // randomAlphabetic(int count)
    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }
    
    // randomAlphanumeric(int count)
    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }
    
    // randomNumeric(int count)
    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    // random(int count, boolean letters, boolean numbers)
    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(10, true, true);
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }
    
    // random(int count, int start, int end, boolean letters, boolean numbers)
    @Test
    public void testRandomWithStartAndEnd() {
        String result = RandomStringUtils.random(10, 'a', 'f', true, false);
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(c >= 'a' && c <= 'f');
        }
    }
    
    // random(int count, int start, int end, boolean letters, boolean numbers, char[] chars)
    @Test
    public void testRandomWithCustomChars() {
        String result = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(c == 'a' || c == 'b' || c == 'c');
        }
    }
    
    // random(int count, String chars)
    @Test
    public void testRandomWithCharsString() {
        String result = RandomStringUtils.random(10, "abc");
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(c == 'a' || c == 'b' || c == 'c');
        }
    }
    
    // random(int count, char... chars)
    @Test
    public void testRandomWithCharsArray() {
        String result = RandomStringUtils.random(10, 'a', 'b', 'c');
        assert(result.length() == 10);
        for (char c : result.toCharArray()) {
            assert(c == 'a' || c == 'b' || c == 'c');
        }
    }

}