import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import static com.google.javascript.rhino.jstype.RecordType.isSubtype;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordType = new RecordType(registry, Maps.newHashMap());
 }

 @Test
 public void testConstructor() {
 JSType recordTypeInstance = recordType;
 Assert.assertNotNull(recordTypeInstance);
 Assert.assertFalse(recordTypeInstance.isFunctionType());
 Assert.assertFalse(recordTypeInstance.isUnionType());
 Assert.assertFalse(recordTypeInstance.isIntersectionType());
 Assert.assertFalse(recordTypeInstance.isEnumType());
 Assert.assertFalse(recordTypeInstance.isNativeType());
 Assert.assertFalse(recordTypeInstance.isArrayType());
 Assert.assertFalse(recordTypeInstance.isPrototypeObjectType());
 Assert.assertTrue(recordTypeInstance.isRecordType());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

 Assert.assertTrue(recordType1.isEquivalentTo(recordType1));
 Assert.assertFalse(recordType1.isEquivalentTo(recordType2));
 Assert.assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 Assert.assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecordType() {
 recordType.defineProperty("test", registry.getStringType(), false, null);
 }

 @Test
 public void testDefineProperty() {
 recordType.defineProperty("test", registry.getStringType(), false, null);
 JSType propertyType = recordType.getPropertyType("test");
 Assert.assertEquals(registry.getStringType(), propertyType);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("test1", registry.getBooleanType(), false, null);
 recordType2.defineProperty("test2", registry.getStringType(), false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 Assert.assertEquals(recordType2, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("test1", registry.getBooleanType(), false, null);
 recordType1.defineProperty("test2", registry.getStringType(), false, null);

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("test1", registry.getBooleanType(), false, null);
 recordType2.defineProperty("test3", registry.getStringType(), false, null);

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("test1", registry.getBooleanType(), false, null);
 recordType3.defineProperty("test4", registry.getStringType(), false, null);

 Assert.assertTrue(isSubtype(recordType1, recordType1));
 Assert.assertTrue(isSubtype(recordType1, recordType2));
 Assert.assertFalse(isSubtype(recordType1, recordType3));
 }

 @Test
 public void testResolveInternal() {
 Node testNode = new Node();
 testNode.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 JSType resolvedType = recordType.resolveInternal(null, null);
 Assert.assertEquals(JSType.UNKNOWN, resolvedType.getTypeClass());
 }
}