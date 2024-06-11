import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder recordTypeBuilder;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty("property1", type, propertyNode);

    RecordTypeBuilder.RecordProperty recordProperty = recordTypeBuilder.getProperties().get("property1");
    assertNotNull(recordProperty);
    assertEquals(type, recordProperty.getType());
    assertEquals(propertyNode, recordProperty.getPropertyNode());
  }

  @Test
  public void testAddDuplicateProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty("property1", type, propertyNode);

    RecordTypeBuilder duplicateRecordTypeBuilder = new RecordTypeBuilder(registry);
    assertNull(duplicateRecordTypeBuilder.addProperty("property1", type, propertyNode));
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType emptyRecord = recordTypeBuilder.build();

    assertEquals(objectType, emptyRecord);
  }

  @Test
  public void testBuildRecord() {
    JSType type1 = new JSType();
    Node propertyNode1 = new Node();
    JSType type2 = new JSType();
    Node propertyNode2 = new Node();
    recordTypeBuilder.addProperty("property1", type1, propertyNode1);
    recordTypeBuilder.addProperty("property2", type2, propertyNode2);

    JSType record = recordTypeBuilder.build();

    assertTrue(record instanceof RecordType);
    assertEquals(Collections.unmodifiableMap(recordTypeBuilder.getProperties()), ((RecordType) record).getProperties());
  }
}