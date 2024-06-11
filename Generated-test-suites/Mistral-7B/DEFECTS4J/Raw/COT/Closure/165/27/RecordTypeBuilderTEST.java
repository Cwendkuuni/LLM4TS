import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for the RecordTypeBuilder class.
 */
public class RecordTypeBuilderTest {

  @SuppressWarnings("unchecked")
  private static <T> T newRecordTypeBuilder(JSTypeRegistry registry) {
    return (T) new RecordTypeBuilder(registry);
  }

  /**
   * Test method for the addProperty() method.
   */
  @org.junit.Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = newRecordTypeBuilder(registry);

    // Test adding a property with valid arguments.
    String propertyName = "propertyName";
    JSType propertyType = JSType.STRING;
    Node propertyNode = new Node();
    RecordProperty result = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(result);
    assertEquals(propertyName, result.getPropertyNode().toString());
    assertEquals(propertyType, result.getType());

    // Test adding a property with a duplicate name.
    builder = newRecordTypeBuilder(registry);
    String duplicatePropertyName = "propertyName";
    JSType duplicatePropertyType = JSType.NUMBER;
    Node duplicatePropertyNode = new Node();
    RecordProperty duplicateResult = builder.addProperty(duplicatePropertyName, duplicatePropertyType, duplicatePropertyNode);
    assertNull(duplicateResult);

    // Test adding a property with null arguments.
    builder = newRecordTypeBuilder(registry);
    String nullPropertyName = null;
    JSType nullPropertyType = null;
    Node nullPropertyNode = null;
    RecordProperty nullResult = builder.addProperty(nullPropertyName, nullPropertyType, nullPropertyNode);
    assertNull(nullResult);
  }

  /**
   * Test method for the build() method.
   */
  @org.junit.Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = newRecordTypeBuilder(registry);

    // Test building an empty record.
    RecordType result = (RecordType) builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getType());

    // Test building a non-empty record.
    String propertyName = "propertyName";
    JSType propertyType = JSType.STRING;
    Node propertyNode = new Node();
    RecordProperty property = builder.addProperty(propertyName, propertyType, propertyNode).get();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put(propertyName, property);

    result = new RecordType(registry, Collections.unmodifiableMap(properties));
    assertEquals(JSType.RECORD, result.getType());
    assertEquals(propertyName, result.getProperties().get(propertyName).getName());
    assertEquals(propertyType, result.getProperties().get(propertyName).getType());
  }
}