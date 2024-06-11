import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("String", null));
    properties.put("age", new RecordProperty("int", null));
    
    RecordType recordType = new RecordType(registry, properties);
    
    Assert.assertNotNull(recordType);
    Assert.assertTrue(recordType.isSynthetic());
    Assert.assertFalse(recordType.isSubtype(null));
    Assert.assertTrue(recordType.isSubtype(recordType));
    Assert.assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }
  
  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("String", null));
    properties.put("age", new RecordProperty("int", null));
    
    RecordType recordType = new RecordType(registry, properties);
    
    // Try defining a property when the record type is frozen.
    boolean result = recordType.defineProperty("address", registry.getNativeType(JSTypeNative.OBJECT_TYPE), true, new Node());
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testRecordTypeCheckEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("String", null));
    properties1.put("age", new RecordProperty("int", null));
    
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("String", null));
    properties2.put("age", new RecordProperty("float", null));
    
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    
    boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.EQUALS);
    
    Assert.assertFalse(result);
  }
  
}