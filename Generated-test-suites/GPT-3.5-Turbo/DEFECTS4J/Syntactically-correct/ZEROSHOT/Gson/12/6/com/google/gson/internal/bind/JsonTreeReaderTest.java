import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  private JsonElement element;
  private JsonTreeReader reader;

  @Before
  public void setUp() {
    element = new JsonArray();
    reader = new JsonTreeReader(element);
  }

  @Test
  public void testBeginArray() throws Exception {
    reader.beginArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray() throws Exception {
    reader.endArray();
  }

  @Test
  public void testBeginObject() throws Exception {
    reader.beginObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject() throws Exception {
    reader.endObject();
  }

  @Test
  public void testHasNext() throws Exception {
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testPeek() throws Exception {
    reader.peek();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextName() throws Exception {
    reader.nextName();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextString() throws Exception {
    reader.nextString();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBoolean() throws Exception {
    reader.nextBoolean();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNull() throws Exception {
    reader.nextNull();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDouble() throws Exception {
    reader.nextDouble();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLong() throws Exception {
    reader.nextLong();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextInt() throws Exception {
    reader.nextInt();
  }

  @Test
  public void testClose() throws Exception {
    reader.close();
    assertTrue(reader.isClosed());
  }

  @Test(expected = IllegalStateException.class)
  public void testSkipValue() throws Exception {
    reader.skipValue();
  }

  @Test
  public void testPromoteNameToValue() throws Exception {
    reader.beginObject();
    reader.promoteNameToValue();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testGetPath() throws Exception {
    assertEquals("$", reader.getPath());
  }
}