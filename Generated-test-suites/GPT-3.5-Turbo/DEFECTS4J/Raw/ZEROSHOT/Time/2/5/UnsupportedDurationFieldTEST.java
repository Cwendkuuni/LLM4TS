import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class UnsupportedDurationFieldTest {

   private UnsupportedDurationField field;

   @Before
   public void setUp() {
      field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
   }

   @Test
   public void testGetType() {
      assertEquals(DurationFieldType.YEARS, field.getType());
   }

   @Test
   public void testGetName() {
      assertEquals("Years", field.getName());
   }

   @Test
   public void testIsSupported() {
      assertFalse(field.isSupported());
   }

   @Test
   public void testIsPrecise() {
      assertTrue(field.isPrecise());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetValue() {
      field.getValue(1000L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetValueAsLong() {
      field.getValueAsLong(1000L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetValueWithInstant() {
      field.getValue(1000L, 123456L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetValueAsLongWithInstant() {
      field.getValueAsLong(1000L, 123456L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetMillisWithIntValue() {
      field.getMillis(5);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetMillisWithLongValue() {
      field.getMillis(123456L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetMillisWithIntValueAndInstant() {
      field.getMillis(5, 123456L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetMillisWithLongValueAndInstant() {
      field.getMillis(123456L, 789012L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testAddWithIntValue() {
      field.add(123456L, 5);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testAddWithLongValue() {
      field.add(123456L, 789012L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetDifference() {
      field.getDifference(987654L, 123456L);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testGetDifferenceAsLong() {
      field.getDifferenceAsLong(987654L, 123456L);
   }

   @Test
   public void testGetUnitMillis() {
      assertEquals(0L, field.getUnitMillis());
   }

   @Test
   public void testCompareTo() {
      UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
      assertEquals(0, field.compareTo(otherField));
   }

   @Test
   public void testEquals() {
      UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
      assertTrue(field.equals(otherField));
   }

   @Test
   public void testHashCode() {
      UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
      assertEquals(field.hashCode(), otherField.hashCode());
   }

   @Test
   public void testToString() {
      assertEquals("UnsupportedDurationField[Years]", field.toString());
   }

}