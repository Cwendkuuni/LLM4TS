import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty property2 = new RecordProperty("prop2", registry.getNumberType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", property1);
 properties.put("prop2", property2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 assertFalse(recordType1.isEquivalentTo(recordType3));

 RecordType recordType4 = new RecordType(registry, properties);
 recordType4.isFrozen = true;
 assertFalse(recordType1.isEquivalentTo(recordType4));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("prop3", registry.getStringType(), false, null));
 recordType.isFrozen = false;
 assertTrue(recordType.defineProperty("prop3", registry.getStringType(), false, null));
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty("prop3", registry.getStringType(), false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType stringType = registry.getStringType();
 JSType numberType = registry.getNumberType();
 JSType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("prop1", stringType, false, null);
 recordType1.defineProperty("prop2", numberType, false, null);
 JSType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop1", stringType, false, null);
 recordType2.defineProperty("prop2", numberType, false, null);
 recordType2.defineProperty("prop3", numberType, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, greatestSubtype);

 JSType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop1", stringType, false, null);
 recordType3.defineProperty("prop2", numberType, false, null);
 recordType3.defineProperty("prop3", registry.getBooleanType(), false, null);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, greatestSubtype);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("prop1", registry.getStringType(), false, null);
 recordType1.defineProperty("prop2", registry.getNumberType(), false, null);

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 properties.put("prop3", prop3);
 RecordType recordType3 = new RecordType(registry, properties);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(objectType));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.NO_TYPE)));

 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType4.defineProperty("prop1", registry.getStringType(), false, null);
 recordType4.defineProperty("prop2", registry.getNumberType(), false, null);
 recordType4.defineProperty("prop3", registry.getStringType(), false, null);

 // This is not a property type, but it is a subtype of the record type.
 assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("prop1")));

 // This is a property type that is not a subtype of the record type.
 assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("prop3").isSubtype(recordType1));
 }

 @Test
 public void testResolveInternal() {
 JSType stringType = registry.getStringType();
 JSType numberType = registry.getNumberType();
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 recordType.defineProperty("prop1", stringType, false, null);
 recordType.defineProperty("prop2", numberType, false, null);

 Map<String, JSType> resolvedTypes = Maps.newHashMap();
 JSType resolvedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 resolvedTypes.put("Object", resolvedType);
 resolvedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("prop1");
 resolvedTypes.put("Object.prop1", resolvedType);

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop1", stringType, false, null);
 recordType2.defineProperty("prop2", numberType, false, null);
 recordType2.defineProperty("prop3", registry.getBooleanType(), false, null);
 recordType2.resolveInternal(null, null);

 assertEquals(resolvedTypes, recordType2.resolveInternal(null, null).getProperties());
 }
}