import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setUp() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = mock(Gson.class);
 }

 /**
 * Test create method with null targetType
 */
 @Test(expected = NullPointerException.class)
 public void testCreateNullTargetType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 factory.create(gson, null);
 }

 /**
 * Test create method with null annotation
 */
 @Test
 public void testCreateNullAnnotation() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 assertNull(factory.create(gson, new TypeToken<Integer>(){}));
 }

 /**
 * Test create method with valid annotation and TypeAdapter
 */
 @Test
 public void testCreateTypeAdapter() {
 JsonAdapter annotation = mock(JsonAdapter.class);
 when(annotation.value()).thenReturn(TypeAdapter.class);
 TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(() -> typeAdapter);

 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> result = factory.create(gson, new TypeToken<String>(){});
 assertNotNull(result);
 verify(constructorConstructor).get(TypeToken.get(TypeAdapter.class));
 }

 /**
 * Test create method with valid annotation and TypeAdapterFactory
 */
 @Test
 public void testCreateTypeAdapterFactory() {
 JsonAdapter annotation = mock(JsonAdapter.class);
 when(annotation.value()).thenReturn(TypeAdapterFactory.class);
 TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(() -> typeAdapterFactory);

 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> result = factory.create(gson, new TypeToken<String>(){});
 assertNotNull(result);
 verify(constructorConstructor).get(TypeToken.get(TypeAdapterFactory.class));
 }

 /**
 * Test create method with invalid annotation value
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateInvalidAnnotationValue() {
 JsonAdapter annotation = mock(JsonAdapter.class);
 when(annotation.value()).thenReturn(String.class);

 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 factory.create(gson, new TypeToken<Integer>(){});
 }

 /**
 * Test getTypeAdapter method with null constructorConstructor
 */
 @Test(expected = NullPointerException.class)
 public void testGetTypeAdapterNullConstructorConstructor() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(null);
 assertNull(JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Integer>(){}, mock(JsonAdapter.class)));
 }

 /**
 * Test getTypeAdapter method with null gson
 */
 @Test(expected = NullPointerException.class)
 public void testGetTypeAdapterNullGson() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 assertNull(JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(null, new TypeToken<Integer>(){}, mock(JsonAdapter.class)));
 }

 /**
 * Test getTypeAdapter method with null fieldType
 */
 @Test(expected = NullPointerException.class)
 public void testGetTypeAdapterNullFieldType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 assertNull(JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, null, mock(JsonAdapter.class)));
 }

 /**
 * Test getTypeAdapter method with null annotation
 */
 @Test(expected = NullPointerException.class)
 public void testGetTypeAdapterNullAnnotation() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 assertNull(JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Integer>(){}, null));
 }

 /**
 * Test getTypeAdapter method with valid arguments
 */
 @Test
 public void testGetTypeAdapterValidArguments() {
 TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(() -> typeAdapter);

 Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
 JsonAdapter annotation = mock(JsonAdapter.class);
 when(annotation.value()).thenReturn(typeAdapterClass);

 TypeAdapter<String> result = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<String>(){}, annotation);
 assertNotNull(result);
 verify(constructorConstructor).get(TypeToken.get(TypeAdapter.class));
 }
}