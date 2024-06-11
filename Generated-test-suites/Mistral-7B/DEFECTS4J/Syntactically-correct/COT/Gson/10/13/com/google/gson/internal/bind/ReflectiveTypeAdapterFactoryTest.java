import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;

  @Before
  public void setup() {
    gson = new GsonBuilder().create();
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
  }

  @Test
  public void testCreate_NullTypeToken_ReturnsNull() {
    // Given
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeToken<Object> nullTypeToken = null;

    // When
    TypeAdapter<Object> adapter = factory.create(gson, nullTypeToken);

    // Then
    assertNull(adapter);
  }

  @Test
  public void testCreate_NonObjectType_ReturnsNull() {
    // Given
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};

    // When
    TypeAdapter<Integer> adapter = factory.create(gson, intTypeToken);

    // Then
    assertNull(adapter);
  }

  @Test
  public void testCreate_EmptyClass_ReturnsNull() {
    // Given
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeToken<Object> objectTypeToken = new TypeToken<Object>() {};

    // When
    TypeAdapter<Object> adapter = factory.create(gson, objectTypeToken);

    // Then
    assertNull(adapter);
  }

  @Test
  public void testCreate_ValidType_CreatesAdapterInstance() {
    // Given
    TestClass testClass = new TestClass();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};

    // When
    TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);

    // Then
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeField_NullField_ReturnsFalse() {
    // Given
    Field nullField = null;
    boolean exclude = ReflectiveTypeAdapterFactory.excludeField(nullField, true);

    // Then
    assertFalse(exclude);
  }

  @Test
  public void testExcludeField_NullExcluder_ReturnsFalse() {
    // Given
    Field field = mock(Field.class);
    boolean exclude = ReflectiveTypeAdapterFactory.excludeField(field, true, null);

    // Then
    assertFalse(exclude);
  }

  @Test
  public void testExcludeField_ExcludedClass_ReturnsFalse() {
    // Given
    Field field = mock(Field.class);
    when(field.getType()).thenReturn(ExcluderTestClass.class);
    boolean exclude = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);

    // Then
    assertFalse(exclude);
  }

  @Test
  public void testExcludeField_ExcludedField_ReturnsFalse() {
    // Given
    Field field = mock(Field.class);
    when(field.getName()).thenReturn("excludedField");
    when(excluder.excludeField(field, true)).thenReturn(true);
    boolean exclude = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);

    // Then
    assertFalse(exclude);
  }

  @Test
  public void testGetFieldNames_NullField_ReturnsEmptyList() {
    // Given
    Field nullField = null;

    // When
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(nullField);

    // Then
    assertTrue(fieldNames.isEmpty());
  }

  @Test
  public void testGetFieldNames_SerializedNamePresent_ReturnsListContainingSerializedName() {
    // Given
    TestClass testClass = new TestClass();
    Field field = new TestField();
    when(testClass.getClass()).thenReturn(TestClassWithSerializedName.class);
    when(field.getAnnotation(SerializedName.class)).thenReturn(new SerializedName("testField"));

    // When
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);

    // Then
    assertTrue(fieldNames.size() > 0);
    assertEquals("testField", fieldNames.get(0));
  }

  @Test
  public void testGetFieldNames_SerializedNameAbsent_ReturnsListContainingDefaultName() {
    // Given
    TestClass testClass = new TestClass();
    Field field = new TestField();
    when(testClass.getClass()).thenReturn(TestClassWithoutSerializedName.class);

    // When
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);

    // Then
    assertTrue(fieldNames.size() > 0);
    assertEquals(field.getName(), fieldNames.get(0));
  }

  @Test
  public void testCreateBoundField_NullContext_ThrowsIllegalArgumentException() {
    // Given
    Field field = mock(Field.class);
    String name = "test";
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    boolean serialize = true;
    boolean deserialize = true;

    // When & Then
    expectException(IllegalArgumentException.class, () -> ReflectiveTypeAdapterFactory.createBoundField(null, field, name, fieldType, serialize, deserialize));
  }

  @Test
  public void testCreateBoundField_JsonAdapterPresent_ReturnsTypeAdapterWrapper() {
    // Given
    TestClass testClass = new TestClass();
    TestField testField = new TestField();
    when(testClass.getClass()).thenReturn(TestClassWithJsonAdapter.class);
    when(testField.getAnnotation(JsonAdapter.class)).thenReturn(new JsonAdapter(JsonAdapterTestTypeAdapter.class));
    Field field = new TestField();
    String name = "testField";
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    boolean serialize = true;
    boolean deserialize = true;

    // When
    BoundField boundField = ReflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);

    // Then
    assertTrue(boundField instanceof TypeAdapterRuntimeTypeWrapper);
  }

  @Test
  public void testCreateBoundField_JsonAdapterAbsent_ReturnsDefaultTypeAdapter() {
    // Given
    TestClass testClass = new TestClass();
    TestField testField = new TestField();
    when(testClass.getClass()).thenReturn(TestClassWithoutJsonAdapter.class);
    Field field = new TestField();
    String name = "testField";
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    boolean serialize = true;
    boolean deserialize = true;

    // When
    BoundField boundField = ReflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);

    // Then
    assertNotNull(boundField);
    assertFalse(boundField instanceof TypeAdapterRuntimeTypeWrapper);
  }

  private static class TestClass {
    private TestField testField;
  }

  private static class TestField {}

  private static class TestClassWithoutSerializedName {
    @SerializedName("testField")
    private TestField testField;
  }

  private static class TestClassWithSerializedName {
    @SerializedName("testField")
    private TestField testField;
  }

  private static class TestClassWithJsonAdapter {
    @JsonAdapter(JsonAdapterTestTypeAdapter.class)
    private TestField testField;
  }

  private static class TestClassWithoutJsonAdapter {
    private TestField testField;
  }

  private static class SerializedName {
    String value() { return "testField"; }
    String[] alternate() { return new String[0]; }
  }

  private static class JsonAdapter {
    Class<? extends TypeAdapter<?>> value() { return JsonAdapterTestTypeAdapter.class; }
  }

  private static class JsonAdapterTestTypeAdapter {}

  private static class TestFieldException {
    private int errorCode;

    TestFieldException(int errorCode) {
      this.errorCode = errorCode;
    }
  }

  private static void expectException(Class<? extends Throwable> expectedType, Runnable runnable) {
    try {
      runnable.run();
      fail("Expected " + expectedType.getName() + " to be thrown.");
    } catch (Throwable e) {
      if (expectedType.isInstance(e)) {
        return;
      }
      throw new RuntimeException("Unexpected exception: " + e.getClass().getName(), e);
    }
  }
}