import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import org.junit.Assert;

import java.util.Collections;
import java.util.Map;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreationWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getBooleanType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("RecordType", recordType.getName());
    assertEquals(1, recordType.getProperties().size());
    assertEquals(registry.getBooleanType(), recordType.getPropertyType("prop1"));
  }

  @Test
  public void testCheckRecordEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> prop1 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getBooleanType()));
    Map<String, RecordProperty> prop2 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getBooleanType()));
    Map<String, RecordProperty> prop3 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);
    RecordType recordType3 = new RecordType(registry, prop3, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);

    recordType.defineProperty("prop1", registry.getBooleanType(), false, null);

    assertEquals(1, recordType.getProperties().size());
    assertEquals(registry.getBooleanType(), recordType.getPropertyType("prop1"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyAfterFreeze() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);
    recordType.isFrozen = true;

    recordType.defineProperty("prop1", registry.getBooleanType(), false, null);
  }

  @Test
  public void testErrorReporting() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message) {
        Assert.fail(message);
      }

      @Override
      public void fatalError(String message) {
        Assert.fail(message);
      }
    };

    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true) {
      @Override
      public void error(String message) {
        super.error(message);
        errorReporter.error(message);
      }

      @Override
      public void fatalError(String message) {
        super.fatalError(message);
        errorReporter.fatalError(message);
      }
    };

    recordType.defineProperty("prop1", registry.getNullType(), false, null);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> prop1 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getBooleanType()));
    Map<String, RecordProperty> prop2 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> prop1 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getStringType()));
    Map<String, RecordProperty> prop2 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getNumberType()));
    Map<String, RecordProperty> prop3 = Collections.singletonMap(
        "prop1", new RecordProperty("prop1", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);
    RecordType recordType3 = new RecordType(registry, prop3, true);

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType1.getClass(), greatestSubtype.getClass());

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType1.getClass(), greatestSubtype.getClass());
  }
}