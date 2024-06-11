import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.CommonJsTestUtil.parseAndEvaluate;
import static com.google.javascript.rhino.testing.TypeChecker.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeChecker;
import com.google.javascript.rhino.testing.TypeCheckerRegistry;
import com.google.javascript.rhino.jstype.*;

import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {

  private static final TypeChecker typeChecker = new TypeChecker();

  @Test
  public void testRecordType() {
    JSTypeRegistry registry = TypeCheckerRegistry.create();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT));

    RecordType recordType = new RecordType(registry, properties);
    JSType type = recordType;

    // Test the record type with valid data
    Object record = parseAndEvaluate(registry, "{\"name\": \"John\", \"age\": 30}");
    assertEquals(type, typeChecker.checkType(record));

    // Test the record type with a missing property
    try {
      Object missingPropertyRecord = parseAndEvaluate(registry, "{\"name\": \"John\"}");
      typeChecker.checkType(missingPropertyRecord, type);
      fail("should have thrown an error");
    } catch (TypeMismatchError e) {
      // Expected
    }

    // Test the record type with an extra property
    try {
      Object extraPropertyRecord = parseAndEvaluate(registry, "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
      typeChecker.checkType(extraPropertyRecord, type);
      fail("should have thrown an error");
    } catch (TypeMismatchError e) {
      // Expected
    }

    // Test the record type with the wrong property type
    try {
      Object wrongPropertyTypeRecord = parseAndEvaluate(registry, "{\"name\": 30, \"age\": \"John\"}");
      typeChecker.checkType(wrongPropertyTypeRecord, type);
      fail("should have thrown an error");
    } catch (TypeMismatchError e) {
      // Expected
    }
  }
}