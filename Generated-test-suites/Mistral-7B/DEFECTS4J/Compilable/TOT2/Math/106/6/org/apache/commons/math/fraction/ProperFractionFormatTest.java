package org.apache.commons.math.fraction;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String expected = "3 1/2";
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(3, 2);

        format.format(fraction, toAppendTo, null);
        String actual = toAppendTo.toString();

        assertEquals(expected, actual);
    }
}