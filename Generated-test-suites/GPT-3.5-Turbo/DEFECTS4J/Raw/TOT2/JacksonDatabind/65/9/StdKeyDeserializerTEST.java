import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

  private StdKeyDeserializer stdKeyDeserializer;

  @Before
  public void setup() {
    stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
  }

  @Test
  public void testDeserializeKey() throws IOException {
    Object result = stdKeyDeserializer.deserializeKey("test", new DeserializationContext());
    assertEquals("test", result);
  }

  @Test
  public void testDeserializeKey_NullKey() throws IOException {
    Object result = stdKeyDeserializer.deserializeKey(null, new DeserializationContext());
    assertNull(result);
  }

  @Test
  public void testDeserializeKey_InvalidKey() throws IOException {
    try {
      stdKeyDeserializer.deserializeKey("", new DeserializationContext());
      fail("Expected IOException");
    } catch (IOException e) {
      // Pass
    }
  }

  @Test
  public void testParse_String() throws Exception {
    Object result = stdKeyDeserializer._parse("test", new DeserializationContext());
    assertEquals("test", result);
  }

  @Test
  public void testParse_Boolean() throws Exception {
    stdKeyDeserializer = new StdKeyDeserializer(1, Boolean.class);
    Object result = stdKeyDeserializer._parse("true", new DeserializationContext());
    assertEquals(true, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParse_InvalidInt() throws Exception {
    stdKeyDeserializer = new StdKeyDeserializer(5, Integer.class);
    stdKeyDeserializer._parse("abc", new DeserializationContext());
  }

  @Test
  public void testParse_URISyntaxException() throws Exception {
    stdKeyDeserializer = new StdKeyDeserializer(13, URI.class);
    try {
      stdKeyDeserializer._parse("invalid", new DeserializationContext());
      fail("Expected IOException");
    } catch (Exception e) {
      // Pass
    }
  }

  @Test
  public void testParse_MalformedURLException() throws Exception {
    stdKeyDeserializer = new StdKeyDeserializer(14, URL.class);
    try {
      stdKeyDeserializer._parse("invalid", new DeserializationContext());
      fail("Expected IOException");
    } catch (Exception e) {
      // Pass
    }
  }

  @Test
  public void testParse_ClassNotFoundException() throws Exception {
    stdKeyDeserializer = new StdKeyDeserializer(15, Class.class);
    try {
      stdKeyDeserializer._parse("invalid", new DeserializationContext());
      fail("Expected IOException");
    } catch (Exception e) {
      // Pass
    }
  }

  @Test
  public void testParse_UnknownKey() {
    try {
      stdKeyDeserializer._parse("unknown", new DeserializationContext());
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Pass
    }
  }
}