import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeWithInvalidProperty;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("prop1", JSType.STRING);
 RecordProperty property2 = new RecordProperty("prop2", JSType.NUMBER);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", property1);
 properties.put("prop2", property2);
 recordType = new RecordType(registry, properties);
 recordTypeWithInvalidProperty = new RecordType(registry, null);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, null);
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());

 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 assertFalse(recordType1.isEquivalentTo(recordType4));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("newProp", JSType.STRING, false, null));
 assertTrue(recordTypeWithInvalidProperty.defineProperty("newProp", JSType.STRING, false, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecord() {
 recordType.defineProperty("prop1", JSType.STRING, false, null);
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType4.addProperty("prop1", JSType.STRING, null);
 recordType4.addProperty("prop2", JSType.NUMBER, null);
 RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
 recordType5.addProperty("prop1", JSType.STRING, null);
 recordType5.addProperty("prop3", JSType.BOOLEAN, null);

 assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType2));
 assertEquals(recordType1, recordType2.getLeastSupertype(recordType1));
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType3));
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType4));
 assertEquals(recordType4, recordType1.getLeastSupertype(recordType5));
 }

 @Test
 public void testGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.addProperty("prop1", JSType.STRING, null);
 recordType2.addProperty("prop2", JSType.NUMBER, null);

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 unionTypeBuilder.addAlternate(objectType);
 unionTypeBuilder.addAlternate(recordType1);
 unionTypeTypeBuilder.addAlternate(recordType2);
 JSType unionType = unionTypeBuilder.build();

 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(unionType));
 assertEquals(recordType2, recordType2.getGreatestSubtypeHelper(unionType));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType1, recordType2.getGreatestSubtypeHelper(recordType1));
 }

 @Test
 public void testIsSubtype() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.addProperty("prop1", JSType.STRING, null);
 recordType2.addProperty("prop3", JSType.BOOLEAN, null);

 assertTrue(recordType1.isSubtype(objectType));
 assertTrue(objectType.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testResolveInternal() {
 Node propertyNode = new Node_Property(null, "prop1", null, null);
 RecordType recordType = new RecordType(registry, properties);
 RecordProperty property = properties.get("prop1");
 JSType propertyType = property.getType();

 JSType resolvedType = recordType.resolveInternal(null, null);
 assertEquals(propertyType, resolvedType.getPropertyType("prop1"));
 }
}