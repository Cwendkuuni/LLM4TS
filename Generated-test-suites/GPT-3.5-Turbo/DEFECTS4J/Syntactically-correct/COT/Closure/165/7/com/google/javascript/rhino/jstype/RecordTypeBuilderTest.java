
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
  
  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test adding a property with a valid name and type
    String propertyName = "testProperty";
    JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(result);
    
    // Test adding a property with a duplicate name
    result = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNull(result);
  }
  
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Test building an empty record
    JSType emptyRecordType = builder.build();
    assertTrue(emptyRecordType instanceof ObjectType);
    
    // Test building a record with properties
    String propertyName = "testProperty";
    JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    builder.addProperty(propertyName, propertyType, propertyNode);
    JSType recordType = builder.build();
    assertTrue(recordType instanceof RecordType);
    RecordType recordTypeObj = (RecordType) recordType;
    assertTrue(recordTypeObj.getPropertyType(propertyName) != null);
    
  }
}
