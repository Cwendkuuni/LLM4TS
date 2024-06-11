import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 recordType = new RecordType(registry, Maps.newHashMap());
 }

 @Test
 public void testConstructor() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 assertNotNull(recordType);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 assertTrue(recordType1.isEquivalentTo(recordType2));

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.properties.put("test", new IntType(registry));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 recordType.isFrozen = true;
 recordType.defineProperty("test", new IntType(registry), false, null);
 fail("Should not be able to define property on a frozen record type.");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.properties.put("test", new IntType(registry));

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.properties.put("test", new StringType(registry));
 recordType2.properties.put("anotherTest", new IntType(registry));

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertSame(recordType2, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.properties.put("test", new IntType(registry));

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.properties.put("test", new StringType(registry));
 recordType2.properties.put("anotherTest", new IntType(registry));

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.properties.put("test", new IntType(registry));
 recordType3.properties.put("anotherTest", new IntType(registry));

 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType4.properties.put("test", new StringType(registry));
 recordType4.properties.put("anotherTest", new IntType(registry));

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType4));
 }

 @Test
 public void testResolveInternal() {
 RecordProperty recordProperty = new RecordProperty("test", new IntType(registry));
 recordType.properties.put("test", recordProperty);

 JSType resolvedType = recordType.resolveInternal(null, null);
 assertEquals(recordType, resolvedType);
 }
}