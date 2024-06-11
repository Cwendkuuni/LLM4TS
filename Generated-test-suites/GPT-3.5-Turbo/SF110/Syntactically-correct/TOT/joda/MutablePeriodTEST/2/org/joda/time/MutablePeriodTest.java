import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import java.util.Locale;

public class MutablePeriodTest {

   @Test
   public void testParse() {
      String periodString = "P1Y2M3W4DT5H6M7.8S";
      
      MutablePeriod expectedPeriod = new MutablePeriod();
      expectedPeriod.setYears(1);
      expectedPeriod.setMonths(2);
      expectedPeriod.setWeeks(3);
      expectedPeriod.setDays(4);
      expectedPeriod.setHours(5);
      expectedPeriod.setMinutes(6);
      expectedPeriod.setSeconds(7);
      expectedPeriod.setMillis(800);
      
      MutablePeriod parsedPeriod = MutablePeriod.parse(periodString);
      
      assertEquals(expectedPeriod, parsedPeriod);
   }

   @Test
   public void testParseWithFormatter() {
      String periodString = "P1Y2M3W4DT5H6M7.8S";
      PeriodFormatter formatter = new PeriodFormatterBuilder().appendYears().appendSuffix("Y") 
                                                             .appendMonths().appendSuffix("M") 
                                                             .appendWeeks().appendSuffix("W")
                                                             .appendDays().appendSuffix("D")
                                                             .appendHours().appendSuffix("H")
                                                             .appendMinutes().appendSuffix("M")
                                                             .appendSecondsWithOptionalMillis().appendSuffix("S")
                                                             .toFormatter(Locale.ENGLISH);
      
      MutablePeriod expectedPeriod = new MutablePeriod();
      expectedPeriod.setYears(1);
      expectedPeriod.setMonths(2);
      expectedPeriod.setWeeks(3);
      expectedPeriod.setDays(4);
      expectedPeriod.setHours(5);
      expectedPeriod.setMinutes(6);
      expectedPeriod.setSeconds(7);
      expectedPeriod.setMillis(800);
      
      MutablePeriod parsedPeriod = MutablePeriod.parse(periodString, formatter);
      
      assertEquals(expectedPeriod, parsedPeriod);
   }

   @Test
   public void testMutablePeriod() {
      MutablePeriod period = new MutablePeriod();
      
      assertEquals(0, period.getYears());
      assertEquals(0, period.getMonths());
      assertEquals(0, period.getWeeks());
      assertEquals(0, period.getDays());
      assertEquals(0, period.getHours());
      assertEquals(0, period.getMinutes());
      assertEquals(0, period.getSeconds());
      assertEquals(0, period.getMillis());
   }
   
   @Test
   public void testMutablePeriodWithType() {
      PeriodType periodType = PeriodType.standard();
      MutablePeriod period = new MutablePeriod(periodType);
      
      assertEquals(0, period.getYears());
      assertEquals(0, period.getMonths());
      assertEquals(0, period.getWeeks());
      assertEquals(0, period.getDays());
      assertEquals(0, period.getHours());
      assertEquals(0, period.getMinutes());
      assertEquals(0, period.getSeconds());
      assertEquals(0, period.getMillis());
      assertEquals(periodType, period.getPeriodType());
   }

   @Test
   public void testMutablePeriodWithIntegers() {
      MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
      
      assertEquals(0, period.getYears());
      assertEquals(0, period.getMonths());
      assertEquals(0, period.getWeeks());
      assertEquals(0, period.getDays());
      assertEquals(1, period.getHours());
      assertEquals(2, period.getMinutes());
      assertEquals(3, period.getSeconds());
      assertEquals(4, period.getMillis());
   }
   
   @Test
   public void testMutablePeriodWithIntegersAndType() {
      PeriodType periodType = PeriodType.standard();
      MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, periodType);
      
      assertEquals(1, period.getYears());
      assertEquals(2, period.getMonths());
      assertEquals(3, period.getWeeks());
      assertEquals(4, period.getDays());
      assertEquals(5, period.getHours());
      assertEquals(6, period.getMinutes());
      assertEquals(7, period.getSeconds());
      assertEquals(8, period.getMillis());
      assertEquals(periodType, period.getPeriodType());
   }

   @Test
   public void testMutablePeriodWithLong() {
      long millis = 123456789;
      MutablePeriod period = new MutablePeriod(millis);
      
      Period expectedPeriod = Period.millis((int)millis);
      MutablePeriod expectedMutablePeriod = expectedPeriod.toMutablePeriod();
      
      assertEquals(expectedMutablePeriod, period);
   }
   
   @Test
   public void testMutablePeriodWithLongAndType() {
      long millis = 123456789;
      PeriodType periodType = PeriodType.standard();
      MutablePeriod period = new MutablePeriod(millis, periodType);
      
      Period expectedPeriod = Period.millis((int)millis);
      MutablePeriod expectedMutablePeriod = expectedPeriod.toMutablePeriod();
      
      assertEquals(expectedMutablePeriod, period);
      assertEquals(periodType, period.getPeriodType());
   }

   @Test
   public void testMutablePeriodWithLongAndChronology() {
      long millis = 123456789;
      Chronology chronology = ISOChronology.getInstance();
      MutablePeriod period = new MutablePeriod(millis, chronology);
      
      Period expectedPeriod = Period.millis((int)millis);
      MutablePeriod expectedMutablePeriod = expectedPeriod.toMutablePeriod();
      
      assertEquals(expectedMutablePeriod, period);
      assertEquals(chronology, period.getChronology());
   }
   
   @Test
   public void testMutablePeriodWithLongTypeChronology() {
      long millis = 123456789;
      PeriodType periodType = PeriodType.standard();
      Chronology chronology = ISOChronology.getInstance();
      MutablePeriod period = new MutablePeriod(millis, periodType, chronology);
      
      Period expectedPeriod = Period.millis((int)millis);
      MutablePeriod expectedMutablePeriod = expectedPeriod.toMutablePeriod();
      
      assertEquals(expectedMutablePeriod, period);
      assertEquals(periodType, period.getPeriodType());
      assertEquals(chronology, period.getChronology());
   }
   
   // ... additional test cases for other methods ...

}