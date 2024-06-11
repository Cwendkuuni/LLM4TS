package org.joda.time.field.test;

import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfYear();
 private static final DurationFieldType HOURS = DurationFieldType.hourOfDay();
 private static final DurationFieldType MINUTES = DurationFieldType.minute();
 private static final DurationFieldType SECONDS = DurationFieldType.secondOfMinute();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(DAYS, field.getType());
 }

 @Test
 public void testGetInstanceTwice() {
 DurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 DurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(field1, field2);
 }

 @Test
 public void testGetInstanceNullType() {
 assertThrows(NullPointerException.class, () -> UnsupportedDurationField.getInstance(null));
 }

 @Test
 public void testIsSupported() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertFalse(field.isSupported());
 }

 @Test
 public void testIsPrecise() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertTrue(field.isPrecise());
 }

 @Test
 public void testGetValue() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.getValue(0L));
 }

 @Test
 public void testGetValueAsLong() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(0L));
 }

 @Test
 public void testGetValueWithInstant() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.getValue(0L, 0L));
 }

 @Test
 public void testGetValueAsLongWithInstant() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(0L, 0L));
 }

 @Test
 public void testGetMillis() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0));
 }

 @Test
 public void testGetMillisWithInstant() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0, 0L));
 }

 @Test
 public void testAdd() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.add(0L, 1));
 }

 @Test
 public void testAddWithInstant() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.add(0L, 1, 0L));
 }

 @Test
 public void testGetDifference() {
 DurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 DurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field1.getDifference(0L, 0L));
 }

 @Test
 public void testGetDifferenceAsLong() {
 DurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 DurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field1.getDifferenceAsLong(0L, 0L));
 }

 @Test
 public void testGetUnitMillis() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(0, field.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 DurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 DurationField field2 = UnsupportedDurationField.getInstance(HOURS);
 assertEquals(1, field2.compareTo(field1));
 }

 @Test
 public void testEquals() {
 DurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 DurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertTrue(field1.equals(field2));
 }

 @Test
 public void testHashCode() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(DAYS.hashCode(), field.hashCode());
 }

 @Test
 public void testToString() {
 DurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("UnsupportedDurationField[dayOfYear]", field.toString());
 }
}