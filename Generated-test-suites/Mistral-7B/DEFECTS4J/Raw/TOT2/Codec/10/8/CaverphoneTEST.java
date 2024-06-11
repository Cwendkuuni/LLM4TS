import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        // Test empty string
        String result = new Caverphone().caverphone("");
        assertEquals("1111111111", result);

        // Test single character string
        String input = "a";
        result = new Caverphone().caverphone(input);
        assertEquals("a", result);

        // Test common English words
        String words[] = {"hello", "world", "java", "test", "example"};
        for (String word : words) {
            result = new Caverphone().caverphone(word);
            System.out.println(word + " -> " + result);
            assertTrue(result.length() == 10);
        }

        // Test edge cases and exceptions
        try {
            new Caverphone().caverphone(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        String input2 = "123456abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        result = new Caverphone().caverphone(input2);
        assertEquals(input2, result); // This should be a fail, as per specification
    }
}