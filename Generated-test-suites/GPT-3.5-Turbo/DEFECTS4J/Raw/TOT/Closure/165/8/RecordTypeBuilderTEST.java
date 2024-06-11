import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
  
  private JSTypeRegistry registry;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }
  
  @Test
  public void testAddProperty_ExistingProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    
    // Adding a property with the same name should return null
    assertNull(builder.addProperty("name", type, propertyNode));
  }
  
  @Test
  public void testAddProperty_ValidProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    
    // Add a valid property should increment the size of properties
    assertEquals(1, builder.getProperties().size());
  }
  
  @Test
  public void testAddProperty_NullPropertyNode() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    
    // Adding a property with null property node should return null
    assertNull(builder.addProperty("name", type, null));
  }
  
  @Test
  public void testBuild_EmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Building an empty record should return the object type
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(objectType, builder.build());
  }
  
  @Test
  public void testBuild_NonEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    
    // Building a non-empty record should return a RecordType instance
    assertTrue(builder.build() instanceof RecordType);
  }
  
  @Test
  public void testBuild_RecordWithProperties() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    Node propertyNode1 = new Node();
    Node propertyNode2 = new Node();
    builder.addProperty("name1", type1, propertyNode1);
    builder.addProperty("name2", type2, propertyNode2);
    
    RecordType record = (RecordType) builder.build();
    
    // Check if the properties are correctly added to the record
    assertEquals(2, record.getProperties().size());
    assertTrue(record.getProperties().containsKey("name1"));
    assertTrue(record.getProperties().containsKey("name2"));
    assertEquals(type1, record.getProperties().get("name1").getType());
    assertEquals(type2, record.getProperties().get("name2").getType());
  }
}