import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
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
import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testExcludeField1() {
    Field f = null;
    boolean serialize = true;
    boolean expected = false;
    boolean result = ReflectiveTypeAdapterFactory.excludeField(f, serialize);
    assertEquals(expected, result);
  }

  @Test
  public void testExcludeField2() {
    Field f = null;
    boolean serialize = true;
    boolean expected = false;
    Excluder excluder = new Excluder();
    boolean result = ReflectiveTypeAdapterFactory.excludeField(f, serialize, excluder);
    assertEquals(expected, result);
  }

  @Test
  public void testGetFieldNames() {
    Field f = null;
    List<String> expected = new ArrayList<String>();
    expected.add("name");
    List<String> result = new ReflectiveTypeAdapterFactory().getFieldNames(f);
    assertEquals(expected, result);
  }

  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeToken<?> type = TypeToken.get(Object.class);
    TypeAdapter<?> expected = null;
    TypeAdapter<?> result = new ReflectiveTypeAdapterFactory().create(gson, type);
    assertEquals(expected, result);
  }

  @Test
  public void testCreateBoundField() {
    Gson context = new Gson();
    Field field = null;
    String name = "name";
    TypeToken<?> fieldType = TypeToken.get(Object.class);
    boolean serialize = true;
    boolean deserialize = true;
    ReflectiveTypeAdapterFactory.BoundField expected = null;
    ReflectiveTypeAdapterFactory.BoundField result = new ReflectiveTypeAdapterFactory().createBoundField(context, field, name, fieldType, serialize, deserialize);
    assertEquals(expected, result);
  }

  @Test
  public void testGetBoundFields() {
    Gson context = new Gson();
    TypeToken<?> type = TypeToken.get(Object.class);
    Class<?> raw = Object.class;
    Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>();
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = new ReflectiveTypeAdapterFactory().getBoundFields(context, type, raw);
    assertEquals(expected, result);
  }

  @Test
  public void testWriteField1() {
    Object value = null;
    boolean expected = false;
    boolean result = new ReflectiveTypeAdapterFactory.BoundField("name", true, true).writeField(value);
    assertEquals(expected, result);
  }

  @Test
  public void testWriteField2() {
    Object value = new Object();
    boolean expected = true;
    boolean result = new ReflectiveTypeAdapterFactory.BoundField("name", true, true).writeField(value);
    assertEquals(expected, result);
  }

  @Test
  public void testWrite() {
    JsonWriter writer = null;
    Object value = null;
    try {
      new ReflectiveTypeAdapterFactory.BoundField("name", true, true).write(writer, value);
    } catch (IOException e) {
      fail("IOException thrown");
    } catch (IllegalAccessException e) {
      fail("IllegalAccessException thrown");
    }
  }

  @Test
  public void testRead() {
    JsonReader reader = null;
    Object value = null;
    try {
      new ReflectiveTypeAdapterFactory.BoundField("name", true, true).read(reader, value);
    } catch (IOException e) {
      fail("IOException thrown");
    } catch (IllegalAccessException e) {
      fail("IllegalAccessException thrown");
    }
  }

  @Test
  public void testAdapterRead() {
    JsonReader in = null;
    try {
      new ReflectiveTypeAdapterFactory.Adapter<Object>(new ConstructorConstructor(), new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>()).read(in);
    } catch (IOException e) {
      fail("IOException thrown");
    }
  }

  @Test
  public void testAdapterWrite() {
    JsonWriter out = null;
    try {
      new ReflectiveTypeAdapterFactory.Adapter<Object>(new ConstructorConstructor(), new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>()).write(out, null);
    } catch (IOException e) {
      fail("IOException thrown");
    }
  }
  
}