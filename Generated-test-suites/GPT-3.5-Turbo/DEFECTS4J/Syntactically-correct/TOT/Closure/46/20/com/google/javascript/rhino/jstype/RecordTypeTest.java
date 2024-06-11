import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  // Test case for isEquivalentTo() method
  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Create record type with properties { a : NUMBER, b : STRING }
    RecordType recordType1 = builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER))
                                      .addProperty("b", registry.getNativeType(JSTypeNative.STRING))
                                      .build();
                                      
    // Create record type with properties { a : NUMBER, b : STRING }
    RecordType recordType2 = builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER))
                                      .addProperty("b", registry.getNativeType(JSTypeNative.STRING))
                                      .build();
                                      
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  // Test case for defineProperty() method
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Create record type without any properties
    RecordType recordType = builder.build();
    
    // Define a property "a" with type NUMBER
    recordType.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER), false, null);
    
    // Check if the property "a" is defined
    assertTrue(recordType.hasProperty("a"));
    
    // Check the type of property "a" is NUMBER
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER), recordType.getPropertyType("a"));
  }

  // Test case for getLeastSupertype() method
  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Create record type with properties { a : NUMBER, b : STRING }
    RecordType recordType1 = builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER))
                                      .addProperty("b", registry.getNativeType(JSTypeNative.STRING))
                                      .build();
                                      
    // Create record type with properties { a : NUMBER }
    RecordType recordType2 = builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER))
                                      .build();
                                      
    // Get the least supertype of recordType1 and recordType2
    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
    
    // Check if the least supertype has property "a"
    assertTrue(leastSupertype.hasProperty("a"));
    
    // Check if the type of property "a" is NUMBER
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER), leastSupertype.getPropertyType("a"));
    
    // Check if the least supertype has only property "a"
    assertFalse(leastSupertype.hasProperty("b"));
  }

  // Test case for isSubtype() method
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Create record type with properties { a : NUMBER }
    RecordType recordType1 = builder.addProperty("a", registry.getNativeType(JSTypeNative.NUMBER))
                                      .build();
                                      
    // Create record type with properties { a : FOO }
    RecordType recordType2 = builder.addProperty("a", registry.getGlobalType("FOO"))
                                      .build();
                                      
    assertTrue(recordType1.isSubtype(recordType2));
  }

  // Test case for resolveInternal() method
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    
    // Create record type with property { a : UNKNOWN_TYPE }
    RecordType recordType = builder.addProperty("a", registry.getNativeType(JSTypeNative.UNKNOWN_TYPE))
                                     .build();
                                     
    StaticScope<JSType> scope = new StaticScope<JSType>();
    
    // Resolve the record type
    JSType resolvedType = recordType.resolveInternal(null, scope);
    
    // Check if the resolved type of property "a" is UNKNOWN_TYPE
    assertEquals(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), resolvedType.getPropertyType("a"));
  }
}