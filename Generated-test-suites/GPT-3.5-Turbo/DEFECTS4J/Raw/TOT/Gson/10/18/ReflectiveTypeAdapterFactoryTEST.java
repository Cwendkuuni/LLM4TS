import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;

  @Before
  public void setup() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    FieldNamingStrategy fieldNamingStrategy = mock(FieldNamingStrategy.class);
    Excluder excluder = mock(Excluder.class);
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    gson = new GsonBuilder().registerTypeAdapterFactory(reflectiveTypeAdapterFactory).create();
  }

  @Test
  public void testExcludeField() {
    Field field = mock(Field.class);
    boolean serialize = true;
    boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, reflectiveTypeAdapterFactory);

    assertFalse(result);
    verify(reflectiveTypeAdapterFactory, times(1)).excludeField(field.getType(), serialize);
    verify(reflectiveTypeAdapterFactory, times(1)).excludeField(field, serialize);
  }

  @Test
  public void testCreateTypeAdapterForPrimitiveType() {
    TypeToken<Integer> typeToken = mock(TypeToken.class);
    when(typeToken.getRawType()).thenReturn(int.class);

    TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);

    assertNull(result);
  }

  @Test
  public void testCreateTypeAdapterForObject() {
    TypeToken<Object> typeToken = mock(TypeToken.class);
    when(typeToken.getRawType()).thenReturn(Object.class);

    TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, typeToken);

    assertNotNull(result);
  }

  @Test
  public void testCreateBoundField() {
    Gson context = mock(Gson.class);
    Field field = mock(Field.class);
    String name = "testField";
    TypeToken<?> fieldType = mock(TypeToken.class);
    boolean serialize = true;
    boolean deserialize = true;
    JsonAdapter annotation = mock(JsonAdapter.class);
    TypeAdapter<?> mapped = mock(TypeAdapter.class);
    when(field.getAnnotation(JsonAdapter.class)).thenReturn(annotation);
    when(reflectiveTypeAdapterFactory.getTypeAdapter(reflectiveTypeAdapterFactory.constructorConstructor, context, fieldType, annotation)).thenReturn(mapped);
    when(context.getAdapter(fieldType)).thenReturn(mapped);

    ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, name, fieldType, serialize, deserialize);

    assertNotNull(result);
    verify(field, times(1)).setAccessible(true);
    verify(reflectiveTypeAdapterFactory, times(1)).getTypeAdapter(reflectiveTypeAdapterFactory.constructorConstructor, context, fieldType, annotation);
    verify(mapped, times(1)).write(null, null);
    verify(mapped, times(1)).read(null);
    verify(field, times(1)).get(null);
    verify(field, times(1)).set(null, null);
  }

  @Test
  public void testGetBoundFieldsWithNonInterfaceClass() {
    Gson context = mock(Gson.class);
    TypeToken<?> type = mock(TypeToken.class);
    Class<?> raw = Object.class;

    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);

    assertNotNull(result);
    // additional assertions can be added to verify the behavior of the method
  }

  @Test
  public void testAdapterReadWithNullValue() throws IOException {
    JsonReader in = new JsonReader(new StringReader("null"));

    Object result = reflectiveTypeAdapterFactory.new Adapter(null, null).read(in);

    assertNull(result);
    in.close();
  }

  @Test
  public void testAdapterReadWithNonNullableField() throws IOException {
    JsonReader in = new JsonReader(new StringReader("{\"fieldName\":\"testValue\"}"));

    Object result = reflectiveTypeAdapterFactory.new Adapter(null, null).read(in);

    assertNull(result);
    in.close();
  }

  @Test
  public void testAdapterReadSuccess() throws IOException {
    JsonReader in = new JsonReader(new StringReader("{\"fieldName\":\"testValue\"}"));
    ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    boundFields.put("fieldName", boundField);
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = reflectiveTypeAdapterFactory.new Adapter(null, boundFields);

    Object result = adapter.read(in);

    assertNotNull(result);
    verify(boundField, times(1)).read(in, result);
    in.close();
  }

  @Test
  public void testAdapterWriteWithNullValue() throws IOException {
    JsonWriter out = new JsonWriter(new StringWriter());

    reflectiveTypeAdapterFactory.new Adapter(null, null).write(out, null);

    out.close();
    // additional assertions can be added to verify the behavior of the method
  }

  @Test
  public void testAdapterWriteWithNonNullValue() throws IOException {
    JsonWriter out = new JsonWriter(new StringWriter());
    Object value = mock(Object.class);
    ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    boundFields.put("fieldName", boundField);
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = reflectiveTypeAdapterFactory.new Adapter(null, boundFields);

    adapter.write(out, value);

    verify(boundField, times(1)).writeField(value);
    out.close();
    // additional assertions can be added to verify the behavior of the method
  }

}