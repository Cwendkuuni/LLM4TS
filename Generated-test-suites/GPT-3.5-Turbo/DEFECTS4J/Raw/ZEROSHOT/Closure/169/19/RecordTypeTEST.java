import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {
  
  private RecordType recordType;
  private JSTypeRegistry registry;
  private Map<String, RecordProperty> properties;
  
  @Mock
  private Node propertyNode;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    registry = new JSTypeRegistry();
    properties = Maps.newHashMap();
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testRecordType() {
    assertNotNull(recordType);
  }
  
  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.NO_TYPE), true, propertyNode));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    assertNotNull(recordType.getImplicitPrototype());
  }
  
  @Test
  public void testIsSubtype() {
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)));
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    assertNotNull(recordType.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(registry, properties);
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = registry.getNativeType(JSTypeNative.NO_TYPE);
    assertNotNull(recordType.getGreatestSubtypeHelper(that));
  }
  
  @Test
  public void testToMaybeRecordType() {
    assertNotNull(recordType.toMaybeRecordType());
  }
}