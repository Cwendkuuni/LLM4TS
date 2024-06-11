import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
  
  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type1 = new RecordType(registry, new HashMap<>());
    RecordType type2 = new RecordType(registry, new HashMap<>());
    assertTrue(type1.isEquivalentTo(type2));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type = new RecordType(registry, new HashMap<>());
    ObjectType prototype = type.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
  }
  
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type = new RecordType(registry, new HashMap<>());
    boolean result = type.defineProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    assertTrue(result);
  }
  
  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type1 = new RecordType(registry, new HashMap<>());
    RecordType type2 = new RecordType(registry, new HashMap<>());
    JSType leastSupertype = type1.getLeastSupertype(type2);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);
  }
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type1 = new RecordType(registry, new HashMap<>());
    RecordType type2 = new RecordType(registry, new HashMap<>());
    assertTrue(type1.isSubtype(type2));
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter reporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType type = new RecordType(registry, new HashMap<>());
    JSType resolvedType = type.resolveInternal(reporter, scope);
    assertEquals(type, resolvedType);
  }
  
}