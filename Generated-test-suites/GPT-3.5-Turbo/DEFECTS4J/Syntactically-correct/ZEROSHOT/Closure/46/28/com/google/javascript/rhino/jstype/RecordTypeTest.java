import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {
  
  private RecordType recordType;
  
  @Before
  public void setUp() {
    recordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testIsEquivalentTo() {
    RecordType otherRecord = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(otherRecord));
   
    RecordProperty prop = new RecordProperty("property", JSTypeNative.STRING_TYPE, null);
    otherRecord.defineProperty("property", JSTypeNative.STRING_TYPE, false, prop.getPropertyNode());
    assertFalse(recordType.isEquivalentTo(otherRecord));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    ObjectType prototype = recordType.getImplicitPrototype();
    assertTrue(prototype instanceof NativeObjectType);
    assertEquals(JSTypeNative.OBJECT_TYPE, prototype.getReferenceName());
  }
  
  @Test
  public void testDefineProperty() {
    String propertyName = "property";
    JSType type = JSTypeNative.STRING_TYPE;
    boolean inferred = false;
    Node propertyNode = null;
    
    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    assertTrue(result);
    
    JSType propertyType = recordType.getPropertyType(propertyName);
    assertEquals(type, propertyType);
  }
  
  @Test
  public void testGetLeastSupertype() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType leastSupertype = builder.build();
    JSType result = recordType.getLeastSupertype(leastSupertype);
    assertEquals(recordType, result);
    
    RecordProperty prop = new RecordProperty("property", JSTypeNative.STRING_TYPE, null);
    builder.addProperty("property", JSTypeNative.STRING_TYPE, prop.getPropertyNode());
    leastSupertype = builder.build();
    result = recordType.getLeastSupertype(leastSupertype);
    assertEquals(builder.build(), result);
  }
  
  @Test
  public void testToMaybeRecordType() {
    RecordType result = recordType.toMaybeRecordType();
    assertEquals(recordType, result);
  }
  
  @Test
  public void testIsSubtype() {
    RecordType otherType = new RecordType(registry, properties);
    assertTrue(recordType.isSubtype(otherType));
    
    assertFalse(recordType.isSubtype(JSTypeNative.NO_OBJECT_TYPE));
    
    JSType type = new JSType(registry, JSTypeNative.OBJECT_TYPE, null, null);
    assertFalse(recordType.isSubtype(type));
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = null;
    StaticScope<JSType> scope = null;
    
    JSType resolvedType = recordType.resolveInternal(t, scope);
    assertEquals(recordType, resolvedType);
    
    JSType propertyType = recordType.getPropertyType("property");
    JSType resolvedPropertyType = propertyType.resolve(t, scope);
    assertEquals(propertyType, resolvedPropertyType);
  }
}