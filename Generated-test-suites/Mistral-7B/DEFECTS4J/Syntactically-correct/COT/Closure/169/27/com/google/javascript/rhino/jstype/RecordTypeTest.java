import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Maps;

import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry() {
      @Override
      public JSType buildRecordType(Map<String, RecordProperty> properties) {
        return new RecordType(this, properties, true);
      }

      @Override
      public JSType buildRecordType(Map<String, RecordProperty> properties, boolean declared) {
        return new RecordType(this, properties, declared);
      }

      @Override
      public UnionType buildUnionType(Set<JSType> types) {
        // A simple implementation of UnionType for testing purposes
        return new UnionType(this, types) {
          @Override
          JSType getLeastSupertype(UnionType other) {
            Set<JSType> unionTypes = new java.util.HashSet<>(this.types);
            unionTypes.addAll(other.types);
            return new UnionType(registry, unionTypes);
          }
        };
      }

      @Override
      public ObjectType getNativeObjectType(JSTypeNative type) {
        // A simple implementation of ObjectType for testing purposes
        return new ObjectType(registry, null, null) {
          @Override
          public boolean isEquivalentTo(ObjectType other) {
            return this == other;
          }

          @Override
          public boolean isSubtype(JSType other) {
            return other instanceof ObjectType;
          }

          @Override
          public JSType getPropertyType(String propertyName) {
            return null;
          }

          @Override
          public Node getPropertyNode(String propertyName) {
            return null;
          }

          @Override
          public boolean hasProperty(String propertyName) {
            return false;
          }
        };
      }
    };
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));
    properties.put("b", new RecordProperty("b", JSType.getBooleanType()));

    RecordType recordType = (RecordType) registry.buildRecordType(properties);
    assertNotNull(recordType);
    assertEquals(2, recordType.properties.size());
  }

  @Test
  public void testIsSynthetic() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));
    properties.put("b", new RecordProperty("b", JSType.getBooleanType()));

    RecordType recordType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSynthetic());

    RecordType declaredRecordType = new RecordType(registry, properties, true);
    assertFalse(declaredRecordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSType.getIntType()));
    properties1.put("b", new RecordProperty("b", JSType.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSType.getIntType()));
    properties2.put("b", new RecordProperty("b", JSType.getBooleanType()));
    properties2.put("c", new RecordProperty("c", JSType.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    RecordType recordType3 = new RecordType(registry, properties1, true);
    RecordType recordType4 = new RecordType(registry, properties2, true);

    assertFalse(recordType3.checkRecordEquivalenceHelper(recordType4, false));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSType.getIntType()));
    properties1.put("b", new RecordProperty("b", JSType.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSType.getIntType()));
    properties2.put("b", new RecordProperty("b", JSType.getBooleanType()));
    properties2.put("c", new RecordProperty("c", JSType.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType1, greatestSubtype);

    // The second record type should not be a subtype of the first
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType greatestSubtype2 = recordType1.getGreatestSubtypeHelper(emptyRecordType);
    assertEquals(emptyRecordType, greatestSubtype2);

    // Record type with conflicting properties
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", JSType.getIntType()));
    properties3.put("b", new RecordProperty("b", JSType.getBooleanType()));

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty("a", JSType.getIntType()));
    properties4.put("b", new RecordProperty("b", JSType.getStringType()));

    RecordType recordType3 = new RecordType(registry, properties3, true);
    RecordType recordType4 = new RecordType(registry, properties4, true);

    JSType noType = registry.getNativeType(JSTypeNative.NO_TYPE);
    JSType greatestSubtype3 = recordType3.getGreatestSubtypeHelper(recordType4);
    assertEquals(noType, greatestSubtype3);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSType.getIntType()));
    properties1.put("b", new RecordProperty("b", JSType.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSType.getIntType()));
    properties2.put("b", new RecordProperty("b", JSType.getBooleanType()));
    properties2.put("c", new RecordProperty("c", JSType.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    RecordType recordType3 = new RecordType(registry, properties1, true);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Record type is a subtype of ObjectType
    assertTrue(recordType1.isSubtype(objectType));

    // Record type1 is a subtype of record type2
    assertTrue(recordType1.isSubtype(recordType2));

    // ObjectType is not a subtype of record type1
    assertFalse(objectType.isSubtype(recordType1));

    // Record type2 is not a subtype of record type1
    assertFalse(recordType2.isSubtype(recordType1));

    // Record type1 is not a subtype of record type3
    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testToString() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.getIntType()));
    properties.put("b", new RecordProperty("b", JSType.getBooleanType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("{a: int, b: boolean}", recordType.toString());
  }
}