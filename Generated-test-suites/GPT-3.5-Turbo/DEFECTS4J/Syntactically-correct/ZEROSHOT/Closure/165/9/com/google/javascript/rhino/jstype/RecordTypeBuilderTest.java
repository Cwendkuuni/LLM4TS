import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;
import java.util.Map;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder builder;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    String name = "property1";
    JSType type = registry.createStringType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);

    assertNotNull(result);
  }

  @Test
  public void testAddDuplicateProperty() {
    String name = "property1";
    JSType type = registry.createStringType();
    Node propertyNode = new Node();
    
    builder.addProperty(name, type, propertyNode);
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    
    assertNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType result = builder.build();

    assertEquals(expected, result);
  }

  @Test
  public void testBuildRecord() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.createStringType(), new Node()));
    properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.createNumberType(), new Node()));
    builder.addProperty("property1", registry.createStringType(), new Node());
    builder.addProperty("property2", registry.createNumberType(), new Node());

    JSType expected = new RecordType(registry, Collections.unmodifiableMap(properties));
    JSType result = builder.build();

    assertEquals(expected, result);
  }
}