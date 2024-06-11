import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType.RecordProperty;

public class RecordTypeTest {
  
  private JSTypeRegistry registry;
  private Map<String, RecordProperty> properties;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    properties = new HashMap<>();
  }
  
  @Test
  public void testConstructor() {
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
  }
  
  @Test
  public void testConstructorWithDeclaredProperty() {
    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testConstructorWithNullRecordProperty() {
    properties.put("property", null);
    RecordType recordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testDefineDeclaredProperty() {
    RecordType recordType = new RecordType(registry, properties, true);
    recordType.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);
    assertTrue(recordType.hasProperty("property"));
    assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("property"));
  }
  
  @Test
  public void testDefineSynthesizedProperty() {
    RecordType recordType = new RecordType(registry, properties);
    recordType.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);
    assertTrue(recordType.hasProperty("property"));
    assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("property"));
  }
  
  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.METHOD1));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }
  
  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }
  
  @Test
  public void testToMaybeRecordType() {
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType, recordType.toMaybeRecordType());
  }
  
  @Test
  public void testIsSubtypeStatic() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(RecordType.isSubtype(recordType1, recordType2));
  }
  
}