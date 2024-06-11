import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
  
  private RecordType recordType;
  
  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties.put("property2", new RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    
    recordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(new JSTypeRegistry(), new HashMap<>());
    
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }
  
  @Test
  public void testGetPropertyType() {
    assertEquals(recordType.getPropertyType("property1"), recordType.registry.getNativeType(JSTypeNative.STRING_TYPE));
  }
  
  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("newProperty", recordType.registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    ObjectType that = recordType.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    assertEquals(recordType.getGreatestSubtypeHelper(that), recordType.registry.getNativeType(JSTypeNative.NO_TYPE));
  }
  
  @Test
  public void testIsSubtype() {
    ObjectType that = recordType.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    assertTrue(recordType.isSubtype(that));
  }
  
}