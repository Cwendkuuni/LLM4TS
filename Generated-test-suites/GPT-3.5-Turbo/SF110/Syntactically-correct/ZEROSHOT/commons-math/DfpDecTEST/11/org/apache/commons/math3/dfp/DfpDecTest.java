import org.junit.*;
import static org.junit.Assert.*;

public class DfpDecTest {

    private DfpDec instance;

    @Before
    public void setUp() {
        instance = new DfpDec(new DfpField());
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testConstructor1() {
        assertNotNull(instance);
    }

    @Test
    public void testConstructor2() {
        DfpDec result = new DfpDec(new DfpField(), (byte) 0);
        assertNotNull(result);
    }

    @Test
    public void testConstructor3() {
        DfpDec result = new DfpDec(new DfpField(), 0);
        assertNotNull(result);
    }

    @Test
    public void testConstructor4() {
        DfpDec result = new DfpDec(new DfpField(), 0L);
        assertNotNull(result);
    }

    @Test
    public void testConstructor5() {
        DfpDec result = new DfpDec(new DfpField(), 0.0);
        assertNotNull(result);
    }

    @Test
    public void testConstructor6() {
        Dfp result = new DfpDec(new DfpField());
        assertNotNull(result);
    }

    @Test
    public void testConstructor7() {
        DfpDec result = new DfpDec(new DfpField(), "0");
        assertNotNull(result);
    }

    @Test
    public void testConstructor8() {
        DfpDec result = new DfpDec(new DfpField(), (byte) 0, (byte) 0);
        assertNotNull(result);
    }

    @Test
    public void testNewInstance1() {
        Dfp result = instance.newInstance();
        assertNotNull(result);
    }

    @Test
    public void testNewInstance2() {
        Dfp result = instance.newInstance((byte) 0);
        assertNotNull(result);
    }

    @Test
    public void testNewInstance3() {
        Dfp result = instance.newInstance(0);
        assertNotNull(result);
    }

    @Test
    public void testNewInstance4() {
        Dfp result = instance.newInstance(0L);
        assertNotNull(result);
    }

    @Test
    public void testNewInstance5() {
        Dfp result = instance.newInstance(0.0);
        assertNotNull(result);
    }

    @Test
    public void testNewInstance6() {
        Dfp result = instance.newInstance(new Dfp());
        assertNotNull(result);
    }

    @Test
    public void testNewInstance7() {
        Dfp result = instance.newInstance("0");
        assertNotNull(result);
    }

    @Test
    public void testNewInstance8() {
        Dfp result = instance.newInstance((byte) 0, (byte) 0);
        assertNotNull(result);
    }

    @Test
    public void testGetDecimalDigits() {
        int result = instance.getDecimalDigits();
        assertEquals(0, result);
    }

    @Test
    public void testRound() {
        int result = instance.round(0);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        Dfp result = instance.nextAfter(new Dfp());
        assertNotNull(result);
    }
}