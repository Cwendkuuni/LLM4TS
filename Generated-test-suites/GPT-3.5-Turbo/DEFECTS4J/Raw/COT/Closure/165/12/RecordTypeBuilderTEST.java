import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new SomeJSType();
    Node propertyNode = new Node();
    assertTrue(builder.addProperty("name", type, propertyNode) != null);
  }

  @Test
  public void testBuild() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new SomeJSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
  }

  @Test
  public void testEmptyBuild() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType recordType = builder.build();
    assertTrue(recordType instanceof ObjectType);
  }

}