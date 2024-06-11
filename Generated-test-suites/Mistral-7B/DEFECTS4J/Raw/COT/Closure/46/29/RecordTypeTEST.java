import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty1 = new RecordProperty("property1", String.class);
 RecordProperty recordProperty2 = new RecordProperty("property2", Number.class);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", recordProperty1);
 properties.put("property2", recordProperty2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 RecordType recordType4 = new RecordType(registry, properties);

 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 assertFalse(recordType1.isEquivalentTo(recordType4));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType actual = recordType.getImplicitPrototype();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_whenIsFrozenIsTrue() {
 recordType.isFrozen = true;
 recordType.defineProperty("propertyName", String.class, false, null);
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 RecordType recordType3 = new RecordType(registry, properties);
 RecordType recordType4 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 recordType4.defineProperty("newProperty", Number.class, false, null);

 JSType expected = recordType1;
 JSType actual = recordType1.getLeastSupertype(recordType2);
 assertEquals(expected, actual);

 expected = recordType3;
 actual = recordType1.getLeastSupertype(recordType3);
 assertEquals(expected, actual);

 expected = recordType4;
 actual = recordType1.getLeastSupertype(recordType4);
 assertEquals(recordType3, actual);

 expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 actual = recordType1.getLeastSupertype(expected);
 assertEquals(recordType1, actual);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 recordType3.defineProperty("newProperty", Number.class, false, null);
 RecordType recordType4 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 recordType4.defineProperty("newProperty", String.class, false, null);

 RecordType expected = recordType1;
 RecordType actual = recordType2;
 assertTrue(recordType1.isSubtype(actual));

 actual = recordType3;
 assertFalse(recordType1.isSubtype(actual));

 actual = recordType4;
 assertTrue(recordType1.isSubtype(actual));
 }

 @Test
 public void testGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 recordType3.defineProperty("newProperty", Number.class, false, null);
 RecordType recordType4 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 recordType4.defineProperty("newProperty", String.class, false, null);

 JSType expected = recordType1;
 JSType actual = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(expected, actual);

 expected = recordType3;
 actual = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(expected, actual);

 expected = recordType4;
 actual = recordType1.getGreatestSubtypeHelper(recordType4);
 assertEquals(recordType3, actual);

 // Test with a complex subtype
 UnionType unionType = new UnionType(registry);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
 unionType.addAlternate(objectType);
 unionType.addAlternate(stringType);

 expected = unionType;
 actual = recordType1.getGreatestSubtypeHelper(unionType);
 assertEquals(expected, actual);
 }

 @Test
 public void testResolveInternal() {
 ErrorReporter errorReporter = new ErrorReporter() {};
 StaticScope<JSType> scope = new StaticScope<JSType>() {};
 JSType expected = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);
 JSType actual = recordType.resolveInternal(errorReporter, scope);
 assertEquals(expected, actual);
 }
}