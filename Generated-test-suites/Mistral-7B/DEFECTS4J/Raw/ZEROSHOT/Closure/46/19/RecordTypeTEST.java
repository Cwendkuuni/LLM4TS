import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getRecordType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getRecordTypeBuilder;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getUnionType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getIntType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getObjectType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getStringType;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter t;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    t = new ErrorReporter() {};
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", getIntType()));
    properties.put("prop2", new RecordProperty("prop2", getStringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
  }

  @Test
  public void testIsEquivalentToSameType() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = recordType1;

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentTypeWithSameProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop2", stringType);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentTypeWithDifferentProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop3", stringType);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToNull() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertFalse(recordType.isEquivalentTo(null));
  }

  @Test
  public void testIsEquivalentToNonRecordType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertFalse(recordType.isEquivalentTo(getIntType()));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertEquals(getObjectType(registry), recordType.getImplicitPrototype());
  }

  @Test
  public void testDefinePropertyFrozenRecordType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);
    recordType.freeze();

    assertFalse(recordType.defineProperty("prop1", getStringType(), false, null));
  }

  @Test
  public void testGetLeastSupertype() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop3", stringType);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    // RecordType { prop1: int, prop2: string, prop3: string }
    //    is a subtype of
    // RecordType { prop1: int, prop2: string }
    assertTrue(recordType1.isSubtype(leastSupertype));
  }

  @Test
  public void testGetGreatestSubtypeNull() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    JSType greatestSubtype = recordType.getGreatestSubtype(null);

    assertEquals(getObjectType(registry), greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeRecordTypeWithSameProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop2", stringType);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    // RecordType { prop1: int, prop2: string }
    //    is a subtype of
    // RecordType { prop1: int, prop2: string }
    assertEquals(recordType1, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeRecordTypeWithDifferentProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop3", stringType);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    // RecordType { prop1: int, prop2: string }
    //    is a subtype of
    // RecordType { prop1: int, prop3: string }
    // Since "prop2" is not present in recordType2, the greatest subtype only
    // contains the common properties.
    RecordType expectedRecordType = createRecordType("prop1", intType);
    assertEquals(expectedRecordType, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeRecordTypeWithConflictingProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop1", stringType);
    RecordType recordType2 = createRecordType("prop1", getIntType(), "prop1", getStringType());

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    // The NO_TYPE type is returned since the two record types have a
    // conflicting property "prop1".
    assertEquals(getNativeObjectType(registry.getRegistry(), JSTypeNative.NO_TYPE), greatestSubtype);
  }

  @Test
  public void testIsSubtypeSameType() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);

    assertTrue(recordType1.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeRecordTypeToEmptyObjectType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertTrue(getObjectType(registry).isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeEmptyRecordTypeToRecordType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertTrue(getObjectType(registry).isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeRecordTypeToRecordTypeWithSameProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop2", stringType);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeRecordTypeToRecordTypeWithDifferentProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop3", stringType);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeRecordTypeToRecordTypeWithConflictingProperties() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop1", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop2", stringType);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeRecordTypeToObjectType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertTrue(getObjectType(registry).isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeObjectTypeToRecordType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    assertFalse(recordType.isSubtype(getObjectType(registry)));
  }

  @Test
  public void testIsSubtypeRecordTypeToUnionType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);
    UnionType unionType = createUnionType(getIntType(), getStringType());

    assertFalse(recordType.isSubtype(unionType));
  }

  @Test
  public void testIsSubtypeUnionTypeToRecordType() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);
    UnionType unionType = createUnionType(getIntType(), getStringType());

    assertFalse(unionType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeRecordTypeToRecordTypeWithInheritance() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop3", stringType);

    RecordType recordType3 = createRecordType("prop1", intType, "prop2", stringType, recordType2);

    assertTrue(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testIsSubtypeRecordTypeWithFrozenPropertyToRecordTypeWithMutableProperty() {
    RecordType recordType1 = createRecordType("prop1", intType, "prop2", stringType);
    RecordType recordType2 = createRecordType("prop1", intType, "prop2", stringType);
    recordType2.setProperty("prop2", getStringType(), null, true);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = createRecordType("prop1", intType, "prop2", stringType);

    JSType result = recordType.resolveInternal(t, null);

    assertEquals(recordType, result);
  }

  private RecordType createRecordType(String... propertyInfos) {
    RecordTypeBuilder builder = getRecordTypeBuilder(registry);
    for (String propertyInfo : propertyInfos) {
      String propertyName = propertyInfo.split(":")[0];
      JSType propertyType = JSTypeFactory.getJSTypeForTypeName(propertyInfo.split(":")[1]);
      builder.addProperty(propertyName, propertyType, null);
    }
    return builder.build();
  }

  private UnionType createUnionType(JSType... types) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    for (JSType type : types) {
      builder.addAlternate(type.toMaybeObjectType());
    }
    return builder.build();
  }

  private JSType getIntType() {
    return JSTypeFactory.getIntType();
  }

  private JSType getStringType() {
    return JSTypeFactory.getStringType();
  }

  private JSType getNativeObjectType(JSTypeRegistry registry, int nativeType) {
    return registry.getNativeType(nativeType);
  }
}