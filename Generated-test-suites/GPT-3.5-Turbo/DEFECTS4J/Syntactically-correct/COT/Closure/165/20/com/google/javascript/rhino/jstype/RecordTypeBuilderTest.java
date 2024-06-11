import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty("property1", type, propertyNode);
    Map<String, RecordTypeBuilder.RecordProperty> properties = Collections.singletonMap("property1", new RecordTypeBuilder.RecordProperty(type, propertyNode));
    Assert.assertEquals(new RecordType(new JSTypeRegistry(), properties), recordTypeBuilder.build());
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType expectedType = new JSType();
    JSTypeRegistry registry = new JSTypeRegistry();
    Assert.assertEquals(expectedType, recordTypeBuilder.build());
    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordTypeBuilder.build());
  }
}