import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 // Test addProperty method with valid arguments
 @Test
 public void testAddPropertyValidArguments() {
 String name = "name";
 JSType type = JSType.STRING;
 Node propertyNode = new Node();
 RecordProperty expectedRecordProperty = new RecordProperty(type, propertyNode);
 RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode).get();
 assertEquals(expectedRecordProperty, actualRecordProperty);
 }

 // Test addProperty method with duplicate name
 @Test(expected = IllegalStateException.class)
 public void testAddPropertyDuplicateName() {
 String name = "name";
 JSType type = JSType.STRING;
 Node propertyNode = new Node();
 recordTypeBuilder.addProperty(name, type, propertyNode);
 recordTypeBuilder.addProperty(name, type, propertyNode);
 }

 // Test build method with empty record
 @Test
 public void testBuildEmptyRecord() {
 JSType actualRecordType = recordTypeBuilder.build();
 JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(expectedRecordType, actualRecordType);
 }

 // Test build method with non-empty record
 @Test
 public void testBuildNonEmptyRecord() {
 String name1 = "name1";
 JSType type1 = JSType.NUMBER;
 Node propertyNode1 = new Node();
 recordTypeBuilder.addProperty(name1, type1, propertyNode1);
 String name2 = "name2";
 JSType type2 = JSType.BOOLEAN;
 Node propertyNode2 = new Node();
 recordTypeBuilder.addProperty(name2, type2, propertyNode2);
 JSType actualRecordType = recordTypeBuilder.build();
 Map<String, RecordProperty> actualProperties = ((RecordType) actualRecordType).getProperties();
 Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
 expectedProperties.put(name1, new RecordProperty(type1, propertyNode1));
 expectedProperties.put(name2, new RecordProperty(type2, propertyNode2));
 assertEquals(expectedProperties, actualProperties);
 }

 // Test build method with null registry
 @Test(expected = NullPointerException.class)
 public void testBuildNullRegistry() {
 recordTypeBuilder = new RecordTypeBuilder(null);
 recordTypeBuilder.build();
 }

 // Test addProperty method with null type
 @Test(expected = NullPointerException.class)
 public void testAddPropertyNullType() {
 String name = "name";
 Node propertyNode = new Node();
 recordTypeBuilder.addProperty(name, null, propertyNode);
 }

 // Test addProperty method with null propertyNode
 @Test(expected = NullPointerException.class)
 public void testAddPropertyNullPropertyNode() {
 String name = "name";
 JSType type = JSType.STRING;
 recordTypeBuilder.addProperty(name, type, null);
 }
}