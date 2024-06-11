import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty prop1 = new RecordProperty(TYPE_1, propertyNode1);
    RecordProperty prop2 = new RecordProperty(TYPE_2, propertyNode2);
    properties.put("a", prop1);
    properties.put("b", prop2);

    RecordType testRecordType = new RecordType(registry, properties, true);
    
    Assert.assertTrue(testRecordType.isFrozen);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType testRecordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);

    boolean result = testRecordType.defineProperty("a", TYPE_1, false, propertyNode);
    
    Assert.assertTrue(result);
    Assert.assertEquals(TYPE_1, testRecordType.getPropertyType("a"));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType testRecordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    RecordType thatRecordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    JSType testType = testRecordType.getGreatestSubtypeHelper(thatRecordType);

    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), testType);
  }
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType testRecordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    RecordType thatRecordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    
    boolean result = testRecordType.isSubtype(thatRecordType);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> scope = new MockStaticScope();
    ErrorReporter reporter= new MockErrorReporter();
    RecordType testRecordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    
    JSType resolvedType = testRecordType.resolveInternal(reporter, scope);
    
    Assert.assertEquals(testRecordType, resolvedType);
  }
}