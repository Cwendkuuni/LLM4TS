import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeCheckMode.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.testing.TestContext;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static final ErrorReporter REPORTER = new ErrorReporter() {};
  private static final JSTypeRegistry REGISTRY = new JSTypeRegistry();

  @org.junit.Test
  public void testRecordType() throws Exception {
    // Test creating a simple record type
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.TYPE));

    RecordType recordType = new RecordType(REGISTRY, properties);

    // Test properties
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.TYPE, recordType.getPropertyType("age"));

    // Test isEquivalentTo
    RecordType recordType1 = new RecordType(REGISTRY, properties);
    RecordType recordType2 = new RecordType(REGISTRY, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test isSubtype
    RecordType recordType3 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    RecordType recordType4 = new RecordType(REGISTRY, properties);
    assertTrue(recordType3.isSubtype(recordType4));

    RecordType recordType5 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    RecordType recordType6 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType6.defineProperty("name", Integer.TYPE.toMaybeRecordType(), true, null);
    assertFalse(recordType3.isSubtype(recordType6));

    // Test getLeastSupertype
    RecordType recordType7 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType7.defineProperty("name", String.class, true, null);
    RecordType recordType8 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType8.defineProperty("name", String.class, true, null);
    recordType8.defineProperty("age", Integer.TYPE, true, null);

    JSType leastSupertype = recordType7.getLeastSupertype(recordType8);
    assertTrue(leastSupertype instanceof RecordType);
    RecordType leastSupertypeRecord = (RecordType) leastSupertype;
    assertEquals(2, leastSupertypeRecord.properties.size());
    assertTrue(leastSupertypeRecord.hasProperty("name"));
    assertTrue(leastSupertypeRecord.hasProperty("age"));

    // Test getGreatestSubtype
    RecordType recordType9 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType9.defineProperty("name", String.class, true, null);
    recordType9.defineProperty("age", Integer.TYPE, true, null);

    RecordType recordType10 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType10.defineProperty("name", String.class, true, null);
    recordType10.defineProperty("age", Integer.TYPE, true, null);
    recordType10.defineProperty("city", String.class, true, null);

    JSType greatestSubtype = recordType9.getGreatestSubtype(recordType10);
    assertTrue(greatestSubtype instanceof RecordType);
    RecordType greatestSubtypeRecord = (RecordType) greatestSubtype;
    assertEquals(2, greatestSubtypeRecord.properties.size());
    assertTrue(greatestSubtypeRecord.hasProperty("name"));
    assertTrue(greatestSubtypeRecord.hasProperty("age"));

    // Test defineProperty
    TestContext context = new TestContext(REGISTRY, REPORTER);
    context.init();
    JSType factory = JSTypeFactory.getInstance(context);
    RecordType recordType11 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());

    recordType11.defineProperty("name", factory.getString(), true, null);
    recordType11.defineProperty("age", factory.getInt(), true, null);

    JSType definedRecordType = recordType11.resolve(REPORTER, context.getGlobalScope());
    JSType nameProperty = definedRecordType.getProperty("name");
    JSType ageProperty = definedRecordType.getProperty("age");

    assertEquals(factory.getString(), nameProperty);
    assertEquals(factory.getInt(), ageProperty);

    // Test isEquivalentTo with different order of properties
    RecordType recordType12 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType12.defineProperty("age", Integer.TYPE, true, null);
    recordType12.defineProperty("name", String.class, true, null);

    RecordType recordType13 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    recordType13.defineProperty("name", String.class, true, null);
    recordType13.defineProperty("age", Integer.TYPE, true, null);

    assertTrue(recordType12.isEquivalentTo(recordType13));

    // Test with UnionType property
    RecordProperty recordProperty = new RecordProperty("name", UnionType.getInstance(factory.getArrayType(factory.getString()), factory.getString()));
    Map<String, RecordProperty> unionProperties = Maps.newHashMap();
    unionProperties.put("name", recordProperty);

    RecordType unionRecordType = new RecordType(REGISTRY, unionProperties);

    JSType unionType = unionRecordType.getPropertyType("name");
    assertTrue(unionType instanceof UnionType);
    UnionType union = (UnionType) unionType;
    assertTrue(union.getAlternates().size() == 2);
    assertTrue(union.getAlternates().get(0) instanceof String);
    assertTrue(union.getAlternates().get(1) instanceof JSType.ArrayType);
  }

  @org.junit.Test
  public void testRecordTypeWithEmptyProperties() throws Exception {
    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();

    RecordType emptyRecordType = new RecordType(REGISTRY, emptyProperties);

    // Test isEquivalentTo
    RecordType emptyRecordType1 = new RecordType(REGISTRY, emptyProperties);
    RecordType emptyRecordType2 = new RecordType(REGISTRY, emptyProperties);

    assertTrue(emptyRecordType1.isEquivalentTo(emptyRecordType2));

    // Test isSubtype
    RecordType emptyRecordType3 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());

    assertTrue(emptyRecordType.isSubtype(emptyRecordType3));

    // Test getLeastSupertype
    RecordType emptyRecordType4 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    emptyRecordType4.defineProperty("name", String.class, true, null);

    JSType leastSupertype = emptyRecordType.getLeastSupertype(emptyRecordType4);
    assertTrue(leastSupertype instanceof RecordType);
    RecordType leastSupertypeRecord = (RecordType) leastSupertype;
    assertEquals(1, leastSupertypeRecord.properties.size());
    assertTrue(leastSupertypeRecord.hasProperty("name"));

    // Test getGreatestSubtype
    RecordType emptyRecordType5 = new RecordType(REGISTRY, Maps.<String, RecordProperty>newHashMap());
    emptyRecordType5.defineProperty("name", String.class, true, null);

    JSType greatestSubtype = emptyRecordType.getGreatestSubtype(emptyRecordType5);
    assertTrue(greatestSubtype instanceof RecordType);
    RecordType greatestSubtypeRecord = (RecordType) greatestSubtype;
    assertEquals(1, greatestSubtypeRecord.properties.size());
    assertTrue(greatestSubtypeRecord.hasProperty("name"));
  }
}