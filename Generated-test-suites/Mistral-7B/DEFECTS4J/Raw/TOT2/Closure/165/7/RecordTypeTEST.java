
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordProperty recordProperty;

  @Before
  public void setup() {
    errorReporter = mock(ErrorReporter.class);
    registry = new JSTypeRegistry();

    recordProperty = new RecordProperty("name", registry.getStringType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", recordProperty);
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testCreateRecordType() {
    assertEquals("RecordType{name=String}", recordType.toString());
  }

  @Test
  public void testDefinePropertyWithErrorReporter() {
    RecordProperty newProperty = new RecordProperty("age", registry.getIntType());

    try {
      recordType.defineProperty("age", newProperty.getType(), false, null, errorReporter);
      fail("should throw IllegalStateException");
    } catch (IllegalStateException e) {
      verify(errorReporter, times(1)).error("Cannot define property on a frozen RecordType");
    }

    recordType.setFrozen(false);
    recordType.defineProperty("age", newProperty.getType(), false, null, errorReporter);
    assertEquals("RecordType{name=String, age=Number}", recordType.toString());
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getIntType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeWithEmptyObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testIsSubtypeWithEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newTreeMap());

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeWithInvalidObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeWithNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(null.isSubtype(recordType));
  }
}
