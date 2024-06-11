import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructorWithDfpField() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertEquals(new DfpDec(new DfpField()), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfpFieldAndByte() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1);
        assertEquals(new DfpDec(new DfpField(), (byte) 1), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfpFieldAndInt() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1);
        assertEquals(new DfpDec(new DfpField(), 1), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfpFieldAndLong() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1L);
        assertEquals(new DfpDec(new DfpField(), 1L), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfpFieldAndDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1.0);
        assertEquals(new DfpDec(new DfpField(), 1.0), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfp() {
        DfpDec dfpDec = new DfpDec(new Dfp(1.0));
        assertEquals(new DfpDec(new Dfp(1.0)), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfpFieldAndString() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "1.0");
        assertEquals(new DfpDec(new DfpField(), "1.0"), dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfpFieldByteByte() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1, (byte) 2);
        assertEquals(new DfpDec(new DfpField(), (byte) 1, (byte) 2), dfpDec);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance();
        assertEquals(new DfpDec(new DfpField()), dfpDec);
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance((byte) 1);
        assertEquals(new DfpDec(new DfpField(), (byte) 1), dfpDec);
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1);
        assertEquals(new DfpDec(new DfpField(), 1), dfpDec);
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1L);
        assertEquals(new DfpDec(new DfpField(), 1L), dfpDec);
    }

    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1.0);
        assertEquals(new DfpDec(new DfpField(), 1.0), dfpDec);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(new Dfp(1.0));
        assertEquals(new DfpDec(new Dfp(1.0)), dfpDec);
    }

    @Test
    public void testNewInstanceWithString() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance("1.0");
        assertEquals(new DfpDec(new DfpField(), "1.0"), dfpDec);
    }

    @Test
    public void testNewInstanceWithByteByte() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 2);
        assertEquals(new DfpDec(new DfpField(), (byte) 1, (byte) 2), dfpDec);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertEquals(dfpDec.getRadixDigits() * 4 - 3, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertEquals(0, dfpDec.round(0));
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertEquals(new DfpDec(new DfpField()), dfpDec.nextAfter(new DfpDec(new DfpField())));
    }
}