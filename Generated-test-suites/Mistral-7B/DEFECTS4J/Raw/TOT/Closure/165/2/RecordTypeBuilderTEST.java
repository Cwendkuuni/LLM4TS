import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

  @org.junit.Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    // Test adding a property to the record type
    JSType stringType = mock(JSType.class);
    Node propertyNode = mock(Node.class);
    RecordProperty recordProperty = recordTypeBuilder.addProperty("name", stringType, propertyNode);
    assertNotNull(recordProperty);
    assertEquals(stringType, recordProperty.getType());

    // Test building the record type
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", recordProperty);
    RecordType recordType = recordTypeBuilder.build();
    assertNotNull(recordType);
    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(properties, recordType.getProperties());

    // Test creating an empty record type
    RecordTypeBuilder emptyRecordTypeBuilder = new RecordTypeBuilder(registry);
    RecordType emptyRecordType = emptyRecordTypeBuilder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecordType.getType());
  }
}