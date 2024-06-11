import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry, "a", new IntType(registry)));
    properties.put("b", new RecordProperty(registry, "b", new StringType(registry)));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("{a: int, b: string}", recordType.toString());
  }

  @Test
  public void testCreateSyntheticRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry, "a", new IntType(registry)));
    properties.put("b", new RecordProperty(registry, "b", new StringType(registry)));

    RecordType recordType = new RecordType(registry, properties, false);
    assertEquals("{a: int, b: string}", recordType.toString());
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(registry, "a", new IntType(registry)));
    propertiesA.put("b", new RecordProperty(registry, "b", new StringType(registry)));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(registry, "a", new IntType(registry)));
    propertiesB.put("b", new RecordProperty(registry, "b", new StringType(registry)));
    propertiesB.put("c", new RecordProperty(registry, "c", new IntType(registry)));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

    propertiesB.remove("c");
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

    RecordType recordTypeC = new RecordType(registry, propertiesB, true);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, false));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry, "a", new IntType(registry)));

    RecordType recordType = new RecordType(registry, properties, true);

    Node propertyNode = new DoubleNode(1.0);
    JSType propertyType = new IntType(registry);

    boolean result = recordType.defineProperty(
        "a", propertyType, false, propertyNode);
    assertFalse(result);

    recordType = new RecordType(registry, properties, false);

    result = recordType.defineProperty(
        "a", propertyType, false, propertyNode);
    assertTrue(result);

    assertNotNull(recordType.getPropertyNode("a"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeA.defineProperty("a", new IntType(registry), false, null);

    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeB.defineProperty("a", new StringType(registry), false, null);
    recordTypeB.defineProperty("b", new IntType(registry), false, null);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertNull(greatestSubtype);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertNull(greatestSubtype);

    recordTypeA.defineProperty("b", new IntType(registry), false, null);

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertEquals(recordTypeA, greatestSubtype);

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeC.defineProperty("a", new IntType(registry), false, null);
    recordTypeC.defineProperty("b", new StringType(registry), false, null);
    recordTypeC.defineProperty("c", new IntType(registry), false, null);

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeC.getGreatestSubtypeHelper(recordTypeA);
    assertEquals(recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeC.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeA.defineProperty("a", new IntType(registry), false, null);

    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeB.defineProperty("a", new IntType(registry), false, null);

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeC.defineProperty("a", new StringType(registry), false, null);

    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeD.defineProperty("a", new IntType(registry), false, null);
    recordTypeD.defineProperty("b", new StringType(registry), false, null);

    RecordType recordTypeE = new RecordType(registry, Maps.newHashMap(), true);
    recordTypeE.defineProperty("c", new IntType(registry), false, null);

    // Type A is a subtype of type A
    assertTrue(recordTypeA.isSubtype(recordTypeA));

    // Type A is a subtype of type B
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // Type B is a subtype of type A
    assertTrue(recordTypeB.isSubtype(recordTypeA));

    // Type A is not a subtype of type C
    assertFalse(recordTypeA.isSubtype(recordTypeC));

    // Type C is not a subtype of type A
    assertFalse(recordTypeC.isSubtype(recordTypeA));

    // Type A is a subtype of type D
    assertTrue(recordTypeA.isSubtype(recordTypeD));

    // Type D is not a subtype of type A
    assertFalse(recordTypeD.isSubtype(recordTypeA));

    // Type D is a subtype of type E
    assertTrue(recordTypeD.isSubtype(recordTypeE));

    // Type E is not a subtype of type D
    assertFalse(recordTypeE.isSubtype(recordTypeD));
  }
}