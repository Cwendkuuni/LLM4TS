import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder.RecordProperty recordProperty = recordTypeBuilder.addProperty("name", type, propertyNode);
    assertNotNull(recordProperty);
    assertEquals(type, recordProperty.getType());
    assertEquals(propertyNode, recordProperty.getPropertyNode());
  }

  @Test
  public void testBuildWithEmptyRecord() {
    JSType recordType = recordTypeBuilder.build();
    JSType objectType = recordTypeBuilder.getRegistry().getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(objectType, recordType);
  }

  @Test
  public void testBuildWithNonEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    recordTypeBuilder.addProperty("prop1", new JSType(), new Node());
    recordTypeBuilder.addProperty("prop2", new JSType(), new Node());
    JSType recordType = recordTypeBuilder.build();
    RecordType expected = new RecordType(registry, Collections.unmodifiableMap(recordTypeBuilder.getProperties()));
    assertEquals(expected, recordType);
  }
}