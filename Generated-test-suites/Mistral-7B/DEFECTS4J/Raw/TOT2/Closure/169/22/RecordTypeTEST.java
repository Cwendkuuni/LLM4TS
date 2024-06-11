import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest extends JSTestCase {
  private ErrorReporter errorReporter = new ErrorReporter();
  private JSTypeRegistry registry = new JSTypeRegistry(errorReporter);

  // Test method for RecordType constructor with declared properties
  public void testRecordTypeWithDeclaredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(StringType.getInstance(), recordType.getPropertyType("name"));
    assertEquals(NumberType.INT, recordType.getPropertyType("age"));

    // Basic functionality test
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record = builder.build();
    record.defineProperty("name", StringType.getInstance(), false, null);
    record.defineProperty("age", NumberType.INT, false, null);
    assertEquals(recordType, record);
  }

  // Test method for RecordType constructor with synthesized properties
  public void testRecordTypeWithSynthesizedProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties, false);

    assertEquals(StringType.getInstance(), recordType.getPropertyType("name"));
    assertEquals(NumberType.INT, recordType.getPropertyType("age"));

    // Basic functionality test
    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    JSType recordTypeAsUnion = unionBuilder.addAlternate(recordType)
        .build();
    RecordType record = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(recordTypeAsUnion);
    record.defineProperty("name", StringType.getInstance(), false, null);
    record.defineProperty("age", NumberType.INT, false, null);
    assertEquals(recordType, record);
  }

  // Test method for isSynthetic()
  public void testIsSynthetic() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties, true);
    assertFalse(recordType.isSynthetic());

    properties.clear();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType2 = new RecordType(registry, properties, false);
    assertTrue(recordType2.isSynthetic());
  }

  // Test method for checkRecordEquivalenceHelper()
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("name", new RecordProperty("name", StringType.getInstance()));
    props1.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType1 = new RecordType(registry, props1, true);

    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("name", new RecordProperty("name", StringType.getInstance()));
    props2.put("age", new RecordProperty("age", NumberType.INT));
    props2.put("city", new RecordProperty("city", StringType.getInstance()));
    RecordType recordType2 = new RecordType(registry, props2, true);

    // Test with tolerateUnknowns = false
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    // Test with tolerateUnknowns = true
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  // Test method for getImplicitPrototype()
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  // Test method for defineProperty()
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties, true);

    // Test for property that is already defined
    assertFalse(recordType.defineProperty("name", StringType.getInstance(), false, null));

    // Test for property that is not defined
    assertTrue(recordType.defineProperty("city", StringType.getInstance(), false, null));
    assertEquals(StringType.getInstance(), recordType.getPropertyType("city"));
  }

  // Test method for getGreatestSubtypeHelper()
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("name", new RecordProperty("name", StringType.getInstance()));
    RecordType recordType1 = new RecordType(registry, props1, true);

    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("name", new RecordProperty("name", StringType.getInstance()));
    props2.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType2 = new RecordType(registry, props2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType2, greatestSubtype);

    Map<String, RecordProperty> props3 = Maps.newHashMap();
    props3.put("name", new RecordProperty("name", StringType.getInstance()));
    props3.put("age", new RecordProperty("age", NumberType.INT));
    props3.put("city", new RecordProperty("city", StringType.getInstance()));
    RecordType recordType3 = new RecordType(registry, props3, true);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType2, greatestSubtype);

    JSType otherType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(otherType);
    assertNotNull(greatestSubtype);
    assertTrue(otherType.isSubtype(greatestSubtype));
    assertFalse(recordType2.isSubtype(greatestSubtype));
  }

  // Test method for isSubtype()
  public void testIsSubtype() {
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("name", new RecordProperty("name", StringType.getInstance()));
    RecordType recordType1 = new RecordType(registry, props1, true);

    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("name", new RecordProperty("name", StringType.getInstance()));
    props2.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType2 = new RecordType(registry, props2, true);

    assertTrue(recordType1.isSubtype(recordType2));

    Map<String, RecordProperty> props3 = Maps.newHashMap();
    props3.put("name", new RecordProperty("name", StringType.getInstance()));
    props3.put("age", new RecordProperty("age", NumberType.INT));
    props3.put("city", new RecordProperty("city", StringType.getInstance()));
    RecordType recordType3 = new RecordType(registry, props3, true);

    RecordType recordType4 = new RecordType(registry, props3, false);

    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType1.isSubtype(recordType4));

    Map<String, RecordProperty> props4 = Maps.newHashMap();
    props4.put("name", new RecordProperty("name", StringType.getInstance()));
    props4.put("city", new RecordProperty("city", StringType.getInstance()));
    RecordType recordType5 = new RecordType(registry, props4, true);

    assertTrue(recordType1.isSubtype(recordType5));
  }

  // Test method for resolveInternal()
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties, true);

    RecordType resolvedRecordType = recordType.resolveInternal(errorReporter, null);
    assertEquals(recordType, resolvedRecordType);
  }
}