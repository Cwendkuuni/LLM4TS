import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneSimple() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String expected = "A0P0P1E";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        metaphone.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneEmptyInput() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        metaphone.metaphone(input);
    }

    @Test
    public void testMetaphoneVowels() {
        Metaphone metaphone = new Metaphone();
        String input = "aeiou";
        String expected = "A0E0I0O0U0";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneConsonants() {
        Metaphone metaphone = new Metaphone();
        String input = "bcdfghjklmnpqrstvwxyz";
        String expected = "B0C0D0F0G0H0J0K0L0M0N0P0Q0R0S0T0V0W0X0Y0Z0";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneSpecialCases() {
        Metaphone metaphone = new Metaphone();
        String input = "kangaroo, apple pie, echidna, aardvark, chinaman, shanghai, metaphone";
        String expected = "K0NG0K0P0L0E0 A0P0P1E0 A0E0D0N0 A00R0V0K0 M0T0P0H0N0 M0T0P0H0N0 M0T0P0H0N0";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        String input = "metaphoneisawesome";
        String expected = "M0T0P0H0N3 I0S0M0V0E2 A0W0V0E1 S0M0E2";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }
}