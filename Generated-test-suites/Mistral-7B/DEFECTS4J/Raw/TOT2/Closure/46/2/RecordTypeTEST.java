import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.*;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Object... args) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void warning(String message, Object... args) {
        // Ignore warnings
      }
    };

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getNativeStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType3 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertFalse(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testDefineProperty() {
    recordType.defineProperty("color", registry.getNativeStringType(), false, null);
    assertTrue(recordType.hasProperty("color"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyFrozen() {
    recordType.isFrozen = true;
    recordType.defineProperty("color", registry.getNativeStringType(), false, null);
    fail("Expected IllegalStateException");
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testGetLeastSupertype() {
    RecordProperty nameProperty = new RecordProperty("name", registry.getNativeStringType());
    RecordProperty ageProperty = new RecordProperty("age", registry.getNumberType());
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", nameProperty);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.putAll(properties1);
    properties2.put("age", ageProperty);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType2, recordType1.getLeastSupertype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordProperty nameProperty = new RecordProperty("name", registry.getNativeStringType());
    RecordProperty ageProperty = new RecordProperty("age", registry.getNumberType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", nameProperty);
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("age", ageProperty);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertTrue(recordType1.isSubtype(greatestSubtype));
  }
}