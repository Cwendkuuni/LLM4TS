import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreateMethod() {
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<String> stringAdapter = jsonAdapterFactory.create(gson, targetType);
    assertNotNull(stringAdapter);
  }
}