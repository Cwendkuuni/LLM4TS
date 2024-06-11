import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod p = MutablePeriod.parse("P1Y2M3D");
        assertNotNull(p);
        assertEquals(1, p.getYears());
        assertEquals(2, p.getMonths());
        assertEquals(3, p.getDays());
    }

    @Test
    public void testParseWithFormatter() {
        PeriodFormatter formatter = PeriodFormat.wordBased();
        MutablePeriod p = MutablePeriod.parse("one year, one month, one day", formatter);
        assertNotNull(p);
        assertEquals(1, p.getYears());
        assertEquals(1, p.getMonths());
        assertEquals(1, p.getDays());
    }

    @Test
    public void testDefaultConstructor() {
        MutablePeriod p = new MutablePeriod();
        assertNotNull(p);
        assertEquals(0, p.getYears());
        assertEquals(0, p.getMonths());
        assertEquals(0, p.getDays());
        assertEquals(0, p.getHours());
        assertEquals(0, p.getMinutes());
        assertEquals(0, p.getSeconds());
        assertEquals(0, p.getMillis());
    }

    @Test
    public void testPeriodTypeConstructor() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod p = new MutablePeriod(periodType);
        assertNotNull(p);
        assertEquals(periodType, p.getPeriodType());
    }

    @Test
    public void testIntConstructor() {
        int n = 1;
        int n2 = 2;
        int n3 = 3;
        int n4 = 4;
        MutablePeriod p = new MutablePeriod(n, n2, n3, n4);
        assertNotNull(p);
        assertEquals(n, p.getYears());
        assertEquals(n2, p.getMonths());
        assertEquals(n3, p.getDays());
        assertEquals(n4, p.getHours());
    }
    
    // ... continue with testing other methods in the class ...
    
}