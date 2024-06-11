import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction1 = new Fraction(3, 4);
        StringBuffer sb1 = new StringBuffer();
        FieldPosition pos1 = new FieldPosition(0);
        assertEquals("3 / 4", format.format(fraction1, sb1, pos1).toString());

        Fraction fraction2 = new Fraction(-5, 2);
        StringBuffer sb2 = new StringBuffer();
        FieldPosition pos2 = new FieldPosition(0);
        assertEquals("-2 1 / 2", format.format(fraction2, sb2, pos2).toString());

        Fraction fraction3 = new Fraction(7, 3);
        StringBuffer sb3 = new StringBuffer();
        FieldPosition pos3 = new FieldPosition(0);
        assertEquals("2 1 / 3", format.format(fraction3, sb3, pos3).toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        String source1 = "3 / 4";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction fraction1 = format.parse(source1, pos1);
        assertEquals(new Fraction(3, 4), fraction1);

        String source2 = "-2 1 / 2";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction fraction2 = format.parse(source2, pos2);
        assertEquals(new Fraction(-5, 2), fraction2);

        String source3 = "2 1 / 3";
        ParsePosition pos3 = new ParsePosition(0);
        Fraction fraction3 = format.parse(source3, pos3);
        assertEquals(new Fraction(7, 3), fraction3);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}