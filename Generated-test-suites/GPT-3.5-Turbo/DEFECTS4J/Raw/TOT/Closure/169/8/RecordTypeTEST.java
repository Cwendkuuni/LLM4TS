import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class RecordTypeTest {
  
  private RecordType recordType;
  private JSTypeRegistry registry;
  
  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty(new JSType()));
    properties.put("property2", new RecordProperty(new JSType()));
    properties.put("property3", new RecordProperty(new JSType()));
    recordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testDefineDeclaredProperty() {
    boolean result = recordType.defineProperty("property4", new JSType(), true, new Node());
    assertTrue(result);
    assertTrue(recordType.hasProperty("property4"));
  }
  
  @Test
  public void testDefineSynthesizedProperty() {
    boolean result = recordType.defineProperty("property5", new JSType(), false, new Node());
    assertTrue(result);
    assertTrue(recordType.hasProperty("property5"));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    that.defineDeclaredProperty("property1", new JSType(), true, new Node());
    that.defineDeclaredProperty("property2", new JSType(), true, new Node());
    that.defineDeclaredProperty("property3", new JSType(), true, new Node());
    
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    assertTrue(greatestSubtype.isRecordType());
  }
  
  @Test
  public void testIsSubtype() {
    JSType that = new JSType();
    that.defineDeclaredProperty("property1", new JSType(), true, new Node());
    that.defineDeclaredProperty("property2", new JSType(), true, new Node());
    that.defineDeclaredProperty("property3", new JSType(), true, new Node());
    
    boolean result = recordType.isSubtype(that);
    assertTrue(result);
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    JSType resolvedType = recordType.resolveInternal(t, scope);
    assertNotNull(resolvedType);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(registry, new HashMap<>());
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
    assertTrue(result);
  }
}