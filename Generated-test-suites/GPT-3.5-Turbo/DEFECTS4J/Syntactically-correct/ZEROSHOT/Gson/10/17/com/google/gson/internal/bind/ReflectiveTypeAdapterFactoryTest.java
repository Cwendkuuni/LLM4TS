import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class ReflectiveTypeAdapterFactoryTest {
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;
  
  @Before
  public void setUp() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingPolicy = new FieldNamingStrategy();
    Excluder excluder = new Excluder();
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    gson = new Gson();
  }
  
  @Test
  public void testExcludeField() {
    Field f = ReflectiveTypeAdapterFactoryTest.class.getDeclaredField("gson");
    boolean serialize = true;
    boolean result = reflectiveTypeAdapterFactory.excludeField(f, serialize);
    assertFalse(result);
  }
  
  @Test
  public void testCreate() {
    TypeToken<T> type = new TypeToken<T>() {};
    TypeAdapter<T> result = reflectiveTypeAdapterFactory.create(gson, type);
    assertNotNull(result);
  }
  
  @Test
  public void testGetFieldNames() {
    Field f = ReflectiveTypeAdapterFactoryTest.class.getDeclaredField("refleciveTypeAdapterFactory");
    List<String> result = reflectiveTypeAdapterFactory.getFieldNames(f);
    assertEquals(Collections.singletonList("refleciveTypeAdapterFactory"), result);
  }
  
  @Test
  public void testCreateBoundField() {
    Gson context = new Gson();
    Field field = ReflectiveTypeAdapterFactoryTest.class.getDeclaredField("gson");
    String name = "gson";
    TypeToken<?> fieldType = new TypeToken<?>() {};
    boolean serialize = true;
    boolean deserialize = true;
    ReflectiveTypeAdapterFactoryTest.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, name, fieldType, serialize, deserialize);
    assertNotNull(result);
  }
  
  @Test
  public void testGetBoundFields() {
    Gson context = new Gson();
    TypeToken<?> type = new TypeToken<?>() {};
    Class<?> raw = ReflectiveTypeAdapterFactoryTest.class;
    Map<String, ReflectiveTypeAdapterFactoryTest.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);
    assertNotNull(result);
  }
  
  @Test
  public void testAdapterRead() throws IOException {
    JsonReader in = new JsonReader();
    T result = reflectiveTypeAdapterFactory.Adapter.read(in);
    assertNull(result);
  }
  
  @Test
  public void testAdapterWrite() throws IOException {
    JsonWriter out = new JsonWriter();
    T value = null;
    reflectiveTypeAdapterFactory.Adapter.write(out, value);
  }
}