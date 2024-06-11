import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreateNullTargetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new GsonBuilder().setPrePreProcessor(factory).create();
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreateTargetTypeWithJsonAdapterAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new GsonBuilder().setPrePreProcessor(factory).create();

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    String jsonAdapterAnnotationType = String.class.getName() + "$MyStringJsonAdapter";
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return JsonAdapterAnnotationTypeAdapterFactoryTest_StringJsonAdapter.class;
      }
    };
    String jsonString = "\"Hello World\"";
    String expectedJsonString = "{\"value\":\"Hello World\"}";

    TypeAdapter<String> stringTypeAdapter = factory.create(gson, stringTypeToken);
    assertEquals(expectedJsonString, stringTypeAdapter.toJson(new String("Hello World")));
    assertEquals("Hello World", stringTypeAdapter.fromJson("{\"value\":\"Hello World\"}", String.class));
  }

  @Test
  public void testCreateTargetTypeWithNoJsonAdapterAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new GsonBuilder().setPrePreProcessor(factory).create();

    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    Integer intValue = 1;
    Integer expectedIntValue = 1;

    TypeAdapter<Integer> intTypeAdapter = factory.create(gson, intTypeToken);
    assertEquals(expectedIntValue, intTypeAdapter.fromJson("1", Integer.class));
    assertEquals(intValue.toString(), intTypeAdapter.toJson(intValue));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTargetTypeWithInvalidJsonAdapterAnnotationValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new GsonBuilder().setPrePreProcessor(factory).create();

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    String jsonAdapterAnnotationType = String.class.getName() + "$MyStringJsonAdapter";
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return new Object() {};
      }
    };
    String jsonString = "{\"value\":\"Hello World\"}";

    TypeAdapter<String> stringTypeAdapter = factory.create(gson, stringTypeToken);
    // This test should fail with an IllegalArgumentException.
    stringTypeAdapter.fromJson(jsonString, String.class);
  }

  @Test
  public void testCreateTargetTypeWithTypeAdapterFactoryJsonAdapterAnnotationValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new GsonBuilder().setPrePreProcessor(factory).create();

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    String jsonAdapterAnnotationType = StringJsonAdapterFactory.class.getName();
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return JsonAdapterAnnotationTypeAdapterFactoryTest_StringJsonAdapterFactory.class;
      }
    };
    String jsonString = "{\"value\":\"Hello World\"}";
    String expectedJsonString = "{\"value\":\"Hello World\"}";

    TypeAdapter<String> stringTypeAdapter = factory.create(gson, stringTypeToken);
    assertEquals(expectedJsonString, stringTypeAdapter.toJson(new String("Hello World")));
    assertEquals("Hello World", stringTypeAdapter.fromJson(jsonString, String.class));
  }

  @Test(expected = NullPointerException.class)
  public void testCreateTargetTypeWithNullAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new GsonBuilder().setPrePreProcessor(factory).create();

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapter annotation = null;
    String jsonString = "{\"value\":\"Hello World\"}";

    TypeAdapter<String> stringTypeAdapter = factory.create(gson, stringTypeToken);
    // This test should fail with a NullPointerException.
    stringTypeAdapter.fromJson(jsonString, String.class);
  }

  static class StringJsonAdapter {
    String value;

    String toJson() {
      return "{\"value\":\"" + value + "\"}";
    }

    String fromJson(String jsonString) {
      String json = jsonString.substring(1, jsonString.length() - 1);
      String jsonValue = json.substring(json.indexOf(":") + 1, json.length());
      this.value = jsonValue.substring(1, jsonValue.length() - 1);
      return this.value;
    }
  }

  static class StringJsonAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      return new StringJsonAdapter();
    }
  }

  static class JsonAdapterAnnotationTypeAdapterFactoryTest_StringJsonAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader jsonReader) throws IOException {
      return jsonReader.nextString();
    }

    @Override
    public void write(JsonWriter jsonWriter, String value) throws IOException {
      jsonWriter.value(value);
    }
  }
}