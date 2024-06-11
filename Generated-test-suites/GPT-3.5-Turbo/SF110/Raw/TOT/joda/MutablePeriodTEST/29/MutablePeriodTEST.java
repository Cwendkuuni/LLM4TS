import org.joda.time.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MutablePeriodTest {

   private MutablePeriod mutablePeriod;

   @Before
   public void setUp() {
      mutablePeriod = new MutablePeriod();
   }

   @Test
   public void testParse() {
      MutablePeriod parsedPeriod = MutablePeriod.parse("P2Y4M6D");
      assertEquals(2, parsedPeriod.getYears());
      assertEquals(4, parsedPeriod.getMonths());
      assertEquals(6, parsedPeriod.getDays());
   }

   @Test
   public void testParse_withPeriodFormatter() {
      PeriodFormatter periodFormatter = ISOPeriodFormat.alternateExtended();
      MutablePeriod parsedPeriod = MutablePeriod.parse("P3Y2M5D", periodFormatter);
      assertEquals(3, parsedPeriod.getYears());
      assertEquals(2, parsedPeriod.getMonths());
      assertEquals(5, parsedPeriod.getDays());
   }

   @Test
   public void testConstructor_noArgs() {
      assertEquals(0, mutablePeriod.getYears());
      assertEquals(0, mutablePeriod.getMonths());
      assertEquals(0, mutablePeriod.getWeeks());
      assertEquals(0, mutablePeriod.getDays());
      assertEquals(0, mutablePeriod.getHours());
      assertEquals(0, mutablePeriod.getMinutes());
      assertEquals(0, mutablePeriod.getSeconds());
      assertEquals(0, mutablePeriod.getMillis());
   }

   @Test
   public void testConstructor_withPeriodType() {
      PeriodType periodType = PeriodType.forFields(new DurationFieldType[] {DurationFieldType.years(), DurationFieldType.months()});
      mutablePeriod = new MutablePeriod(periodType);
      assertEquals(0, mutablePeriod.getYears());
      assertEquals(0, mutablePeriod.getMonths());
      assertEquals(0, mutablePeriod.getWeeks());
      assertEquals(0, mutablePeriod.getDays());
      assertEquals(0, mutablePeriod.getHours());
      assertEquals(0, mutablePeriod.getMinutes());
      assertEquals(0, mutablePeriod.getSeconds());
      assertEquals(0, mutablePeriod.getMillis());
   }

   @Test
   public void testConstructor_withIntParams() {
      mutablePeriod = new MutablePeriod(1, 2, 3, 4);
      assertEquals(0, mutablePeriod.getYears());
      assertEquals(0, mutablePeriod.getMonths());
      assertEquals(0, mutablePeriod.getWeeks());
      assertEquals(0, mutablePeriod.getDays());
      assertEquals(1, mutablePeriod.getHours());
      assertEquals(2, mutablePeriod.getMinutes());
      assertEquals(3, mutablePeriod.getSeconds());
      assertEquals(4, mutablePeriod.getMillis());
   }

   @Test
   public void testConstructor_withLongParam() {
      mutablePeriod = new MutablePeriod(150L);
      assertEquals(0, mutablePeriod.getYears());
      assertEquals(0, mutablePeriod.getMonths());
      assertEquals(0, mutablePeriod.getWeeks());
      assertEquals(0, mutablePeriod.getDays());
      assertEquals(0, mutablePeriod.getHours());
      assertEquals(0, mutablePeriod.getMinutes());
      assertEquals(0, mutablePeriod.getSeconds());
      assertEquals(150, mutablePeriod.getMillis());
   }

   @Test
   public void testConstructor_withInstantParams() {
      ReadableInstant instant1 = new DateTime("2022-01-01T00:00:00");
      ReadableInstant instant2 = new DateTime("2022-01-02T00:00:00");
      mutablePeriod = new MutablePeriod(instant1, instant2);
      assertEquals(0, mutablePeriod.getYears());
      assertEquals(0, mutablePeriod.getMonths());
      assertEquals(0, mutablePeriod.getWeeks());
      assertEquals(1, mutablePeriod.getDays());
      assertEquals(0, mutablePeriod.getHours());
      assertEquals(0, mutablePeriod.getMinutes());
      assertEquals(0, mutablePeriod.getSeconds());
      assertEquals(0, mutablePeriod.getMillis());
   }

   // More test methods for the remaining methods...

}