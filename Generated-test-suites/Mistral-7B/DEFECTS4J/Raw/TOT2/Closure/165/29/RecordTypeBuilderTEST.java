
import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeRegistryType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilderConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    // Test constructor with an instance of JSTypeRegistry
    assertNotNull(builder);
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = new IntType();
    Node propertyNode = null; // Can be provided if needed

    RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);
    assertTrue(recordProperty != null);
    assertEquals(type, recordProperty.getType());
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name1 = "name1";
    String name2 = "name2";

    JSType intType = new IntType();
    JSType stringType = new StringType();

    builder.addProperty(name1, intType, null);
    builder.addProperty(name2, stringType, null);

    JSType recordType = builder.build();
    assertFalse(recordType instanceof JSTypeNative);

    // Check if properties were added correctly
    Map<String, JSType> properties = ((RecordType) recordType).getProperties();
    assertEquals(2, properties.size());
    assertEquals(intType, properties.get(name1));
    assertEquals(stringType, properties.get(name2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullJSTypeRegistry() {
    RecordTypeBuilder builder = new RecordTypeBuilder(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicatePropertyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType intType = new IntType();

    builder.addProperty(name, intType, null);
    builder.addProperty(name, intType, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "name";
    Node propertyNode = null;
    JSType invalidType = new ObjectType(); // Not a valid type for this test

    builder.addProperty(name, invalidType, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType intType = new IntType();

    builder.addProperty(name, intType, null);
  }

  @Test
  public void testEmptyBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType recordType = builder.build();
    assertTrue(recordType instanceof JSTypeNative);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
  }
}