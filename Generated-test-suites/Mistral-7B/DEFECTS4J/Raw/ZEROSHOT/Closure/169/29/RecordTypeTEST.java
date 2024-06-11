import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.JSTypeFactory;
import static com.google.javascript.rhino.JSType.NativeType;
import static com.google.javascript.rhino.JSType.PropertyType;
import static com.google.javascript.rhino.JSType.RecordType;
import static com.google.javascript.rhino.JSType.UnionType;
import static com.google.javascript.rhino.JSType.getRecordType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.BeforeClass
  public static void setUpClass() {
    registry.init();
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> propMap = Maps.newHashMap();
    propMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    propMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    RecordType record1 = new RecordType(registry, propMap, true);
    RecordType record2 = new RecordType(registry, propMap, true);

    // Check that the record types are equivalent.
    assertTrue(record1.checkRecordEquivalenceHelper(record2, true));
    assertTrue(record2.checkRecordEquivalenceHelper(record1, true));

    // Test that record types with the same properties but different orders
    // are not equivalent.
    Map<String, RecordProperty> reversePropMap = Maps.newHashMap();
    reversePropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));
    reversePropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));

    RecordType record3 = new RecordType(registry, reversePropMap, true);
    assertFalse(record1.checkRecordEquivalenceHelper(record3, true));
    assertFalse(record3.checkRecordEquivalenceHelper(record1, true));

    // Test that record types with different properties are not equivalent.
    Map<String, RecordProperty> additionalPropMap = Maps.newHashMap();
    additionalPropMap.putAll(propMap);
    additionalPropMap.put("z", new RecordProperty("z", PropertyType.BOOL, Node.NULL));

    RecordType record4 = new RecordType(registry, additionalPropMap, true);
    assertFalse(record1.checkRecordEquivalenceHelper(record4, true));
    assertFalse(record4.checkRecordEquivalenceHelper(record1, true));
  }

  @org.junit.Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> recordPropMap = Maps.newHashMap();
    recordPropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    recordPropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    RecordType record1 = new RecordType(registry, recordPropMap, true);
    RecordType record2 = new RecordType(registry, recordPropMap, false);

    // Test that record type has no greatest subtype when compared to itself.
    assertEquals(record1, record1.getGreatestSubtypeHelper(record1));

    // Test that record type compared to itself is a supertype.
    assertEquals(record1, record1.getGreatestSubtypeHelper(record2));
    assertEquals(record2, record1.getGreatestSubtypeHelper(record1));

    // Test record types with different declared properties.
    Map<String, RecordProperty> record3PropMap = Maps.newHashMap();
    record3PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record3PropMap.put("z", new RecordProperty("z", PropertyType.BOOL, Node.NULL));

    RecordType record3 = new RecordType(registry, record3PropMap, true);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record3));

    // Test record types with different inferred properties.
    RecordType record4 = new RecordType(registry, recordPropMap, false);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record4));

    // Test record types with different property types.
    Map<String, RecordProperty> record5PropMap = Maps.newHashMap();
    record5PropMap.put("x", new RecordProperty("x", PropertyType.DOUBLE, Node.NULL));
    record5PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    RecordType record5 = new RecordType(registry, record5PropMap, true);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record5));

    // Test record types with different orders of properties.
    Map<String, RecordProperty> record6PropMap = Maps.newHashMap();
    record6PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));
    record6PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));

    RecordType record6 = new RecordType(registry, record6PropMap, true);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record6));

    // Test record types with duplicate properties.
    Map<String, RecordProperty> record7PropMap = Maps.newHashMap();
    record7PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record7PropMap.put("x", new RecordProperty("x", PropertyType.STRING, Node.NULL));

    RecordType record7 = new RecordType(registry, record7PropMap, true);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record7));

    // Test record types with a property that is a union type.
    Map<String, RecordProperty> record8PropMap = Maps.newHashMap();
    record8PropMap.put("x", new RecordProperty("x", UnionType.ofTypes(PropertyType.INT, PropertyType.DOUBLE), Node.NULL));

    RecordType record8 = new RecordType(registry, record8PropMap, true);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record8));

    // Test record types with a property that is a record type.
    Map<String, RecordProperty> record9PropMap = Maps.newHashMap();
    SortedMap<String, JSType> record9Properties = new TreeMap<>();
    record9Properties.put("x", PropertyType.INT);
    record9Properties.put("y", PropertyType.STRING);
    JSType record9 = RecordType.ofProperties(registry, record9Properties);
    record9PropMap.put("xRecord", new RecordProperty("xRecord", record9, Node.NULL));

    RecordType record9 = new RecordType(registry, record9PropMap, true);
    assertEquals(NativeType.OBJECT_TYPE, record1.getGreatestSubtypeHelper(record9));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> record1PropMap = Maps.newHashMap();
    record1PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record1PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    RecordType record1 = new RecordType(registry, record1PropMap, true);

    // Test that record type is a subtype of itself.
    assertTrue(record1.isSubtype(record1));

    // Test that record type is a subtype of an object type.
    assertTrue(record1.isSubtype(NativeType.OBJECT_TYPE));

    // Test that record type is a subtype of a record type with the same properties.
    Map<String, RecordProperty> record2PropMap = Maps.newHashMap();
    record2PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record2PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    RecordType record2 = new RecordType(registry, record2PropMap, true);
    assertTrue(record1.isSubtype(record2));
    assertTrue(record2.isSubtype(record1));

    // Test that record type is not a subtype of a record type with fewer properties.
    Map<String, RecordProperty> record3PropMap = Maps.newHashMap();
    record3PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));

    RecordType record3 = new RecordType(registry, record3PropMap, true);
    assertFalse(record1.isSubtype(record3));
    assertFalse(record3.isSubtype(record1));

    // Test that record type is not a subtype of a record type with more properties.
    Map<String, RecordProperty> record4PropMap = Maps.newHashMap();
    record4PropMap.putAll(record1PropMap);
    record4PropMap.put("z", new RecordProperty("z", PropertyType.BOOL, Node.NULL));

    RecordType record4 = new RecordType(registry, record4PropMap, true);
    assertFalse(record1.isSubtype(record4));
    assertFalse(record4.isSubtype(record1));

    // Test that record type is not a subtype of a record type with properties of incompatible types.
    Map<String, RecordProperty> record5PropMap = Maps.newHashMap();
    record5PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record5PropMap.put("y", new RecordProperty("y", PropertyType.BOOL, Node.NULL));

    RecordType record5 = new RecordType(registry, record5PropMap, true);
    assertFalse(record1.isSubtype(record5));
    assertFalse(record5.isSubtype(record1));

    // Test that record type is not a subtype of a record type with properties of incompatible orders.
    Map<String, RecordProperty> record6PropMap = Maps.newHashMap();
    record6PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));
    record6PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));

    RecordType record6 = new RecordType(registry, record6PropMap, true);
    assertFalse(record1.isSubtype(record6));
    assertFalse(record6.isSubtype(record1));

    // Test that record type is a subtype of a union type of its properties.
    Map<String, RecordProperty> record7PropMap = Maps.newHashMap();
    record7PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record7PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    UnionType unionType = UnionType.ofTypes(PropertyType.INT, PropertyType.DOUBLE);
    RecordType record7 = new RecordType(registry, record7PropMap, true);
    RecordType record8 = new RecordType(registry, record7PropMap, false);

    RecordType record9 = new RecordType(registry, Arrays.asList(record1, record8), true);
    assertTrue(record1.isSubtype(record9));
    assertTrue(record9.isSubtype(record1));
  }

  @org.junit.Test
  public void testRecordTypeWithInferredProperties() {
    Map<String, RecordProperty> recordPropMap = Maps.newHashMap();

    // Create a record type with inferred properties.
    RecordType record1 = new RecordType(registry, recordPropMap, false);

    // Test that record type with inferred properties is not a subtype of itself.
    assertFalse(record1.isSubtype(record1));

    // Test that record type with inferred properties is a subtype of an object type.
    assertTrue(record1.isSubtype(NativeType.OBJECT_TYPE));

    // Test that record type with inferred properties is not a subtype of a record type with the same properties but declared.
    Map<String, RecordProperty> record2PropMap = Maps.newHashMap();
    record2PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));
    record2PropMap.put("y", new RecordProperty("y", PropertyType.STRING, Node.NULL));

    RecordType record2 = new RecordType(registry, record2PropMap, true);
    assertFalse(record1.isSubtype(record2));
    assertFalse(record2.isSubtype(record1));

    // Test that record type with inferred properties is not a subtype of a record type with fewer properties.
    Map<String, RecordProperty> record3PropMap = Maps.newHashMap();
    record3PropMap.put("x", new RecordProperty("x", PropertyType.INT, Node.NULL));

    RecordType record3 = new RecordType(registry, record3PropMap, true);
    assertFalse(record1.isSubtype(record3));
    assertFalse(record3.isSubtype(record1));
  }
}