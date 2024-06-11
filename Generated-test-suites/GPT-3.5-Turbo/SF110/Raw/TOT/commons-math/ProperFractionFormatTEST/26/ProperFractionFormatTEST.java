import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    private Fraction testFraction;

    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
        testFraction = new Fraction(3, 4);
    }

    @Test
    public void testFormat() {
        StringBuffer expectedOutput = new StringBuffer();
        expectedOutput.append("3 / 4");
        StringBuffer actualOutput = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        properFractionFormat.format(testFraction, actualOutput, pos);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat expectedFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        expectedFormat.setMaximumFractionDigits(0);
        assertEquals(expectedFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        String input = "5 / 6";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(5, 6);
        Fraction actualFraction = properFractionFormat.parse(input, pos);
        assertEquals(expectedFraction, actualFraction);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat newFormat = NumberFormat.getPercentInstance(Locale.getDefault());
        newFormat.setMaximumIntegerDigits(1);
        properFractionFormat.setWholeFormat(newFormat);
        assertEquals(newFormat, properFractionFormat.getWholeFormat());
    }

}