import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeClass;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    JSType type = JSType.STRING;
    Node propertyNode = Node.newLiteral("propertyName");
    RecordProperty recordProperty = builder.addProperty("propertyName", type, propertyNode);
    assertNotNull(recordProperty);
  }

  @Test
  public void testAddDuplicateProperty() {
    JSType type = JSType.STRING;
    Node propertyNode = Node.newLiteral("propertyName");
    builder.addProperty("propertyName", type, propertyNode);
    assertNull(builder.addProperty("propertyName", type, propertyNode));
  }

  @Test
  public void testBuildEmptyRecordType() {
    JSType recordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
  }

  @Test
  public void testBuildRecordType() {
    JSType stringType = JSType.STRING;
    Node stringPropertyNode = Node.newLiteral("propertyName");
    RecordProperty recordProperty = builder.addProperty("propertyName", stringType, stringPropertyNode);
    JSType recordType = builder.build();
    RecordType expectedRecordType = new RecordType(registry, Collections.singletonMap("propertyName", recordProperty));
    assertEquals(expectedRecordType.getTypeClass(), recordType.getTypeClass());
  }

  @Test
  public void testAddPropertyWithNullRegistry() {
    RecordTypeBuilder builder = new RecordTypeBuilder(null);
    assertNull(builder);
  }

  @Test
  public void testAddPropertyWithNullPropertyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    Node propertyNode = Node.newLiteral("propertyName");
    assertNull(builder.addProperty(null, JSType.STRING, propertyNode));
  }

  @Test
  public void testAddPropertyWithNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "propertyName";
    Node propertyNode = Node.newLiteral("propertyName");
    assertNull(builder.addProperty(propertyName, null, propertyNode));
  }

  @Test
  public void testAddPropertyWithNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNull(builder.addProperty("propertyName", JSType.STRING, null));
  }

  @Test
  public void testAddPropertyWithEmptyPropertyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNull(builder.addProperty("", JSType.STRING, Node.newLiteral("propertyName")));
  }

  @Test
  public void testAddPropertyWithInvalidType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "propertyName";
    Node propertyNode = Node.newLiteral("propertyName");
    assertNull(builder.addProperty(propertyName, new InvalidType(), propertyNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithNullRegistry() {
    RecordTypeBuilder builder = new RecordTypeBuilder(null);
    builder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "propertyName";
    Node propertyNode = Node.newLiteral("propertyName");
    builder.addProperty(propertyName, null, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("propertyName", JSType.STRING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithEmptyPropertyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("", JSType.STRING, Node.newLiteral("propertyName"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithInvalidPropertyName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty(Node.newLiteral("invalidPropertyName"), JSType.STRING, Node.newLiteral("propertyName"));
  }

  @Test
  public void testBuildRecordWithDuplicateProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType type = JSType.STRING;
    String propertyName = "propertyName";
    Node propertyNode = Node.newLiteral("propertyName");

    RecordProperty firstRecordProperty = builder.addProperty(propertyName, type, propertyNode);
    RecordProperty secondRecordProperty = builder.addProperty(propertyName, type, propertyNode);

    try {
      builder.build();
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Duplicate property name"));
    }
  }

  private static class InvalidType implements JSType {
    @Override
    public JSTypeClass getTypeClass() {
      throw new UnsupportedOperationException();
    }
  }
}