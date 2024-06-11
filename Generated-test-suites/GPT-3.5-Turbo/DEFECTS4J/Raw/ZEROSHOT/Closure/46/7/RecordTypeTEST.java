import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType record2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    assertTrue(record1.isEquivalentTo(record2));
  }
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType record2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    assertTrue(record1.isSubtype(record2));
  }
  
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, new HashMap<String, RecordProperty>());
    boolean result = record.defineProperty("property", new JSType(), false, new Node());
    assertTrue(result);
  }
  
  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType record2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    JSType supertype = record1.getLeastSupertype(record2);
    assertNotNull(supertype);
  }
  
  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType record2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    JSType subtype = record1.getGreatestSubtype(record2);
    assertNotNull(subtype);
  }
  
  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType maybeRecord = record.toMaybeRecordType();
    assertNotNull(maybeRecord);
    assertEquals(record, maybeRecord);
  }
  
  @Test
  public void testIsSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType record2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    boolean result = JSType.isSubtypeHelper(record1, record2);
    assertTrue(result);
  }
  
  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, new HashMap<String, RecordProperty>());
    JSType propertyType = record.getPropertyType("property");
    assertNull(propertyType);
  }
  
  @Test
  public void testHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, new HashMap<String, RecordProperty>());
    boolean result = record.hasProperty("property");
    assertFalse(result);
  }
  
  @Test
  public void testIsRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, new HashMap<String, RecordProperty>());
    boolean result = record.isRecordType();
    assertTrue(result);
  }

}