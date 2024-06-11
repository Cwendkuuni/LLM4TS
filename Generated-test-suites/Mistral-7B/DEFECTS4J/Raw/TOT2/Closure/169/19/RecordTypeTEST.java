import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordTypeA;
  private RecordType recordTypeB;
  private RecordType recordTypeC;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("propA", new RecordProperty("propA", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    recordTypeA = new RecordType(registry, propsA, true);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("propA", new RecordProperty("propA", registry.getNativeType(JSTypeNative.STRING_TYPE)));
    propsB.put("propB", new RecordProperty("propB", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    recordTypeB = new RecordType(registry, propsB, true);

    Map<String, RecordProperty> propsC = new Maps.NewHashMap<String, RecordProperty>() {{
      put("propA", new RecordProperty("propA", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
      put("propB", new RecordProperty("propB", registry.getNativeType(JSTypeNative.STRING_TYPE).getLeastSubtype(
          registry.getNativeType(JSTypeNative.NUMBER_TYPE))));
    }};
    recordTypeC = new RecordType(registry, propsC, true);
  }

  @Test
  public void testRecordTypeCreation() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), false);
    assertNotNull(recordType);
  }

  @Test
  public void testRecordEquivalence() {
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeA, true));
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, true));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordTypeA.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    String propName = "propX";
    JSType propType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    recordTypeA.defineProperty(propName, propType, false, null);

    assertFalse(recordTypeA.hasProperty(propName));
    assertNull(recordTypeA.getPropertyNode(propName));

    recordTypeA.defineProperty(propName, propType, false, null);
    assertTrue(recordTypeA.hasProperty(propName));
    assertNotNull(recordTypeA.getPropertyNode(propName));
  }

  @Test
  public void testGetGreatestSubtype() {
    JSType subtypeA = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    JSType subtypeB = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType subtypeC = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(subtypeA);
    assertEquals(subtypeA, greatestSubtype);

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(subtypeB);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(subtypeC);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(subtypeA);
    UnionType greatestSubtypeUnion = (UnionType) greatestSubtype;
    assertTrue(greatestSubtypeUnion.contains(subtypeA));
    assertTrue(greatestSubtypeUnion.contains(recordTypeA));
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    assertFalse(recordTypeA.isSubtype(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
  }

  @Test
  public void testCheckRecordEquivalenceHelper_tolerateUnknowns() {
    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true);
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeD, true));
  }
}