
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    RecordTypeBuilder.RecordProperty property = builder.properties.get("name");
    assertNotNull(property);
    assertEquals(type, property.getType());
    assertEquals(propertyNode, property.getPropertyNode());
  }

  @Test
  public void testBuildEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = builder.build();
    assertNotNull(type);
    assertTrue(type instanceof ObjectType);
    assertEquals(JSTypeNative.OBJECT_TYPE, type.getDisplayName());
  }

  @Test
  public void testBuildRecordWithProperties() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    Node propertyNode1 = new Node();
    Node propertyNode2 = new Node();
    builder.addProperty("name1", type1, propertyNode1);
    builder.addProperty("name2", type2, propertyNode2);
    JSType type = builder.build();
    assertNotNull(type);
    assertTrue(type instanceof RecordType);
    RecordType recordType = (RecordType) type;
    assertEquals(2, recordType.getProperties().size());
    assertTrue(recordType.getProperties().containsKey("name1"));
    assertTrue(recordType.getProperties().containsKey("name2"));
    assertEquals(type1, recordType.getProperties().get("name1").getType());
    assertEquals(propertyNode1, recordType.getProperties().get("name1").getPropertyNode());
    assertEquals(type2, recordType.getProperties().get("name2").getType());
    assertEquals(propertyNode2, recordType.getProperties().get("name2").getPropertyNode());
  }
}

