import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.node.Node;
import com.google.javascript.rhino.type.ObjectType;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeConstructor() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty(new IntType(), null));
 properties.put("b", new RecordProperty(new StringType(), null));

 RecordType recordType = new RecordType(registry, properties, true);

 Assert.assertEquals(registry, recordType.getRegistry());
 Assert.assertEquals(Maps.newTreeMap(properties), recordType.properties);
 Assert.assertTrue(recordType.isDeclared());
 }

 @Test
 public void testRecordTypeConstructorWithNullProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 RecordType recordType = new RecordType(registry, properties, true);

 Assert.assertEquals(registry, recordType.getRegistry());
 Assert.assertNull(recordType.properties);
 }

 @Test
 public void testRecordTypeCheckRecordEquivalenceHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty(new IntType(), null));
 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("b", new RecordProperty(new IntType(), null));
 propertiesB.put("a", new RecordProperty(new IntType(), null));

 RecordType recordTypeA = new RecordType(registry, propertiesA, true);
 RecordType recordTypeB = new RecordType(registry, propertiesB, true);

 Assert.assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
 }

 @Test
 public void testRecordTypeCheckRecordEquivalenceHelperWithDifferentProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty(new IntType(), null));
 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("b", new RecordProperty(new IntType(), null));
 propertiesB.put("c", new RecordProperty(new IntType(), null));

 RecordType recordTypeA = new RecordType(registry, propertiesA, true);
 RecordType recordTypeB = new RecordType(registry, propertiesB, true);

 Assert.assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
 }

 @Test
 public void testRecordTypeDefineProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty(new IntType(), null));
 RecordType recordType = new RecordType(registry, properties, false);

 Assert.assertFalse(recordType.defineProperty("x", new IntType(), false, null));
 Assert.assertFalse(recordType.hasProperty("x"));

 recordType.defineProperty("a", new IntType(), false, null);
 Assert.assertTrue(recordType.hasProperty("a"));
 }

 @Test
 public void testRecordTypeGetImplicitPrototype() {
 RecordType recordType = new RecordType(new RecordTypeRegistry(new RecordTypeRegistry()),
 Maps.newHashMap(), false);

 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 Assert.assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeName());
 }

 @Test
 public void testRecordTypeGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty(new IntType(), null));
 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty(new IntType(), null));
 propertiesB.put("b", new RecordProperty(new IntType(), null));

 RecordType recordTypeA = new RecordType(registry, propertiesA, true);
 RecordType recordTypeB = new RecordType(registry, propertiesB, true);
 RecordType recordTypeC = new RecordType(registry, propertiesA, true);
 RecordType recordTypeD = new RecordType(registry, propertiesB, true);
 RecordType recordTypeE = new RecordType(registry, Maps.newHashMap(), true);

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(recordTypeC);
 unionTypeTypeB = unionTypeBuilder.build();

 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 Assert.assertEquals(recordTypeE, greatestSubtype);

 greatestSubtype = recordTypeA.getGreatestSubtypeHelper(unionTypeB);
 Assert.assertEquals(recordTypeC, greatestSubtype);

 greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
 Assert.assertEquals(recordTypeB, greatestSubtype);

 greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeE);
 Assert.assertEquals(recordTypeA, greatestSubtype);
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty(new IntType(), null));
 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty(new IntType(), null));
 propertiesB.put("b", new RecordProperty(new IntType(), null));

 RecordType recordTypeA = new RecordType(registry, propertiesA, true);
 RecordType recordTypeB = new RecordType(registry, propertiesB, true);
 RecordType recordTypeC = new RecordType(registry, propertiesA, true);
 ObjectType objectType = registry.getNativeObjectType(OBJECT_TYPE);

 Assert.assertTrue(recordTypeA.isSubtype(recordTypeC));
 Assert.assertTrue(recordTypeA.isSubtype(objectType));
 Assert.assertTrue(objectType.isSubtype(recordTypeC));
 Assert.assertFalse(recordTypeA.isSubtype(recordTypeB));
 Assert.assertFalse(recordTypeB.isSubtype(recordTypeA));
 }

 @Test
 public void testRecordTypeIsSubtypeWithNullTypes() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);
 ObjectType objectType = registry.getNativeObjectType(OBJECT_TYPE);

 Assert.assertTrue(recordTypeA.isSubtype(objectType));
 Assert.assertTrue(objectType.isSubtype(recordTypeA));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeDefinePropertyWhenFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty(new IntType(), null));
 RecordType recordType = new RecordType(registry, properties, false);
 recordType.isFrozen = true;

 recordType.defineProperty("x", new IntType(), false, null);
 }

 @Test
 public void testRecordTypeToString() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty(new IntType(), null));
 properties.put("b", new RecordProperty(new StringType(), null));

 RecordType recordType = new RecordType(registry, properties, true);

 Assert.assertEquals("RecordType: [\"a\", \"b\"]", recordType.toString());
 }
}