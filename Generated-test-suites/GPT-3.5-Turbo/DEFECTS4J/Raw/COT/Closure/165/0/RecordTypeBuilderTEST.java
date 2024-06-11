import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

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
    JSType type = new JSType();
    Node propertyNode = new Node();

    assertTrue(builder.addProperty("property1", type, propertyNode) != null);
    assertFalse(builder.addProperty("property1", type, propertyNode) != null);
  }

  @Test
  public void testBuild() {
    JSType type = new JSType();
    Node propertyNode = new Node();

    builder.addProperty("property1", type, propertyNode);

    assertTrue(builder.build() instanceof RecordType);
    assertFalse(builder.isEmpty);
  }
}