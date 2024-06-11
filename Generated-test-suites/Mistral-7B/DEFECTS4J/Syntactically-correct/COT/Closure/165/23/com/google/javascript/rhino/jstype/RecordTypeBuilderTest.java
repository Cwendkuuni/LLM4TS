import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

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

 @Test
 public void testAddProperty() {
 // Test adding a property with valid arguments
 String name = "testName";
 JSType type = registry.getNullType();
 Node propertyNode = new Node();
 RecordProperty expectedRecordProperty = new RecordProperty(type, propertyNode);
 RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
 assertEquals(expectedRecordProperty, actualRecordProperty);

 // Test adding a property with duplicate name
 String duplicateName = "testName";
 RecordProperty duplicateRecordProperty = recordTypeBuilder.addProperty(duplicateName, type, propertyNode);
 assertNull(duplicateRecordProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyWithNullType() {
 String name = "testName";
 Node propertyNode = new Node();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty(name, null, propertyNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyWithNullPropertyNode() {
 String name = "testName";
 JSType type = registry.getNullType();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty(name, type, null);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType expectedRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType actualRecord = recordTypeBuilder.build();
 assertEquals(expectedRecord, actualRecord);
 }

 @Test
 public void testBuildNonEmptyRecord() {
 String name1 = "testName1";
 String name2 = "testName2";
 JSType type1 = registry.getBooleanType();
 JSType type2 = registry.getNumberType();
 Node propertyNode1 = new Node();
 Node propertyNode2 = new Node();
 RecordProperty recordProperty1 = recordTypeBuilder.addProperty(name1, type1, propertyNode1).get(0);
 RecordProperty recordProperty2 = recordTypeBuilder.addProperty(name2, type2, propertyNode2).get(0);
 Map<String, RecordProperty> expectedProperties = new HashMap<String, RecordProperty>() {{
 put(name1, recordProperty1);
 put(name2, recordProperty2);
 }};
 Map<String, RecordProperty> actualProperties = recordTypeBuilder.build().getProperties();
 assertEquals(expectedProperties, actualProperties);
 }

}