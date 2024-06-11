import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNull(adapter); // expected null since no @JsonAdapter annotation is present
    
    TypeToken<Data> targetType2 = new TypeToken<Data>() {};
    TypeAdapter<Data> adapter2 = factory.create(gson, targetType2);
    assertNotNull(adapter2);
  }

  @Test
  public void testGetTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Object> fieldType = new TypeToken<Object>() {};
    JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);
    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    assertNotNull(adapter);
  }

  @JsonAdapter(DataAdapter.class)
  private static class Data {
    private String value;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  private static class DataAdapter extends TypeAdapter<Data> {
    public DataAdapter() {}

    @Override
    public void write(JsonWriter out, Data value) throws IOException {
      // implementation for write
    }

    @Override
    public Data read(JsonReader in) throws IOException {
      // implementation for read
      return null;
    }
  }

}