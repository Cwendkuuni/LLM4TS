import static org.junit.Assert.*;
import static com.google.javascript.rhino.junit.JSTypeAssert.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    properties.put("b", new RecordProperty("b", registry.integerType()));

    RecordType record = new RecordType(registry, properties, true);
    assertEquals("{a: string, b: number}", record.getDisplayName());
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.stringType()));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.stringType()));
    propertiesB.put("b", new RecordProperty("b", registry.integerType()));

    RecordType recordA = new RecordType(registry, propertiesA, true);
    RecordType recordB = new RecordType(registry, propertiesB, true);

    // Record A is a subtype of Record B because it has all the properties
    // declared in Record B with the same types.
    assertTrue(recordA.isSubtype(recordB));

    // Record B is not a subtype of Record A because it has an additional property.
    assertFalse(recordB.isSubtype(recordA));

    // Record A is a supertype of Record A because it's equivalent to itself.
    assertTrue(recordA.isSubtype(recordA));

    // Object Type is a supertype of every record type, including itself.
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(recordA));
    assertTrue(recordA.isSubtype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.stringType()));
    propertiesA.put("b", new RecordProperty("b", registry.integerType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.stringType()));
    propertiesB.put("b", new RecordProperty("b", registry.booleanType()));
    propertiesB.put("c", new RecordProperty("c", registry.integerType()));

    RecordType recordA = new RecordType(registry, propertiesA, true);
    RecordType recordB = new RecordType(registry, propertiesB, true);

    // The greatest common subtype of Record A and Record B is the record
    // type with only the properties "a" and "b", with the intersection
    // of their types.
    RecordType greatestCommonSubtype = recordA.getGreatestSubtype(recordB);
    assertEquals("{a: string, b: number}", greatestCommonSubtype.getDisplayName());

    // Record A is not a subtype of Record C because it's missing a property
    // declared in Record C.
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.stringType()));
    propertiesC.put("c", new RecordProperty("c", registry.integerType()));

    RecordType recordC = new RecordType(registry, propertiesC, true);
    assertFalse(recordA.isSubtype(recordC));

    // The greatest common subtype of Record A and Record A is Record A itself.
    RecordType greatestCommonSubtypeAA = recordA.getGreatestSubtype(recordA);
    assertEquals(recordA.getDisplayName(), greatestCommonSubtypeAA.getDisplayName());
  }

  @Test
  public void testRecordTypeCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.stringType()));
    propertiesA.put("b", new RecordProperty("b", registry.integerType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", registry.integerType()));
    propertiesB.put("a", new RecordProperty("a", registry.stringType()));

    RecordType recordA = new RecordType(registry, propertiesA, true);
    RecordType recordB = new RecordType(registry, propertiesB, true);

    // Record A and Record B have the same properties but in a different order,
    // so they are equivalent.
    assertTrue(recordA.checkRecordEquivalenceHelper(recordB, true));

    // Record A and Record C have different properties, so they are not equivalent.
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("c", new RecordProperty("c", registry.integerType()));

    RecordType recordC = new RecordType(registry, propertiesC, true);
    assertFalse(recordA.checkRecordEquivalenceHelper(recordC, true));
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));

    RecordType record = new RecordType(registry, properties, true);

    // Record properties can't be modified after construction.
    assertFalse(record.defineProperty("a", registry.booleanType(), false, null));
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();

    RecordType record = new RecordType(registry, properties, true);
    ObjectType implicitPrototype = record.getImplicitPrototype();

    // The implicit prototype of a record type is the native ObjectType.
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testRecordTypeUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));

    RecordType record = new RecordType(registry, properties, true);

    // Find the union type of all classes that have a property "a" with a
    // compatible property type and which are a subtype of the given type.
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    unionTypeBuilder.addAlternate(objType);

    // Consider the following variable declarations:
    // var x: {a: string};
    // var y: {a: number};
    // var z: {a: boolean};

    // The union type of record types {a: string}, {a: number}, and {a: boolean}
    // is a union type of all three types.
    for (ObjectType refType : registry.getEachReferenceTypeWithProperty("a")) {
      if (refType != objType && refType.isSubtype(record)) {
        JSType propertyType = refType.getPropertyType("a");
        if (propertyType != null && record.getPropertyType("a")
            .isCompatibleWith(propertyType)) {
          unionTypeBuilder.addAlternate(refType);
        }
      }
    }

    JSType greatestLowerBound = unionTypeBuilder.build();
    assertEquals("{a: string|number|boolean}", greatestLowerBound.getDisplayName());
  }
}