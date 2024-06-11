import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Maps;

public class RecordTypeTest {
  
  private JSTypeRegistry registry;
  private Map<String, RecordProperty> properties;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    properties = Maps.newTreeMap();
    // Define properties for testing
    properties.put("a", new RecordProperty("TYPE_1", new Node()));
    properties.put("b", new RecordProperty("TYPE_2", new Node()));
  }
  
  @Test
  public void testRecordType() {
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testRecordTypeWithNullProperty() {
    properties.put("c", null);
    RecordType recordType = new RecordType(registry, properties);
  }
  
  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }
  
  @Test
  public void testIsEquivalentToWithDifferentProperties() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("c", new RecordProperty("TYPE_3", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }
  
  @Test
  public void testIsEquivalentToWithDifferentPropertyType() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("a", new RecordProperty("TYPE_X", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }
  
  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getImplicitPrototype(),
                 registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }
  
  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.defineProperty("c", new JSType(), false, new Node()));
    assertTrue(recordType.defineProperty("d", new JSType(), true, new Node()));
    assertTrue(recordType.hasOwnProperty("d"));
  }
  
  @Test
  public void testGetLeastSupertype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(recordType1, recordType1.getLeastSupertype(recordType2));
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType(registry, properties);
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(recordType1);
    assertEquals(builder.build(), recordType1.getGreatestSubtypeHelper(recordType1));
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithDifferentProperties() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("c", new RecordProperty("TYPE_3", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
                 recordType1.getGreatestSubtypeHelper(recordType2));
  }
  
  @Test
  public void testGetGreatestSubtypeHelperWithDifferentPropertyType() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("a", new RecordProperty("TYPE_X", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
                 recordType1.getGreatestSubtypeHelper(recordType2));
  }
  
  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testIsSubtypeWithDifferentProperties() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("c", new RecordProperty("TYPE_3", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertFalse(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testIsSubtypeWithDifferentPropertyType() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("a", new RecordProperty("TYPE_X", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertFalse(recordType1.isSubtype(recordType2));
  }
  
  @Test
  public void testIsSubtypeWithObjectType() {
    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.isSubtype(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }
  
  @Test
  public void testIsSubtypeWithNonRecordType() {
    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.isSubtype(new JSType()));
  }
  
  @Test
  public void testIsSubtypeHelper() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(RecordType.isSubtype(recordType1, recordType2));
  }
  
  @Test
  public void testIsSubtypeHelperWithDifferentProperties() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("c", new RecordProperty("TYPE_3", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertFalse(RecordType.isSubtype(recordType1, recordType2));
  }
  
  @Test
  public void testIsSubtypeHelperWithDifferentPropertyType() {
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("a", new RecordProperty("TYPE_X", new Node()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertFalse(RecordType.isSubtype(recordType1, recordType2));
  }
  
  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType, recordType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>()));
  }
  
}