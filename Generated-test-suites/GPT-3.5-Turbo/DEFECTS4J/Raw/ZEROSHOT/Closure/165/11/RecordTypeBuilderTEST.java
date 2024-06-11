import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty_duplicateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    JSType type1 = new JSType();
    Node propertyNode1 = new Node();
    JSType type2 = new JSType();
    Node propertyNode2 = new Node();
    
    builder.addProperty("name", type1, propertyNode1);
    RecordTypeBuilder result = builder.addProperty("name", type2, propertyNode2);
    
    assertNull(result);
  }
  
  @Test
  public void testAddProperty_validName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    
    assertNotNull(result);
  }
  
  @Test
  public void testBuild_emptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    JSType result = builder.build();
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }
  
  @Test
  public void testBuild_nonEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    JSType type1 = new JSType();
    Node propertyNode1 = new Node();
    JSType type2 = new JSType();
    Node propertyNode2 = new Node();
    
    builder.addProperty("name1", type1, propertyNode1);
    builder.addProperty("name2", type2, propertyNode2);
    
    JSType result = builder.build();
    
    assertEquals(new RecordType(registry, Collections.unmodifiableMap(builder.properties)), result);
  }
}