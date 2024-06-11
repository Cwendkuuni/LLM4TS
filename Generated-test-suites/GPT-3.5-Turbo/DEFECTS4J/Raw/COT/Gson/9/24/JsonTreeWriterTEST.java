import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test(expected = IllegalStateException.class)
  public void testGetWhenStackNotEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenStackEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenPendingNameNotNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWhenClosed() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenStackEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenPendingNameNotNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenStackEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenPendingNameNotNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1");
    writer.name("name2");
  }

  @Test
  public void testValueWithNullString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value((String) null);
    assertEquals("{\"value\":null}", writer.get().toString());
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    assertEquals("null", writer.get().toString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    assertEquals("true", writer.get().toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleWithNonLenientAndNaN() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleWithNonLenientAndInfinite() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testValueDoubleWithLenient() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setLenient(true);
    writer.value(Double.NaN);
    assertEquals("NaN", writer.get().toString());
    writer.value(Double.POSITIVE_INFINITY);
    assertEquals("Infinity", writer.get().toString());
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10);
    assertEquals("10", writer.get().toString());
  }

  @Test
  public void testValueNumberWithNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value((Number) null);
    assertEquals("null", writer.get().toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueNumberWithNonLenientAndNaN() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueNumberWithNonLenientAndInfinite() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testValueNumberWithLenient() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setLenient(true);
    writer.value(Double.NaN);
    assertEquals("NaN", writer.get().toString());
    writer.value(Double.POSITIVE_INFINITY);
    assertEquals("Infinity", writer.get().toString());
  }

  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
    // No assertions since flush doesn't modify the state
  }

  @Test(expected = IOException.class)
  public void testCloseWhenStackNotEmpty() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
  }
}