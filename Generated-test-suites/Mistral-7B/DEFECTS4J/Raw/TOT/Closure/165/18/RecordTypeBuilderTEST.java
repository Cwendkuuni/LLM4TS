import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecordTypeBuilderTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testBuildEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClassType());
  }

  @Test
  public void testBuildRecordWithOneProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringType();
    builder.addProperty("name", stringType, null);
    JSType recordType = builder.build();

    ImmutableMap<String, RecordProperty> properties = recordType.asRecordType().getProperties();
    assertEquals(1, properties.size());
    RecordProperty property = properties.get("name");
    assertEquals(stringType, property.getType());
  }

  @Test
  public void testBuildRecordWithDuplicateProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringType();
    builder.addProperty("name", stringType, null);
    builder.addProperty("name", stringType, null);
    JSType recordType = builder.build();
    assertNotNull(recordType);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildRecordWithInvalidType() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    Object invalidType = new Object();
    builder.addProperty("name", (JSType) invalidType, null);
    builder.build();
  }
}