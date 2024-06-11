import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Reporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.errors.TypeError;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static final Reporter REPORTER = new Reporter();
  private static final NodeFactory NODE_FACTORY = new NodeFactory(REPORTER);

  private static RecordType createRecordType(String[] propertyNames, JSType[] propertyTypes) {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    for (int i = 0; i < propertyNames.length; i++) {
      String propertyName = propertyNames[i];
      JSType propertyType = propertyTypes[i];
      properties.put(propertyName, new RecordProperty(propertyName, propertyType));
    }

    JSTypeRegistry registry = new JSTypeRegistry();
    return new RecordType(registry, properties);
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    String[] propertyNames = {"a", "b"};
    JSType[] propertyTypes = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType = createRecordType(propertyNames, propertyTypes);
    assertEquals(propertyNames.length, recordType.getPropertyNames().length);
    assertEquals(propertyTypes[0], recordType.getPropertyType("a"));
    assertEquals(propertyTypes[1], recordType.getPropertyType("b"));
  }

  @org.junit.Test
  public void testRecordTypeFreeze() {
    String[] propertyNames = {"a", "b"};
    JSType[] propertyTypes = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType = createRecordType(propertyNames, propertyTypes);
    recordType.defineProperty("c", JSTypeFactory.booleanType(), false, NODE_FACTORY.booleanLiteral(true));
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("c", JSTypeFactory.booleanType(), false, NODE_FACTORY.booleanLiteral(true));
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    String[] propertyNames1 = {"a", "b"};
    JSType[] propertyTypes1 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType1 = createRecordType(propertyNames1, propertyTypes1);

    String[] propertyNames2 = {"a", "b", "c"};
    JSType[] propertyTypes2 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType(), JSTypeFactory.numberType()};

    RecordType recordType2 = createRecordType(propertyNames2, propertyTypes2);

    assertFalse(recordType1.isEquivalentTo(recordType2));

    String[] propertyNames3 = {"a", "b"};
    JSType[] propertyTypes3 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType3 = createRecordType(propertyNames3, propertyTypes3);

    recordType1.freeze();
    recordType3.freeze();

    assertTrue(recordType1.isEquivalentTo(recordType3));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    String[] propertyNames1 = {"a", "b"};
    JSType[] propertyTypes1 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType1 = createRecordType(propertyNames1, propertyTypes1);

    String[] propertyNames2 = {"a", "b", "c"};
    JSType[] propertyTypes2 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType(), JSTypeFactory.numberType()};

    RecordType recordType2 = createRecordType(propertyNames2, propertyTypes2);

    // recordType1 is a supertype of recordType2
    assertTrue(recordType1.isSubtype(recordType2));

    String[] propertyNames3 = {"a", "b", "d"};
    JSType[] propertyTypes3 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType(), JSTypeFactory.numberType()};

    RecordType recordType3 = createRecordType(propertyNames3, propertyTypes3);

    // recordType1 is not a supertype of recordType3
    assertFalse(recordType1.isSubtype(recordType3));
  }

  @org.junit.Test
  public void testRecordTypeUnion() {
    String[] propertyNames1 = {"a", "b"};
    JSType[] propertyTypes1 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType1 = createRecordType(propertyNames1, propertyTypes1);

    String[] propertyNames2 = {"a", "c"};
    JSType[] propertyTypes2 = {JSTypeFactory.booleanType(), JSTypeFactory.stringType()};

    RecordType recordType2 = createRecordType(propertyNames2, propertyTypes2);

    UnionType unionType = recordType1.getGreatestSubtype(recordType2);

    assertNotNull(unionType);

    JSType expectedPropertyTypes = JSTypeFactory.recordType(Maps.newHashMap());
    expectedPropertyTypes.putProperty("a", JSTypeFactory.booleanType());
    expectedPropertyTypes.putProperty("b", JSTypeFactory.numberType());
    expectedPropertyTypes.putProperty("c", JSTypeFactory.stringType());

    assertEquals(expectedPropertyTypes, unionType);
  }

  @org.junit.Test
  public void testRecordTypeLeastCommonSubtype() {
    String[] propertyNames1 = {"a", "b"};
    JSType[] propertyTypes1 = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType1 = createRecordType(propertyNames1, propertyTypes1);

    String[] propertyNames2 = {"a", "c"};
    JSType[] propertyTypes2 = {JSTypeFactory.booleanType(), JSTypeFactory.stringType()};

    RecordType recordType2 = createRecordType(propertyNames2, propertyTypes2);

    UnionType unionType = recordType1.getLeastSupertype(recordType2);

    JSType expectedPropertyTypes = JSTypeFactory.recordType(Maps.newHashMap());
    expectedPropertyTypes.putProperty("a", JSTypeFactory.booleanType());

    assertEquals(expectedPropertyTypes, unionType);
  }

  @org.junit.Test
  public void testRecordTypeWithIncompatiblePropertyTypes() {
    String[] propertyNames = {"a", "b"};
    JSType[] propertyTypes = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordProperty recordProperty1 = new RecordProperty("a", JSTypeFactory.stringType());
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(JSTypeRegistry.empty());
    recordTypeBuilder.addProperty("a", JSTypeFactory.booleanType(), null);
    recordTypeBuilder.addProperty("b", JSTypeFactory.numberType(), null);
    recordTypeBuilder.addProperty("c", recordProperty1.getType(), recordProperty1.getNode());

    try {
      RecordType recordType = recordTypeBuilder.build();
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testRecordTypeWithMissingProperty() {
    String[] propertyNames = {"a", "b"};
    JSType[] propertyTypes = {JSTypeFactory.booleanType(), JSTypeFactory.numberType()};

    RecordType recordType = createRecordType(propertyNames, propertyTypes);

    try {
      recordType.getPropertyType("c");
      fail("Expected NoSuchElementException");
    } catch (NoSuchElementException e) {
      // Expected
    }
  }
}