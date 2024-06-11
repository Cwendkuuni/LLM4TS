import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

/**
 * Test class for RecordTypeBuilder.
 */
public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddProperty() {
 String name = "name";
 JSType type = registry.getNullType();
 Node propertyNode = Node.newNull();
 RecordProperty expectedRecordProperty = new RecordProperty(type, propertyNode);
 RecordProperty actualRecordProperty = builder.addProperty(name, type, propertyNode);

 assertEquals("Unexpected RecordProperty", expectedRecordProperty, actualRecordProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateProperty() {
 String name = "name";
 JSType type = registry.getNullType();
 Node propertyNode = Node.newNull();
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, type, propertyNode);
 RecordProperty duplicateProperty = builder.addProperty(name, type, propertyNode);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType actualType = builder.build();
 JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals("Unexpected RecordType", expectedType, actualType);
 }

 @Test
 public void testBuildRecordWithOneProperty() {
 String name = "name";
 JSType type = registry.getNativeStringType();
 Node propertyNode = Node.newStringLiteral("Property Node");
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, type, propertyNode);
 JSType actualType = builder.build();
 RecordType expectedType = new RecordType(registry, Maps.newHashMap());
 expectedType.putProperty(name, type);
 assertEquals("Unexpected RecordType", expectedType, actualType);
 }

 @Test
 public void testBuildRecordWithMultipleProperties() {
 String name1 = "name1";
 String name2 = "name2";
 JSType type1 = registry.getNativeStringType();
 JSType type2 = registry.getNativeNumberType();
 Node propertyNode1 = Node.newStringLiteral("Property Node 1");
 Node propertyNode2 = Node.newNumberLiteral(1);
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name1, type1, propertyNode1);
 builder.addProperty(name2, type2, propertyNode2);
 JSType actualType = builder.build();
 RecordType expectedType = new RecordType(registry, Maps.newHashMap());
 expectedType.putProperty(name1, type1);
 expectedType.putProperty(name2, type2);
 assertEquals("Unexpected RecordType", expectedType, actualType);
 }
}