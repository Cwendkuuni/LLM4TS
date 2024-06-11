import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

 // Test constructor
 @Test
 public void testRecordTypeConstructor() {
 RecordProperty[] properties = new RecordProperty[] {
 new RecordProperty("name", JSType.STRING),
 new RecordProperty("age", JSType.NUMBER)
 };
 RecordType expectedRecordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 expectedRecordType.defineProperty("name", JSType.STRING, false, null);
 expectedRecordType.defineProperty("age", JSType.NUMBER, false, null);

 RecordType actualRecordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 actualRecordType.defineProperty("name", JSType.STRING, false, null);
 actualRecordType.defineProperty("age", JSType.NUMBER, false, null);

 assertEquals(expectedRecordType, actualRecordType);
 }

 // Test isSubtype method
 @Test
 public void testIsSubtype() {
 RecordProperty[] propertiesA = new RecordProperty[] {
 new RecordProperty("name", JSType.STRING),
 new RecordProperty("age", JSType.NUMBER)
 };
 RecordType recordTypeA = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 recordTypeA.defineProperty("name", JSType.STRING, false, null);
 recordTypeA.defineProperty("age", JSType.NUMBER, false, null);

 RecordProperty[] propertiesB = new RecordProperty[] {
 new RecordProperty("name", JSType.STRING),
 new RecordProperty("age", JSType.NUMBER),
 new RecordProperty("city", JSType.STRING)
 };
 RecordType recordTypeB = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 recordTypeB.defineProperty("name", JSType.STRING, false, null);
 recordTypeB.defineProperty("age", JSType.NUMBER, false, null);
 recordTypeB.defineProperty("city", JSType.STRING, false, null);

 RecordProperty[] propertiesC = new RecordProperty[] {
 new RecordProperty("name", JSType.STRING),
 new RecordProperty("age", JSType.NUMBER),
 new RecordProperty("city", JSType.NUMBER)
 };
 RecordType recordTypeC = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 recordTypeC.defineProperty("name", JSType.STRING, false, null);
 recordTypeC.defineProperty("age", JSType.NUMBER, false, null);
 recordTypeC.defineProperty("city", JSType.NUMBER, false, null);

 RecordProperty[] propertiesD = new RecordProperty[] {
 new RecordProperty("city", JSType.STRING)
 };
 RecordType recordTypeD = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 recordTypeD.defineProperty("city", JSType.STRING, false, null);

 RecordProperty[] propertiesE = new RecordProperty[] {
 new RecordProperty("name", JSType.STRING),
 new RecordProperty("age", JSType.STRING)
 };
 RecordType recordTypeE = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 recordTypeE.defineProperty("name", JSType.STRING, false, null);
 recordTypeE.defineProperty("age", JSType.STRING, false, null);

 RecordProperty[] propertiesF = new RecordProperty[] {
 new RecordProperty("name", JSType.NUMBER),
 new RecordProperty("age", JSType.NUMBER)
 };
 RecordType recordTypeF = new RecordType(
 new JSTypeRegistry(), Maps.newHashMapFromMap(Maps.newHashMap()), true);
 recordTypeF.defineProperty("name", JSType.NUMBER, false, null);
 recordTypeF.defineProperty("age", JSType.NUMBER, false, null);

 assertEquals(recordTypeA.isSubtype(recordTypeA), true);
 assertEquals(recordTypeA.isSubtype(recordTypeB), true);
 assertEquals(recordTypeB.isSubtype(recordTypeA), true);
 assertEquals(recordTypeA.isSubtype(recordTypeC), false);
 assertEquals(recordTypeC.isSubtype(recordTypeA), false);
 assertEquals(recordTypeA.isSubtype(recordTypeD), false);
 assertEquals(recordTypeD.isSubtype(recordTypeA), false);
 assertEquals(recordTypeA.isSubtype(recordTypeE), false);
 assertEquals(recordTypeE.isSubtype(recordTypeA), false);
 assertEquals(recordTypeA.isSubtype(recordTypeF), false);
 assertEquals(recordTypeF.isSubtype(recordTypeA), false);
 }

 // Test getGreatestSubtypeHelper method
 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordTypeA = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordTypeA.defineProperty("name", JSType.STRING, false, null);
 recordTypeA.defineProperty("age", JSType.NUMBER, false, null);

 RecordType recordTypeB = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordTypeB.defineProperty("name", JSType.STRING, false, null);
 recordTypeB.defineProperty("age", JSType.NUMBER, false, null);
 recordTypeB.defineProperty("city", JSType.STRING, false, null);

 RecordType recordTypeC = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordTypeC.defineProperty("name", JSType.STRING, false, null);
 recordTypeC.defineProperty("age", JSType.NUMBER, false, null);
 recordTypeC.defineProperty("city", JSType.STRING, false, null);
 recordTypeC.defineProperty("address", JSType.OBJECT, false, null);

 assertEquals(recordTypeA.getGreatestSubtypeHelper(recordTypeB), recordTypeA);
 assertEquals(recordTypeB.getGreatestSubtypeHelper(recordTypeA), recordTypeA);
 assertEquals(recordTypeA.getGreatestSubtypeHelper(recordTypeC), recordTypeA);
 assertEquals(recordTypeC.getGreatestSubtypeHelper(recordTypeA), recordTypeA);
 assertEquals(recordTypeB.getGreatestSubtypeHelper(recordTypeC), recordTypeB);
 assertEquals(recordTypeC.getGreatestSubtypeHelper(recordTypeB), recordTypeB);
 }

 // Test checkRecordEquivalenceHelper method
 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordTypeA = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordTypeA.defineProperty("name", JSType.STRING, false, null);
 recordTypeA.defineProperty("age", JSType.NUMBER, false, null);

 RecordType recordTypeB = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordTypeB.defineProperty("age", JSType.NUMBER, false, null);
 recordTypeB.defineProperty("name", JSType.STRING, false, null);

 RecordType recordTypeC = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordTypeC.defineProperty("name", JSType.STRING, false, null);
 recordTypeC.defineProperty("age", JSType.NUMBER, false, null);
 recordTypeC.defineProperty("city", JSType.STRING, false, null);

 assertEquals(recordTypeA.checkRecordEquivalenceHelper(recordTypeA, true), true);
 assertEquals(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true), false);
 assertEquals(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, true), false);
 assertEquals(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, true), false);
 assertEquals(recordTypeB.checkRecordEquivalenceHelper(recordTypeB, true), true);
 assertEquals(recordTypeB.checkRecordEquivalenceHelper(recordTypeC, true), false);
 assertEquals(recordTypeC.checkRecordEquivalenceHelper(recordTypeA, true), false);
 assertEquals(recordTypeC.checkRecordEquivalenceHelper(recordTypeB, true), false);
 assertEquals(recordTypeC.checkRecordEquivalenceHelper(recordTypeC, true), true);
 }

 // Test defineProperty method with known property type
 @Test
 public void testDefinePropertyWithKnownPropertyType() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordType.defineProperty("name", JSType.STRING, false, null);
 recordType.defineProperty("age", JSType.NUMBER, false, null);

 assertEquals(recordType.getPropertyType("name"), JSType.STRING);
 assertEquals(recordType.getPropertyType("age"), JSType.NUMBER);
 }

 // Test defineProperty method with inferred property type
 @Test
 public void testDefinePropertyWithInferredPropertyType() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordType.defineProperty("name", null, false, null);
 recordType.defineProperty("age", null, false, null);

 assertEquals(recordType.getPropertyType("name"), JSType.STRING);
 assertEquals(recordType.getPropertyType("age"), JSType.NUMBER);
 }

 // Test getImplicitPrototype method
 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);

 assertEquals(recordType.getImplicitPrototype(), JSTypeRegistry.getNativeObjectType());
 }

 // Test resolveInternal method
 @Test
 public void testResolveInternal() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(), true);
 recordType.defineProperty("name", JSType.STRING, false, null);
 recordType.defineProperty("age", JSType.NUMBER, false, null);

 JSType stringType = JSType.STRING;
 JSType numberType = JSType.NUMBER;

 assertEquals(recordType.resolveInternal(null, null), recordType);
 assertEquals(recordType.resolveInternal(null, null).getType(), recordType.getType());

 assertEquals(recordType.getPropertyType("name").resolveInternal(null, null), stringType);
 assertEquals(recordType.getPropertyType("name").resolveInternal(null, null).getType(), stringType.getType());

 assertEquals(recordType.getPropertyType("age").resolveInternal(null, null), numberType);
 assertEquals(recordType.getPropertyType("age").resolveInternal(null, null).getType(), numberType.getType());
 }
}