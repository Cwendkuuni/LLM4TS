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
  public void testEmptyRecord() {
    JSType recordType = recordTypeBuilder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
  }
  
  @Test
  public void testAddProperty() {
    JSType type = registry.createTypeFromName("string");
    Node propertyNode = new Node();
    RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(type, propertyNode);
    
    recordTypeBuilder.addProperty("name", type, propertyNode);
    
    assertEquals(property, recordTypeBuilder.properties.get("name"));
  }
  
  @Test
  public void testAddDuplicateProperty() {
    JSType type = registry.createTypeFromName("string");
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty("name", type, propertyNode);
    RecordTypeBuilder builder = recordTypeBuilder.addProperty("name", type, propertyNode);
    
    assertNull(builder);
  }
  
  @Test
  public void testBuild() {
    JSType type = registry.createTypeFromName("string");
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty("name", type, propertyNode);
    
    JSType recordType = recordTypeBuilder.build();
    RecordType expectedRecordType = new RecordType(
      registry, Collections.unmodifiableMap(recordTypeBuilder.properties));
    
    assertEquals(expectedRecordType, recordType);
  }
}