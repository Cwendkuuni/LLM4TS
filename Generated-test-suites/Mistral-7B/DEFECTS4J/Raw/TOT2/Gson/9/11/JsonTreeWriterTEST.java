import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_BasicUsage() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.name("city").value("New York");
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    Assert.assertEquals("name", jsonObject.get("name").getAsString());
    Assert.assertEquals(30, jsonObject.get("age").getAsInt());
    Assert.assertEquals("New York", jsonObject.get("city").getAsString());
  }

  @Test
  public void testJsonTreeWriter_ArrayUsage() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    jsonTreeWriter.value(3);
    jsonTreeWriter.endArray();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();

    List<Integer> expectedList = new ArrayList<>();
    expectedList.add(1);
    expectedList.add(2);
    expectedList.add(3);

    Assert.assertEquals(expectedList.size(), jsonArray.size());

    for (int i = 0; i < jsonArray.size(); i++) {
      Assert.assertEquals(expectedList.get(i), jsonArray.get(i).getAsInt());
    }
  }

  @Test
  public void testJsonTreeWriter_NestedObjectsAndArrays() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("user").beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("cities").beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("New York");
    jsonTreeWriter.name("population").value(8_500_000);
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("Los Angeles");
    jsonTreeWriter.name("population").value(4_000_000);
    jsonTreeWriter.endObject();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement user = jsonObject.get("user");

    JsonObject userObject = user.getAsJsonObject();
    Assert.assertEquals("name", userObject.get("name").getAsString());
    Assert.assertEquals(30, userObject.get("age").getAsInt());

    JsonArray cities = userObject.get("cities").getAsJsonArray();

    for (JsonElement city : cities) {
      JsonObject cityObject = city.getAsJsonObject();
      Assert.assertEquals(2, cityObject.size());

      String cityName = cityObject.get("name").getAsString();
      Assert.assertNotNull(cityName);

      int cityPopulation = cityObject.get("population").getAsInt();
      Assert.assertFalse(cityPopulation < 0);
    }
  }

  @Test
  public void testJsonTreeWriter_MissingNameAndValue() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name"); // missing value
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    Assert.assertFalse(jsonObject.has("name"));
  }

  @Test
  public void testJsonTreeWriter_MissingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(30); // missing name
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    Assert.assertTrue(jsonObject.hasKey(""));
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_NestedObjectsMismatchedEnd() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("user").beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("cities").beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("New York");
    jsonTreeWriter.name("population").value(8_500_000);
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject(); // this should not be here

    jsonTreeWriter.get();
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testJsonTreeWriter_MissingEndArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    jsonTreeWriter.value(3);

    jsonTreeWriter.get(); // should throw an exception
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testJsonTreeWriter_MissingEndObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");

    jsonTreeWriter.get(); // should throw an exception
  }
}