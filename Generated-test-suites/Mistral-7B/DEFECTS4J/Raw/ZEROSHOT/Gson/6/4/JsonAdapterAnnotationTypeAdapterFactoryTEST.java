import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 constructorConstructor = Mockito.mock(ConstructorConstructor.class);
 gson = Mockito.mock(Gson.class);
 }

 @Test
 public void testNullTypeToken() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 assertNull(factory.create(gson, null));
 }

 @Test
 public void testNullJsonAdapterAnnotation() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 assertNull(factory.create(gson, typeToken));
 }

 @Test
 public void testJsonAdapterIsTypeAdapter() {
 TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
 Integer intAdapter = Mockito.mock(Integer.class);
 JsonAdapter annotation = new JsonAdapter(intAdapter.getClass());
 when(constructorConstructor.get(TypeToken.get(Integer.class))).thenReturn(intAdapter);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> adapter = factory.create(gson, typeToken);
 assertNotNull(adapter);
 assertSame(intAdapter, adapter);
 }

 @Test
 public void testJsonAdapterIsTypeAdapterFactory() {
 TypeToken<String> stringListTypeToken = new TypeToken<List<String>>() {};
 ListTypeAdapterFactory stringListAdapterFactory = Mockito.mock(ListTypeAdapterFactory.class);
 JsonAdapter annotation = new JsonAdapter(stringListAdapterFactory.getClass());
 when(constructorConstructor.get(TypeToken.get(ListTypeAdapterFactory.class)))
     .thenReturn(stringListAdapterFactory);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<List<String>> adapter = factory.create(gson, stringListTypeToken);
 assertNotNull(adapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueIsNotTypeAdapterOrTypeAdapterFactory() {
 TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
 JsonAdapter annotation = new JsonAdapter("not a TypeAdapter or TypeAdapterFactory");
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 factory.create(gson, typeToken);
 }

 @Test
 public void testCreateNullSafeAdapter() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 String stringAdapter = Mockito.mock(String.class);
 JsonAdapter annotation = new JsonAdapter(String.class);
 when(constructorConstructor.get(TypeToken.get(String.class))).thenReturn(stringAdapter);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> adapter = factory.create(gson, typeToken);
 assertNotNull(adapter);
 assertNotNull(adapter.nullSafe());
 }

}