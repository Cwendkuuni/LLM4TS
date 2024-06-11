import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {
  
  private RecordType recordType;
  private JSTypeRegistry registry;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    recordType = new RecordType(registry, properties);
  }
  
  @After
  public void tearDown() {
    registry = null;
    recordType = null;
  }
  
  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(registry, properties);
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, false));
    
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
    RecordType differentRecord = new RecordType(registry, otherProperties);
    assertFalse(recordType.checkRecordEquivalenceHelper(differentRecord, true));
    assertFalse(recordType.checkRecordEquivalenceHelper(differentRecord, false));
  }
  
  @Test
  public void testIsSubtype() {
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = registry.getErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertSame(recordType, resolvedType);
  }
}