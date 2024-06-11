import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.BeforeClass
  public static void setUp() {
    registry.init(null, null);
  }

  @org.junit.AfterClass
  public static void tearDown() {
    registry.shutdown();
  }

  @org.junit.Test
  public void testRecordTypeWithSimpleProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    // Test getters
    assertEquals("name", recordType.getName());
    assertEquals("{ name : String, age : Number}", recordType.toString());
    assertEquals("name", recordType.getPropertyNames().get(0));
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Number.class, recordType.getPropertyType("age"));

    // Test setting and getting properties
    recordType.setProperty("name", "John Doe");
    assertEquals("John Doe", recordType.getProperty("name"));

    recordType.setProperty("age", 30);
    assertEquals(30, recordType.getProperty("age").asInt());

    // Test setting and getting property types
    recordType.setPropertyType("name", String.class);
    assertEquals(String.class, recordType.getPropertyType("name"));

    recordType.setPropertyType("age", Int.class);
    assertEquals(Int.class, recordType.getPropertyType("age"));

    // Test freezing the record
    recordType.freeze();
    assertFalse(recordType.canDefineProperty("name"));
    assertFalse(recordType.canDefineProperty("age"));

    // Test subtyping
    RecordType subType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSubtype(subType));

    // Test property equivalence
    RecordType equivalentRecord = new RecordType(registry, properties, true);
    assertTrue(recordType.checkRecordEquivalenceHelper(equivalentRecord, true));

    // Test inequality
    RecordType differentRecord = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType.checkRecordEquivalenceHelper(differentRecord, true));

    // Test equals
    RecordType equalRecord = new RecordType(registry, properties, true);
    assertEquals(recordType, equalRecord);

    // Test hashCode
    int hashCode1 = recordType.hashCode();
    int hashCode2 = recordType.hashCode();
    assertEquals(hashCode1, hashCode2);

    // Test getImplicitPrototype
    ObjectType prototype = recordType.getImplicitPrototype();
    assertEquals(NativeObjectType.OBJECT_TYPE, prototype.getClass());

    // Test defineProperty
    recordType.defineProperty("gender", String.class, false, null);
    assertTrue(recordType.hasProperty("gender"));

    // Test getGreatestSubtype
    RecordType emptyRecord = registry.getNativeObjectType(ObjectTypeNative.EMPTY_OBJECT_TYPE);
    JSType greatestSubtype = recordType.getGreatestSubtype(emptyRecord);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithStringProperty =
        new RecordType(registry, Maps.newHashMap(), true) {
          @Override
          public String getName() {
            return "recordWithStringProperty";
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithStringProperty);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithIntProperty =
        new RecordType(registry, Maps.newHashMap(), true) {
          @Override
          public Int getAge() {
            return Int.INT_30;
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithIntProperty);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithUnknownProperty =
        new RecordType(registry, Maps.newHashMap(), true) {
          @Override
          public JSType getPropertyType(String name) {
            if ("unknown".equals(name)) {
              return UnknownType.UNKNOWN;
            }
            return super.getPropertyType(name);
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithUnknownProperty);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithSubtype =
        new RecordType(registry, Maps.newHashMap(), true) {
          @Override
          public JSType getPropertyType(String name) {
            if ("subtype".equals(name)) {
              return registry.getNativeType(NumberTypeNative.NUMBER_TYPE);
            }
            return super.getPropertyType(name);
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithSubtype);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithMultipleProperties =
        new RecordType(registry, Maps.newHashMap(), true) {
          @Override
          public SortedMap<String, JSType> getProperties() {
            SortedMap<String, JSType> map = super.getProperties();
            map.put("multiple", registry.getNativeType(StringTypeNative.STRING_TYPE));
            return map;
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithMultipleProperties);
    assertEquals(recordType, greatestSubtype);
  }

  @org.junit.Test
  public void testRecordTypeWithInferredProperties() {
    SortedMap<String, JSType> properties = Maps.newTreeMap();
    properties.put("name", null);
    properties.put("age", null);

    RecordType recordType = new RecordType(registry, properties, false);

    // Test setting and getting properties
    recordType.setProperty("name", "John Doe");
    assertEquals("John Doe", recordType.getProperty("name"));

    recordType.setProperty("age", 30);
    assertEquals(30, recordType.getProperty("age").asInt());

    // Test setting and getting property types
    recordType.setPropertyType("name", String.class);
    assertEquals(String.class, recordType.getPropertyType("name"));

    recordType.setPropertyType("age", Int.class);
    assertEquals(Int.class, recordType.getPropertyType("age"));

    // Test freezing the record
    recordType.freeze();
    assertFalse(recordType.canDefineProperty("name"));
    assertFalse(recordType.canDefineProperty("age"));

    // Test subtyping
    RecordType subType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSubtype(subType));

    // Test property equivalence
    RecordType equivalentRecord = new RecordType(registry, properties, false);
    assertTrue(recordType.checkRecordEquivalenceHelper(equivalentRecord, true));

    // Test inequality
    RecordType differentRecord = new RecordType(registry, Maps.newHashMap(), false);
    assertFalse(recordType.checkRecordEquivalenceHelper(differentRecord, true));

    // Test equals
    RecordType equalRecord = new RecordType(registry, properties, false);
    assertEquals(recordType, equalRecord);

    // Test hashCode
    int hashCode1 = recordType.hashCode();
    int hashCode2 = recordType.hashCode();
    assertEquals(hashCode1, hashCode2);

    // Test getImplicitPrototype
    ObjectType prototype = recordType.getImplicitPrototype();
    assertEquals(NativeObjectType.OBJECT_TYPE, prototype.getClass());

    // Test defineProperty
    recordType.defineProperty("gender", String.class, false, null);
    assertTrue(recordType.hasProperty("gender"));

    // Test getGreatestSubtype
    RecordType emptyRecord = registry.getNativeObjectType(ObjectTypeNative.EMPTY_OBJECT_TYPE);
    JSType greatestSubtype = recordType.getGreatestSubtype(emptyRecord);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithStringProperty =
        new RecordType(registry, Maps.newHashMap(), false) {
          @Override
          public String getName() {
            return "recordWithStringProperty";
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithStringProperty);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithIntProperty =
        new RecordType(registry, Maps.newHashMap(), false) {
          @Override
          public Int getAge() {
            return Int.INT_30;
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithIntProperty);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithUnknownProperty =
        new RecordType(registry, Maps.newHashMap(), false) {
          @Override
          public JSType getPropertyType(String name) {
            if ("unknown".equals(name)) {
              return UnknownType.UNKNOWN;
            }
            return super.getPropertyType(name);
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithUnknownProperty);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithSubtype =
        new RecordType(registry, Maps.newHashMap(), false) {
          @Override
          public JSType getPropertyType(String name) {
            if ("subtype".equals(name)) {
              return registry.getNativeType(NumberTypeNative.NUMBER_TYPE);
            }
            return super.getPropertyType(name);
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithSubtype);
    assertEquals(recordType, greatestSubtype);

    RecordType recordWithMultipleProperties =
        new RecordType(registry, Maps.newHashMap(), false) {
          @Override
          public SortedMap<String, JSType> getProperties() {
            SortedMap<String, JSType> map = super.getProperties();
            map.put("multiple", registry.getNativeType(StringTypeNative.STRING_TYPE));
            return map;
          }
        };
    greatestSubtype = recordType.getGreatestSubtype(recordWithMultipleProperties);
    assertEquals(recordType, greatestSubtype);
  }
}