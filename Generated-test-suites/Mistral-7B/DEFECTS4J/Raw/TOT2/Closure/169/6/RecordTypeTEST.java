import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType recordTypeWithExtraProperty;
  private RecordType recordTypeWithIncompatibleProperty;
  private RecordType recordTypeWithInvariantProperty;
  private RecordType recordTypeWithSubtypeProperty;
  private RecordType recordTypeWithUnknownProperty;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    recordType = new RecordType(registry, properties, true);

    Map<String, RecordProperty> propertiesWithExtraProperty = new LinkedHashMap<>();
    propertiesWithExtraProperty.putAll(properties);
    propertiesWithExtraProperty.put("gender", new RecordProperty("gender", registry.getStringType()));

    recordTypeWithExtraProperty = new RecordType(registry, propertiesWithExtraProperty, true);

    Map<String, RecordProperty> propertiesWithIncompatibleProperty = new LinkedHashMap<>();
    propertiesWithIncompatibleProperty.putAll(properties);
    propertiesWithIncompatibleProperty.put("age", new RecordProperty("age", registry.getBooleanType()));

    recordTypeWithIncompatibleProperty = new RecordType(registry, propertiesWithIncompatibleProperty, true);

    Map<String, RecordProperty> propertiesWithInvariantProperty = new LinkedHashMap<>();
    propertiesWithInvariantProperty.putAll(properties);
    propertiesWithInvariantProperty.put("name", new RecordProperty("name", registry.getStringType(), Node.NEW));

    recordTypeWithInvariantProperty = new RecordType(registry, propertiesWithInvariantProperty, true);

    Map<String, RecordProperty> propertiesWithSubtypeProperty = new LinkedHashMap<>();
    propertiesWithSubtypeProperty.putAll(properties);
    propertiesWithSubtypeProperty.put("type", new RecordProperty("type", new RecordType(registry, properties, false)));

    recordTypeWithSubtypeProperty = new RecordType(registry, propertiesWithSubtypeProperty, true);

    Map<String, RecordProperty> propertiesWithUnknownProperty = new LinkedHashMap<>();
    propertiesWithUnknownProperty.putAll(properties);
    propertiesWithUnknownProperty.put("unknown", new RecordProperty("unknown", registry.getUnknownType()));

    recordTypeWithUnknownProperty = new RecordType(registry, propertiesWithUnknownProperty, true);
  }

  @Test
  public void testRecordTypeCreation() {
    // Test creating a new record type
    RecordType recordType = new RecordType(registry, Maps.newTreeMap(), true);
    assertNotNull(recordType);
  }

  @Test
  public void testGetImplicitPrototype() {
    // Test getting the implicit prototype
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    // Test defining a property
    recordType.defineProperty("gender", registry.getStringType(), false, Node.NEW);
    assertTrue(recordType.hasProperty("gender"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("gender"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyFrozen() {
    // Test defining a property on a frozen record type
    recordTypeWithExtraProperty.freeze();
    recordTypeWithExtraProperty.defineProperty("gender", registry.getStringType(), false, Node.NEW);
  }

  @Test
  public void testCheckRecordEquivalence() {
    // Test checking record equivalence
    assertTrue(recordType.checkRecordEquivalence(recordTypeWithExtraProperty, false));
    assertFalse(recordType.checkRecordEquivalence(recordTypeWithIncompatibleProperty, false));
    assertTrue(recordType.checkRecordEquivalence(recordTypeWithInvariantProperty, false));
    assertTrue(recordType.checkRecordEquivalence(recordTypeWithSubtypeProperty, false));
    assertTrue(recordType.checkRecordEquivalence(recordTypeWithUnknownProperty, false));
  }

  @Test
  public void testIsSubtype() {
    // Test record type subtyping
    assertTrue(recordType.isSubtype(recordTypeWithExtraProperty));
    assertFalse(recordType.isSubtype(recordTypeWithIncompatibleProperty));
    assertTrue(recordType.isSubtype(recordTypeWithInvariantProperty));
    assertTrue(recordType.isSubtype(recordTypeWithSubtypeProperty));
    assertTrue(recordType.isSubtype(recordTypeWithUnknownProperty));
    assertFalse(recordType.isSubtype(new RecordType(registry, Maps.newTreeMap(), true).getImplicitPrototype()));
  }

  @Test
  public void testGetGreatestSubtype() {
    // Test finding the greatest common subtype
    JSType greatestSubtype = recordTypeWithSubtypeProperty.getGreatestSubtype(recordType);
    assertNotNull(greatestSubtype);
    assertEquals(recordType, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordTypeWithUnknownProperty.getGreatestSubtype(recordType);
    assertNotNull(greatestSubtype);
    assertEquals(recordType, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordTypeWithInvariantProperty.getGreatestSubtype(recordType);
    assertNotNull(greatestSubtype);
    assertEquals(recordType, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordTypeWithIncompatibleProperty.getGreatestSubtype(recordType);
    assertNotNull(greatestSubtype);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

    greatestSubtype = recordTypeWithExtraProperty.getGreatestSubtype(recordType);
    assertNotNull(greatestSubtype);
    assertEquals(recordType, greatestSubtype.toMaybeRecordType());
  }

  @Test
  public void testToString() {
    // Test record type toString
    assertEquals("Record<name: String, age: Number>", recordType.toString());
  }
}