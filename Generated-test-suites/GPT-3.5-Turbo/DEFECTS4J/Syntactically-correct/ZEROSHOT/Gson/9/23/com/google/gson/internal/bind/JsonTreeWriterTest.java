import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testGet() {
    Gson gson = new Gson();
    jsonTreeWriter.beginObject()
        .name("name")
        .value("John")
        .name("age")
        .value(25)
        .endObject();

    String json = gson.toJson(jsonTreeWriter.get());

    Assert.assertEquals("{\"name\":\"John\",\"age\":25}", json);
  }

  @Test(expected = IllegalStateException.class)
  public void testEmptyStack() {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testPendingName() {
    jsonTreeWriter.beginObject()
        .name("name")
        .name("age")
        .value(25)
        .endObject();
  }

  @Test
  public void testNullValue() {
    Gson gson = new Gson();
    jsonTreeWriter.beginObject()
        .name("name")
        .value("John")
        .name("age")
        .nullValue()
        .endObject();

    String json = gson.toJson(jsonTreeWriter.get());

    Assert.assertEquals("{\"name\":\"John\",\"age\":null}", json);
  }

  @Test
  public void testBooleanValue() {
    Gson gson = new Gson();
    jsonTreeWriter.beginObject()
        .name("name")
        .value("John")
        .name("isStudent")
        .value(true)
        .endObject();

    String json = gson.toJson(jsonTreeWriter.get());

    Assert.assertEquals("{\"name\":\"John\",\"isStudent\":true}", json);
  }

  @Test
  public void testDoubleValue() {
    Gson gson = new Gson();
    jsonTreeWriter.beginObject()
        .name("name")
        .value("John")
        .name("price")
        .value(9.99)
        .endObject();

    String json = gson.toJson(jsonTreeWriter.get());

    Assert.assertEquals("{\"name\":\"John\",\"price\":9.99}", json);
  }

  @Test
  public void testLongValue() {
    Gson gson = new Gson();
    jsonTreeWriter.beginObject()
        .name("name")
        .value("John")
        .name("count")
        .value(10L)
        .endObject();

    String json = gson.toJson(jsonTreeWriter.get());

    Assert.assertEquals("{\"name\":\"John\",\"count\":10}", json);
  }

  @Test
  public void testNumberValue() {
    Gson gson = new Gson();
    jsonTreeWriter.beginObject()
        .name("name")
        .value("John")
        .name("score")
        .value(85)
        .endObject();

    String json = gson.toJson(jsonTreeWriter.get());

    Assert.assertEquals("{\"name\":\"John\",\"score\":85}", json);
  }
}