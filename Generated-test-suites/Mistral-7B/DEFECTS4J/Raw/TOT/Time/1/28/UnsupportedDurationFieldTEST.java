import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertEquals(DurationFieldType.MILLISECONDS, unsupportedField.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1000L;
 try {
 unsupportedField.getValue(duration);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1000L;
 try {
 unsupportedField.getValueAsLong(duration);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1000L;
 long instant = 123456L;
 try {
 unsupportedField.getValue(duration, instant);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1000L;
 long instant = 123456L;
 try {
 unsupportedField.getValueAsLong(duration, instant);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 int value = 1000;
 try {
 unsupportedField.getMillis(value);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long value = 1000L;
 try {
 unsupportedField.getMillis(value);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 int value = 1000;
 long instant = 123456L;
 try {
 unsupportedField.getMillis(value, instant);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValueAndInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long value = 1000L;
 long instant = 123456L;
 try {
 unsupportedField.getMillis(value, instant);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long instant = 123456L;
 int value = 1000;
 try {
 unsupportedField.add(instant, value);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long instant = 123456L;
 long value = 1000L;
 try {
 unsupportedField.add(instant, value);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long minuendInstant = 123456L;
 long subtrahendInstant = 123455L;
 try {
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long minuendInstant = 123456L;
 long subtrahendInstant = 123455L;
 try {
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertEquals(0, unsupportedField.getUnitMillis());
 }
}