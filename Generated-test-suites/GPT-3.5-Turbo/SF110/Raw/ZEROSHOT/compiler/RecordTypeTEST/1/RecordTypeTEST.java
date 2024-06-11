import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeCheck;
import com.google.javascript.rhino.jstype.EquivalenceMethod;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType);
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    
    boolean isSynthetic = recordType.isSynthetic();
    
    assertFalse(isSynthetic);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    
    boolean areEquivalent = recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.SAME_FUNCTION);
    
    assertTrue(areEquivalent);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    
    boolean isPropertyDefined = recordType.defineProperty("propertyName", new JSType(), true, new Node());
    
    assertTrue(isPropertyDefined);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType1 = new RecordType(registry, properties);
    JSType that = new JSType();
    
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(that);
    
    assertNotNull(greatestSubtype);
  }

  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    
    RecordType maybeRecordType = recordType.toMaybeRecordType();
    
    assertNotNull(maybeRecordType);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    JSType that = new JSType();
    
    boolean isSubtype = recordType.isSubtype(that);
    
    assertFalse(isSubtype);
  }

  @Test
  public void testIsSubtypeWithTypeAAndTypeB() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType typeA = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType typeB = new RecordType(registry, new HashMap<String, RecordProperty>());
    
    boolean isSubtype = RecordType.isSubtype(typeA, typeB);
    
    assertTrue(isSubtype);
  }
}