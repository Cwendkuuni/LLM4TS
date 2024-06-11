import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;
  private Map<String, RecordProperty> properties;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    properties = Maps.newTreeMap();
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo_sameRecordType_expectTrue() {
    RecordType otherRecordType = new RecordType(registry, properties);
    
    boolean result = recordType.isEquivalentTo(otherRecordType);
    
    assertTrue(result);
  }

  @Test
  public void testIsEquivalentTo_differentRecordType_expectFalse() {
    Map<String, RecordProperty> otherProperties = Maps.newTreeMap();
    RecordType otherRecordType = new RecordType(registry, otherProperties);
    
    boolean result = recordType.isEquivalentTo(otherRecordType);
    
    assertFalse(result);
  }

  @Test
  public void testGetImplicitPrototype_expectNativeObjectType() {
    ObjectType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    ObjectType result = recordType.getImplicitPrototype();

    assertEquals(expectedType, result);
  }

  @Test
  public void testDefineProperty_newProperty_expectTrue() {
    String propertyName = "testProperty";
    JSType type = new JSType();
    boolean inferred = false;
    Node propertyNode = new Node();
    
    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    
    assertTrue(result);
  }

  @Test
  public void testDefineProperty_existingProperty_expectFalse() {
    String propertyName = "existingProperty";
    JSType type = new JSType();
    boolean inferred = false;
    Node propertyNode = new Node();
    properties.put(propertyName, new RecordProperty(type, propertyNode));
    
    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    
    assertFalse(result);
  }

  @Test
  public void testGetLeastSupertype_subtype_expectSupertype() {
    Map<String, RecordProperty> otherProperties = Maps.newTreeMap();
    JSType otherType = new RecordType(registry, otherProperties);
    otherProperties.put("propertyA", new RecordProperty(new JSType(), new Node()));
    otherProperties.put("propertyB", new RecordProperty(new JSType(), new Node()));
    properties.put("propertyA", new RecordProperty(new JSType(), new Node()));
    
    JSType expectedType = new RecordType(registry, properties);
    
    JSType result = recordType.getLeastSupertype(otherType);

    assertEquals(expectedType, result);
  }

  @Test
  public void testGetGreatestSubtype_helper_expectGreatestSubtype() {
    JSType that = new JSType();
    
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.NO_TYPE);
    
    JSType result = recordType.getGreatestSubtypeHelper(that);
    
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsSubtype_subtype_expectTrue() {
    JSType otherType = new JSType();
    
    boolean result = recordType.isSubtype(otherType);
    
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_notSubtype_expectFalse() {
    Map<String, RecordProperty> otherProperties = Maps.newTreeMap();
    JSType otherType = new RecordType(registry, otherProperties);
    
    boolean result = recordType.isSubtype(otherType);
    
    assertFalse(result);
  }

  @Test
  public void testIsSubtype_recordTypeWithSameProperties_expectTrue() {
    Map<String, RecordProperty> otherProperties = Maps.newTreeMap();
    otherProperties.put("propertyA", new RecordProperty(new JSType(), new Node()));
    otherProperties.put("propertyB", new RecordProperty(new JSType(), new Node()));
    JSType otherType = new RecordType(registry, otherProperties);
    
    boolean result = recordType.isSubtype(otherType);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_topOfRecordTypes_expectTrue() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    
    boolean result = recordType.isSubtype(objectType);
    
    assertTrue(result);
  }
  
  @Test
  public void testResolveInternal_resolveProperties_expectResolvedInternalType() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    JSType result = recordType.resolveInternal(errorReporter, scope);
    
    assertEquals(recordType, result);
    // Verify properties have been resolved
    for (RecordProperty property : properties.values()) {
      assertEquals(property.getType().resolve(errorReporter, scope), property.getType());
    }
  }
}