import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.stringType()));
    recordProperties.put("b", new RecordProperty("b", registry.numberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    // Basic checks
    assertEquals("RecordType", recordType.getClass().getSimpleName());
    assertEquals(1, recordType.getPropertyNames().length);
    assertEquals("a", recordType.getPropertyNames()[0]);

    // Check properties
    assertEquals(2, recordType.getDeclaredProperties().length);
    assertEquals("a", recordType.getDeclaredPropertyNames()[0]);
    assertEquals(registry.stringType(), recordType.getDeclaredPropertyTypes()[0]);

    assertEquals(2, recordType.getDeclaredPropertyNodes().length);
    Node propertyNodeA = recordType.getDeclaredPropertyNodes()[0];
    assertNotNull(propertyNodeA);
    assertEquals("a", propertyNodeA.getIdent().getName());

    assertEquals(1, recordType.getProperties().size());
    assertEquals("a", recordType.getPropertyNames()[0]);
    assertEquals(registry.stringType(), recordType.getPropertyTypes()[0]);

    // Freeze test
    recordType.freeze();
    assertFalse(recordType.defineProperty("c", registry.numberType(), false, null));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", registry.numberType()));
    recordPropertiesA.put("b", new RecordProperty("b", registry.stringType()));

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordProperty("a", registry.numberType()));
    recordPropertiesB.put("b", new RecordProperty("b", registry.stringType()));
    recordPropertiesB.put("c", new RecordProperty("c", registry.numberType()));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeC = new RecordType(registry, recordPropertiesA);

    // Reflexivity
    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));

    // Symmetry
    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    assertTrue(recordTypeC.isEquivalentTo(recordTypeA));

    // Transitivity
    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    assertTrue(recordTypeC.isEquivalentTo(recordTypeB));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

    // Non-equivalence
    assertFalse(recordTypeA.isEquivalentTo(registry.objectType()));
    assertFalse(recordTypeA.isEquivalentTo(registry.booleanType()));

    // Subtyping
    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());
    RecordType recordTypeE = new RecordType(registry, recordPropertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(recordTypeA));

    assertTrue(recordTypeA.isSubtype(recordTypeE));
    assertFalse(recordTypeE.isSubtype(recordTypeA));

    // Least common supertype
    RecordType recordTypeF = new RecordType(registry, recordPropertiesB);
    RecordType recordTypeG = new RecordType(registry, recordPropertiesA);

    RecordType lcs = recordTypeF.getLeastSupertype(recordTypeG);
    assertEquals(recordTypeA.getClass(), lcs.getClass());
  }

  @Test
  public void testRecordTypeLeastSupertype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", registry.numberType()));
    recordPropertiesA.put("b", new RecordProperty("b", registry.stringType()));

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordProperty("a", registry.numberType()));
    recordPropertiesB.put("c", new RecordProperty("c", registry.numberType()));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    JSType leastSupertypeAB = recordTypeA.getLeastSupertype(recordTypeB);
    JSType leastSupertypeBA = recordTypeB.getLeastSupertype(recordTypeA);

    assertEquals(recordTypeA.getClass(), leastSupertypeAB.getClass());
    assertEquals(recordTypeB.getClass(), leastSupertypeBA.getClass());

    // Check properties
    SortedMap<String, JSType> propsAB = leastSupertypeAB.getProperties();
    assertEquals(2, propsAB.size());
    assertEquals("a", propsAB.get("a").getName());
    assertEquals("b", propsAB.get("b").getName());

    SortedMap<String, JSType> propsBA = leastSupertypeBA.getProperties();
    assertEquals(2, propsBA.size());
    assertEquals("a", propsBA.get("a").getName());
    assertEquals("b", propsBA.get("b").getName());

    // Check types
    assertEquals(registry.numberType(), propsAB.get("a"));
    assertEquals(registry.stringType(), propsAB.get("b"));

    assertEquals(registry.numberType(), propsBA.get("a"));
    assertEquals(registry.stringType(), propsBA.get("b"));

    // Check greatest subtype
    JSType greatestSubtypeAB = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    JSType greatestSubtypeBA = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(recordTypeA.getClass(), greatestSubtypeAB.getClass());
    assertEquals(recordTypeB.getClass(), greatestSubtypeBA.getClass());

    // Check properties
    SortedMap<String, JSType> propsGreatestSubtypeAB = greatestSubtypeAB.getProperties();
    assertEquals(2, propsGreatestSubtypeAB.size());
    assertEquals("a", propsGreatestSubtypeAB.get("a").getName());
    assertEquals("b", propsGreatestSubtypeAB.get("b").getName());

    SortedMap<String, JSType> propsGreatestSubtypeBA = greatestSubtypeBA.getProperties();
    assertEquals(2, propsGreatestSubtypeBA.size());
    assertEquals("a", propsGreatestSubtypeBA.get("a").getName());
    assertEquals("b", propsGreatestSubtypeBA.get("b").getName());

    // Check types
    assertEquals(registry.numberType(), propsGreatestSubtypeAB.get("a"));
    assertEquals(registry.stringType(), propsGreatestSubtypeAB.get("b"));

    assertEquals(registry.numberType(), propsGreatestSubtypeBA.get("a"));
    assertEquals(registry.stringType(), propsGreatestSubtypeBA.get("b"));

    // Test with a record type having a union type as a property
    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("a", new RecordProperty("a", registry.numberType()));
    recordPropertiesC.put("b", new RecordProperty("b", new UnionTypeBuilder(registry)
        .addAlternate(registry.stringType())
        .addAlternate(registry.numberType())
        .build()));

    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    JSType greatestSubtypeAC = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
    JSType greatestSubtypeCA = recordTypeC.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(recordTypeC.getClass(), greatestSubtypeAC.getClass());
    assertEquals(recordTypeC.getClass(), greatestSubtypeCA.getClass());

    // Check properties
    SortedMap<String, JSType> propsGreatestSubtypeAC = greatestSubtypeAC.getProperties();
    assertEquals(3, propsGreatestSubtypeAC.size());
    assertEquals("a", propsGreatestSubtypeAC.get("a").getName());
    assertEquals("b", propsGreatestSubtypeAC.get("b").getName());

    // Check types
    assertEquals(registry.numberType(), propsGreatestSubtypeAC.get("a"));

    SortedMap<String, JSType> propsB = recordTypeB.getProperties();
    assertEquals(2, propsB.size());
    assertEquals("a", propsB.get("a").getName());
    assertEquals("c", propsB.get("c").getName());

    assertEquals(registry.numberType(), propsB.get("a"));
    assertEquals(registry.numberType(), propsB.get("c"));

    assertEquals(registry.numberType(), propsGreatestSubtypeAC.get("a"));
    assertEquals(new UnionTypeBuilder(registry)
        .addAlternate(registry.stringType())
        .addAlternate(registry.numberType())
        .build(), propsGreatestSubtypeAC.get("b"));

    assertEquals(registry.numberType(), propsGreatestSubtypeAC.get("c"));

    SortedMap<String, JSType> propsGreatestSubtypeCA = greatestSubtypeCA.getProperties();
    assertEquals(3, propsGreatestSubtypeCA.size());
    assertEquals("a", propsGreatestSubtypeCA.get("a").getName());
    assertEquals("b", propsGreatestSubtypeCA.get("b").getName());

    // Check types
    assertEquals(registry.numberType(), propsGreatestSubtypeCA.get("a"));

    assertEquals(registry.numberType(), propsB.get("c"));

    assertEquals(registry.numberType(), propsGreatestSubtypeCA.get("c"));

    // Check greatest subtype of union type
    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(recordTypeA);
    unionBuilder.addAlternate(recordTypeB);
    JSType greatestSubtypeUnion = unionBuilder.build();

    JSType greatestSubtypeUnionAB = recordTypeA.getGreatestSubtypeHelper(greatestSubtypeUnion);
    JSType greatestSubtypeUnionBA = greatestSubtypeUnion.getGreatestSubtypeHelper(recordTypeA);

    assertEquals(greatestSubtypeUnion.getClass(), greatestSubtypeAB.getClass());
    assertEquals(greatestSubtypeUnion.getClass(), greatestSubtypeCA.getClass());
    assertEquals(greatestSubtypeUnion.getClass(), greatestSubtypeUnionAB.getClass());
    assertEquals(greatestSubtypeUnion.getClass(), greatestSubtypeUnionBA.getClass());

    // Check properties
    SortedMap<String, JSType> propsGreatestSubtypeUnionAB = greatestSubtypeUnionAB.getProperties();
    assertEquals(3, propsGreatestSubtypeUnionAB.size());
    assertEquals("a", propsGreatestSubtypeUnionAB.get("a").getName());
    assertEquals("b", propsGreatestSubtypeUnionAB.get("b").getName());

    // Check types
    assertEquals(registry.numberType(), propsGreatestSubtypeUnionAB.get("a"));

    SortedMap<String, JSType> propsUnionAB = greatestSubtypeUnion.getProperties();
    assertEquals(3, propsUnionAB.size());
    assertEquals("a", propsUnionAB.get("a").getName());
    assertEquals("b", propsUnionAB.get("b").getName());

    assertEquals(registry.numberType(), propsUnionAB.get("a"));

    assertEquals(registry.numberType(), propsGreatestSubtypeUnionAB.get("a"));
    assertEquals(registry.stringType(), propsGreatestSubtypeUnionAB.get("b"));

    assertEquals(registry.numberType(), propsGreatestSubtypeUnionAB.get("c"));

    assertEquals(registry.numberType(), propsUnionAB.get("a"));

    assertEquals(registry.numberType(), propsUnionAB.get("c"));
  }
}