import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry(new JSTypeFactory() {
      @Override
      public JSType getNativeObjectType(String typeName) {
        return objectType(typeName);
      }

      @Override
      public JSType getNativeType(String typeName) {
        return nativeType(typeName);
      }
    });
  }

  @Test
  public void testRecordTypeConstructor() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", new RecordProperty("property1", intType()));
    recordType = new RecordType(registry, properties);

    assertEquals("{property1 : int}", recordType.toString());
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property1", new RecordProperty("property1", intType()));
    properties2.put("property2", new RecordProperty("property2", intType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("property1", new RecordProperty("property1", intType()));
    properties3.put("property2", new RecordProperty("property2", stringType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertTrue(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    assertEquals(objectType("Object"), recordType.getImplicitPrototype());
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWhenFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;
    recordType.defineProperty("property1", intType(), false, null);
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property1", new RecordProperty("property1", intType()));
    properties2.put("property2", new RecordProperty("property2", stringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("property1", new RecordProperty("property1", intType()));
    properties3.put("property2", new RecordProperty("property2", intType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
    assertEquals(recordType3, leastSupertype);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property1", new RecordProperty("property1", intType()));
    properties2.put("property2", new RecordProperty("property2", stringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("property1", new RecordProperty("property1", intType()));
    properties3.put("property2", new RecordProperty("property2", intType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("property1", new RecordProperty("property1", intType()));
    properties4.put("property2", new RecordProperty("property2", intType()));
    properties4.put("property3", new RecordProperty("property3", intType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType3.getGreatestSubtypeHelper(recordType4);
    assertEquals(recordType4, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property1", new RecordProperty("property1", intType()));
    properties2.put("property2", new RecordProperty("property2", intType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("property1", new RecordProperty("property1", intType()));
    properties3.put("property2", new RecordProperty("property2", stringType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("property1", new RecordProperty("property1", stringType()));
    RecordType recordType5 = new RecordType(registry, properties5);

    Map<String, RecordProperty> properties6 = Maps.newHashMap();
    properties6.put("property1", new RecordProperty("property1", intType()));
    properties6.put("property2", new RecordProperty("property2", intType()));
    properties6.put("property3", new RecordProperty("property3", intType()));
    RecordType recordType6 = new RecordType(registry, properties6);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType1.isSubtype(recordType5));
    assertTrue(recordType2.isSubtype(recordType1));
    assertTrue(recordType2.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType2));
    assertFalse(recordType3.isSubtype(recordType4));
    assertFalse(recordType3.isSubtype(recordType5));
    assertTrue(recordType4.isSubtype(recordType3));
    assertTrue(recordType4.isSubtype(recordType4));
    assertTrue(recordType4.isSubtype(recordType6));
    assertTrue(recordType5.isSubtype(recordType3));
    assertTrue(recordType5.isSubtype(recordType4));
    assertTrue(recordType5.isSubtype(recordType5));
    assertTrue(recordType6.isSubtype(recordType3));
    assertTrue(recordType6.isSubtype(recordType4));
    assertTrue(recordType6.isSubtype(recordType6));
  }

  @Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", new RecordProperty("property1", intType()));
    RecordType recordType = new RecordType(registry, properties);

    JSType resolvedType = recordType.resolveInternal(errorReporter, null);
    assertEquals(recordType, resolvedType);
  }
}