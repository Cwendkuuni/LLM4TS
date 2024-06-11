import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {
  
  private JSTypeRegistry registry;
  private RecordType recordType;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }
  
  @Test
  public void testConstructorWithDeclaredTrue() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    properties.put("property2", new RecordProperty(JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null));
    recordType = new RecordType(registry, properties, true);
    
    assertEquals(properties.size(), recordType.getOwnPropertyNames().size());
  }
  
  @Test
  public void testConstructorWithDeclaredFalse() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    properties.put("property2", new RecordProperty(JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null));
    recordType = new RecordType(registry, properties, false);
    
    assertEquals(properties.size(), recordType.getOwnPropertyNames().size());
  }
  
  @Test
  public void testIsSynthetic() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(JSTypeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    properties.put("property2", new RecordProperty(JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null));
    recordType = new RecordType(registry, properties, false);
    
    assertFalse(recordType.isSynthetic());
  }
  
  // add more test methods for other methods in the RecordType class
  
}