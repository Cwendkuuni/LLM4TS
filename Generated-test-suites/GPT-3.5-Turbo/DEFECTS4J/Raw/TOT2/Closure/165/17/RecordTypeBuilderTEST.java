import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty() {
    String propertyName = "testProperty";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();

    RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    assertNotNull(result);
    assertNotNull(recordTypeBuilder.getProperties().get(propertyName));
    assertEquals(propertyType, recordTypeBuilder.getProperties().get(propertyName).getType());
    assertEquals(propertyNode, recordTypeBuilder.getProperties().get(propertyName).getPropertyNode());
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeObjectType());
  }

  @Test
  public void testBuildNonEmptyRecord() {
    String propertyName = "testProperty";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();

    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
    assertEquals(RecordType.class, result.getClass());
  }
}