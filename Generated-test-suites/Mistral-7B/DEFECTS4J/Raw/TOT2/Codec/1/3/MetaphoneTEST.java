import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneBasic() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String expected = "A0P0L0E0";
        String output = metaphone.metaphone(input);
        assertEquals(expected, output);
    }

    @Test
    public void testMetaphoneWithEdgeCases() {
        Metaphone metaphone = new Metaphone();
        String input1 = "banana";
        String expected1 = "B2N1A1N1A1";
        String output1 = metaphone.metaphone(input1);
        assertEquals(expected1, output1);

        String input2 = "cinematography";
        String expected2 = "S5T2N2T1C1M1A0T1O1G1R1A1F1";
        String output2 = metaphone.metaphone(input2);
        assertEquals(expected2, output2);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNullInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        metaphone.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithEmptyInput() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        metaphone.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNonASCIIInput() {
        Metaphone metaphone = new Metaphone();
        String input = "étable";
        metaphone.metaphone(input);
    }
}