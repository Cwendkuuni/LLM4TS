import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  // Test for the constructor
  @Test
  public void testConstructor() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    assertNotNull(recordTypeBuilder);
  }

  // Test for addProperty method
  @Test
  public void testAddProperty() {
    String name = "testProperty";
    JSType type = JSType.STRING;
    Node propertyNode = new Node();
    RecordProperty recordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNotNull(recordProperty);
    assertEquals(type, recordProperty.getType());
  }

  // Test for build method (with an empty record)
  @Test
  public void testBuildEmptyRecord() {
    JSType record = recordTypeBuilder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, record.getClass());
  }
}