import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty_singleProperty_addedPropertyCorrectly() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE);
    String expectedName = "age";
    Node propertyNode = new Node(Token.NAME, expectedName);
    builder.addProperty(expectedName, expectedType, propertyNode);
    JSType recordType = builder.build();

    assertTrue(recordType instanceof RecordType);
    RecordType record = (RecordType) recordType;
    Map<String, RecordTypeBuilder.RecordProperty> properties = record.getProperties();
    assertTrue(properties.containsKey(expectedName));
    assertEquals(expectedType, properties.get(expectedName).getType());
    assertEquals(propertyNode, properties.get(expectedName).getPropertyNode());
  }

  @Test
  public void testAddProperty_duplicatePropertyName_nullReturned() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);
    String propertyName = "name";
    Node propertyNode = new Node(Token.NAME, propertyName);
    builder.addProperty(propertyName, type, propertyNode);
    builder.addProperty(propertyName, type, propertyNode);
    JSType recordType = builder.build();

    assertNull(recordType);
  }

  @Test
  public void testBuild_emptyRecord_emptyRecordTypeReturned() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    assertTrue(recordType instanceof RecordType);
    RecordType record = (RecordType) recordType;
    Map<String, RecordTypeBuilder.RecordProperty> properties = record.getProperties();
    assertTrue(properties.isEmpty());
  }

}