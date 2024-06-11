import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder builder;
  
  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty() {
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();

    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    
    assertEquals(builder, result);
    assertEquals(1, builder.properties.size());
    assertEquals(type, builder.properties.get(name).getType());
    assertEquals(propertyNode, builder.properties.get(name).getPropertyNode());
  }

  @Test
  public void testAddDuplicateProperty() {
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();

    builder.addProperty(name, type, propertyNode);
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    
    assertEquals(null, result);
    assertEquals(1, builder.properties.size());
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType result = builder.build();
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testBuildNonEmptyRecord() {
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();

    builder.addProperty(name, type, propertyNode);
    JSType result = builder.build();
    
    assertEquals(new RecordType(registry, Collections.unmodifiableMap(properties)), result);
  }
}