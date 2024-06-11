import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    RecordType record1 = new RecordType(registry, properties);
    RecordType record2 = new RecordType(registry, properties);
    
    assertTrue(record1.isEquivalentTo(record2));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    RecordType record = new RecordType(registry, properties);
    ObjectType prototype = record.getImplicitPrototype();
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
  }
  
  @Test
  public void testDefineProperty() {
    RecordType record = new RecordType(registry, properties);
    boolean defined = record.defineProperty("property", type, inferred, propertyNode);
    
    assertTrue(defined);
  }
  
  @Test
  public void testGetLeastSupertype() {
    RecordType record1 = new RecordType(registry, properties);
    RecordType record2 = new RecordType(registry, properties);
    
    JSType leastSupertype = record1.getLeastSupertype(record2);
    
    assertEquals(record1, leastSupertype);
  }
  
  @Test
  public void testGreatestSubtypeHelper() {
    RecordType record = new RecordType(registry, properties);
    JSType subtype = record.getGreatestSubtypeHelper(that);
    
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), subtype);
  }
  
  @Test
  public void testIsSubtype() {
    RecordType record1 = new RecordType(registry, properties);
    RecordType record2 = new RecordType(registry, properties);
    
    assertTrue(record1.isSubtype(record2));
  }
  
  @Test
  public void testResolveInternal() {
    RecordType record = new RecordType(registry, properties);
    JSType resolvedType = record.resolveInternal(t, scope);
    
    assertEquals(record, resolvedType);
  }
  
}