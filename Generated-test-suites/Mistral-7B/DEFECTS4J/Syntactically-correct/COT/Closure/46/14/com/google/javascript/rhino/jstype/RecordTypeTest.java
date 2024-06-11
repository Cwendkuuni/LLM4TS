import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty1 = new RecordProperty("prop1", JSType.STRING);
 RecordProperty recordProperty2 = new RecordProperty("prop2", JSType.NUMBER);
 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("prop1", recordProperty1);
 recordProperties.put("prop2", recordProperty2);
 recordType = new RecordType(registry, recordProperties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, recordProperties);
 RecordType recordType2 = new RecordType(registry, recordProperties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType actual = recordType.getImplicitPrototype();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_whenIsFrozen_thenThrowsException() {
 recordType.defineProperty("prop1", JSType.STRING, false, null);
 recordType.isFrozen = true;
 recordType.defineProperty("prop1", JSType.NUMBER, false, null);
 }

 @Test
 public void testGetLeastSupertype_whenBothRecordTypeHaveCommonProperties_thenReturnsRecordType() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.putAll(recordProperties);
 recordPropertiesB.put("prop3", new RecordProperty("prop3", JSType.BOOLEAN));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 JSType expected = new RecordType(registry, recordProperties);
 JSType actual = recordTypeA.getLeastSupertype(recordTypeB);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeastSupertype_whenNoCommonProperties_thenReturnsEmptyObject() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.put("prop3", new RecordProperty("prop3", JSType.BOOLEAN));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 JSType expected = registry.getNativeObjectType(JSTypeNative.EMPTY_OBJECT_TYPE);
 JSType actual = recordTypeA.getLeastSupertype(recordTypeB);
 assertEquals(expected, actual);
 }

 @Test
 public void testGreatestSubtypeHelper_whenBothRecordTypeHaveCommonProperties_thenReturnsRecordType() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.put("prop3", new RecordProperty("prop3", JSType.BOOLEAN));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 JSType expected = new RecordType(registry, recordProperties);
 JSType actual = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
 assertEquals(expected, actual);
 }

 @Test
 public void testGreatestSubtypeHelper_whenNoCommonProperties_thenReturnsEmptyObject() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.put("prop3", new RecordProperty("prop3", JSType.BOOLEAN));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 JSType expected = registry.getNativeObjectType(JSTypeNative.EMPTY_OBJECT_TYPE);
 JSType actual = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsEquivalentTo_whenOneRecordTypeIsSubtypeOfTheOther_thenReturnsTrue() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
 recordPropertiesC.putAll(recordProperties);
 recordPropertiesC.put("prop3", new RecordProperty("prop3", JSType.BOOLEAN));
 RecordType recordTypeC = new RecordType(registry, recordPropertiesC);
 RecordType recordTypeB = new RecordType(registry, recordProperties);
 assertTrue(recordTypeB.isEquivalentTo(recordTypeC));
 }

 @Test
 public void testIsSubtype_whenBothRecordTypesHaveCommonProperties_thenReturnsTrue() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 RecordType recordTypeB = new RecordType(registry, recordProperties);
 assertTrue(recordTypeA.isSubtype(recordTypeB));
 }

 @Test
 public void testIsSubtype_whenBothRecordTypesHaveDifferentProperties_thenReturnsFalse() {
 JSType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.put("prop3", new RecordProperty("prop3", JSType.BOOLEAN));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 assertFalse(recordTypeA.isSubtype(recordTypeB));
 }

 @Test
 public void testResolveInternal() {
 Node propertyNode1 = new Node(1, "prop1");
 Node propertyNode2 = new Node(1, "prop2");
 recordType.defineProperty("prop1", JSType.STRING, false, propertyNode1);
 recordType.defineProperty("prop2", JSType.NUMBER, false, propertyNode2);
 JSType expected = new UnionType(registry, JSType.STRING, JSType.NUMBER);
 JSType actual = recordType.resolveInternal(null, null);
 assertEquals(expected, actual);
 }
}