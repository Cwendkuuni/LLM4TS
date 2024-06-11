import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node node = new Node();
    assertTrue(builder.addProperty("name", type, node) != null);
  }

  @Test
  public void testBuildEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType record = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, record.getType());
  }

  @Test
  public void testBuildRecordWithProperties() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    Node node1 = new Node();
    Node node2 = new Node();
    builder.addProperty("name1", type1, node1);
    builder.addProperty("name2", type2, node2);
    JSType record = builder.build();
    assertEquals("Expected number of properties", 2, record.getProperties().size());
  }

  @Test
  public void testDuplicatePropertyName() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node node = new Node();
    builder.addProperty("name", type, node);
    assertNull(builder.addProperty("name", type, node));
  }

}