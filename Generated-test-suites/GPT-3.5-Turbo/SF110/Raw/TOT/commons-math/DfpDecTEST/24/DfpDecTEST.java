import org.apache.commons.math3.dfp.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testConstructorDfpField() {
        DfpField field = new DfpField(10);
        DfpDec instance = new DfpDec(field);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfpFieldByte() {
        DfpField field = new DfpField(10);
        byte x = 5;
        DfpDec instance = new DfpDec(field, x);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfpFieldInt() {
        DfpField field = new DfpField(10);
        int x = 10;
        DfpDec instance = new DfpDec(field, x);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfpFieldLong() {
        DfpField field = new DfpField(10);
        long x = 100;
        DfpDec instance = new DfpDec(field, x);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfpFieldDouble() {
        DfpField field = new DfpField(10);
        double x = 2.5;
        DfpDec instance = new DfpDec(field, x);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfp() {
        Dfp d = new Dfp(10);
        DfpDec instance = new DfpDec(d);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfpFieldString() {
        DfpField field = new DfpField(10);
        String s = "3.14";
        DfpDec instance = new DfpDec(field, s);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorDfpFieldByteByte() {
        DfpField field = new DfpField(10);
        byte sign = 1;
        byte nans = 1;
        DfpDec instance = new DfpDec(field, sign, nans);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(10);
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance();
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceByte() {
        DfpField field = new DfpField(10);
        byte x = 5;
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(x);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceInt() {
        DfpField field = new DfpField(10);
        int x = 10;
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(x);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceLong() {
        DfpField field = new DfpField(10);
        long x = 100;
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(x);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceDouble() {
        DfpField field = new DfpField(10);
        double x = 2.5;
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(x);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpField field = new DfpField(10);
        Dfp d = new Dfp(field);
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(d);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceString() {
        DfpField field = new DfpField(10);
        String s = "3.14";
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(s);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpField field = new DfpField(10);
        byte sign = 1;
        byte nans = 1;
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.newInstance(sign, nans);
        assertNotNull(result);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec instance = new DfpDec(field);
        int expResult = 7;
        int result = instance.getDecimalDigits();
        assertEquals(expResult, result);
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec instance = new DfpDec(field);
        int in = 0;
        int expResult = 0;
        int result = instance.round(in);
        assertEquals(expResult, result);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        Dfp x = new Dfp(field);
        DfpDec instance = new DfpDec(field);
        Dfp result = instance.nextAfter(x);
        assertNotNull(result);
    }

}