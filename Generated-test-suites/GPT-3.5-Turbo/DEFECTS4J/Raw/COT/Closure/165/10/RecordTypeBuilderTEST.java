import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private RecordTypeBuilder builder;

  @Before
  public void setup() {
    builder = new RecordTypeBuilder();
  }

  @Test
  public void testAddPropertyDuplicateName() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    assertNull(builder.addProperty("name", type, propertyNode));
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType record = builder.build();
    assertEquals(record.getType(), JSTypeNative.OBJECT_TYPE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullName() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty(null, type, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullType() {
    Node propertyNode = new Node();
    builder.addProperty("name", null, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullPropertyNode() {
    JSType type = new JSType();
    builder.addProperty("name", type, null);
  }

}