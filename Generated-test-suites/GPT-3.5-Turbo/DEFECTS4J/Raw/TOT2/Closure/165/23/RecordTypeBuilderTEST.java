import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder();
    builder.addProperty("name", new JSType(), new Node());
    assertNotNull(builder.addProperty("name", new JSType(), new Node()));
  }

  @Test
  public void testBuildEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder();
    JSType result = builder.build();
    assertEquals(result, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testBuildNonEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder();
    builder.addProperty("name", new JSType(), new Node());
    JSType result = builder.build();
    assertNotNull(result);
  }

  @Test
  public void testDuplicateProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder();
    builder.addProperty("name", new JSType(), new Node());
    assertNull(builder.addProperty("name", new JSType(), new Node()));
  }

}