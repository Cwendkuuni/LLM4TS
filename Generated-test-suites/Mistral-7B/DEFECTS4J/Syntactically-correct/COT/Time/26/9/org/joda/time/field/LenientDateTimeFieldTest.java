import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology CHRONOLOGY = ZoneUtils.getDefault();
 private static final DateTimeField STRICT_FIELD = new LocalDateTimeField(DateTimeFieldType.dayOfYear(), CHRONOLOGY);

 @Test
 public void testGetInstance() {
 LenientDateTimeField lenientField = LenientDateTimeField.getInstance(STRICT_FIELD, CHRONOLOGY);
 Assert.assertNotNull(lenientField);
 Assert.assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet_ValidValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 MutableDateTime mutableDateTime = new MutableDateTime(1L, CHRONOLOGY);
 long result = lenientField.set(mutableDateTime.getLong(), 1);
 assertEquals(mutableDateTime.getLong(), result);
 }

 @Test
 public void testSet_OutOfBoundsValue_SameChronology() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 MutableDateTime mutableDateTime = new MutableDateTime(1L, CHRONOLOGY);
 long result = lenientField.set(mutableDateTime.getLong(), Integer.MIN_VALUE);
 assertTrue(result > mutableDateTime.getLong());
 }

 @Test
 public void testSet_OutOfBoundsValue_DifferentChronology() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 MutableDateTime mutableDateTime = new MutableDateTime(1L, CHRONOLOGY);
 long result = lenientField.set(mutableDateTime.getLong(), Integer.MIN_VALUE);
 MutableDateTime mutableDateTime2 = new MutableDateTime(result, CHRONOLOGY);
 assertEquals(mutableDateTime2.getLong(), result);
 }

 @Test
 public void testSet_SameInstant_SameValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 MutableDateTime mutableDateTime = new MutableDateTime(1L, CHRONOLOGY);
 long result = lenientField.set(mutableDateTime.getLong(), get(mutableDateTime.getLong()));
 assertEquals(mutableDateTime.getLong(), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullInstant() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 lenientField.set(null, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 lenientField.set(1L, null);
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, CHRONOLOGY);
 assertTrue(lenientField.isLenient());
 }
}