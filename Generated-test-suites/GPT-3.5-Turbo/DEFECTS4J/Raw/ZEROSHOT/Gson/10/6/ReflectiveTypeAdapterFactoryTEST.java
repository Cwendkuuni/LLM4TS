import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
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
  
  private final Gson gson = new Gson();
  
  @Test
  public void testCreate() {
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
      @Override
      public String translateName(Field f) {
        return f.getName();
      }
    }, new Excluder());
    
    TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(ReflectiveTypeAdapterFactoryTest.class));
    
    assertNotNull(typeAdapterFactory);
  }
  
  @Test
  public void testExcludeField() throws NoSuchFieldException {
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
      @Override
      public String translateName(Field f) {
        return f.getName();
      }
    }, new Excluder());
    
    boolean excludeField = ReflectiveTypeAdapterFactory.excludeField(field, false, reflectiveTypeAdapterFactory.excluder);
    
    assertFalse(excludeField);
  }
  
  @Test
  public void testGetFieldNames() throws NoSuchFieldException {
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
      @Override
      public String translateName(Field f) {
        return f.getName();
      }
    }, new Excluder());
    
    List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
    
    assertEquals(1, fieldNames.size());
    assertEquals("constructorConstructor", fieldNames.get(0));
  }
  
  @Test
  public void testCreateBoundField() throws NoSuchFieldException {
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
      @Override
      public String translateName(Field f) {
        return f.getName();
      }
    }, new Excluder());
    
    TypeToken<?> fieldType = TypeToken.get(field.getGenericType());
    ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "constructorConstructor", fieldType, true, true);
    
    assertNotNull(boundField);
  }
  
  @Test
  public void testGetBoundFields() {
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
      @Override
      public String translateName(Field f) {
        return f.getName();
      }
    }, new Excluder());
    
    TypeToken<?> typeToken = TypeToken.get(ReflectiveTypeAdapterFactory.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, ReflectiveTypeAdapterFactory.class);
    
    assertNotNull(boundFields);
    assertFalse(boundFields.isEmpty());
  }
  
  @Test
  public void testBoundField() throws NoSuchFieldException {
    Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    
    ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("constructorConstructor", true, true) {
      @Override
      boolean writeField(Object value) throws IOException, IllegalAccessException {
        return true;
      }
      
      @Override
      void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
        
      }
      
      @Override
      void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        
      }
    };
    
    assertNotNull(boundField);
    assertEquals("constructorConstructor", boundField.name);
    assertTrue(boundField.serialized);
    assertTrue(boundField.deserialized);
  }
  
  @Test
  public void testAdapter() {
    ObjectConstructor<ReflectiveTypeAdapterFactoryTest> constructor = new ObjectConstructor<ReflectiveTypeAdapterFactoryTest>() {
      @Override
      public ReflectiveTypeAdapterFactoryTest construct() {
        return new ReflectiveTypeAdapterFactoryTest();
      }
    };
    
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactoryTest> adapter = new ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactoryTest>(constructor, new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>());
    
    assertNotNull(adapter);
  }
  
  @Test
  public void testAdapterRead() throws IOException {
    JsonReader jsonReader = new JsonReader(null);
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactoryTest> adapter = new ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactoryTest>(new ObjectConstructor<ReflectiveTypeAdapterFactoryTest>() {
      @Override
      public ReflectiveTypeAdapterFactoryTest construct() {
        return new ReflectiveTypeAdapterFactoryTest();
      }
    }, new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>());
    ReflectiveTypeAdapterFactoryTest reflectiveTypeAdapterFactoryTest = adapter.read(jsonReader);
    
    assertNull(reflectiveTypeAdapterFactoryTest);
  }
  
  @Test
  public void testAdapterWrite() throws IOException {
    JsonWriter jsonWriter = new JsonWriter(null);
    ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactoryTest> adapter = new ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactoryTest>(new ObjectConstructor<ReflectiveTypeAdapterFactoryTest>() {
      @Override
      public ReflectiveTypeAdapterFactoryTest construct() {
        return new ReflectiveTypeAdapterFactoryTest();
      }
    }, new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>());
    
    adapter.write(jsonWriter, null);
  }
}