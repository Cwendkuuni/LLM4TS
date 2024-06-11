import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

/**
 * Tests for {@link RecordType}.
 */
public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create record type with two properties: "a" of type Number and "b" of type String.
    RecordProperty aProp = new RecordProperty("a", JSTypeFactory.intType());
    RecordProperty bProp = new RecordProperty("b", JSTypeFactory.stringType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", aProp);
    properties.put("b", bProp);
    RecordType recordType = new RecordType(registry, properties);

    // Test property accessors.
    assertEquals("a", recordType.getPropertyName("a"));
    assertEquals("b", recordType.getPropertyName("b"));
    assertEquals(JSTypeFactory.intType(), recordType.getPropertyType("a"));
    assertEquals(JSTypeFactory.stringType(), recordType.getPropertyType("b"));

    // Test equals method with same record type instance.
    RecordType recordTypeInstance = recordType;
    assertTrue(recordType.isEquivalentTo(recordTypeInstance));

    // Test equals method with different record type instance but same properties.
    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.putAll(properties);
    RecordType anotherRecordType = new RecordType(registry, newProperties);
    assertTrue(recordType.isEquivalentTo(anotherRecordType));

    // Test equals method with different record type instance and different properties.
    RecordProperty cProp = new RecordProperty("c", JSTypeFactory.booleanType());
    Map<String, RecordProperty> differentProperties = Maps.newHashMap();
    differentProperties.putAll(properties);
    differentProperties.put("c", cProp);
    RecordType differentRecordType = new RecordType(registry, differentProperties);
    assertFalse(recordType.isEquivalentTo(differentRecordType));

    // Test equals method with null type.
    assertFalse(recordType.isEquivalentTo(null));

    // Test equals method with different type.
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertFalse(recordType.isEquivalentTo(objectType));

    // Test definition of property.
    recordType.defineProperty("c", JSTypeFactory.doubleType(), false, Node.newIdent("c"));
    assertEquals(JSTypeFactory.doubleType(), recordType.getPropertyType("c"));

    // Test equals method after property definition.
    RecordType recordTypeWithC = new RecordType(registry, properties);
    recordTypeWithC.defineProperty("c", JSTypeFactory.doubleType(), false, Node.newIdent("c"));
    assertTrue(recordType.isEquivalentTo(recordTypeWithC));

    // Test getLeastSupertype method with empty record type.
    RecordType emptyRecordType = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
    assertEquals(recordType, recordType.getLeastSupertype(emptyRecordType));

    // Test getLeastSupertype method with same record type.
    assertEquals(recordType, recordType.getLeastSupertype(recordType));

    // Test getLeastSupertype method with subtype record type.
    RecordProperty dProp = new RecordProperty("d", JSTypeFactory.intType());
    Map<String, RecordProperty> subProperties = Maps.newLinkedHashMap();
    subProperties.putAll(properties);
    subProperties.put("d", dProp);
    RecordType subRecordType = new RecordType(registry, subProperties);
    assertEquals(recordType, recordType.getLeastSupertype(subRecordType));

    // Test getLeastSupertype method with supertype record type.
    RecordProperty eProp = new RecordProperty("e", JSTypeFactory.stringType());
    Map<String, RecordProperty> superProperties = Maps.newLinkedHashMap();
    superProperties.putAll(properties);
    superProperties.put("e", eProp);
    RecordType superRecordType = new RecordType(registry, superProperties);
    assertEquals(superRecordType, superRecordType.getLeastSupertype(recordType));

    // Test getLeastSupertype method with object type.
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getLeastSupertype(objectType));

    // Test getGreatestSubtypeHelper method with same record type.
    assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType));

    // Test getGreatestSubtypeHelper method with empty record type.
    assertEquals(recordType, recordType.getGreatestSubtypeHelper(emptyRecordType));

    // Test getGreatestSubtypeHelper method with record type with fewer properties.
    Map<String, RecordProperty> fewerProperties = Maps.newLinkedHashMap();
    fewerProperties.put("a", aProp);
    RecordType fewerRecordType = new RecordType(registry, fewerProperties);
    assertEquals(recordType, recordType.getGreatestSubtypeHelper(fewerRecordType));

    // Test getGreatestSubtypeHelper method with record type with more properties.
    RecordProperty fProp = new RecordProperty("f", JSTypeFactory.booleanType());
    Map<String, RecordProperty> moreProperties = Maps.newLinkedHashMap();
    moreProperties.putAll(properties);
    moreProperties.put("f", fProp);
    RecordType moreRecordType = new RecordType(registry, moreProperties);
    RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(moreRecordType);
    assertEquals(recordType, greatestSubtype);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create record types.
    RecordProperty aProp = new RecordProperty("a", JSTypeFactory.intType());
    RecordProperty bProp = new RecordProperty("b", JSTypeFactory.stringType());
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", aProp);
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", aProp);
    propertiesB.put("b", new RecordProperty("b", JSTypeFactory.booleanType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test isSubtype method with same record type.
    assertTrue(recordTypeA.isSubtype(recordTypeA));

    // Test isSubtype method with record type that is a subtype of.
    RecordProperty cProp = new RecordProperty("c", JSTypeFactory.doubleType());
    Map<String, RecordProperty> subProperties = Maps.newLinkedHashMap();
    subProperties.putAll(propertiesA);
    subProperties.put("c", cProp);
    RecordType subRecordType = new RecordType(registry, subProperties);
    assertTrue(recordTypeA.isSubtype(subRecordType));

    // Test isSubtype method with record type that is a supertype of.
    RecordProperty dProp = new RecordProperty("d", JSTypeFactory.intType());
    Map<String, RecordProperty> superProperties = Maps.newLinkedHashMap();
    superProperties.putAll(propertiesB);
    superProperties.put("d", dProp);
    RecordType superRecordType = new RecordType(registry, superProperties);
    assertTrue(recordTypeB.isSubtype(superRecordType));

    // Test isSubtype method with record type that is not a subtype of.
    RecordType recordTypeC = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
    assertFalse(recordTypeA.isSubtype(recordTypeC));

    // Test isSubtype method with object type.
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertFalse(recordTypeA.isSubtype(objectType));

    // Test isSubtype method with undefined type.
    JSType undefinedType = registry.getNativeType(JSTypeNative.UNDEFINED_TYPE);
    assertFalse(recordTypeA.isSubtype(undefinedType));

    // Test isSubtype method with boolean type.
    JSType booleanType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
    assertFalse(recordTypeA.isSubtype(booleanType));

    // Test isSubtype method with number type.
    JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    assertFalse(recordTypeA.isSubtype(numberType));

    // Test isSubtype method with string type.
    JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    assertFalse(recordTypeA.isSubtype(stringType));

    // Test isSubtype method with array type.
    JSType arrayType = registry.getNativeType(JSTypeNative.ARRAY_TYPE, JSTypeFactory.intType());
    assertFalse(recordTypeA.isSubtype(arrayType));

    // Test isSubtype method with function type.
    JSType functionType = registry.getNativeType(JSTypeNative.FUNCTION_TYPE, JSTypeFactory.voidType(),
        JSTypeFactory.intType(), JSTypeFactory.intType(), JSTypeFactory.intType());
    assertFalse(recordTypeA.isSubtype(functionType));

    // Test isSubtype method with object type with property of same type.
    Map<String, JSType> objectProperties = Maps.newHashMap();
    objectProperties.put("a", recordTypeA.getPropertyType("a"));
    ObjectType objectTypeWithProperty = registry.newObjectType("ObjectWithPropertyA", objectProperties);
    assertTrue(recordTypeA.isSubtype(objectTypeWithProperty));

    // Test isSubtype method with object type with property of supertype.
    Map<String, JSType> objectPropertiesSupertype = Maps.newHashMap();
    objectPropertiesSupertype.put("a", JSTypeFactory.objectType());
    ObjectType objectTypeWithPropertySupertype = registry.newObjectType("ObjectWithPropertyASupertype", objectPropertiesSupertype);
    assertTrue(recordTypeA.isSubtype(objectTypeWithPropertySupertype));

    // Test isSubtype method with object type with property of incompatible type.
    Map<String, JSType> objectPropertiesIncompatible = Maps.newHashMap();
    objectPropertiesIncompatible.put("a", JSTypeFactory.booleanType());
    ObjectType objectTypeWithPropertyIncompatible = registry.newObjectType("ObjectWithPropertyAIncompatible", objectPropertiesIncompatible);
    assertFalse(recordTypeA.isSubtype(objectTypeWithPropertyIncompatible));

    // Test isSubtype method with record type with fewer properties.
    RecordProperty eProp = new RecordProperty("e", JSTypeFactory.intType());
    Map<String, RecordProperty> fewProperties = Maps.newLinkedHashMap();
    fewProperties.put("a", aProp);
    fewProperties.put("e", eProp);
    RecordType fewRecordType = new RecordType(registry, fewProperties);
    assertFalse(recordTypeA.isSubtype(fewRecordType));

    // Test isSubtype method with record type with more properties.
    RecordProperty fProp = new RecordProperty("f", JSTypeFactory.booleanType());
    Map<String, RecordProperty> moreProperties2 = Maps.newLinkedHashMap();
    moreProperties2.putAll(propertiesA);
    moreProperties2.put("f", fProp);
    RecordType moreRecordType2 = new RecordType(registry, moreProperties2);
    assertFalse(recordTypeA.isSubtype(moreRecordType2));
  }
}