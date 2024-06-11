import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
  private RecordTypeBuilder recordTypeBuilder;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    String name = "property";
    JSType type = registry.createType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    RecordTypeBuilder.RecordProperty recordProperty =
        recordTypeBuilder.addProperty(name, type, propertyNode);

    Assert.assertNotNull(recordProperty);
    Assert.assertEquals(type, recordProperty.getType());
    Assert.assertEquals(propertyNode, recordProperty.getPropertyNode());
    Assert.assertFalse(recordTypeBuilder.isEmpty());
    Assert.assertTrue(recordTypeBuilder.addProperty(name, type, propertyNode) == null);
  }

  @Test
  public void testBuildEmpty() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType recordType = recordTypeBuilder.build();
    Map<String, RecordTypeBuilder.RecordProperty> properties = Collections.emptyMap();

    Assert.assertEquals(objectType, recordType);
    Assert.assertTrue(recordType instanceof RecordType);
    Assert.assertEquals(properties, ((RecordType) recordType).getProperties());
  }

  @Test
  public void testBuild() {
    String name = "property";
    JSType type = registry.createType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty(name, type, propertyNode);

    JSType recordType = recordTypeBuilder.build();
    Map<String, RecordTypeBuilder.RecordProperty> properties = Maps.newHashMap();
    properties.put(name, new RecordTypeBuilder.RecordProperty(type, propertyNode));

    Assert.assertTrue(recordType instanceof RecordType);
    Assert.assertEquals(properties, ((RecordType) recordType).getProperties());
  }
}