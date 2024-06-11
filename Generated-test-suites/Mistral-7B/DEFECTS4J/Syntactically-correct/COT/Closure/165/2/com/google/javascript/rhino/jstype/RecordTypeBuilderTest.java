import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for RecordTypeBuilder.
 */
public class RecordTypeBuilderTest {

  @SuppressWarnings("unchecked")
  private JSTypeRegistry createRegistryMock() {
    return new JSTypeRegistry() {
      @Override
      public JSType getNativeObjectType(int type) {
        return (JSType) new JSTypeNative.ObjectType();
      }

      @Override
      public JSType getBooleanType() {
        return JSType.BOOLEAN;
      }

      @Override
      public JSType getNumberType() {
        return JSType.NUMBER;
      }

      @Override
      public JSType getStringType() {
        return JSType.STRING;
      }

      @Override
      public JSType getNullType() {
        return JSType.NULL;
      }
    };
  }

  /**
   * Test method for {@link RecordTypeBuilder#addProperty(String, JSType, Node)}.
   */
  @Test
  public void testAddProperty() {
    String name = "testProperty";
    JSType expectedType = JSType.STRING;
    Node propertyNode = new Node();

    RecordTypeBuilder builder = new RecordTypeBuilder(createRegistryMock());
    RecordProperty result = builder.addProperty(name, expectedType, propertyNode);

    assertNull(result); // Should return null if property already exists.

    builder = new RecordTypeBuilder(createRegistryMock());
    result = builder.addProperty(name, expectedType, propertyNode);

    assertEquals(expectedType, result.getType());
    assertNotNull(result.getPropertyNode());
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()}.
   */
  @Test
  public void testBuildEmpty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(createRegistryMock());

    JSType result = builder.build();

    assertEquals(JSType.OBJECT_TYPE, result.getClass());
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()}.
   */
  @Test
  public void testBuildWithProperties() {
    String name1 = "testProperty1";
    String name2 = "testProperty2";
    JSType type1 = JSType.BOOLEAN;
    JSType type2 = JSType.NUMBER;

    RecordTypeBuilder builder = new RecordTypeBuilder(createRegistryMock());
    builder.addProperty(name1, type1, new Node());
    builder.addProperty(name2, type2, new Node());

    RecordType result = (RecordType) builder.build();
    Map<String, JSType> expectedProperties = Maps.newHashMap();
    expectedProperties.put(name1, type1);
    expectedProperties.put(name2, type2);

    assertEquals(expectedProperties, result.getProperties());
  }

  /**
   * Test method for {@link RecordTypeBuilder#build()} with duplicate property.
   */
  @Test(expected = IllegalStateException.class)
  public void testBuildWithDuplicateProperty() {
    String name = "testProperty";
    JSType type = JSType.STRING;

    RecordTypeBuilder builder = new RecordTypeBuilder(createRegistryMock());
    builder.addProperty(name, type, new Node());

    // This should throw an IllegalStateException since addProperty returns null
    // when a property with the same name already exists.
    builder.addProperty(name, type, new Node());
  }
}