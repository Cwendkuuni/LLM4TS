import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

/**
 * Test cases for RecordTypeBuilder class.
 */
public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilderWithEmptyList() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    JSType result = recordTypeBuilder.build();
    assertTrue(result instanceof JSTypeNative && result.getTypeID() == JSTypeNative.OBJECT_TYPE);
  }

  @Test
  public void testAddPropertyWithValidArguments() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propertyName";
    JSType propertyType = new IntType(1);
    Node propertyNode = new Node(); // Assuming it's a valid Node instance
    RecordProperty expectedProperty = new RecordProperty(propertyType, propertyNode);

    RecordProperty actualProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode)
        .getProperty();

    assertEquals(expectedProperty.getType(), actualProperty.getType());
    assertEquals(expectedProperty.getPropertyNode(), actualProperty.getPropertyNode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithDuplicateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propertyName";
    JSType propertyType = new IntType(1);
    Node propertyNode = new Node(); // Assuming it's a valid Node instance
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    // This should throw an IllegalArgumentException due to a duplicate property name.
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
  }

  @Test
  public void testBuildWithSingleProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propertyName";
    JSType propertyType = new IntType(1);
    Node propertyNode = new Node(); // Assuming it's a valid Node instance

    RecordProperty property = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode)
        .getProperty();

    JSType result = recordTypeBuilder.build();

    RecordType expectedRecordType = new RecordType(registry, Collections.singletonMap(propertyName, property));
    RecordType actualRecordType = (RecordType) result;

    assertEquals(expectedRecordType.getPropertyNames(), actualRecordType.getPropertyNames());
    assertEquals(expectedRecordType.getProperties().get(propertyName), actualRecordType.getProperties().get(propertyName));
  }
}