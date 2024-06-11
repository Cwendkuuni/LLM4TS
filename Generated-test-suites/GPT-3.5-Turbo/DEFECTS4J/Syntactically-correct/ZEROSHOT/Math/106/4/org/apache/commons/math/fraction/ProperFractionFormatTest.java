import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
  
    private ProperFractionFormat formatter;

    @Before
    public void setUp() {
        formatter = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        assertEquals("1 / 2", formatter.format(new Fraction(1, 2)));
        assertEquals("3 / 4", formatter.format(new Fraction(3, 4)));
        assertEquals("5 1/4", formatter.format(new Fraction(21, 4)));
    }

    @Test
    public void testParse() {
        Fraction fraction = formatter.parse("1 / 2");
        assertEquals(new Fraction(1, 2), fraction);

        fraction = formatter.parse("3 / 4");
        assertEquals(new Fraction(3, 4), fraction);

        fraction = formatter.parse("5 1/4");
        assertEquals(new Fraction(21, 4), fraction);
    }

    @Test
    public void testParseInvalidInput() {
        Fraction fraction = formatter.parse("1/2/3");
        assertNull(fraction);

        fraction = formatter.parse("1/2/3");
        assertNull(fraction);
    }
}