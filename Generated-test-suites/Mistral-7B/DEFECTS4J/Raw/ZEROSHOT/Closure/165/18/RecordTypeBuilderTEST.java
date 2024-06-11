import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.JSTYPE;
import static com.google.javascript.rhino.JSType.NATIVE_OBJECT_TYPE;
import static com.google.javascript.rhino.JSType.STRING_TYPE;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeRegistry;

public class RecordTypeBuilderTest {

  @Test
  public void testEmptyRecord() {
    RecordTypeRegistry registry = new RecordTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertEquals(NATIVE_OBJECT_TYPE, recordType.getType());
  }

  @Test
  public void testBuildRecordWithOneProperty() {
    RecordTypeRegistry registry = new RecordTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "testProperty";
    JSType propertyType = JSTYPE.STRING_TYPE;
    Node propertyNode = new Node(); // Dummy Node

    JSType recordType = builder.addProperty(propertyName, propertyType, propertyNode)
        .build();

    Map<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
    assertEquals(1, properties.size());
    RecordProperty property = properties.values().iterator().next();
    assertEquals(propertyName, property.getName());
    assertEquals(propertyType, property.getType());
  }

  @Test
  public void testBuildRecordWithDuplicateProperty() {
    RecordTypeRegistry registry = new RecordTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String propertyName = "testProperty";
    JSType propertyType = JSTYPE.STRING_TYPE;
    Node propertyNode = new Node(); // Dummy Node

    RecordTypeBuilder builder1 = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(builder1);

    RecordTypeBuilder builder2 = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNull(builder2);

    JSType recordType = builder.build();
    Map<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
    assertEquals(1, properties.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildRecordAfterAddingEmptyRecord() {
    RecordTypeRegistry registry = new RecordTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.build();
    builder.build();
  }

}