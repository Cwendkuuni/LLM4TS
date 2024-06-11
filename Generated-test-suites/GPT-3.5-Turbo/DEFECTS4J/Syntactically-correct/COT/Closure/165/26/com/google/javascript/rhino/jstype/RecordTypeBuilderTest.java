import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder builder;
  
  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(new JSTypeRegistry());
  }
  
  @Test
  public void testAddProperty() {
    JSType type = new JSType();
    Node node = new Node();
    RecordTypeBuilder result = builder.addProperty("property1", type, node);
    assertNotNull(result);
  }
  
  @Test
  public void testBuild_emptyRecord() {
    JSType result = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeType());
  }
  
  @Test
  public void testBuild_nonEmptyRecord() {
    JSType type = new JSType();
    Node node = new Node();
    builder.addProperty("property1", type, node);
    JSType result = builder.build();
    assertEquals(RecordType.class, result.getClass());
  }
  
  @Test
  public void testBuild_nonEmptyRecord_propertyAddedTwice() {
    JSType type = new JSType();
    Node node = new Node();
    builder.addProperty("property1", type, node);
    builder.addProperty("property1", type, node);
    JSType result = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeType());
  }
}