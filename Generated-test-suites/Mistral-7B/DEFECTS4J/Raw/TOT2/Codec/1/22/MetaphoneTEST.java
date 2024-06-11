import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String expected = "A0P0L0E0";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }
}