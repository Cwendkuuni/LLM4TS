import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;

  @Before
  public void setUp() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingStrategy = new MyFieldNamingStrategy();
    Excluder excluder = new Excluder();
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapterFactory(reflectiveTypeAdapterFactory);
    gson = gsonBuilder.create();
  }

  @Test
  public void testCreate_DefaultImplementation_Successful() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    TypeAdapter<List<String>> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
    Assert.assertNotNull(typeAdapter);
    List<String> expected = new ArrayList<>();
    expected.add("test1");
    expected.add("test2");
    expected.add("test3");

    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    try {
      typeAdapter.write(jsonWriter, expected);
      jsonWriter.flush();
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
    String json = writer.toString();

    JsonReader reader = new JsonReader(new StringReader(json));

    try {
      List<String> actual = typeAdapter.read(reader);
      Assert.assertEquals(expected, actual);
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreate_PrimitiveType_Successful() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    TypeAdapter<Integer> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
    Assert.assertNull(typeAdapter);
  }

  @Test
  public void testCreate_NullValue_ReturnsNull() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    TypeAdapter<List<String>> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
    Assert.assertNotNull(typeAdapter);

    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    try {
      typeAdapter.write(jsonWriter, null);
      jsonWriter.flush();
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
    String json = writer.toString();

    JsonReader reader = new JsonReader(new StringReader(json));

    try {
      List<String> actual = typeAdapter.read(reader);
      Assert.assertNull(actual);
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_DuplicateFields_ThrowsIllegalArgumentException() {
    TypeToken<DuplicateFieldsClass> typeToken = new TypeToken<DuplicateFieldsClass>() {};
    TypeAdapter<DuplicateFieldsClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
    Assert.assertNotNull(typeAdapter);
  }

  @Test
  public void testAdapter_ReadInvalidJson_ThrowsJsonSyntaxException() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    TypeAdapter<List<String>> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
    Assert.assertNotNull(typeAdapter);

    String invalidJsonString = "[\"test1\",\"test2\"";
    JsonReader reader = new JsonReader(new StringReader(invalidJsonString));

    try {
      typeAdapter.read(reader);
      Assert.fail("Expected JsonSyntaxException to be thrown");
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    } catch (JsonSyntaxException e) {
      // Expected exception
    }
  }

  private static class MyFieldNamingStrategy implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
      return field.getName().toUpperCase();
    }
  }

  private static class DuplicateFieldsClass {
    @SerializedName("name")
    private String name1;

    @SerializedName("name")
    private String name2;
  }

}