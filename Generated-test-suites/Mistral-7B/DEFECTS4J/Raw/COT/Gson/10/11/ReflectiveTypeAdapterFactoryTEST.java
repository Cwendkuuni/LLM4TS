import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.FieldInfo;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class ReflectiveTypeAdapterFactoryTest {

  private static final String DEFAULT_NAME = "defaultName";
  private static final String ALTERNATE_NAME = "alternateName";
  private static final String SERIALIZED_NAME = "serializedName";
  private static final String ALTERNATE_SERIALIZED_NAME = "alternateSerializedName";
  private static final String CLASS_NAME = "testClass";
  private static final String INTERFACE_NAME = "testInterface";
  private static final String FIELD_NAME = "testField";
  private static final String FIELD_VALUE = "testValue";

  private Gson gson;
  private Excluder excluder;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

  @Before
  public void setup() {
    excluder = mock(Excluder.class);
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    gson = new GsonBuilder()
        .setFieldNamingStrategy(fieldNamingPolicy)
        .setExcluder(excluder)
        .setConstructorConstructor(constructorConstructor)
        .create();
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreate_NullTypeToken_ReturnsNull() {
    assertNull(reflectiveTypeAdapterFactory.create(gson, null));
  }

  @Test
  public void testCreate_NotObjectType_ReturnsNull() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    assertNull(reflectiveTypeAdapterFactory.create(gson, typeToken));
  }

  @Test
  public void testCreate_ObjectType_ReturnsTypeAdapter() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    TypeAdapter<TestClass> typeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(typeToken)).thenReturn(mock(ObjectConstructor.class));
    when(constructorConstructor.get(typeToken, TestClass.class)).thenReturn(typeAdapter);
    when(fieldNamingPolicy.translateName(ArgumentMatchers.any(Field.class)))
        .thenAnswer(invocation -> DEFAULT_NAME);

    TypeAdapter<TestClass> returnedTypeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
    assertSame(typeAdapter, returnedTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExcludeField_NullField_ThrowsIllegalArgumentException() {
    reflectiveTypeAdapterFactory.excludeField(null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExcludeField_NullSerialize_ThrowsIllegalArgumentException() {
    reflectiveTypeAdapterFactory.excludeField(new Field() {}, null);
  }

  @Test
  public void testGetFieldNames_NoAnnotation_ReturnsSingleList() {
    Field field = new Field() {
      @Override public Type getType() {
        return String.class;
      }
    };

    List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
    assertSame(Collections.singletonList(DEFAULT_NAME), fieldNames);
  }

  @Test
  public void testGetFieldNames_SerializedNameAnnotation_ReturnsListWithDefaultAndAlternateNames() {
    Field field = new Field() {
      @Override public Type getType() {
        return String.class;
      }

      @SerializedName("testSerializedName")
      public String testSerializedName;
    };

    List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
    List<String> expectedFieldNames = new ArrayList<String>();
    expectedFieldNames.add(SERIALIZED_NAME);
    expectedFieldNames.add(DEFAULT_NAME);

    assertSame(expectedFieldNames, fieldNames);
  }

  @Test
  public void testCreateBoundField_NullContext_ThrowsNullPointerException() {
    Field field = new Field() {
      @Override public Type getType() {
        return String.class;
      }
    };

    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(typeAdapter.read(ArgumentMatchers.any(JsonReader.class)))
        .thenAnswer(invocation -> FIELD_VALUE);
    when(typeAdapter.write(ArgumentMatchers.any(JsonWriter.class), ArgumentMatchers.eq(FIELD_VALUE)))
        .thenAnswer(invocation -> {});

    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class)))
        .thenReturn(mock(ObjectConstructor.class));
    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class), ArgumentMatchers.eq(String.class)))
        .thenReturn(typeAdapter);
    when(fieldNamingPolicy.translateName(ArgumentMatchers.any(Field.class)))
        .thenAnswer(invocation -> DEFAULT_NAME);

    ReflectiveTypeAdapterFactory.BoundField boundField =
        reflectiveTypeAdapterFactory.createBoundField(gson, field, DEFAULT_NAME, new TypeToken<String>() {}, true, false);

    assertNull(boundField);
  }

  @Test
  public void testCreateBoundField_JsonAdapterAnnotationPresent_ReturnsTypeAdapter() {
    Field field = new Field() {
      @Override public Type getType() {
        return String.class;
      }

      @JsonAdapter(TestStringAdapter.class)
      public String testField;
    };

    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    when(typeAdapter.read(ArgumentMatchers.any(JsonReader.class)))
        .thenAnswer(invocation -> FIELD_VALUE);
    when(typeAdapter.write(ArgumentMatchers.any(JsonWriter.class), ArgumentMatchers.eq(FIELD_VALUE)))
        .thenAnswer(invocation -> {});

    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class)))
        .thenReturn(mock(ObjectConstructor.class));
    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class), ArgumentMatchers.eq(String.class)))
        .thenReturn(typeAdapter);
    when(fieldNamingPolicy.translateName(ArgumentMatchers.any(Field.class)))
        .thenAnswer(invocation -> DEFAULT_NAME);

    ReflectiveTypeAdapterFactory.BoundField boundField =
        reflectiveTypeAdapterFactory.createBoundField(gson, field, DEFAULT_NAME, new TypeToken<String>() {}, true, false);

    assertSame(typeAdapter, boundField);
  }

  @Test
  public void testCreateBoundField_JsonAdapterAnnotationNotPresent_ReturnsDefaultTypeAdapter() {
    Field field = new Field() {
      @Override public Type getType() {
        return String.class;
      }

      public String testField;
    };

    TypeAdapter<String> defaultTypeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class)))
        .thenReturn(mock(ObjectConstructor.class));
    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class), ArgumentMatchers.eq(String.class)))
        .thenReturn(defaultTypeAdapter);
    when(fieldNamingPolicy.translateName(ArgumentMatchers.any(Field.class)))
        .thenAnswer(invocation -> DEFAULT_NAME);

    ReflectiveTypeAdapterFactory.BoundField boundField =
        reflectiveTypeAdapterFactory.createBoundField(gson, field, DEFAULT_NAME, new TypeToken<String>() {}, true, false);

    assertSame(defaultTypeAdapter, boundField);
  }

  @Test
  public void testGetBoundFields_NullContext_ThrowsNullPointerException() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    List<ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, Object.class);

    assertFalse(boundFields.isEmpty());
  }

  @Test
  public void testGetBoundFields_InterfaceType_ReturnsEmptyMap() {
    TypeToken<TestInterface> typeToken = new TypeToken<TestInterface>() {};
    List<ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestInterface.class);

    assertSame(Collections.emptyList(), boundFields);
  }

  @Test
  public void testGetBoundFields_ClassType_ReturnsBoundFieldsMap() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Class<TestClass> testClass = TestClass.class;

    when(constructorConstructor.get(typeToken))
        .thenReturn(mock(ObjectConstructor.class));
    when(constructorConstructor.get(typeToken, TestClass.class))
        .thenReturn(mock(TypeAdapter.class));
    when(fieldNamingPolicy.translateName(ArgumentMatchers.any(Field.class)))
        .thenAnswer(invocation -> DEFAULT_NAME);

    Field field1 = new Field() {
      @Override public Type getType() {
        return String.class;
      }

      public String testField1;
    };

    Field field2 = new Field() {
      @Override public Type getType() {
        return Integer.class;
      }

      public int testField2;
    };

    when(fieldNamingPolicy.translateName(field1)).thenReturn(DEFAULT_NAME);
    when(fieldNamingPolicy.translateName(field2)).thenReturn(ALTERNATE_NAME);

    List<ReflectiveTypeAdapterFactory.BoundField> boundFields =
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, testClass);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> expectedBoundFields = new LinkedHashMap<>();
    expectedBoundFields.put(DEFAULT_NAME, refFieldBound(DEFAULT_NAME, String.class, true, true));
    expectedBoundFields.put(ALTERNATE_NAME, refFieldBound(ALTERNATE_NAME, Integer.class, true, true));

    assertSame(expectedBoundFields, boundFields);
  }

  private ReflectiveTypeAdapterFactory.BoundField refFieldBound(String name, Type fieldType, boolean serialize,
      boolean deserialize) {
    return new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {
      @Override public boolean writeField(Object value) throws IOException, IllegalAccessException {
        return false;
      }

      @Override public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
      }

      @Override public void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
      }
    };
  }
}

class TestClass {
  public String testField1;
  public int testField2;
}

interface TestInterface {
}

class TestStringAdapter implements JsonAdapter<String> {
  @Override public String read(JsonElement json) throws JsonParseException {
    return null;
  }

  @Override public void write(JsonWriter out, String value) throws IOException {
  }
}