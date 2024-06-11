import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.Fraction;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProperFractionFormatTest {

    @Mock
    private NumberFormat numberFormat;
    @Mock
    private FieldPosition fieldPosition;
    
    private ProperFractionFormat properFractionFormat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        properFractionFormat = new ProperFractionFormat(numberFormat);
    }

    @Test
    public void format_ReturnsFormattedFractionString_WhenFractionIsValid() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer stringBuffer = new StringBuffer();
        when(numberFormat.format(2, stringBuffer, fieldPosition)).thenReturn(stringBuffer);
        when(numberFormat.format(3, stringBuffer, fieldPosition)).thenReturn(stringBuffer);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("2 / 3", stringBuffer.toString());
    }

    @Test
    public void format_PrecedesFractionStringWithWholeNumber_WhenWholeNumberIsNonZero() {
        Fraction fraction = new Fraction(5, 3);
        StringBuffer stringBuffer = new StringBuffer();
        when(numberFormat.format(5, stringBuffer, fieldPosition)).thenReturn(stringBuffer);
        when(numberFormat.format(2, stringBuffer, fieldPosition)).thenReturn(stringBuffer);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("5 2 / 3", stringBuffer.toString());
    }

    @Test
    public void format_UsesAbsoluteValueForNumerator_WhenFractionHasNegativeWholeNumber() {
        Fraction fraction = new Fraction(-2, 3);
        StringBuffer stringBuffer = new StringBuffer();
        when(numberFormat.format(-2, stringBuffer, fieldPosition)).thenReturn(stringBuffer);
        when(numberFormat.format(3, stringBuffer, fieldPosition)).thenReturn(stringBuffer);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("-2 / 3", stringBuffer.toString());
    }

    @Test
    public void format_UsesAbsoluteValueForNumerator_WhenFractionHasNegativeDenominator() {
        Fraction fraction = new Fraction(2, -3);
        StringBuffer stringBuffer = new StringBuffer();
        when(numberFormat.format(2, stringBuffer, fieldPosition)).thenReturn(stringBuffer);
        when(numberFormat.format(3, stringBuffer, fieldPosition)).thenReturn(stringBuffer);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("2 / -3", stringBuffer.toString());
    }

    @Test
    public void parse_ReturnsParsedFractionWithWholeNumber_WhenSourceStringHasValidFormat() {
        String source = "5 2 / 3";
        ParsePosition parsePosition = new ParsePosition(0);
        when(numberFormat.parse("5", parsePosition)).thenReturn(5);
        when(numberFormat.parse("2", parsePosition)).thenReturn(2);
        when(numberFormat.parse("3", parsePosition)).thenReturn(3);

        Fraction fraction = properFractionFormat.parse(source, parsePosition);

        assertEquals(new Fraction(17, 3), fraction);
    }

    @Test
    public void parse_ReturnsParsedFractionWithoutWholeNumber_WhenSourceStringHasValidFormatWithoutWholeNumber() {
        String source = "2 / 3";
        ParsePosition parsePosition = new ParsePosition(0);
        when(numberFormat.parse("2", parsePosition)).thenReturn(2);
        when(numberFormat.parse("3", parsePosition)).thenReturn(3);

        Fraction fraction = properFractionFormat.parse(source, parsePosition);

        assertEquals(new Fraction(2, 3), fraction);
    }

    @Test
    public void parse_ReturnsNull_WhenSourceStringIsInvalid() {
        String source = "2 3 /";
        ParsePosition parsePosition = new ParsePosition(0);
        when(numberFormat.parse("2", parsePosition)).thenReturn(2);
        when(numberFormat.parse("3", parsePosition)).thenReturn(3);

        Fraction fraction = properFractionFormat.parse(source, parsePosition);

        asserEquals(null, fraction);
    }

}