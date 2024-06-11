import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertEquals("1 / 2", format.format(new Fraction(1, 2)));
        Assert.assertEquals("3 / 4", format.format(new Fraction(3, 4)));
        Assert.assertEquals("5 / 6", format.format(new Fraction(5, 6)));
        Assert.assertEquals("7 / 8", format.format(new Fraction(7, 8)));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertEquals(new Fraction(1, 2), format.parse("1 / 2"));
        Assert.assertEquals(new Fraction(3, 4), format.parse("3 / 4"));
        Assert.assertEquals(new Fraction(5, 6), format.parse("5 / 6"));
        Assert.assertEquals(new Fraction(7, 8), format.parse("7 / 8"));
    }
    
    @Test
    public void testParse_withWhitespace() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertEquals(new Fraction(1, 2), format.parse("  1 / 2  "));
        Assert.assertEquals(new Fraction(3, 4), format.parse("  3 / 4  "));
        Assert.assertEquals(new Fraction(5, 6), format.parse("  5 / 6  "));
        Assert.assertEquals(new Fraction(7, 8), format.parse("  7 / 8  "));
    }
    
    @Test
    public void testParse_withWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertEquals(new Fraction(1, 2), format.parse("1 1 / 2"));
        Assert.assertEquals(new Fraction(3, 4), format.parse("2 3 / 4"));
        Assert.assertEquals(new Fraction(5, 6), format.parse("3 5 / 6"));
        Assert.assertEquals(new Fraction(7, 8), format.parse("4 7 / 8"));
    }
    
    @Test
    public void testParse_withNegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertNull(format.parse("-1 / 2"));
        Assert.assertNull(format.parse("1 / -2"));
        Assert.assertNull(format.parse("-1 / -2"));
    }
    
    @Test
    public void testParse_withInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertNull(format.parse("1 2"));
        Assert.assertNull(format.parse("1 /"));
        Assert.assertNull(format.parse("1 / 2 / 3"));
        Assert.assertNull(format.parse("1 / abc"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}