import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testRecordTypeBuilderWithOneProperty() {
    JSType numberType = registry.getPrimitiveType(JSTypeNative.NUMBER_TYPE);
    JSType stringType = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);

    Node propertyNode = Node.newObjectLiteral();
    propertyNode.setProperty("name", Node.newString("propertyName"));
    propertyNode.setProperty("type", Node.newString("Number"));

    RecordTypeBuilder recordTypeBuilder = this.recordTypeBuilder;
    RecordType recordType = recordTypeBuilder.addProperty("propertyName", numberType, propertyNode)
        .build();

    assertNotNull(recordType);
    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(1, recordType.getPropertyCount());
    RecordProperty property = recordType.getProperty("propertyName");
    assertEquals(numberType, property.getType());
  }

  @Test
  public void testRecordTypeBuilderWithTwoProperties() {
    JSType stringType = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);
    JSType numberType = registry.getPrimitiveType(JSTypeNative.NUMBER_TYPE);

    Node propertyNode1 = Node.newObjectLiteral();
    propertyNode1.setProperty("name", Node.newString("propertyName1"));
    propertyNode1.setProperty("type", Node.newString("String"));

    Node propertyNode2 = Node.newObjectLiteral();
    propertyNode2.setProperty("name", Node.newString("propertyName2"));
    propertyNode2.setProperty("type", Node.newString("Number"));

    RecordTypeBuilder recordTypeBuilder = this.recordTypeBuilder;
    RecordType recordType = recordTypeBuilder.addProperty("propertyName1", stringType, propertyNode1)
        .addProperty("propertyName2", numberType, propertyNode2)
        .build();

    assertNotNull(recordType);
    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(2, recordType.getPropertyCount());
    RecordProperty property1 = recordType.getProperty("propertyName1");
    assertEquals(stringType, property1.getType());
    RecordProperty property2 = recordType.getProperty("propertyName2");
    assertEquals(numberType, property2.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeBuilderWithDuplicateProperty() {
    JSType stringType = registry.getPrimitiveType(JSTypeNative.STRING_TYPE);

    Node propertyNode = Node.newObjectLiteral();
    propertyNode.setProperty("name", Node.newString("propertyName"));
    propertyNode.setProperty("type", Node.newString("String"));

    RecordTypeBuilder recordTypeBuilder = this.recordTypeBuilder;
    recordTypeBuilder.addProperty("propertyName", stringType, propertyNode)
        .addProperty("propertyName", stringType, propertyNode);

    recordTypeBuilder.build();
  }

  @Test(expected = NullPointerException.class)
  public void testRecordTypeBuilderWithNullPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    Node propertyNode = Node.newObjectLiteral();
    propertyNode.setProperty("name", Node.newString("propertyName"));
    propertyNode.setProperty("type", Node.newNull());

    recordTypeBuilder.addProperty("propertyName", null, propertyNode);
    recordTypeBuilder.build();
  }
}