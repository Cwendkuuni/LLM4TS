import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setup() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = mock(Gson.class);
 }

 @Test
 public void testNullTargetType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Object> typeAdapter = factory.create(gson, null);
 verify(gson, never()).fromJson(any(), any());
 verify(gson, never()).toJson(any(), any(), any());
 assertNull(typeAdapter);
 }

 @Test
 public void testNoJsonAdapterAnnotation() {
 int intType = 0;
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> typeAdapter = factory.create(gson, TypeToken.get(intType));
 assertNull(typeAdapter);
 }

 @Test
 public void testJsonAdapterTypeAdapter() {
 int intType = 0;
 JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
 when(jsonAdapterAnnotation.value()).thenReturn(TypeAdapterInt.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapterInt.class))).thenReturn(new TypeAdapterInt());

 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> typeAdapter = factory.create(gson, TypeToken.get(intType));
 assertEquals(TypeAdapterInt.class, typeAdapter.getClass());
 }

 @Test
 public void testJsonAdapterTypeAdapterFactory() {
 int intType = 0;
 JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
 when(jsonAdapterAnnotation.value()).thenReturn(TypeAdapterFactoryInt.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapterFactoryInt.class))).thenReturn(new TypeAdapterFactoryInt());

 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> typeAdapter = factory.create(gson, TypeToken.get(intType));
 verify(constructorConstructor).get(TypeToken.get(TypeAdapterFactoryInt.class));
 TypeAdapterFactoryInt factoryInstance = new TypeAdapterFactoryInt();
 TypeAdapter<Integer> expectedTypeAdapter = mock(TypeAdapter.class);
 when(factoryInstance.create(gson, TypeToken.getIntType())).thenReturn(expectedTypeAdapter);
 TypeAdapter<Integer> actualTypeAdapter = typeAdapter;
 assertSame(expectedTypeAdapter, actualTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueIsNotTypeAdapterOrTypeAdapterFactory() {
 int intType = 0;
 JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
 when(jsonAdapterAnnotation.value()).thenReturn(String.class);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> typeAdapter = factory.create(gson, TypeToken.get(intType));
 fail("TypeAdapterFactory expected but got Class<String>.");
 }
}

class TypeAdapterInt implements TypeAdapter<Integer> {
}

class TypeAdapterFactoryInt implements TypeAdapterFactory<Integer> {
 @Override
 public TypeAdapter<Integer> create(Gson gson, TypeToken<Integer> type) {
 return new TypeAdapterInt();
 }
}