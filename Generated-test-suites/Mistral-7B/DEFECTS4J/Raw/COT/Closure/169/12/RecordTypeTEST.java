import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int lineOffset, Node node) {
        // ignore
      }

      @Override
      public void warning(String message, int line, int lineOffset, Node node) {
        // ignore
      }
    };
  }

  @Test
  public void testRecordTypeConstructor() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType.properties);
    assertEquals(1, recordType.properties.size());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop1", registry.getIntType(), true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentPropertyOrder() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop2", registry.getIntType(), true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentPropertyTypes() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop1", registry.getStringType(), true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithUnknownProperties() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop1", registry.getStringType(), false);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithIncompatibleProperties() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop1", registry.getUndefinedType(), true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testIsSynthetic() {
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), false);
    assertTrue(recordType1.isSynthetic());

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType2.isSynthetic());
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
    assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertFalse(recordType.defineProperty("prop1", registry.getIntType(), false, null));
    assertFalse(recordType.defineProperty("prop1", registry.getIntType(), true, null));

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.getIntType()));
    recordType = new RecordType(registry, properties, true);

    assertTrue(recordType.defineProperty("prop1", registry.getIntType(), false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop1", registry.getIntType(), true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertSame(recordType1, greatestSubtype);

    recordType2 = createRecordType("prop1", registry.getStringType(), true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertSame(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

    recordType1 = createRecordType("prop1", registry.getIntType(), true);
    recordType2 = createRecordType("prop1", registry.getIntType(), false);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertSame(recordType1, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = createRecordType("prop1", registry.getIntType(), true);
    RecordType recordType2 = createRecordType("prop1", registry.getStringType(), true);

    assertFalse(recordType1.isSubtype(recordType2));

    recordType2 = createRecordType("prop1", registry.getIntType(), true);
    assertTrue(recordType1.isSubtype(recordType2));

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(recordType1.isSubtype(objectType));

    UnionType unionType = new UnionTypeBuilder(registry).addAlternate(recordType1).build();
    assertTrue(recordType1.isSubtype(unionType));
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = createRecordType("prop1", registry.getIntType(), true);
    JSType propertyType = registry.getIntType();
    Map<String, JSType> properties = Maps.newHashMap();
    properties.put("prop1", propertyType);
    recordType = new RecordType(registry, properties, true);

    JSType resolvedType = recordType.resolveInternal(errorReporter, null);
    assertSame(recordType, resolvedType);

    propertyType = registry.getStringType();
    properties.put("prop1", propertyType);
    recordType = new RecordType(registry, properties, true);

    resolvedType = recordType.resolveInternal(errorReporter, null);
    assertSame(recordType, resolvedType);
  }

  private RecordType createRecordType(String propertyName, JSType propertyType, boolean declared) {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put(propertyName, new RecordProperty(propertyName, propertyType));
    return new RecordType(registry, properties, declared);
  }
}