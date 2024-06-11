import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.chrono.iso.ISOChronology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class LenientDateTimeFieldTest {

 private DateTimeField field;
 private DateTimeField lenientField;
 private Chronology base;

 @Before
 public void setUp() {
 base = new ISOChronology();
 field = new LocalDateTimeField(DateTimeFieldType.dayOfMonth(), base);
 lenientField = LenientDateTimeField.getInstance(field, base);
 }

 @Test
 public void testInstance() {
 assertEquals(lenientField.getClass(), LenientDateTimeField.class);
 assertNotSame(lenientField, field);
 assertEquals(field.isLenient(), false);
 assertEquals(lenientField.isLenient(), true);
 }

 @Test
 public void testGetInstance_null() {
 assertEquals(LenientDateTimeField.getInstance(null, base), null);
 }

 @Test
 public void testGetInstance_lenient() {
 DateTimeField lenientField = new LenientLocalDateTimeField(DateTimeFieldType.dayOfMonth(), base);
 assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, base));
 }

 @Test
 public void testGetInstance_wrapped() {
 DateTimeField wrappedField = new StrictLocalDateTimeField(DateTimeFieldType.dayOfMonth(), base);
 assertEquals(lenientField, LenientDateTimeField.getInstance(wrappedField, base));
 }

 @Test
 public void testSet_validValue() {
 MutableDateTime dt = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, base);
 assertEquals(1, lenientField.get(dt.getLongMilliseconds()));
 lenientField.set(dt.getLongMilliseconds(), 1);
 assertEquals(1, lenientField.get(dt.getLongMilliseconds()));
 }

 @Test
 public void testSet_outOfBoundsValue_positive() {
 MutableDateTime dt = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, base);
 lenientField.set(dt.getLongMilliseconds(), 32);
 assertEquals(32, lenientField.get(dt.getLongMilliseconds()));
 }

 @Test
 public void testSet_outOfBoundsValue_negative() {
 MutableDateTime dt = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, base);
 lenientField.set(dt.getLongMilliseconds(), -32);
 assertEquals(0, lenientField.get(dt.getLongMilliseconds()));
 }

 @Test
 public void testSet_localDateTime() {
 MutableLocalDateTime ldt = new MutableLocalDateTime(2022, 1, 1, 0, 0, 0, base);
 assertEquals(1, lenientField.get(ldt.toLong()));
 lenientField.set(ldt.toLong(), 32);
 assertEquals(32, lenientField.get(ldt.toLong()));
 }

 @Test
 public void testSet_localDate() {
 MutableDateTime dt = new MutableDateTime(new LocalDate(2022, 1, 1).toDate(), base);
 assertEquals(1, lenientField.get(dt.getLongMilliseconds()));
 lenientField.set(dt.getLongMilliseconds(), 32);
 assertEquals(32, lenientField.get(dt.getLongMilliseconds()));
 }

 @Test
 public void testSet_localTime() {
 MutableDateTime dt = new MutableDateTime(new LocalDateTime(2022, 1, 1, 14, 30, 0, 0, base).toDate(), base);
 assertEquals(14, lenientField.get(dt.getLongMilliseconds()));
 lenientField.set(dt.getLongMilliseconds(), 15);
 assertEquals(15, lenientField.get(dt.getLongMilliseconds()));
 }

 @Test
 public void testSet_invalidChronology() {
 try {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }
}