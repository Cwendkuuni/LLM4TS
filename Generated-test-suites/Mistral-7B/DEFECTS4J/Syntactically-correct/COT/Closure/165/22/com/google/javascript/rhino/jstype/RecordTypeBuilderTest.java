import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

/**
 * Test cases for the RecordTypeBuilder class.
 */
public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = Node.newLiteralNode("propertyNodeValue");

    // Test adding a property with the same name results in an error and null is returned
    assertNull(builder.addProperty(name, type, propertyNode));

    // Test adding a property successfully
    builder.addProperty(name, type, propertyNode);
    RecordType recordType = builder.build();
    RecordProperty recordProperty = ((RecordType) recordType).getProperties().get(name);
    assertEquals(type, recordProperty.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = null;
    JSType type = JSType.STRING;
    Node propertyNode = Node.newLiteralNode("propertyNodeValue");

    builder.addProperty(name, type, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = null;
    Node propertyNode = Node.newLiteralNode("propertyNodeValue");

    builder.addProperty(name, type, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = null;

    builder.addProperty(name, type, propertyNode);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name1 = "testProperty1";
    String name2 = "testProperty2";
    JSType type1 = JSType.STRING;
    JSType type2 = JSType.NUMBER;
    Node propertyNode1 = Node.newLiteralNode("propertyNodeValue1");
    Node propertyNode2 = Node.newLiteralNode("propertyNodeValue2");

    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);

    JSType recordType = builder.build();
    RecordProperty recordProperty1 = ((RecordType) recordType).getProperties().get(name1);
    RecordProperty recordProperty2 = ((RecordType) recordType).getProperties().get(name2);

    assertEquals(type1, recordProperty1.getType());
    assertEquals(type2, recordProperty2.getType());
  }
}