import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
  }

  @Test
  public void testRecordWithOneProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", JSType.STRING, null);
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertFalse(recordType.getTypeClass().isInstanceOf(JSTypeNative.class));
    RecordProperty property = builder.properties.get("name");
    assertEquals(JSType.STRING, property.getType());
  }

  @Test
  public void testRecordWithDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", JSType.STRING, null);
    assertNull(builder.addProperty("name", JSType.NUMBER, null));
    JSType recordType = builder.build();
    assertNotNull(recordType);
    // A duplicate property should not cause any issues, but it's an error in
    // the builder API.
    RecordProperty property = builder.properties.get("name");
    assertEquals(JSType.STRING, property.getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordAfterAddingPropertyAndModifyingRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", JSType.STRING, null);
    registry.registerType(JSTypeNative.OBJECT_TYPE, new MyObjectType());
    JSType recordType = builder.build();
  }

  private static class MyObjectType extends JSTypeNative {
    MyObjectType() {
      super(Object.class);
    }
  }
}