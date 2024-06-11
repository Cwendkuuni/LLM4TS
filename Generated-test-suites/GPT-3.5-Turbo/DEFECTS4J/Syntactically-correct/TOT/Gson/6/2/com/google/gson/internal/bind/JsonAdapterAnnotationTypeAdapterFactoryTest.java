import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setup() {
    gson = mock(Gson.class);
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_WhenNoAnnotation_ReturnsNull() {
    TypeToken<Object> targetType = TypeToken.get(Object.class);
    TypeAdapter<Object> result = factory.create(gson, targetType);
    assertEquals(null, result);
  }

  @Test
  public void testCreate_WhenAnnotationIsPresent_CreatesTypeAdapter() {
    TypeToken<TestModel> targetType = TypeToken.get(TestModel.class);
    JsonAdapter annotation = TestModel.class.getAnnotation(JsonAdapter.class);
    Class<TestTypeAdapter> adapterClass = annotation.value();

    TestTypeAdapter adapter = mock(TestTypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(adapterClass))).thenReturn(adapter);
    
    TypeAdapter<TestModel> result = factory.create(gson, targetType);

    verify(constructorConstructor).get(TypeToken.get(adapterClass));
    assertEquals(adapter, result);
  }

  @Test
  public void testGetTypeAdapter_WhenTypeAdapterClass_ReturnsTypeAdapter() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<TestModel> fieldType = TypeToken.get(TestModel.class);
    JsonAdapter annotation = TestModel.class.getAnnotation(JsonAdapter.class);
    Class<TestTypeAdapter> adapterClass = annotation.value();

    TestTypeAdapter adapter = mock(TestTypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(adapterClass))).thenReturn(adapter);

    TypeAdapter<TestModel> result = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    verify(constructorConstructor).get(TypeToken.get(adapterClass));
    assertEquals(adapter, result);
  }

  @Test
  public void testGetTypeAdapter_WhenTypeAdapterFactoryClass_ReturnsTypeAdapterFromCreateMethod() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<TestModel> fieldType = TypeToken.get(TestModel.class);
    JsonAdapter annotation = TestModel.class.getAnnotation(JsonAdapter.class);
    Class<TestAdapterFactory> adapterFactoryClass = annotation.value();

    TestAdapterFactory adapterFactory = mock(TestAdapterFactory.class);
    TestTypeAdapter adapter = mock(TestTypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(adapterFactoryClass))).thenReturn(adapterFactory);
    when(adapterFactory.create(gson, fieldType)).thenReturn(adapter);

    TypeAdapter<TestModel> result = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    verify(constructorConstructor).get(TypeToken.get(adapterFactoryClass));
    verify(adapterFactory).create(gson, fieldType);
    assertEquals(adapter, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_WhenAnnotationValueIsInvalid_ThrowsIllegalArgumentException() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<TestModel> fieldType = TypeToken.get(TestModel.class);
    JsonAdapter annotation = TestModel.class.getAnnotation(JsonAdapter.class);
    Class<WrongClass> invalidClass = WrongClass.class;
    
    when(annotation.value()).thenReturn(invalidClass);

    JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testGetTypeAdapter_CallsNullSafeOnTypeAdapter() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    Gson gson = mock(Gson.class);
    TypeToken<TestModel> fieldType = TypeToken.get(TestModel.class);
    JsonAdapter annotation = TestModel.class.getAnnotation(JsonAdapter.class);
    Class<TestTypeAdapter> adapterClass = annotation.value();

    TestTypeAdapter adapter = mock(TestTypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(adapterClass))).thenReturn(adapter);

    TypeAdapter<TestModel> result = JsonAdapterAnnotationTypeAdapterFactory
        .getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

    verify(adapter).nullSafe();
  }

  // Additional test cases can be added here for any specific edge cases or error scenarios

}