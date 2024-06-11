import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private TypeToken targetType;
  private JsonAdapter annotation;
  private TypeAdapterFactory factory;

  @Before
  public void setup() {
    constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    gson = Mockito.mock(Gson.class);
    targetType = Mockito.mock(TypeToken.class);
    annotation = Mockito.mock(JsonAdapter.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testCreate_WithAnnotation_ReturnsTypeAdapter() {
    Mockito.when(targetType.getRawType()).thenReturn(TestClass.class);
    Mockito.when(annotation.value()).thenReturn(TestTypeAdapter.class);

    TypeAdapter<TestType> typeAdapter = factory.create(gson, targetType);

    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(typeAdapter instanceof TestTypeAdapter);
  }

  @Test
  public void testCreate_WithoutAnnotation_ReturnsNull() {
    Mockito.when(targetType.getRawType()).thenReturn(TestClass.class);
    Mockito.when(annotation.value()).thenReturn(null);

    TypeAdapter<TestType> typeAdapter = factory.create(gson, targetType);

    Assert.assertNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapter_WithInvalidAnnotation_ThrowsException() {
    Mockito.when(annotation.value()).thenReturn(TestInvalidAdapter.class);

    factory.getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }

  @JsonAdapter(TestTypeAdapter.class)
  private class TestClass {
  }

  private class TestType {
  }

  private static class TestTypeAdapter extends TypeAdapter<TestType> {
    @Override
    public void write(JsonWriter out, TestType value) throws IOException {
    }

    @Override
    public TestType read(JsonReader in) throws IOException {
      return null;
    }
  }

  private static class TestInvalidAdapter {
  }
}