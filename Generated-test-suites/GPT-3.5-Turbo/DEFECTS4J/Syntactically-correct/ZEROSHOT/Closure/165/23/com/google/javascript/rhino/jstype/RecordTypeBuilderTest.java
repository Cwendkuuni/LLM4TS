import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder recordTypeBuilder;
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNotNull(result);
    assertEquals(result, recordTypeBuilder);
  }

  @Test
  public void testAddPropertyDuplicate() {
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);

    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);

    assertNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testBuildNonEmptyRecord() {
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(name, type, propertyNode);

    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
    assertTrue(result instanceof RecordType);
  }
}