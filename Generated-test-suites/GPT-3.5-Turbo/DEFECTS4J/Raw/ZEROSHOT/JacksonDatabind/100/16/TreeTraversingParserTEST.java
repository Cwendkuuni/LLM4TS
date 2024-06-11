import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

  @Test
  public void testSetCodec() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    ObjectCodec codec = new ObjectCodec();
    parser.setCodec(codec);
    assertEquals(codec, parser.getCodec());
  }

  @Test
  public void testIsClosed() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertFalse(parser.isClosed());
    parser.close();
    assertTrue(parser.isClosed());
  }

  @Test
  public void testGetCurrentName() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertNull(parser.getCurrentName());
  }

  @Test
  public void testOverrideCurrentName() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    parser.overrideCurrentName("name");
    assertEquals("name", parser.getCurrentName());
  }

  @Test
  public void testGetParsingContext() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertNotNull(parser.getParsingContext());
  }

  @Test
  public void testGetTokenLocation() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertEquals(JsonLocation.NA, parser.getTokenLocation());
  }

  @Test
  public void testGetCurrentLocation() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertEquals(JsonLocation.NA, parser.getCurrentLocation());
  }

  @Test
  public void testGetText() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertNull(parser.getText());
  }

  @Test
  public void testGetStringText() {
    JsonNode n = new JsonNode();
    n.setValue("value");
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertEquals("value", parser.getText());
  }

  @Test
  public void testGetNumberText() {
    JsonNode n = new JsonNode();
    n.setValue(42);
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertEquals("42", parser.getText());
  }

  @Test
  public void testGetEmbeddedObjectNull() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertNull(parser.getEmbeddedObject());
  }

  @Test
  public void testGetEmbeddedObjectPojo() {
    JsonNode n = new JsonNode();
    n.setValue(new Pojo());
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertEquals(new Pojo(), parser.getEmbeddedObject());
  }

  @Test
  public void testGetEmbeddedObjectBinary() {
    JsonNode n = new JsonNode();
    n.setValue(new Binary());
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertArrayEquals(new Binary().binaryValue(), (byte[]) parser.getEmbeddedObject());
  }

  @Test
  public void testIsNaNFalse() {
    JsonNode n = new JsonNode();
    n.setValue(42);
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertFalse(parser.isNaN());
  }

  @Test
  public void testGetBinaryValueNull() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
  }

  @Test
  public void testGetBinaryValue() {
    JsonNode n = new JsonNode();
    n.setValue(new Binary());
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertArrayEquals(new Binary().binaryValue(), parser.getBinaryValue(Base64Variant.getDefaultVariant()));
  }

  @Test
  public void testReadBinaryValueNull() {
    JsonNode n = new JsonNode();
    TreeTraversingParser parser = new TreeTraversingParser(n);
    assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), new OutputStream()));
  }
}

class JsonNode {
  private Object value;

  public void setValue(Object value) {
    this.value = value;
  }

  public String textValue() {
    return (String) value;
  }

  public int numberValue() {
    return (int) value;
  }

  public boolean isPojo() {
    return value instanceof Pojo;
  }

  public boolean isBinary() {
    return value instanceof Binary;
  }

  public byte[] binaryValue() {
    return ((Binary) value).binaryValue();
  }

  public Number numberValue() {
    return (Number) value;
  }
}

class ObjectCodec {
}

class NodeCursor {
  public boolean currentHasChildren() {
    return false;
  }

  public NodeCursor iterateChildren() {
    return null;
  }

  public JsonToken nextToken() {
    return null;
  }

  public JsonToken endToken() {
    return null;
  }

  public NodeCursor getParent() {
    return null;
  }
}

class ArrayCursor extends NodeCursor {
  public ArrayCursor(JsonNode n, Object o) {
  }
}

class ObjectCursor extends NodeCursor {
  public ObjectCursor(JsonNode n, Object o) {
  }
}

class RootCursor extends NodeCursor {
  public RootCursor(JsonNode n, Object o) {
  }
}

class JsonLocation {
  public static final JsonLocation NA = new JsonLocation();
}

class Pojo {
}

class Binary {
  public byte[] binaryValue() {
    return new byte[0];
  }
}