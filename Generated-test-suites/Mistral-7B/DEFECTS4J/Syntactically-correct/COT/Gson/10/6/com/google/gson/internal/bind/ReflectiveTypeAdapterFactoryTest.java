import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory adapterFactory;
  private Gson gson;

  @Before
  public void setup() {
    adapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {}, Excluder.INSTANCE);
    gson = new GsonBuilder().setLenient().create();
  }

  @Test
  public void testCreateAdapter() {
    TypeAdapterFactory factory = adapterFactory;
    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeField() {
    MyClass myClass = new MyClass();
    Field field = MyClass.class.getDeclaredField("excludedField");
    assertFalse(adapterFactory.excludeField(field, true));
    assertFalse(adapterFactory.excludeField(field, false));

    field.setAccessible(true);
    field.set(myClass, "excludedValue");

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(new byte[0]));
    jsonReader.setLenient(true);
    try {
      gson.fromJson(jsonReader, MyClass.class, adapter);
      fail();
    } catch (JsonSyntaxException e) {
      // expected
    }

    JsonWriter jsonWriter = new JsonWriter(new ByteArrayOutputStream());
    try {
      adapter.write(jsonWriter, myClass);
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testGetBoundFields() {
    MyClass myClass = new MyClass();
    Field field1 = MyClass.class.getDeclaredField("field1");
    Field field2 = MyClass.class.getDeclaredField("field2");

    field1.setAccessible(true);
    field2.setAccessible(true);

    field1.set(myClass, "field1Value");
    field2.set(myClass, "field2Value");

    String json = "{\"field1\":\"field1Value\",\"field2\":\"field2Value\"}";

    JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(json.getBytes()));
    JsonElement jsonElement = jsonReader.readTree();

    MyClass deserializedObject = gson.fromJson(jsonElement, MyClass.class);

    assertNotNull(deserializedObject);
    assertEquals("field1Value", deserializedObject.getField1());
    assertEquals("field2Value", deserializedObject.getField2());

    JsonWriter jsonWriter = new JsonWriter(new ByteArrayOutputStream());
    adapter.write(jsonWriter, myClass);
    String jsonString = jsonWriter.toString();

    JsonElement jsonElement2 = new JsonParser().parse(jsonString);

    assertEquals(json, jsonString);

    MyClass deserializedObject2 = gson.fromJson(jsonElement2, MyClass.class);

    assertNotNull(deserializedObject2);
    assertEquals("field1Value", deserializedObject2.getField1());
    assertEquals("field2Value", deserializedObject2.getField2());
  }
}

class MyClass {
  @SerializedName("field1")
  private String field1;

  @SerializedName("field2")
  private int field2;

  @SerializedName("excludedField")
  private String excludedField;

  public String getField1() {
    return field1;
  }

  public int getField2() {
    return field2;  }

  public String getExcludedField() {
    return excludedField;
  }

  // setters omitted for brevity
}