import static org.junit.Assert.*;
import org.junit.Test;

public class FastDateFormatTest {
  
  @Test
  public void testGetInstance() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceWithPattern() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceWithPatternAndTimeZone() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testGetInstanceWithPatternAndLocale() {
    FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testDateInstance() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
    assertNotNull(instance);
  }
  
  @Test
  public void testDateInstanceWithLocale() {
    FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
    assertNotNull(instance);
  }
  
  @Test
  public void testTimeInstance() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
    assertNotNull(instance);
  }
  
  @Test
  public void testTimeInstanceWithTimeZone() {
    FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testDateTimeInstance() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT);
    assertNotNull(instance);
  }
  
  @Test
  public void testDateTimeInstanceWithTimeZone() {
    FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
    assertNotNull(instance);
  }
  
  @Test
  public void testFormatLong() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotNull(instance.format(System.currentTimeMillis()));
  }
  
  @Test
  public void testParse() throws ParseException {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotNull(instance.parse("2021-12-25"));
  }
  
  @Test
  public void testParseWithParsePosition() {
    FastDateFormat instance = FastDateFormat.getInstance();
    assertNotNull(instance.parse("2021-12-25", new ParsePosition(0)));
  }
  
  @Test
  public void testEquals() {
    FastDateFormat instance1 = FastDateFormat.getInstance();
    FastDateFormat instance2 = FastDateFormat.getInstance();
    assertEquals(instance1, instance2);
  }
  
}