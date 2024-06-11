import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    RecordProperty recordProperty = new RecordProperty("testProperty", int.class);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("testProperty", recordProperty);
    recordType = new RecordType(registry, recordProperties);
  }

  @Test
  public void testIsSubtype() {
    JSType intType = registry.getIntType();
    JSType recordTypeWithIntProperty = new RecordTypeBuilder(registry)
        .addProperty("testProperty", intType, null)
        .build();

    assertTrue(recordType.isSubtype(intType));
    assertTrue(recordType.isSubtype(recordTypeWithIntProperty));
    assertFalse(intType.isSubtype(recordType));
    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(recordType));
  }

  @Test
  public void testIsEquivalent() {
    JSType recordTypeCopy = new RecordType(registry, recordType.properties);
    assertTrue(recordType.isEquivalentTo(recordTypeCopy));
  }

  @Test
  public void testGetPropertyType() {
    assertEquals(int.class, recordType.getPropertyType("testProperty"));
  }

  @Test(expected = NullPointerException.class)
  public void testNullPropertyType() {
    RecordProperty nullRecordProperty = null;
    Map<String, RecordProperty> modifiedProperties = Maps.newHashMap(recordType.properties);
    modifiedProperties.put("testProperty", nullRecordProperty);
    new RecordType(registry, modifiedProperties);
  }

  @Test
  public void testDefineProperty() {
    JSType stringType = registry.getStringType();
    recordType = recordType.defineProperty("testProperty2", stringType, true, null);
    assertEquals(stringType, recordType.getPropertyType("testProperty2"));
  }

  @Test
  public void testDefinePropertyFrozen() {
    recordType.isFrozen = true;
    JSType stringType = registry.getStringType();
    assertFalse(recordType.defineProperty("testProperty3", stringType, true, null) instanceof RecordType);
  }

  @Test
  public void testIsFrozen() {
    assertFalse(recordType.isFrozen);
  }

  @Test
  public void testGreatestSubtype() {
    JSType intRecordType = new RecordTypeBuilder(registry)
        .addProperty("testProperty", int.class, null)
        .build();

    JSType recordTypeWithIntProperty = new RecordTypeBuilder(registry)
        .addProperty("testProperty", int.class, null)
        .addProperty("testProperty2", int.class, null)
        .build();

    JSType intUnionType = new UnionTypeBuilder(registry)
        .addAlternate(intType)
        .build();

    JSType greatestSubtype = recordType.getGreatestSubtype(intRecordType);
    assertEquals(intType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtype(recordTypeWithIntProperty);
    JSType recordWithIntPropertyAsRecord = new RecordTypeBuilder(registry)
        .addProperty("testProperty", int.class, null)
        .addProperty("testProperty2", int.class, null)
        .build();
    assertEquals(recordWithIntPropertyAsRecord, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtype(intUnionType);
    assertEquals(intUnionType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }
}