import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(format.format(new Fraction(1, 2)), "1 / 2");
        assertEquals(format.format(new Fraction(3, 4)), "3 / 4");
        assertEquals(format.format(new Fraction(5)), "5");
        assertEquals(format.format(new Fraction(-1, 2)), "-1 / 2");
        assertEquals(format.format(new Fraction(-3, 4)), "-3 / 4");
        assertEquals(format.format(new Fraction(-5)), "-5");
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(format.parse("1 / 2").toString(), "1 / 2");
        assertEquals(format.parse("-1 / 2").toString(), "-1 / 2");
        assertEquals(format.parse("3 / 4").toString(), "3 / 4");
        assertEquals(format.parse("-3 / 4").toString(), "-3 / 4");
        assertEquals(format.parse("5 / 1").toString(), "5");
        assertEquals(format.parse("-5 / 1").toString(), "-5");
    }
    
    @Test
    public void testEmpty() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(format.format(new Fraction(0)), "0");
        assertEquals(format.parse("0").toString(), "0");
    }
    
    @Test(expected = ParseException.class)
    public void testInvalidFormat1() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("1 2");
    }
    
    @Test(expected = ParseException.class)
    public void testInvalidFormat2() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("1 /");
    }
    
    @Test(expected = ParseException.class)
    public void testInvalidFormat3() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("1 / 2 / 3");
    }
    
    @Test(expected = ParseException.class)
    public void testInvalidFormat4() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("1 / 2 /");
    }
}