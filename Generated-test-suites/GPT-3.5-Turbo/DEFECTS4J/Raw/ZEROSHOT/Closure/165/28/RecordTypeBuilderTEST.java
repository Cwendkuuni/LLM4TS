import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder recordTypeBuilder;
  private JSTypeRegistry registry;
  private JSType objectType;
  private JSType recordType;
  private Node propertyNode;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
    objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    propertyNode = new Node(1, "property_node");
  }

  @Test
  public void testAddProperty() {
    recordTypeBuilder.addProperty("name", objectType, propertyNode);
    assertEquals(objectType, recordTypeBuilder.getProperties().get("name").getType());
    assertEquals(propertyNode, recordTypeBuilder.getProperties().get("name").getPropertyNode());
  }

  @Test
  public void testAddPropertyDuplicate() {
    recordTypeBuilder.addProperty("name", objectType, propertyNode);
    assertNull(recordTypeBuilder.addProperty("name", objectType, propertyNode));
  }

  @Test
  public void testBuildEmptyRecord() {
    recordType = recordTypeBuilder.build();
    assertEquals(objectType, recordType);
  }

  @Test
  public void testBuildRecord() {
    recordTypeBuilder.addProperty("name", objectType, propertyNode);
    recordType = recordTypeBuilder.build();
    assertEquals(objectType, recordType.getSuperClass());
    assertEquals(1, recordType.getProperties().size());
  }

}