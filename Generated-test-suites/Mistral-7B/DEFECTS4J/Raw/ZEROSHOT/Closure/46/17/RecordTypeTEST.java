import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;
 private JSTypeFactory typeFactory;

 @Before
 public void setUp() {
 registry = Mockito.mock(JSTypeRegistry.class);
 errorReporter = Mockito.mock(ErrorReporter.class);
 typeFactory = new JSTypeFactory(registry, errorReporter);
 }

 @Test
 public void testCreateRecordType() {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", typeFactory.stringType()));
 properties.put("prop2", new RecordProperty("prop2", typeFactory.numberType()));

 RecordType recordType = new RecordType(registry, properties);

 assertEquals("RecordType", recordType.getClass().getSimpleName());
 assertEquals(2, recordType.getPropertyCount());

 assertEquals("prop1", recordType.getPropertyNames()[0]);
 assertEquals(typeFactory.stringType(), recordType.getPropertyType("prop1"));

 assertEquals("prop2", recordType.getPropertyNames()[1]);
 assertEquals(typeFactory.numberType(), recordType.getPropertyType("prop2"));
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());
 RecordType recordType2 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());
 RecordType recordType3 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop3", typeFactory.numberType());
 RecordType recordType4 = createRecordType(typeFactory, "prop2", typeFactory.numberType(), "prop1", typeFactory.stringType());

 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 assertFalse(recordType1.isEquivalentTo(recordType4));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());
 assertEquals(typeFactory.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());

 assertFalse(recordType.defineProperty("newProp", typeFactory.booleanType(), false, null));
 assertTrue(recordType.isFrozen());
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());
 RecordType recordType2 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType(), "prop3", typeFactory.booleanType());

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType2, leastSupertype);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());
 RecordType recordType2 = createRecordType(typeFactory, "prop2", typeFactory.numberType(), "prop1", typeFactory.stringType());
 RecordType recordType3 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop3", typeFactory.booleanType());

 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType2, recordType1.getGreatestSubtypeHelper(recordType2));
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType1.getGreatestSubtypeHelper(recordType3));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType());
 RecordType recordType2 = createRecordType(typeFactory, "prop1", typeFactory.stringType(), "prop2", typeFactory.numberType(), "prop3", typeFactory.booleanType());
 RecordType recordType3 = createRecordType(typeFactory, "prop1", typeFactory.stringType());
 RecordType recordType4 = createRecordType(typeFactory, "prop2", typeFactory.numberType(), "prop1", typeFactory.stringType());

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 }

 private RecordType createRecordType(JSTypeFactory typeFactory, String... properties) {
 Map<String, RecordProperty> propertyMap = Maps.newArrayListWithExpectedSize(properties.length);
 for (String property : properties) {
 propertyMap.put(property, new RecordProperty(property, typeFactory.anyType()));
 }
 return new RecordType(registry, propertyMap);
 }

 private RecordType createRecordType(JSTypeFactory typeFactory, String propertyName, JSType propertyType, String... otherProperties) {
 Map<String, RecordProperty> propertyMap = Maps.newArrayListWithExpectedSize(otherProperties.length + 1);
 propertyMap.put(propertyName, new RecordProperty(propertyName, propertyType));
 for (String otherProperty : otherProperties) {
 propertyMap.put(otherProperty, new RecordProperty(otherProperty, typeFactory.anyType()));
 }
 return new RecordType(registry, propertyMap);
 }
}