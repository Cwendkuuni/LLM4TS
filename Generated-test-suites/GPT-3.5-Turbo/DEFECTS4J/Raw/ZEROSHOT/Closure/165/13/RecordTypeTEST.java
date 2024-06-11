import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  
  private JSTypeRegistry registry;
  private RecordType recordType;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    
    // create properties for recordType
    Map<String, RecordProperty> properties = new HashMap<>();
    
    RecordProperty prop1 = new RecordProperty();
    prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
    prop1.setPropertyNode(new Node(Token.STRING_KEY));
    properties.put("prop1", prop1);
    
    RecordProperty prop2 = new RecordProperty();
    prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    prop2.setPropertyNode(new Node(Token.NUMBER_KEY));
    properties.put("prop2", prop2);
    
    recordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testIsEquivalentTo_SameRecordType() {
    RecordType sameRecordType = new RecordType(registry, recordType.properties);
    assertTrue(recordType.isEquivalentTo(sameRecordType));
  }
  
  @Test
  public void testIsEquivalentTo_DifferentRecordType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    
    RecordProperty prop1 = new RecordProperty();
    prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
    prop1.setPropertyNode(new Node(Token.STRING_KEY));
    properties.put("prop1", prop1);
    
    RecordProperty prop2 = new RecordProperty();
    prop2.setType(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
    prop2.setPropertyNode(new Node(Token.STRING_KEY));
    properties.put("prop2", prop2);
    
    RecordType differentRecordType = new RecordType(registry, properties);
    assertFalse(recordType.isEquivalentTo(differentRecordType));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
    assertTrue(implicitPrototype instanceof ObjectType);
  }
  
  @Test
  public void testIsSubtype_SameRecordType() {
    RecordType sameRecordType = new RecordType(registry, recordType.properties);
    assertTrue(recordType.isSubtype(sameRecordType));
  }
  
  @Test
  public void testIsSubtype_Subtype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    
    RecordProperty prop1 = new RecordProperty();
    prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
    prop1.setPropertyNode(new Node(Token.STRING_KEY));
    properties.put("prop1", prop1);
    
    RecordProperty prop2 = new RecordProperty();
    prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    prop2.setPropertyNode(new Node(Token.NUMBER_KEY));
    properties.put("prop2", prop2);
    
    RecordType subtype = new RecordType(registry, properties);
    assertTrue(subtype.isSubtype(recordType));
  }
  
  @Test
  public void testIsSubtype_NotSubtype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    
    RecordProperty prop1 = new RecordProperty();
    prop1.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
    prop1.setPropertyNode(new Node(Token.BOOLEAN_KEY));
    properties.put("prop1", prop1);
    
    RecordType notSubtype = new RecordType(registry, properties);
    assertFalse(notSubtype.isSubtype(recordType));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType otherRecordType = new RecordType(registry, recordType.properties);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecordType);
    assertTrue(greatestSubtype.isRecordType());
  }
  
  @Test
  public void testDefineProperty() {
    boolean result = recordType.defineProperty("prop3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, new Node(Token.BOOLEAN_KEY));
    assertTrue(result);
  }
  
  @Test
  public void testIsRecordType() {
    assertTrue(recordType.isRecordType());
  }
  
  @Test
  public void testToMaybeRecordType() {
    assertTrue(recordType.toMaybeRecordType() instanceof RecordType);
  }
  
  @Test
  public void testIsSubtype_RecordType() {
    ObjectType objectType = new ObjectType(registry);
    assertTrue(RecordType.isSubtype(objectType, recordType));
  }
  
  @Test
  public void testIsSubtype_SameRecordType_StaticMethod() {
    assertTrue(RecordType.isSubtype(recordType, recordType));
  }
  
  @Test
  public void testIsSubtype_Subtype_StaticMethod() {
    Map<String, RecordProperty> properties = new HashMap<>();
    
    RecordProperty prop1 = new RecordProperty();
    prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
    prop1.setPropertyNode(new Node(Token.STRING_KEY));
    properties.put("prop1", prop1);
    
    RecordProperty prop2 = new RecordProperty();
    prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    prop2.setPropertyNode(new Node(Token.NUMBER_KEY));
    properties.put("prop2", prop2);
    
    RecordType subtype = new RecordType(registry, properties);
    assertTrue(RecordType.isSubtype(subtype, recordType));
  }
  
  @Test
  public void testIsSubtype_NotSubtype_StaticMethod() {
    Map<String, RecordProperty> properties = new HashMap<>();
    
    RecordProperty prop1 = new RecordProperty();
    prop1.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
    prop1.setPropertyNode(new Node(Token.BOOLEAN_KEY));
    properties.put("prop1", prop1);
    
    RecordType notSubtype = new RecordType(registry, properties);
    assertFalse(RecordType.isSubtype(notSubtype, recordType));
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter reporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(reporter, scope);
    assertEquals(recordType, resolvedType);
  }

}