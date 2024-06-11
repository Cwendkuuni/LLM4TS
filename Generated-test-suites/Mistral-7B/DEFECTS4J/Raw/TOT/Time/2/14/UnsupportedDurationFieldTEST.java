import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testUnsupportedGetValue() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1234567L; // arbitrary duration
 try {
 unsupported.getValue(duration);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetValueAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1234567L; // arbitrary duration
 try {
 unsupported.getValueAsLong(duration);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetValueInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1234567L; // arbitrary duration
 long instant = 1234567890L; // arbitrary instant
 try {
 unsupported.getValue(duration, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetValueAsLongInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1234567L; // arbitrary duration
 long instant = 1234567890L; // arbitrary instant
 try {
 unsupported.getValueAsLong(duration, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetMillis() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 int value = 123; // arbitrary value
 try {
 unsupported.getMillis(value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetMillisLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long value = 1234567890L; // arbitrary value
 try {
 unsupported.getMillis(value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetMillisIntLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 int value = 123; // arbitrary value
 long instant = 1234567890L; // arbitrary instant
 try {
 unsupported.getMillis(value, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetMillisLongLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long value = 1234567890L; // arbitrary value
 long instant = 1234567890L; // arbitrary instant
 try {
 unsupported.getMillis(value, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedAdd() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long instant = 1234567890L; // arbitrary instant
 int value = 123; // arbitrary value
 try {
 unsupported.add(instant, value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedAddLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long instant = 1234567890L; // arbitrary instant
 long value = 1234567890L; // arbitrary value
 try {
 unsupported.add(instant, value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetDifference() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 1234567890L; // arbitrary minuend instant
 long subtrahendInstant = 1234567880L; // arbitrary subtrahend instant
 try {
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 1234567890L; // arbitrary minuend instant
 long subtrahendInstant = 1234567880L; // arbitrary subtrahend instant
 try {
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedEquals() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(HOURS);
 assertFalse(unsupported1.equals(unsupported2));
 }

 @Test
 public void testUnsupportedHashCode() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(HOURS);
 assertEquals(unsupported1.hashCode(), unsupported1.hashCode()); // should be the same
 assertNotSame(unsupported1.hashCode(), unsupported2.hashCode());
 assertNotSame(unsupported1.hashCode(), unsupported3.hashCode());
 }

 @Test
 public void testUnsupportedToString() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("UnsupportedDurationField[days]", unsupported.toString());
 }

}