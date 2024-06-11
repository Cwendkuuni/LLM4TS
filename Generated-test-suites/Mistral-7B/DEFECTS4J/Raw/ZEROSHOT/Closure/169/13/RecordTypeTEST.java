import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType recordType2;

  @Before
  public void setUp() {
    registry = Mockito.mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties = new TreeMap<>();
    properties.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    recordType = new RecordType(registry, properties, true);

    Map<String, RecordProperty> properties2 = new TreeMap<>();
    properties2.put("prop1", new RecordProperty("prop1", String.class));
    properties2.put("prop2", new RecordProperty("prop2", Float.TYPE));
    recordType2 = new RecordType(registry, properties2, true);
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("prop2", String.class, false, null));
    assertNull(recordType.getPropertyType("prop2"));

    recordType.defineProperty("prop2", String.class, false, null);
    assertEquals(String.class, recordType.getPropertyType("prop2"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType intType = registry.getType(Integer.TYPE);
    JSType stringType = registry.getType(String.class);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(intType);
    assertEquals(intType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(stringType);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

    JSType recordTypeWithProp2 = new RecordType(registry, properties2, true);
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithProp2);
    assertEquals(recordTypeWithProp2, greatestSubtype);

    greatestSubtype = recordTypeWithProp2.getGreatestSubtypeHelper(recordType);
    assertEquals(recordTypeWithProp2, greatestSubtype);

    greatestSubtype = recordTypeWithProp2.getGreatestSubtypeHelper(intType);
    assertEquals(recordTypeWithProp2, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordType.isSubtype(registry.getType(Float.TYPE)));
    assertFalse(recordType.isSubtype(registry.getType(Integer.TYPE)));

    RecordType recordTypeWithProp2 = new RecordType(registry, properties2, true);
    assertTrue(recordType.isSubtype(recordTypeWithProp2));
    assertFalse(recordTypeWithProp2.isSubtype(recordType));

    RecordType recordTypeWithProp2AndInt = new RecordType(registry, properties2, true);
    recordTypeWithProp2AndInt.defineProperty("prop3", intType, false, null);
    assertTrue(recordType.isSubtype(recordTypeWithProp2AndInt));

    JSType unionType = new UnionType(registry);
    unionType.addAlternate(recordType);
    unionType.addAlternate(registry.getType(Float.TYPE));
    assertFalse(recordType.isSubtype(unionType));
  }

  @Test
  public void testIsSubtypeRecord() {
    assertTrue(RecordType.isSubtype(recordType, recordType));
    assertTrue(RecordType.isSubtype(recordType, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(RecordType.isSubtype(recordType, registry.getType(Float.TYPE)));
    assertFalse(RecordType.isSubtype(recordType, registry.getType(Integer.TYPE)));

    RecordType recordTypeWithProp2 = new RecordType(registry, properties2, true);
    assertTrue(RecordType.isSubtype(recordType, recordTypeWithProp2));
    assertFalse(RecordType.isSubtype(recordTypeWithProp2, recordType));

    RecordType recordTypeWithProp2AndInt = new RecordType(registry, properties2, true);
    recordTypeWithProp2AndInt.defineProperty("prop3", intType, false, null);
    assertTrue(RecordType.isSubtype(recordType, recordTypeWithProp2AndInt));
  }
}