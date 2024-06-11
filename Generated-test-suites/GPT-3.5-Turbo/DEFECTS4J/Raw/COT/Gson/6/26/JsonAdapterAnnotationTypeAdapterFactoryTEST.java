
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<String> result = factory.create(gson, typeToken);
    assertNull(result);
  }

  @Test
  public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapterInstance() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    TypeAdapter<TestClass> result = factory.create(gson, typeToken);
    assertNotNull(result);
    assertTrue(result instanceof TestClassTypeAdapter);
  }

  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapterInstance() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<TestInterface> typeToken = TypeToken.get(TestInterface.class);
    TypeAdapter<TestInterface> result = factory.create(gson, typeToken);
    assertNotNull(result);
    assertTrue(result instanceof TestInterfaceTypeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_InvalidJsonAdapterValue_ThrowsIllegalArgumentException() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    Gson gson = new Gson();
    TypeToken<InvalidTestClass> typeToken = TypeToken.get(InvalidTestClass.class);
    factory.create(gson, typeToken);
  }
}

class TestClass {
}

@JsonAdapter(TestClassTypeAdapter.class)
class TestInterface {
}

@JsonAdapter(TestInterfaceTypeAdapter.class)
class InvalidTestClass {
}

class TestClassTypeAdapter extends TypeAdapter<TestClass> {
  @Override
  public void write(JsonWriter out, TestClass value) throws IOException {
  }

  @Override
  public TestClass read(JsonReader in) throws IOException {
    return null;
  }
}

class TestInterfaceTypeAdapter extends TypeAdapter<TestInterface> {
  @Override
  public void write(JsonWriter out, TestInterface value) throws IOException {
  }

  @Override
  public TestInterface read(JsonReader in) throws IOException {
    return null;
  }
}
