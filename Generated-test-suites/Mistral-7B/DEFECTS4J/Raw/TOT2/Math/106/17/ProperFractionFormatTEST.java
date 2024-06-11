import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer expectedOutput = new StringBuffer(100);
        String expected = "1/2";

        formatter.format(fraction, expectedOutput, null);
        String actualOutput = expectedOutput.toString();

        Assertions.assertEquals(expected, actualOutput);
    }
}