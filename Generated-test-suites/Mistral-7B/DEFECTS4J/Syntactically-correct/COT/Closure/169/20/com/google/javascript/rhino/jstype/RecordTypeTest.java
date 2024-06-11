import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.common.collect.Maps;
import org.junit.Test;

public class RecordTypeTest {

 private static Map<String, RecordProperty> getPropertiesMap(JSTypeRegistry registry) {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 return properties;
 }

 @Test
 public void testRecordTypeConstructor() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = getPropertiesMap(registry);
 RecordType recordType = new RecordType(registry, properties, true);

 assertEquals(JSType.getType("{a:number, b:string}"), recordType.getType());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordProperty propA = new RecordProperty("a", registry.getNumberType());
 RecordProperty propB = new RecordProperty("b", registry.getStringType());
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", propA);
 propertiesA.put("b", propB);

 Map<String, RecordProperty> propertiesB = new HashMap<>(propertiesA);
 propertiesB.put("a", null);

 RecordType recordTypeA = new RecordType(registry, propertiesA, true);
 RecordType recordTypeB = new RecordType(registry, propertiesB, true);

 assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

 propertiesB.put("a", propA);

 assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

 RecordType recordTypeC = new RecordType(registry, propertiesB, true);
 assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, false));

 RecordType recordTypeD = new RecordType(registry, propertiesA, false);
 assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeD, false));

 RecordType recordTypeE = new RecordType(registry, propertiesB, false);
 assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeE, false));
}

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructorWithNullProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, null, true);
 }

 @Test
 public void testIsSynthetic() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = getPropertiesMap(registry);
 RecordType recordTypeA = new RecordType(registry, properties, true);
 RecordType recordTypeB = new RecordType(registry, properties, false);

 assertTrue(recordTypeA.isDeclared());
 assertFalse(recordTypeB.isDeclared());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 UnionType unionType = new UnionType(registry, new JSType[] { registry.getNumberType(), registry.getStringType() });
 RecordProperty prop = new RecordProperty("prop", registry.getNumberType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop", prop);
 RecordType recordTypeA = new RecordType(registry, properties, true);
 RecordType recordTypeB = new RecordType(registry, properties, true);
 RecordType recordTypeC = new RecordType(registry, properties, true);
 recordTypeC.defineProperty("prop", unionType, false, Node.nullLiteral());

 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(JSType.getType("{prop: number|string}"), greatestSubtype);

 greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
 assertEquals(unionType, greatestSubtype);
}

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordProperty propA = new RecordProperty("a", registry.getNumberType());
 RecordProperty propB = new RecordProperty("b", registry.getStringType());
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", propA);
 RecordType recordTypeA = new RecordType(registry, propertiesA, true);
 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("b", propB);
 RecordType recordTypeB = new RecordType(registry, propertiesB, true);

 RecordType recordTypeC = new RecordType(registry, propertiesA, true);
 assertTrue(recordTypeA.isSubtype(recordTypeC));

 RecordType recordTypeD = new RecordType(registry, propertiesB, true);
 assertFalse(recordTypeA.isSubtype(recordTypeD));

 RecordType recordTypeE = new RecordType(registry, propertiesA, false);
 assertTrue(recordTypeC.isSubtype(recordTypeE));

 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(objectType.isSubtype(recordTypeA));
}

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordProperty prop = new RecordProperty("prop", registry.getNumberType());
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

 assertFalse(recordType.defineProperty("prop", registry.getNumberType(), false, Node.nullLiteral()));
 recordType.defineProperty("prop", prop.getType(), false, Node.nullLiteral());
 assertTrue(recordType.hasProperty("prop"));
}
}