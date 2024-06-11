import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

   BasicChronology chronology = new BasicChronology();
   BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);

   @Test
   public void testIsLenient() {
      assertFalse(field.isLenient());
   }

   @Test
   public void testGet() {
      assertEquals(6, field.get(1457890400000L)); // typical use case
      assertEquals(1, field.get(145027200000L)); // edge case, minimum value
      assertEquals(12, field.get(1483228799999L)); // edge case, maximum value
   }

   @Test
   public void testAddInt() {
      assertEquals(1457890400000L, field.add(1457890400000L, 0)); // edge case, zero
      assertEquals(1457976800000L, field.add(1457890400000L, 1)); // typical use case, positive value
      assertEquals(1457800800000L, field.add(1457890400000L, -1)); // typical use case, negative value
   }

   @Test
   public void testAddLong() {
      assertEquals(1457890400000L, field.add(1457890400000L, 0L)); // edge case, zero
      assertEquals(1457976800000L, field.add(1457890400000L, 1L)); // typical use case, positive value
      assertEquals(1457800800000L, field.add(1457890400000L, -1L)); // typical use case, negative value
   }

   @Test
   public void testAddReadablePartial() {
      int[] partial = {1, 1, 2016};
      int[] expected = {1, 2, 2016};
      assertArrayEquals(expected, field.add(new MockReadablePartial(partial), 1, partial, 0)); // typical use case, valid fields

      int[] partial2 = {1, 1, 2016};
      int[] expected2 = {1, 1, 2017};
      assertArrayEquals(expected2, field.add(new MockReadablePartial(partial2), 0, partial2, 12)); // typical use case, valid fields

      int[] partial3 = {1, 1, 2016};
      assertArrayEquals(partial3, field.add(new MockReadablePartial(partial3), 0, partial3, 0)); // edge case, zero
   }

   @Test
   public void testAddWrapField() {
      assertEquals(1457976800000L, field.addWrapField(1457890400000L, 1)); // typical use case, positive value
      assertEquals(1457800800000L, field.addWrapField(1457890400000L, -1)); // typical use case, negative value
   }

   @Test
   public void testGetDifferenceAsLong() {
      assertEquals(11, field.getDifferenceAsLong(1457890400000L, 1457800800000L)); // typical use case
      assertEquals(-11, field.getDifferenceAsLong(1457800800000L, 1457890400000L)); // typical use case, negative difference
   }

   @Test
   public void testSet() {
      assertEquals(1459607999999L, field.set(1457890400000L, 12)); // typical use case, maximum value
      assertEquals(1458086399999L, field.set(1457890400000L, 2)); // typical use case, minimum value
   }

   @Test
   public void testGetRangeDurationField() {
      assertEquals(chronology.years(), field.getRangeDurationField());
   }

   @Test
   public void testIsLeap() {
      assertTrue(field.isLeap(1457890400000L)); // typical use case, leap year
      assertFalse(field.isLeap(1457983200000L)); // typical use case, non-leap year
   }

   @Test
   public void testGetLeapAmount() {
      assertEquals(1, field.getLeapAmount(1457890400000L)); // typical use case, leap year
      assertEquals(0, field.getLeapAmount(1457983200000L)); // typical use case, non-leap year
   }

   @Test
   public void testGetLeapDurationField() {
      assertEquals(chronology.days(), field.getLeapDurationField());
   }

   @Test
   public void testGetMinimumValue() {
      assertEquals(1, field.getMinimumValue()); // edge case, minimum value
   }

   @Test
   public void testGetMaximumValue() {
      assertEquals(12, field.getMaximumValue()); // edge case, maximum value
   }

   @Test
   public void testRoundFloor() {
      assertEquals(1451628000000L, field.roundFloor(1457890400000L)); // typical use case
      assertEquals(1456792800000L, field.roundFloor(1457983200000L)); // typical use case
   }

   @Test
   public void testRemainder() {
      assertEquals(626400000L, field.remainder(1457890400000L)); // typical use case
   }

   @Test
   public void testReadResolve() {
      assertEquals(chronology.monthOfYear(), field.readResolve()); // typical use case
   }
}

class MockReadablePartial implements ReadablePartial {

   private int[] values;

   public MockReadablePartial(int[] values) {
      this.values = values;
   }

   @Override
   public int size() {
      return values.length;
   }

   @Override
   public DateTimeFieldType getFieldType(int index) {
      switch(index) {
         case 0:
            return DateTimeFieldType.monthOfYear();
         case 1:
            return DateTimeFieldType.dayOfMonth();
         case 2:
            return DateTimeFieldType.year();
         default:
            return null;
      }
   }

   @Override
   public int getValue(int index) {
      return values[index];
   }

   @Override
   public int get(DateTimeFieldType fieldType) {
      return values[fieldType.ordinal()];
   }

   @Override
   public boolean isSupported(DateTimeFieldType fieldType) {
      return true;
   }

   @Override
   public DateTimeField[] getFields() {
      return null;
   }

   @Override
   public int[] getValues() {
      return values;
   }

   @Override
   public boolean equals(Object readablePartial) {
      return false;
   }

   @Override
   public int hashCode() {
      return 0;
   }
}