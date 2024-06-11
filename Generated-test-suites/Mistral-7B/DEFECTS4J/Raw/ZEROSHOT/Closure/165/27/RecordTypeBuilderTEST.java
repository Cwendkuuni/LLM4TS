import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeMockTestCase;
import com.google.javascript.rhino.type.RecordType;
import com.google.javascript.rhino.type.RecordType.Property;
import com.google.javascript.rhino.type.RecordTypeRegistry;

public class RecordTypeBuilderTest extends TypeMockTestCase {

  private RecordTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = mock(RecordTypeRegistry.class);
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testEmptyRecord() {
    JSType result = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getTypeClass());
  }

  @Test
  public void testAddProperty() {
    String name = "name";
    JSType type = mocks(JSType.class);
    Node propertyNode = mock(Node.class);

    builder.addProperty(name, type, propertyNode);

    RecordType recordType = (RecordType) builder.build();
    Property property = recordType.getProperties().get(name);

    assertEquals(type, property.getType());
  }

  @Test
  public void testAddDuplicateProperty() {
    String name = "name";
    JSType type = mocks(JSType.class);
    Node propertyNode = mock(Node.class);

    builder.addProperty(name, type, propertyNode);
    assertNull(builder.addProperty(name, type, propertyNode));
  }

  @Test
  public void testBuildRecordType() {
    String name1 = "name1";
    String name2 = "name2";
    JSType type1 = mocks(JSType.class);
    JSType type2 = mocks(JSType.class);
    Node propertyNode1 = mock(Node.class);
    Node propertyNode2 = mock(Node.class);

    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);

    RecordType recordType = (RecordType) builder.build();
    Map<String, Property> properties = recordType.getProperties();

    assertEquals(type1, properties.get(name1).getType());
    assertEquals(type2, properties.get(name2).getType());
  }
}