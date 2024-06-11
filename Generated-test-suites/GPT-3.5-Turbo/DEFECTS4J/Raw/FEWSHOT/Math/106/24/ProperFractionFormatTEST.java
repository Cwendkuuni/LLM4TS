import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        NumberFormat wholeFormatMock = mock(NumberFormat.class);
        NumberFormat numeratorFormatMock = mock(NumberFormat.class);
        NumberFormat denominatorFormatMock = mock(NumberFormat.class);
        
        when(wholeFormatMock.format(3)).thenReturn(new StringBuffer("3"));
        when(numeratorFormatMock.format(3)).thenReturn(new StringBuffer("3"));
        when(denominatorFormatMock.format(4)).thenReturn(new StringBuffer("4"));
        
        properFractionFormat.setWholeFormat(wholeFormatMock);
        properFractionFormat.setNumeratorFormat(numeratorFormatMock);
        properFractionFormat.setDenominatorFormat(denominatorFormatMock);
        
        properFractionFormat.format(fraction, buffer, null);
        
        assertEquals("3 / 4", buffer.toString());
    }
    
    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }
}