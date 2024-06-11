import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty_duplicateProperty() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      builder.addProperty("name", JSType.STRING, new Node());
      assertNull(builder.addProperty("name", JSType.NUMBER, new Node()));
  }

  @Test
  public void testAddProperty_edgeCases() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      builder.addProperty("", JSType.STRING, new Node()); // empty property name
      builder.addProperty("age", null, new Node()); // null type
      builder.addProperty("height", JSType.NUMBER, null); // null property node
      // More edge cases can be added here
  }

  @Test
  public void testBuild_emptyRecord() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      JSType recordType = builder.build();
      assertEquals(recordType, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testBuild_nonEmptyRecord() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      builder.addProperty("name", JSType.STRING, new Node());
      builder.addProperty("age", JSType.NUMBER, new Node());
      JSType recordType = builder.build();
      // Perform assertions to check if the record type is correctly built
  }

  // More test methods can be added here to cover all public methods in RecordTypeBuilder class

}