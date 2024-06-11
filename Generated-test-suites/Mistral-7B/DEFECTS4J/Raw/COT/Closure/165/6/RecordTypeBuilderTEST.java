import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.Node.*;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    RecordType expectedRecordType = new RecordType(
        registry,
        Maps.newHashMap()
    );

    JSType actualRecordType = builder.build();

    assertEquals(expectedRecordType.getClass(), actualRecordType.getClass());
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = String.class;
    Node propertyNode = newNameNode(name);

    RecordTypeBuilder actualBuilder = builder.addProperty(name, type, propertyNode);
    RecordType expectedRecordType = new RecordType(
        registry,
        Maps.newHashMap()
    );
    expectedRecordType.put(name, type);
    JSType actualRecordType = actualBuilder.build();

    assertEquals(expectedRecordType.getClass(), actualRecordType.getClass());
    assertEquals(expectedRecordType, actualRecordType);

    assertNull(actualBuilder.addProperty(name, type, propertyNode));
  }

  @Test(expected = NullPointerException.class)
  public void testNullRegistry() {
    new RecordTypeBuilder(null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    Node propertyNode = newNameNode("name");

    builder.addProperty(null, Int.class, propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "name";
    Node propertyNode = newNameNode(name);

    builder.addProperty(name, null, propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "name";
    JSType type = Int.class;

    builder.addProperty(name, type, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = Int.class;
    Node propertyNode = newNameNode(name);

    builder.addProperty(name, type, propertyNode);
    RecordProperty duplicate = builder.addProperty(name, type, propertyNode);
  }
}