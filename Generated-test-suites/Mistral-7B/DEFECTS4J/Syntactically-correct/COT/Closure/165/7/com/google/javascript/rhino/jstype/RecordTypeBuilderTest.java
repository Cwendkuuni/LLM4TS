import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

import org.junit.Test;

import java.util.HashMap;

/**
 * Tests for the RecordTypeBuilder class.
 */
public class RecordTypeBuilderTest {

  @Test
  public void testEmptyRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
  }

  @Test
  public void testRecordTypeBuilderWithProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "testProperty";
    JSType propertyType = JSType.STRING;
    Node propertyNode = new Node(); // Assuming a valid Node representation
    RecordTypeBuilder returnedBuilder = builder.addProperty(propertyName, propertyType, propertyNode);
    assertTrue(returnedBuilder != null);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    RecordType record = (RecordType) recordType;
    RecordProperty property = record.getProperty(propertyName);
    assertEquals(propertyName, property.getName());
    assertEquals(propertyType, property.getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeBuilderWithDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "testProperty";
    JSType propertyType = JSType.STRING;
    Node propertyNode = new Node(); // Assuming a valid Node representation
    builder.addProperty(propertyName, propertyType, propertyNode);
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      builder.addProperty(propertyName, propertyType, propertyNode);
    });
    assertEquals("Property '" + propertyName + "' already exists.", exception.getMessage());
  }

  @Test
  public void testRecordTypeBuilderWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
  }

  @Test
  public void testRecordTypeBuilderWithSingleProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "testProperty";
    JSType propertyType = JSType.NUMBER;
    Node propertyNode = new Node(); // Assuming a valid Node representation
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    RecordType record = (RecordType) recordType;
    RecordProperty property = record.getProperty(propertyName);
    assertEquals(propertyName, property.getName());
    assertEquals(propertyType, property.getType());
  }

  @Test
  public void testRecordTypeBuilderWithMultipleProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName1 = "testProperty1";
    JSType propertyType1 = JSType.NUMBER;
    Node propertyNode1 = new Node(); // Assuming a valid Node representation
    String propertyName2 = "testProperty2";
    JSType propertyType2 = JSType.STRING;
    Node propertyNode2 = new Node(); // Assuming a valid Node representation

    builder.addProperty(propertyName1, propertyType1, propertyNode1);
    builder.addProperty(propertyName2, propertyType2, propertyNode2);

    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    RecordType record = (RecordType) recordType;

    RecordProperty property1 = record.getProperty(propertyName1);
    assertEquals(propertyName1, property1.getName());
    assertEquals(propertyType1, property1.getType());

    RecordProperty property2 = record.getProperty(propertyName2);
    assertEquals(propertyName2, property2.getName());
    assertEquals(propertyType2, property2.getType());
  }
}