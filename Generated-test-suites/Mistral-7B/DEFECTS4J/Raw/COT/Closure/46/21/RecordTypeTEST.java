import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.Node.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 nodeFactory = new NodeFactory(registry);
 }

 @Test
 public void testRecordTypeConstruction() {
 RecordProperty property1 = new RecordProperty("property1", INT_TYPE);
 RecordProperty property2 = new RecordProperty("property2", STRING_TYPE);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", property1);
 properties.put("property2", property2);
 RecordType recordType = new RecordType(registry, properties);

 assertEquals(INT_TYPE, recordType.getPropertyType("property1"));
 assertEquals(STRING_TYPE, recordType.getPropertyType("property2"));
 assertFalse(recordType.isFrozen());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordProperty property1 = new RecordProperty("property1", INT_TYPE);
 RecordProperty property1Copy = new RecordProperty("property1", INT_TYPE);
 RecordProperty property2 = new RecordProperty("property2", STRING_TYPE);
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties1.put("property1", property1);
 properties1.put("property2", property2);
 properties2.put("property1", property1Copy);
 properties2.put("property2", property2);
 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);

 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructionWithNullProperty() {
 RecordProperty property = null;
 RecordProperty[] properties = new RecordProperty[]{property};
 Map<String, RecordProperty> propertyMap = Maps.newHashMap();
 propertyMap.put("property1", property);
 try {
 new RecordType(registry, propertyMap);
 fail();
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 String propertyName = "property1";
 JSType propertyType = INT_TYPE;
 boolean inferred = false;
 Node propertyNode = newNumberNode(3);
 assertTrue(recordType.defineProperty(propertyName, propertyType, inferred,
 propertyNode));
 assertEquals(propertyType, recordType.getPropertyType(propertyName));
 }

 @Test
 public void testDefinePropertyFrozen() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 recordType.isFrozen = true;
 String propertyName = "property1";
 JSType propertyType = INT_TYPE;
 boolean inferred = false;
 Node propertyNode = newNumberNode(3);
 assertFalse(recordType.defineProperty(propertyName, propertyType, inferred,
 propertyNode));
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", INT_TYPE, null)
 .addProperty("property2", STRING_TYPE, null)
 .build();
 RecordType recordType3 = new RecordTypeBuilder(registry)
 .addProperty("property1", INT_TYPE, null)
 .addProperty("property3", STRING_TYPE, null)
 .build();

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType2, leastSupertype);
 JSType greatestSubtype = recordType2.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordTypeBuilder(registry)
 .addProperty("property1", INT_TYPE, null)
 .addProperty("property2", STRING_TYPE, null)
 .build();
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", INT_TYPE, null)
 .addProperty("property3", STRING_TYPE, null)
 .build();
 RecordType recordType3 = new RecordTypeBuilder(registry)
 .addProperty("property2", STRING_TYPE, null)
 .addProperty("property3", STRING_TYPE, null)
 .build();
 RecordType recordType4 = new RecordTypeBuilder(registry)
 .addProperty("property1", INT_TYPE, null)
 .addProperty("property2", STRING_TYPE, null)
 .addProperty("property3", INT_TYPE, null)
 .build();

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType2.isSubtype(recordType3));
 assertTrue(recordType2.isSubtype(recordType4));
 }
}