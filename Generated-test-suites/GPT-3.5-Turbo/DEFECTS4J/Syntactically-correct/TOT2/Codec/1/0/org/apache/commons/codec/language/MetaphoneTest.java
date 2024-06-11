import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {
    
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("hello");
        assertEquals("Expected result not received", "HLO", result);
    }

    @Test
    public void testMetaphoneSingleChar() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("h");
        assertEquals("Expected result not received", "H", result);
    }

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        assertEquals("Expected result not received", "", result);
    }
    
    @Test
    public void testMetaphoneIsEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("hello", "HELLO");
        assertEquals("Expected result not received", true, result);
    }
}