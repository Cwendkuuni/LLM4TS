import org.junit.Test;
import org.apache.commons.codec.language.Metaphone;

import static org.junit.Assert.assertEquals;

public class MetaphoneTest {
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test cases for metaphone(String txt)
        String actual1 = metaphone.metaphone("cat");
        assertEquals("KT", actual1);

        String actual2 = metaphone.metaphone("Chat");
        assertEquals("XT", actual2);

        String actual3 = metaphone.metaphone("art");
        assertEquals("AT", actual3);

        String actual4 = metaphone.metaphone("shirt");
        assertEquals("XRT", actual4);

        String actual5 = metaphone.metaphone("catch");
        assertEquals("KX", actual5);

        String actual6 = metaphone.metaphone("match");
        assertEquals("MX", actual6);

        // Test cases for isMetaphoneEqual(String str1, String str2)
        boolean result1 = metaphone.isMetaphoneEqual("cat", "cat");
        assertEquals(true, result1);

        boolean result2 = metaphone.isMetaphoneEqual("cat", "hat");
        assertEquals(false, result2);

        boolean result3 = metaphone.isMetaphoneEqual("hat", "hat");
        assertEquals(true, result3);
    }
}