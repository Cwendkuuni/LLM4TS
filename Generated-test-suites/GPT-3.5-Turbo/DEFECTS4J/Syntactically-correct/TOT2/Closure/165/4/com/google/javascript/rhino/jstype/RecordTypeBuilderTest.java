import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  @Test
  public void testAddProperty() {
    // Typical use case
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);
    assertNotNull(result);

    // Edge case: Adding duplicate property
    result = builder.addProperty("testProperty", type, propertyNode);
    assertNull(result);
  }
  
  @Test
  public void testBuild() {
    // Edge case: Empty record
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType emptyType = builder.build();

    assertEquals(JSTypeNative.OBJECT_TYPE, emptyType.getNativeType());
    
    // Typical use case
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type1 = new JSType();
    Node propertyNode1 = new Node();
    builder.addProperty("property1", type1, propertyNode1);
    
    JSType type2 = new JSType();
    Node propertyNode2 = new Node();
    builder.addProperty("property2", type2, propertyNode2);
    
    JSType recordType = builder.build();
    
    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(2, ((RecordType) recordType).getProperties().size());
  }

}