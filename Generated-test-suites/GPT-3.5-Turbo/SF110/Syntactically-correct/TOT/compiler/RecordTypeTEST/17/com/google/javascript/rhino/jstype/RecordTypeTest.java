import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  
  private JSTypeRegistry registry = new JSTypeRegistry();
  
  @Test
  public void testRecordTypeConstructorWithProperties() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    properties.put("age", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
  
    assertNotNull(recordType);
    assertTrue(recordType.isDeclared());
    assertTrue(recordType.isFrozen());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }
  
  @Test
  public void testRecordTypeConstructorWithoutProperties() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    
    RecordType recordType = new RecordType(registry, properties);
    
    assertNotNull(recordType);
    assertTrue(recordType.isDeclared());
    assertTrue(recordType.isFrozen());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testRecordTypeConstructorWithNullProperties() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", null);
    
    new RecordType(registry, properties);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelperWithEqualRecords() {
    Map<String, RecordTypeBuilder.RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    Map<String, RecordTypeBuilder.RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    
    boolean result = recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.getDefault());
  
    assertTrue(result);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentRecords() {
    Map<String, RecordTypeBuilder.RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    Map<String, RecordTypeBuilder.RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("age", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
    
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    
    boolean result = recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.getDefault());
  
    assertFalse(result);
  }
  
  @Test
  public void testDefinePropertyWithFrozenType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
  
    boolean result = recordType.defineProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, new Node());
  
    assertFalse(result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithRecordTypes() {
    Map<String, RecordTypeBuilder.RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    Map<String, RecordTypeBuilder.RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    
    recordTypeA.freeze();
    recordTypeB.freeze();
    
    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
  
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithNonRecordType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT));
  
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }
  
  @Test
  public void testIsSubtypeWithEqualRecords() {
    Map<String, RecordTypeBuilder.RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    Map<String, RecordTypeBuilder.RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    
    boolean result = recordTypeA.isSubtype(recordTypeB);
  
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtypeWithDifferentRecords() {
    Map<String, RecordTypeBuilder.RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    Map<String, RecordTypeBuilder.RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("age", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
    
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    
    boolean result = recordTypeA.isSubtype(recordTypeB);
  
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtypeWithNonRecordType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    boolean result = recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtypeWithEmptyType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    boolean result = recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NO_TYPE));
  
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtypeWithUndefinedType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    boolean result = recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.UNDEFINED_TYPE));
  
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtypeWithNullType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    boolean result = recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
  
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtypeWithArrayType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    
    RecordType recordType = new RecordType(registry, properties);
    
    boolean result = recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.ARRAY_TYPE));
  
    assertFalse(result);
  }
}