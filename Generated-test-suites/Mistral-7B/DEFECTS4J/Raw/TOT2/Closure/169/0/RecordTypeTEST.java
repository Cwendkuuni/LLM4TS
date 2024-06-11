import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();
  private ErrorReporter errorReporter;
  private RecordType recordType;
  private RecordType recordTypeWithExtraProperty;
  private RecordType recordTypeWithMismatchedPropertyType;
  private RecordType recordTypeWithFrozenProperties;
  private RecordType recordTypeWithMissingProperty;
  private RecordType recordTypeWithUnknownProperty;
  private RecordType recordTypeWithNullProperty;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();

    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    recordType = new RecordType(registry, properties, true);

    Map<String, RecordProperty> propertiesWithExtraProperty = new LinkedHashMap<>(properties);
    propertiesWithExtraProperty.put("address", new RecordProperty("address", registry.getStringType()));

    recordTypeWithExtraProperty = new RecordType(registry, propertiesWithExtraProperty, true);

    Map<String, RecordProperty> propertiesWithMismatchedPropertyType = new LinkedHashMap<>(properties);
    propertiesWithMismatchedPropertyType.put("age", new RecordProperty("age", registry.getBooleanType()));

    recordTypeWithMismatchedPropertyType = new RecordType(registry, propertiesWithMismatchedPropertyType, true);

    Map<String, RecordProperty> propertiesWithFrozenProperties = new LinkedHashMap<>(properties);

    recordTypeWithFrozenProperties = new RecordType(registry, propertiesWithFrozenProperties, true);
    recordTypeWithFrozenProperties.freezeProperties();

    Map<String, RecordProperty> propertiesWithMissingProperty = new LinkedHashMap<>(properties);
    propertiesWithMissingProperty.remove("name");

    recordTypeWithMissingProperty = new RecordType(registry, propertiesWithMissingProperty, true);

    Map<String, RecordProperty> propertiesWithUnknownProperty = new LinkedHashMap<>(properties);
    propertiesWithUnknownProperty.put("unknown", new RecordProperty("unknown", registry.getUnknownType()));

    recordTypeWithUnknownProperty = new RecordType(registry, propertiesWithUnknownProperty, true);

    Map<String, RecordProperty> propertiesWithNullProperty = new LinkedHashMap<>(properties);
    propertiesWithNullProperty.put("null", new RecordProperty("null", registry.getNullType()));

    recordTypeWithNullProperty = new RecordType(registry, propertiesWithNullProperty, true);
  }

  @Test
  public void testBasicUsage() {
    // Test creating a new record type with known properties
    RecordType expectedRecordType = new RecordType(registry, properties, true);
    assertEquals(expectedRecordType, recordType);

    // Test setting and getting properties
    assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("age"));

    // Test checking if a property exists
    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));

    // Test checking if a property does not exist
    assertFalse(recordType.hasProperty("nonexistent"));

    // Test checking if a record type is a subtype of itself
    assertTrue(recordType.isSubtype(recordType));

    // Test checking if a record type is a subtype of a different record type with the same properties
    assertTrue(recordType.isSubtype(recordTypeWithExtraProperty));

    // Test checking if a record type is a subtype of a different record type with a different property
    assertFalse(recordType.isSubtype(recordTypeWithMismatchedPropertyType));

    // Test checking if a record type is a subtype of a record type with frozen properties
    assertTrue(recordType.isSubtype(recordTypeWithFrozenProperties));

    // Test checking if a record type is a subtype of a record type with a missing property
    assertFalse(recordType.isSubtype(recordTypeWithMissingProperty));

    // Test checking if a record type is a subtype of a record type with an unknown property
    assertFalse(recordType.isSubtype(recordTypeWithUnknownProperty));

    // Test checking if a record type is a subtype of a record type with a null property
    assertFalse(recordType.isSubtype(recordTypeWithNullProperty));

    // Test checking if a record type is a subtype of an object type
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test checking if a record type is a subtype of a record type with the same properties but different order
    Map<String, RecordProperty> reversedProperties = new LinkedHashMap<>(properties);
    Collections.reverse(reversedProperties.entrySet());
    RecordType reversedRecordType = new RecordType(registry, reversedProperties, true);
    assertTrue(recordType.isSubtype(reversedRecordType));
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperties() {
    Map<String, RecordProperty> nullProperties = new LinkedHashMap<>();
    nullProperties.put("name", null);
    new RecordType(registry, nullProperties, true);
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordPropertyNodes() {
    Map<String, RecordProperty> nullPropertyNodes = new LinkedHashMap<>();
    nullPropertyNodes.put("name", new RecordProperty("name", registry.getStringType(), null));
    new RecordType(registry, nullPropertyNodes, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRecordPropertyTypes() {
    Map<String, RecordProperty> invalidPropertyTypes = new LinkedHashMap<>();
    invalidPropertyTypes.put("name", new RecordProperty("name", null));
    new RecordType(registry, invalidPropertyTypes, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordWithDuplicateProperties() {
    Map<String, RecordProperty> duplicateProperties = new LinkedHashMap<>(properties);
    duplicateProperties.put("name", new RecordProperty("name", registry.getStringType()));
    new RecordType(registry, duplicateProperties, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordWithInvalidPropertyName() {
    Map<String, RecordProperty> invalidPropertyName = new LinkedHashMap<>();
    invalidPropertyName.put("\u0000name", new RecordProperty("\u0000name", registry.getStringType()));
    new RecordType(registry, invalidPropertyName, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordWithMissingPropertyNodes() {
    Map<String, RecordProperty> missingPropertyNodes = new LinkedHashMap<>(properties);
    missingPropertyNodes.remove("name").getPropertyNode();
    new RecordType(registry, missingPropertyNodes, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordWithNullPropertyValues() {
    Map<String, RecordProperty> nullPropertyValues = new LinkedHashMap<>(properties);
    nullPropertyValues.forEach((k, v) -> v.setValue(null));
    new RecordType(registry, nullPropertyValues, true);
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordWithFrozenPropertiesSetAfterConstruction() {
    recordTypeWithFrozenProperties.freezeProperties();
    assertFalse(recordTypeWithFrozenProperties.isFrozen());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordWithNegativeInheritedPropertyIndex() {
    Map<String, RecordProperty> negativeIndex = new LinkedHashMap<>();
    negativeIndex.put("name", new RecordProperty("name", registry.getStringType(), null));
    negativeIndex.put("age", new RecordProperty("age", registry.getNumberType(), null));
    negativeIndex.put("index", new RecordProperty("index", registry.getNumberType(), -1));
    new RecordType(registry, negativeIndex, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordWithEmptyPropertyMap() {
    new RecordType(registry, new TreeMap<>(), true);
  }
}