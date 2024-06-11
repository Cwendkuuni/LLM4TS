import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory factory;
  private Excluder excluder;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Gson gson;

  @Before
  public void setup() {
    excluder = Mockito.mock(Excluder.class);
    constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    fieldNamingPolicy = Mockito.mock(FieldNamingStrategy.class);
    gson = new GsonBuilder()
        .setExcluder(excluder)
        .setConstructorConstructor(constructorConstructor)
        .setFieldNamingStrategy(fieldNamingPolicy)
        .create();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  @Test
  public void testCreateNull() {
    TypeAdapter<?> adapter = factory.create(gson, null);
    Assert.assertNull(adapter);
  }

  @Test
  public void testCreateNotObjectType() {
    TypeAdapter<?> adapter = factory.create(gson, new TypeToken<Integer>() {}.getType());
    Assert.assertNull(adapter);
  }

  @Test
  public void testCreateObjectType() throws Exception {
    MyClass myClass = new MyClass();
    MyClass constructorResult = new MyClass();
    ObjectConstructor<MyClass> constructor = Mockito.mock(ObjectConstructor.class);
    Mockito.when(constructor.construct()).thenReturn(constructorResult);
    Mockito.when(constructorConstructor.get(TypeToken.get(MyClass.class))).thenReturn(constructor);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("name", true, false) {
      @Override
      boolean writeField(Object value) throws IOException, IllegalAccessException {
        return false;
      }
    };
    boundFields.put("name", boundField);

    MyClass adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
    TypeAdapter<?> createdAdapter = factory.create(gson, new TypeToken<MyClass>() {}.getType());
    Assert.assertEquals(adapter, createdAdapter);
  }

  @Test
  public void testWrite() throws Exception {
    MyClass myClass = new MyClass();
    JsonWriter writer = Mockito.mock(JsonWriter.class);
    ObjectConstructor<MyClass> constructor = Mockito.mock(ObjectConstructor.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("name", true, false);
    boundFields.put("name", boundField);
    MyClass adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);

    Mockito.when(constructor.construct()).thenReturn(myClass);

    factory.create(gson, new TypeToken<MyClass>() {}.getType()).write(writer, myClass);

    Mockito.verify(writer).value("");
    Mockito.verify(writer).name("name");
    Mockito.verifyNoMoreInteractions(writer);
  }

  @Test
  public void testRead() throws Exception {
    MyClass myClass = new MyClass();
    JsonReader reader = Mockito.mock(JsonReader.class);
    ObjectConstructor<MyClass> constructor = Mockito.mock(ObjectConstructor.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    MyClass expectedValue = new MyClass();
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("name", true, false) {
      @Override
      void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        Assert.assertEquals("name", reader.nextName());
        Assert.assertEquals("value", reader.nextString());
        ((MyClass) value).setName("value");
      }
    };
    boundFields.put("name", boundField);
    MyClass adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);

    Mockito.when(constructor.construct()).thenReturn(myClass);
    Mockito.when(reader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    Mockito.when(reader.nextName()).thenReturn("name");
    Mockito.when(reader.nextNull()).thenReturn(false);
    Mockito.when(reader.nextString()).thenReturn("value");

    factory.create(gson, new TypeToken<MyClass>() {}.getType()).read(reader, myClass);

    Mockito.verify(reader).beginObject();
    Mockito.verify(reader).nextName();
    Mockito.verify(reader).nextString();
    Mockito.verify(reader).nextNull();
    Mockito.verifyNoMoreInteractions(reader);
    Assert.assertEquals("value", myClass.getName());
  }

  static class MyClass {
    @SerializedName("name")
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}