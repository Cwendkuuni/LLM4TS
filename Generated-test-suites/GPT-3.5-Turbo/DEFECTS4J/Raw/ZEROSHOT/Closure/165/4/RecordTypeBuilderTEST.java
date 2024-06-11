import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(null);
  }

  @Test
  public void testAddProperty() {
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder returnedBuilder = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNull(returnedBuilder);
  }

  @Test
  public void testBuild_emptyRecord() {
    JSType expectedType = new JSType();
    expectedType.setRegistry(null);
    expectedType.setNativeObjectType(JSType.JSTypeNative.OBJECT_TYPE);
    JSType builtType = recordTypeBuilder.build();
    assertEquals(expectedType, builtType);
  }

  @Test
  public void testBuild_nonEmptyRecord() {
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);

    JSType expectedType = new JSType();
    expectedType.setRegistry(null);
    expectedType.setRecordTypeBuilder(recordTypeBuilder);
    JSType builtType = recordTypeBuilder.build();
    assertEquals(expectedType, builtType);
  }

}