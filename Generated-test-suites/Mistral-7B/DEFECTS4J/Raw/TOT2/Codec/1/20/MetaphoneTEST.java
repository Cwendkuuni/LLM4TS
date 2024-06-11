import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneBasic() {
        Metaphone encoder = new Metaphone();
        String input = "apple";
        String expected = "A0P0L1E0";
        String result = encoder.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEdgeCases() {
        Metaphone encoder = new Metaphone();
        String input1 = "appelle"; // initial letter E
        String expected1 = "A0P0L1E0";
        String result1 = encoder.metaphone(input1);
        assertEquals(expected1, result1);

        String input2 = "schindler"; // contains CH
        String expected2 = "S0X1N1D1L1R0";
        String result2 = encoder.metaphone(input2);
        assertEquals(expected2, result2);

        String input3 = "congratulations"; // multiple consecutive vowels
        String expected3 = "K0N1G1R1T1S1L1S1H1O1N1S1";
        String result3 = encoder.metaphone(input3);
        assertEquals(expected3, result3);
    }

    @Test(expected = DecoderException.class)
    public void testMetaphoneException() {
        Metaphone encoder = new Metaphone();
        String input = null;
        String result = encoder.metaphone(input);
    }
}