import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetInstance() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Get an instance of UnsupportedDurationField using the getInstance method
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Assert that the returned field is an instance of UnsupportedDurationField
    assertTrue(field instanceof UnsupportedDurationField);
    // Assert that the returned field has the correct type
    assertEquals(type, field.getType());
  }

  @Test
  public void testGetName() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Assert that the name of the field is correct
    assertEquals(type.getName(), field.getName());
  }

  @Test
  public void testIsSupported() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Assert that the field is not supported
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Assert that the field is precise
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getValue method, it should throw an UnsupportedOperationException
    field.getValue(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getValueAsLong method, it should throw an UnsupportedOperationException
    field.getValueAsLong(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue_withInstant() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getValue method with an instant, it should throw an UnsupportedOperationException
    field.getValue(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong_withInstant() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getValueAsLong method with an instant, it should throw an UnsupportedOperationException
    field.getValueAsLong(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_withValue() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getMillis method with a value, it should throw an UnsupportedOperationException
    field.getMillis(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_withLongValue() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getMillis method with a long value, it should throw an UnsupportedOperationException
    field.getMillis(0L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_withValueAndInstant() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getMillis method with a value and instant, it should throw an UnsupportedOperationException
    field.getMillis(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_withLongValueAndInstant() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getMillis method with a long value and instant, it should throw an UnsupportedOperationException
    field.getMillis(0L, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd_withInstantAndValue() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the add method with an instant and value, it should throw an UnsupportedOperationException
    field.add(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd_withInstantAndLongValue() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the add method with an instant and long value, it should throw an UnsupportedOperationException
    field.add(0, 0L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference_withInstant() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getDifference method with two instants, it should throw an UnsupportedOperationException
    field.getDifference(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong_withInstant() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the getDifferenceAsLong method with two instants, it should throw an UnsupportedOperationException
    field.getDifferenceAsLong(0, 0);
  }

  @Test
  public void testGetUnitMillis() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Assert that the unit millis of the field is zero
    assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    // Create another instance of UnsupportedDurationField
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    // Assert that the compareTo method returns zero when comparing the fields
    assertEquals(0, field1.compareTo(field2));
  }

  @Test
  public void testEquals() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    // Create another instance of UnsupportedDurationField
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    // Assert that the fields are equal
    assertEquals(field1, field2);
  }

  @Test
  public void testHashCode() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    // Create another instance of UnsupportedDurationField
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    // Assert that the hash codes of the fields are equal
    assertEquals(field1.hashCode(), field2.hashCode());
  }

  @Test
  public void testToString() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Assert that the toString method returns the correct string representation of the field
    assertEquals("UnsupportedDurationField[" + field.getName() + ']', field.toString());
  }

  @Test
  public void testReadResolve() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    // Serialize and deserialize the field
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(field1);
    }
    UnsupportedDurationField field2 = null;
    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
      field2 = (UnsupportedDurationField) ois.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Assert that the deserialized field is the same as the original
    assertEquals(field1, field2);
  }

  @Test
  public void testUnsupported() {
    // Create an instance of DurationFieldType
    DurationFieldType type = DurationFieldType.millis();
    // Create an instance of UnsupportedDurationField
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    // Call the unsupported method, it should throw an UnsupportedOperationException
    UnsupportedOperationException exception = field.unsupported();
    assertNotNull(exception);
  }

}