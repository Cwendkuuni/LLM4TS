import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setUp() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    Gson gson = new Gson();
    TypeToken<String> targetType = TypeToken.get(String.class);

    TypeAdapter<String> result = factory.create(gson, targetType);

    assertNull(result);
  }

  @Test
  public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
    Gson gson = new Gson();
    TypeToken<TestClass1> targetType = TypeToken.get(TestClass1.class);

    TypeAdapter<TestClass1> result = factory.create(gson, targetType);

    assertNotNull(result);
  }

  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
    Gson gson = new Gson();
    TypeToken<TestClass2> targetType = TypeToken.get(TestClass2.class);

    TypeAdapter<TestClass2> result = factory.create(gson, targetType);

    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapterAnnotation_ThrowsIllegalArgumentException() {
    Gson gson = new Gson();
    TypeToken<TestClass3> targetType = TypeToken.get(TestClass3.class);

    factory.create(gson, targetType);
  }

  private static class TestClass1 {
    // Some fields...
  }

  private static class TestClass2 {
    // Some fields...
  }

  private static class TestClass3 {
    // Some fields...
  }
}