import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType;

public class RecordTypeBuilderTest {
  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testProperty";
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);

    RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);

    assertNotNull(recordProperty);
  }
}