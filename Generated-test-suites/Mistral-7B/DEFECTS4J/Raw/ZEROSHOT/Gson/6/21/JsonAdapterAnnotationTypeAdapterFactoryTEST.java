import com.google.gson.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private ConstructorConstructor constructorConstructor;
 private JsonAdapterAnnotationTypeAdapterFactory factory;

 @Before
 public void setUp() {
 gson = mock(Gson.class);
 constructorConstructor = mock(ConstructorConstructor.class);
 factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testCreateNullTypeToken() {
 Assert.assertNull(factory.create(gson, null));
 }

 @Test
 public void testCreateWithJsonAdapterAnnotation() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 String expectedJsonAdapterClassName = "com.example.MyStringJsonAdapter";
 JsonAdapter annotation = typeToken.getRawType().getAnnotation(JsonAdapter.class);
 annotation.value().setName(expectedJsonAdapterClassName);

 TypeAdapter<String> result = factory.create(gson, typeToken);

 Assert.assertNotNull(result);

 TypeAdapter<?> jsonAdapter = mock(TypeAdapter.class);
 when(constructorConstructor.get(TypeToken.get(JsonAdapter.class))).thenReturn(jsonAdapter);
 when(jsonAdapter.getClass()).thenReturn(TypeAdapter.class);
 when(jsonAdapter.construct()).thenReturn(result);

 TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
 when(typeAdapterFactory.create(eq(gson), any(TypeToken.class))).thenCallRealMethod();
 when(constructorConstructor.get(TypeToken.get(typeAdapterFactory.getClass()))).thenReturn(typeAdapterFactory);

 annotation.value().setAnnotationType(TypeAdapterFactory.class.getName());

 result = factory.create(gson, typeToken);

 Assert.assertEquals(result, typeAdapterFactory.create(gson, typeToken));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidJsonAdapterAnnotationValue() {
 TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
 JsonAdapter annotation = typeToken.getRawType().getAnnotation(JsonAdapter.class);
 annotation.value().setName("InvalidType");

 factory.create(gson, typeToken);
 }
}