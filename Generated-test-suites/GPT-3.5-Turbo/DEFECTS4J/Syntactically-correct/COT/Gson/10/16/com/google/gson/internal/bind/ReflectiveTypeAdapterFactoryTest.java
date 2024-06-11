

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
  
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  
  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    gson = new Gson();
  }
  
  @Test
  public void testCreate_PrimitiveRawType_ReturnsNull() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
    
    assertEquals(null, result);
  }
  
  @Test
  public void testCreate_ObjectRawType_ReturnsAdapter() {
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    ObjectConstructor<Object> constructor = Mockito.mock(ObjectConstructor.class);
    when(constructorConstructor.get(typeToken)).thenReturn(constructor);
    
    TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
    
    assertEquals(ReflectiveTypeAdapterFactory.Adapter.class, result.getClass());
  }
  
  @Test
  public void testExcludeField_FieldExcludedDuringSerialize_ReturnsTrue() {
    Field field = mock(Field.class);
    when(excluder.excludeClass(field.getType(), true)).thenReturn(true);
    
    boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testExcludeField_FieldExcludedDuringDeserialize_ReturnsTrue() {
    Field field = mock(Field.class);
    when(excluder.excludeClass(field.getType(), false)).thenReturn(true);
    
    boolean result = ReflectiveTypeAdapterFactory.excludeField(field, false, excluder);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testExcludeField_FieldNotExcludedDuringSerializeAndDeserialize_ReturnsFalse() {
    Field field = mock(Field.class);
    when(excluder.excludeClass(field.getType(), true)).thenReturn(false);
    when(excluder.excludeField(field, true)).thenReturn(false);
    
    boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
    
    assertEquals(false, result);
  }
  
  @Test
  public void testGetFieldNames_SerializedNameAnnotationNotPresent_ReturnsTranslatedName() {
    Field field = mock(Field.class);
    when(fieldNamingStrategy.translateName(field)).thenReturn("name");
    
    List<String> result = reflectiveTypeAdapterFactory.getFieldNames(field);
    
    assertEquals(Collections.singletonList("name"), result);
  }
  
  @Test
  public void testGetFieldNames_AlternateNamesPresent_ReturnsAllNames() {
    Field field = mock(Field.class);
    SerializedName annotation = mock(SerializedName.class);
    when(annotation.value()).thenReturn("name");
    when(annotation.alternate()).thenReturn(new String[]{"alt1", "alt2"});
    when(field.getAnnotation(SerializedName.class)).thenReturn(annotation);
    
    List<String> result = reflectiveTypeAdapterFactory.getFieldNames(field);
    
    assertEquals(3, result.size());
    assertEquals("name", result.get(0));
    assertEquals("alt1", result.get(1));
    assertEquals("alt2", result.get(2));
  }
  
  @Test
  public void testCreateBoundField_NoJsonAdapterAnnotation_ReturnsBoundField() {
    Gson context = new Gson();
    Field field = mock(Field.class);
    TypeToken<?> fieldType = TypeToken.get(String.class);
    boolean serialize = true;
    boolean deserialize = true;
    
    ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, "name", fieldType, serialize, deserialize);
    
    assertEquals(ReflectiveTypeAdapterFactory.BoundField.class, result.getClass());
  }
  
  @Test
  public void testCreateBoundField_JsonAdapterAnnotationPresent_ReturnsBoundField() {
    Gson context = new Gson();
    Field field = mock(Field.class);
    TypeToken<?> fieldType = TypeToken.get(String.class);
    boolean serialize = true;
    boolean deserialize = true;
    JsonAdapter annotation = mock(JsonAdapter.class);
    when(field.getAnnotation(JsonAdapter.class)).thenReturn(annotation);
    when(getTypeAdapter(constructorConstructor, context, fieldType, annotation)).thenReturn(null);
    when(context.getAdapter(fieldType)).thenReturn(null);
    
    ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, "name", fieldType, serialize, deserialize);
    
    assertEquals(ReflectiveTypeAdapterFactory.BoundField.class, result.getClass());
  }
  
  @Test
  public void testGetBoundFields_InterfaceRawType_ReturnsEmptyMap() {
    Gson context = new Gson();
    TypeToken<?> type = TypeToken.get(Object.class);
    Class<?> raw = Runnable.class;
    
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);
    
    assertEquals(0, result.size());
  }
  
  @Test
  public void testGetBoundFields_PrimitiveField_ReturnsEmptyMap() {
    Gson context = new Gson();
    TypeToken<?> type = TypeToken.get(Object.class);
    Class<?> raw = Object.class;
    
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);
    
    assertEquals(0, result.size());
  }
  
  @Test
  public void testGetBoundFields_ObjectField_ReturnsBoundFields() {
    Gson context = new Gson();
    TypeToken<?> type = TypeToken.get(Object.class);
    Class<?> raw = ReflectiveTypeAdapterFactory.class;
    Field field = mock(Field.class);
    when(excluder.excludeField(field, true)).thenReturn(false);
    when(excluder.excludeField(field, false)).thenReturn(false);
    when(fieldNamingStrategy.translateName(field)).thenReturn("name");
    List<String> fieldNames = new ArrayList<>();
    fieldNames.add("name");
    when(reflectiveTypeAdapterFactory.getFieldNames(field)).thenReturn(fieldNames);
    TypeToken<?> fieldType = TypeToken.get(String.class);
    when(field.getGenericType()).thenReturn(fieldType.getType());
    when($Gson$Types.resolve(type.getType(), raw, field.getGenericType())).thenReturn(fieldType.getType());
    
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);
    
    assertEquals(1, result.size());
    assertEquals(ReflectiveTypeAdapterFactory.BoundField.class, result.get("name").getClass());
  }
  
  @Test
  public void testAdapter_ReadIsNull_ReturnsNull() throws IOException {
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<Object>(Mockito.mock(ObjectConstructor.class), null);
    JsonReader in = new JsonReader(null);
    
    Object result = adapter.read(in);
    
    assertEquals(null, result);
  }
  
  @Test
  public void testAdapter_ReadHasNextField_ReturnsObject() throws IOException {
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<Object>(Mockito.mock(ObjectConstructor.class), null);
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NAME);
    when(in.nextName()).thenReturn("name1");
    
    Object result = adapter.read(in);
    
    assertEquals(Object.class, result.getClass());
  }
  
  @Test
  public void testAdapter_WriteIsNull_WritesNullValue() throws IOException {
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<Object>(Mockito.mock(ObjectConstructor.class), null);
    JsonWriter out = mock(JsonWriter.class);
    Object value = null;
    
    adapter.write(out, value);
    
    verify(out).nullValue();
  }
  
  @Test
  public void testAdapter_WriteIsNotNull_WritesObject() throws IOException {
    ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<Object>(Mockito.mock(ObjectConstructor.class), null);
    JsonWriter out = mock(JsonWriter.class);
    Object value = mock(Object.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
    boundFields.put("name", mock(ReflectiveTypeAdapterFactory.BoundField.class));
    adapter.boundFields = boundFields;
    
    adapter.write(out, value);
    
    verify(out).beginObject();
    verify(out).endObject();
    verify(out).name(anyString());
  }
}

