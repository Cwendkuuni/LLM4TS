import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testInstance() {
        DfpDec dfp = new DfpDec(10);
        assertEquals(10, dfp.newInstance().toDouble(), 0.0);
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(3.14159);
        assertEquals(3.14, dfp.round(2).toDouble(), 0.01);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(10);
        DfpDec dfp2 = dfp1.nextAfter(new DfpDec(5));
        assertEquals(6, dfp2.toDouble(), 0.0);
    }

}