import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.RecordType.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import java.util.Map;
import java.util.SortedMap;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{prop1: int}", recordType.toString());
  }

  @Test
  public void testIsSyntheticMethod() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties, false);

    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties, true);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", longType()));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    properties2.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType3 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, false));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertEquals(registry.getNativeObjectType(OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties, true);

    recordType.defineProperty("prop1", stringType(), false, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
    recordType1.defineProperty("prop1", intType(), false, null);

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
    recordType2.defineProperty("prop1", longType(), false, null);
    recordType2.defineProperty("prop2", stringType(), false, null);

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    recordType3.defineProperty("prop1", intType(), false, null);
    recordType3.defineProperty("prop2", stringType(), false, null);

    RecordType recordType4 = new RecordType(registry, Maps.newHashMap(), true);
    recordType4.defineProperty("prop1", intType(), false, null);
    recordType4.defineProperty("prop3", stringType(), false, null);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType3, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
    assertEquals(recordType1, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty = new RecordProperty("prop1", intType());
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
    recordType1.defineProperty("prop1", intType(), false, null);

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
    recordType2.defineProperty("prop1", intType(), false, null);
    recordType2.defineProperty("prop2", longType(), false, null);

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    recordType3.defineProperty("prop1", intType(), false, null);
    recordType3.defineProperty("prop2", stringType(), false, null);

    RecordType recordType4 = new RecordType(registry, Maps.newHashMap(), true);
    recordType4.defineProperty("prop1", longType(), false, null);
    recordType4.defineProperty("prop2", stringType(), false, null);

    RecordType recordType5 = new RecordType(registry, Maps.newHashMap(), true);
    recordType5.defineProperty("prop1", intType(), false, null);
    recordType5.defineProperty("prop2", stringType(), false, null);
    recordType5.defineProperty("prop3", longType(), false, null);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType3));
    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType1.isSubtype(recordType5));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{prop1: int}", recordType.toString());
  }
}