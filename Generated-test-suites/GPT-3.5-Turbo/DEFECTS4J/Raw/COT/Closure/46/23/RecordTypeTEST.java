import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
  
  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    // Create record types with the same properties but different order
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType type1 = new RecordType(registry, properties1);
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    RecordType type2 = new RecordType(registry, properties2);
    
    assertTrue(type1.isEquivalentTo(type2));
  }
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    // Create record type with properties
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    RecordType type = new RecordType(registry, properties);
    
    // Create subtype with extra property
    Map<String, RecordProperty> propertiesSubtype = new HashMap<>();
    propertiesSubtype.putAll(properties);
    propertiesSubtype.put("d", new RecordProperty(registry.getNativeType(JSTypeNative.NULL_TYPE), null));
    RecordType subtype = new RecordType(registry, propertiesSubtype);
    
    assertTrue(subtype.isSubtype(type));
  }
  
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    // Create record type with property
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType type = new RecordType(registry, properties);
    
    // Create error reporter and scope
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    // Resolve type
    JSType resolvedType = type.resolveInternal(errorReporter, scope);
    
    // Check that property has been resolved
    assertTrue(resolvedType.hasProperty("a"));
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), resolvedType.getPropertyType("a"));
  }
  
  // Add more test cases for other methods...
  
}