import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    // Create a record type with some properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    RecordType recordType = new RecordType(registry, properties);

    // Test the defined properties
    assertTrue(recordType.hasProperty("a"));
    assertTrue(recordType.hasProperty("b"));
    assertTrue(recordType.hasProperty("c"));

    // Test that non-defined properties return null types
    assertNull(recordType.getPropertyType("d"));
    assertNull(recordType.getPropertyType("e"));

    // Test resolveInternal method
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertEquals(recordType, resolvedType);

    // Test isSubtype method
    assertTrue(recordType.isSubtype(recordType));
    assertFalse(recordType.isSubtype(new ObjectType()));
  }
}