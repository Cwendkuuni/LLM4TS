import static org.junit.Assert.*;
import org.junit.*;

public class DfpDecTest {

  private DfpDec dfp;

  @Before
  public void setUp() {
    dfp = new DfpDec(new DfpField());
  }

  @Test
  public void testDfpDec() {
    assertNotNull(dfp);
  }

  @Test
  public void testDfpDecWithFactory() {
    DfpField factory = new DfpField();
    DfpDec dfp1 = new DfpDec(factory);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithFactoryAndByte() {
    DfpField factory = new DfpField();
    byte x = 5;
    DfpDec dfp1 = new DfpDec(factory, x);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithFactoryAndInt() {
    DfpField factory = new DfpField();
    int x = 10;
    DfpDec dfp1 = new DfpDec(factory, x);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithFactoryAndLong() {
    DfpField factory = new DfpField();
    long x = 100;
    DfpDec dfp1 = new DfpDec(factory, x);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithFactoryAndDouble() {
    DfpField factory = new DfpField();
    double x = 3.14;
    DfpDec dfp1 = new DfpDec(factory, x);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithDfp() {
    Dfp d = new Dfp(new DfpField());
    DfpDec dfp1 = new DfpDec(d);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithFactoryAndString() {
    DfpField factory = new DfpField();
    String s = "12345";
    DfpDec dfp1 = new DfpDec(factory, s);
    assertNotNull(dfp1);
  }

  @Test
  public void testDfpDecWithFactoryAndByteByte() {
    DfpField factory = new DfpField();
    byte sign = -1;
    byte nans = 2;
    DfpDec dfp1 = new DfpDec(factory, sign, nans);
    assertNotNull(dfp1);
  }

  @Test
  public void testNewInstance() {
    DfpDec newInstance = dfp.newInstance();
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithByte() {
    byte x = 5;
    DfpDec newInstance = dfp.newInstance(x);
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithInt() {
    int x = 10;
    DfpDec newInstance = dfp.newInstance(x);
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithLong() {
    long x = 100;
    DfpDec newInstance = dfp.newInstance(x);
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithDouble() {
    double x = 3.14;
    DfpDec newInstance = dfp.newInstance(x);
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithDfp() {
    Dfp d = new Dfp(new DfpField());
    DfpDec newInstance = dfp.newInstance(d);
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithString() {
    String s = "12345";
    DfpDec newInstance = dfp.newInstance(s);
    assertNotNull(newInstance);
  }

  @Test
  public void testNewInstanceWithByteByte() {
    byte sign = -1;
    byte nans = 2;
    DfpDec newInstance = dfp.newInstance(sign, nans);
    assertNotNull(newInstance);
  }

  @Test
  public void testGetDecimalDigits() {
    int result = dfp.getDecimalDigits();
    assertNotNull(result);
  }

  @Test
  public void testRound() {
    int in = 10;
    int result = dfp.round(in);
    assertNotNull(result);
  }

  @Test
  public void testNextAfter() {
    Dfp x = new Dfp(new DfpField());
    DfpDec result = dfp.nextAfter(x);
    assertNotNull(result);
  }

}