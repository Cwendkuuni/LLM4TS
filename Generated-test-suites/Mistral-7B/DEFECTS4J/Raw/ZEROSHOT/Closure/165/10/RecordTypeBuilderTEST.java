import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
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
 JSType stringType = registry.getStringType();
 Node propertyNode = Node.newLiteralNode("propertyName");
 RecordProperty expectedProperty = new RecordProperty(stringType, propertyNode);
 RecordProperty actualProperty = recordTypeBuilder.addProperty("propertyName", stringType, propertyNode).get();
 assertEquals(expectedProperty, actualProperty);

 // Test adding a property with duplicate name
 recordTypeBuilder.addProperty("propertyName", stringType, propertyNode);
 RecordProperty duplicateProperty = recordTypeBuilder.addProperty("propertyName", stringType, propertyNode);
 assertNull(duplicateProperty);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType actualRecordType = recordTypeBuilder.build();
 JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(expectedRecordType, actualRecordType);
 }

 @Test
 public void testBuildRecordWithProperties() {
 JSType stringType = registry.getStringType();
 JSType numberType = registry.getNumberType();

 Node stringPropertyNode = Node.newIdentifierNode("stringProperty");
 Node numberPropertyNode = Node.newIdentifierNode("numberProperty");

 recordTypeBuilder.addProperty("stringProperty", stringType, stringPropertyNode);
 recordTypeBuilder.addProperty("numberProperty", numberType, numberPropertyNode);

 RecordType expectedRecordType = new RecordType(registry, Map.of(
 "stringProperty", stringType,
 "numberProperty", numberType
 ));
 JSType actualRecordType = recordTypeBuilder.build();

 assertEquals(expectedRecordType, actualRecordType);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildRecordTwice() {
 RecordType expectedRecordType = new RecordType(registry, Map.of(
 // Properties defined earlier in setUp()
 ));
 RecordType actualRecordType1 = recordTypeBuilder.build();
 assertEquals(expectedRecordType, actualRecordType1);

 RecordType actualRecordType2 = recordTypeBuilder.build();
 fail("Expected IllegalStateException, but got: " + actualRecordType2);
 }
}