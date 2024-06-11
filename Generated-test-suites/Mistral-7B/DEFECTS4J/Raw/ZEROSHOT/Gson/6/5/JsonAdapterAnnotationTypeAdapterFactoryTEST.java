import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory factory;

 @Before
 public void setup() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = mock(Gson.class);
 factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void shouldCreateTypeAdapterWithJsonAdapterAnnotation() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 JsonAdapter jsonAdapterAnnotation = typeToken.getRawType().getAnnotation(JsonAdapter.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
     .thenReturn(mock(TypeAdapter.class));

 TypeAdapter<String> result = factory.create(gson, typeToken);
 assertNotNull(result);
 }

 @Test
 public void shouldCreateTypeAdapterWithJsonAdapterFactoryAnnotation() {
 TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
 JsonAdapter jsonAdapterAnnotation = typeToken.getRawType().getAnnotation(JsonAdapter.class);
 JsonAdapterFactory jsonAdapterFactoryAnnotation = typeToken.getRawType().getAnnotation(JsonAdapterFactory.class);
 when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
     .thenReturn(mock(TypeAdapterFactory.class));

 TypeAdapter<Integer> result = factory.create(gson, typeToken);
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenJsonAdapterValueIsNotTypeAdapterOrTypeAdapterFactory() {
 TypeToken<Double> typeToken = new TypeToken<Double>() {};
 JsonAdapter jsonAdapterAnnotation = typeToken.getRawType().getAnnotation(JsonAdapter.class);
 String value = String.class.getName();

 when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
     .thenThrow(new UnsupportedOperationException());

 try {
 factory.create(gson, typeToken);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Should not catch expected exception", e);
 }

 JsonAdapterException expectedException = new IllegalArgumentException(
     "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
 throw new AssertionError("Should throw expected exception", expectedException);
 }
}