import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getDeclaredProperties().length);

    JSType prop1Type = recordType.getPropertyType("prop1");
    JSType prop2Type = recordType.getPropertyType("prop2");

    assertEquals(registry.getStringType(), prop1Type);
    assertEquals(registry.getNumberType(), prop2Type);
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new LinkedHashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new LinkedHashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop3", new RecordProperty("prop3", registry.getNumberType()));

    RecordType type1 = new RecordType(registry, properties1);
    RecordType type2 = new RecordType(registry, properties2);

    assertTrue(type1.isSubtype(type2));
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    try {
      recordType.defineProperty("prop1", registry.getNumberType(), true, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Cannot modify a RecordType after construction", e.getMessage());
    }
  }

  @Test
  public void testRecordTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new LinkedHashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new LinkedHashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType type1 = new RecordType(registry, properties1);
    RecordType type2 = new RecordType(registry, properties2);

    assertTrue(type1.equals(type1));
    assertTrue(type1.equals(type2));
    assertTrue(type2.equals(type1));

    RecordType type3 = new RecordType(registry, properties1);
    RecordType type4 = new RecordType(registry, properties2);

    assertFalse(type1.equals(type3));
    assertFalse(type1.equals(type4));
    assertFalse(type3.equals(type4));
  }

  @Test
  public void testRecordTypeResolution() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    JSType prop1Type = recordType.getPropertyType("prop1");

    JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

    JSType resolvedType = prop1Type.resolve(new ErrorReporter() {}, null);

    assertEquals(numberType, resolvedType);
  }
}