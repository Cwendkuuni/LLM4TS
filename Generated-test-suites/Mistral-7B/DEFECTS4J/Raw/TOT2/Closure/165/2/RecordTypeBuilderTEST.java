import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {
  @Test
  public void testAddProperty() {
    String name = "property1";
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNumberType();
    Node propertyNode = new Node(); // Assume a valid node

    RecordProperty expectedProperty = new RecordProperty(type, propertyNode);
    RecordTypeBuilder resultBuilder = builder.addProperty(name, type, propertyNode);

    // Check if the builder returned itself
    assertEquals(builder, resultBuilder);

    // Check if the property was added to the properties map
    HashMap<String, RecordProperty> properties = builder.properties;
    assertNotNull(properties);
    RecordProperty actualProperty = properties.get(name);
    assertEquals(expectedProperty.getType(), actualProperty.getType());
  }
}