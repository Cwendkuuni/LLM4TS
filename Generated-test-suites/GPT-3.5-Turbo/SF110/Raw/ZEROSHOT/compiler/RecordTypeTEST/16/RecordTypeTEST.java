import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
  
  private RecordType recordType;
  private JSTypeRegistry registry;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    properties.put("property2", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
    recordType = new RecordType(registry, properties, true);
  }
  
  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(registry, new HashMap<>(), true);
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.STRICT));
  }
  
  @Test
  public void testGetPropertyType() {
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property1"));
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property2"));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
    assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
  }
  
  @Test
  public void testIsSubtype() {
    ObjectType typeA = new ObjectType(registry, null, null);
    assertFalse(RecordType.isSubtype(typeA, recordType));
  }
  
  @Test
  public void testIsSubtypeRecordType() {
    RecordType typeB = new RecordType(registry, new HashMap<>(), true);
    assertTrue(RecordType.isSubtype(recordType, typeB));
  }
}