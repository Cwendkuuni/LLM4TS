import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private DateTimeField field;
 private Chronology chronology;
 private BaseDateTime baseDateTime;

 @Before
 public void setUp() {
 field = LenientDateTimeField.getInstance(new DateTimeFieldType.integerInstance("TestField"), ZoneUtils.UTC);
 chronology = field.getChronology();
 baseDateTime = new BaseDateTime(System.currentTimeMillis(), chronology);
 }

 // Test the static getInstance method
 @Test
 public void testGetInstance() {
 DateTimeField field1 = LenientDateTimeField.getInstance(null, null);
 DateTimeField field2 = LenientDateTimeField.getInstance(new DateTimeFieldType.integerInstance("TestField"), null);
 DateTimeField field3 = LenientDateTimeField.getInstance(null, ZoneUtils.UTC);
 assertTrue(field1 == field2);
 assertTrue(field1 instanceof LenientDateTimeField);
 assertNull(field1.getChronology());
 assertEquals(DateTimeFieldType.integerInstance("TestField"), field2.getType());
 assertNotNull(field2.getChronology());
 assertFalse(field1.isLenient());
 assertFalse(field2.isLenient());
 assertTrue(field3 instanceof LenientDateTimeField);
 assertNotNull(field3.getChronology());
 assertEquals(DateTimeFieldType.integerInstance("TestField"), field3.getType());
 }

 // Test the constructor
 @Test
 public void testConstructor() {
 DateTimeField field1 = new LenientDateTimeField(new DateTimeFieldType.integerInstance("TestField"), ZoneUtils.UTC);
 assertTrue(field1 instanceof LenientDateTimeField);
 assertNotNull(field1.getChronology());
 assertEquals(DateTimeFieldType.integerInstance("TestField"), field1.getType());
 }

 // Test the isLenient method
 @Test
 public void testIsLenient() {
 assertTrue(field.isLenient());
 }

 // Test the set method with valid values
 @Test
 public void testSetValidValues() {
 long instant = baseDateTime.getMillis();
 MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);
 int value = get(instant) + 1;
 long newInstant = field.set(instant, value);
 assertEquals(newInstant, mutableDateTime.add(1, field.getType()).getMillis());
 }

 // Test the set method with out of bounds value
 @Test(expected = IllegalArgumentException.class)
 public void testSetOutOfBoundsValue() {
 long instant = baseDateTime.getMillis();
 MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);
 int value = get(instant) - 1;
 field.set(instant, value);
 fail("Expected IllegalArgumentException");
 }

 // Test the set method with Chronology
 @Test
 public void testSetWithChronology() {
 long instant = baseDateTime.getMillis();
 MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);
 LocalDateTime localDateTime = new LocalDateTime(chronology);
 int value = get(instant) + 1;
 long newInstant = field.set(instant, value);
 assertEquals(newInstant, localDateTime.plus(FieldUtils.safeGet(field, 1)).toDateTimeAtOffset(0, 0).getMillis());
 }

 // Test the set method with null instant
 @Test(expected = NullPointerException.class)
 public void testSetWithNullInstant() {
 field.set(null, 0);
 fail("Expected NullPointerException");
 }

 // Test the set method with null value
 @Test(expected = NullPointerException.class)
 public void testSetWithNullValue() {
 field.set(baseDateTime.getMillis(), null);
 fail("Expected NullPointerException");
 }
}