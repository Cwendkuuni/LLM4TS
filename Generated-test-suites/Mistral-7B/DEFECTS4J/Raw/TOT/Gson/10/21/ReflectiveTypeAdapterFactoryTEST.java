import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory adapterFactory;
  private Gson gson;

  @Before
  public void setup() {
    adapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        });
    gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(adapterFactory)
        .create();
  }

  @Test
  public void testSimpleObjectSerialization() {
    MyClass obj = new MyClass();
    obj.name = "John";
    obj.number = 42;

    String json = gson.toJson(obj);

    assertNotNull(json);
    JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
    assertNotNull(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertNotNull(jsonObject);

    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(42, jsonObject.get("number").getAsInt());
  }

  @Test
  public void testSimpleObjectDeserialization() {
    String json = "{\"name\":\"Jane\",\"number\":5}";

    MyClass obj = gson.fromJson(json, MyClass.class);

    assertNotNull(obj);
    assertEquals("Jane", obj.name);
    assertEquals(5, obj.number);
  }

  @Test
  public void testListSerialization() {
    MyClass obj = new MyClass();
    List<MyListItem> list = new ArrayList<>();
    list.add(new MyListItem("Apples", 1));
    list.add(new MyListItem("Oranges", 2));

    obj.list = list;

    String json = gson.toJson(obj);

    assertNotNull(json);
    JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
    assertNotNull(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertNotNull(jsonObject);

    JsonArray jsonArray = jsonObject.get("list").getAsJsonArray();
    assertNotNull(jsonArray);

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObjectItem = jsonArray.get(i).getAsJsonObject();
      assertEquals("Apples", jsonObjectItem.get("name").getAsString());
      assertEquals(1, jsonObjectItem.get("number").getAsInt());

      jsonObjectItem = jsonArray.get(i + 1).getAsJsonObject();
      assertEquals("Oranges", jsonObjectItem.get("name").getAsString());
      assertEquals(2, jsonObjectItem.get("number").getAsInt());
    }
  }

  @Test
  public void testListDeserialization() {
    String json = "{\"name\":\"MyList\",\"list\":[{\"name\":\"Apples\",\"number\":1},{\"name\":\"Oranges\",\"number\":2}]}";

    MyClass obj = gson.fromJson(json, MyClass.class);

    assertNotNull(obj);
    assertEquals("MyList", obj.name);
    assertNotNull(obj.list);
    assertEquals(2, obj.list.size());

    MyListItem apples = obj.list.get(0);
    assertEquals("Apples", apples.name);
    assertEquals(1, apples.number);

    MyListItem oranges = obj.list.get(1);
    assertEquals("Oranges", oranges.name);
    assertEquals(2, oranges.number);
  }

  @Test
  public void testNullValues() {
    MyClass obj = new MyClass();
    obj.name = null;
    obj.number = null;
    obj.list = null;

    String json = gson.toJson(obj);

    assertNotNull(json);
    JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
    assertNotNull(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertNotNull(jsonObject);

    assertNull(jsonObject.get("name"));
    assertNull(jsonObject.get("number"));
    assertNull(jsonObject.get("list"));
  }

  static class MyClass {
    String name;
    List<MyListItem> list;
    int number;
  }

  static class MyListItem {
    String name;
    int number;
  }
}