import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory factory;

 @Before
 public void setup() {
 gson = new GsonBuilder().setPrePreProcessor(new CustomCreationReflectorPreProcessor()).create();
 factory = new JsonAdapterAnnotationTypeAdapterFactory(gson.getFactory());
 }

 @Test
 public void testNullTypeToken() {
 assertNull(factory.create(gson, null));
 }

 @Test
 public void testJsonAdapterValueIsTypeAdapter() {
 Class<MyJsonAdapter> jsonAdapterClass = MyJsonAdapter.class;
 TypeToken<String> stringToken = new TypeToken<String>() {};
 JsonAdapter annotation = stringToken.getRawType().getAnnotation(JsonAdapter.class);
 assertNotNull(annotation);
 assertEquals(jsonAdapterClass, annotation.value());
 TypeAdapter<String> adapter = factory.create(gson, stringToken);
 assertInstanceOf(MyJsonAdapter.class, adapter);
 }

 @Test
 public void testJsonAdapterValueIsTypeAdapterFactory() {
 Class<MyJsonAdapterFactory> jsonAdapterFactoryClass = MyJsonAdapterFactory.class;
 TypeToken<String> stringToken = new TypeToken<String>() {};
 JsonAdapter annotation = stringToken.getRawType().getAnnotation(JsonAdapter.class);
 assertNotNull(annotation);
 assertEquals(jsonAdapterFactoryClass, annotation.value());
 TypeAdapterFactory factory = gson.getFactory();
 TypeAdapter<String> adapter = factory.create(gson, stringToken);
 assertNotNull(adapter);
 MyJsonAdapter myAdapter = new MyJsonAdapter();
 TypeAdapter<String> expectedAdapter = factory.create(gson, stringToken);
 assertEquals(expectedAdapter, adapter);
 TypeAdapter<String> factoryCreatedAdapter = factory.create(gson, stringToken);
 assertEquals(myAdapter, factoryCreatedAdapter);
 TypeAdapter<String> adapterFromFactory = factory.create(gson, stringToken);
 assertEquals(expectedAdapter, adapterFromFactory);
 TypeAdapter<String> adapterFromTest = factory.create(gson, stringToken, MyJsonAdapter.class);
 assertEquals(expectedAdapter, adapterFromTest);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueIsSelfType() {
 Class<JsonAdapterAnnotationTypeAdapterFactory> selfType = JsonAdapterAnnotationTypeAdapterFactory.class;
 TypeToken<String> stringToken = new TypeToken<String>() {};
 JsonAdapter annotation = stringToken.getRawType().getAnnotation(JsonAdapter.class);
 assertNotNull(annotation);
 assertEquals(selfType, annotation.value());
 TypeAdapter<?> adapter = factory.create(gson, stringToken);
 fail(); // Should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueIsNotTypeOrTypeFactory() {
 Class<Integer> intType = Integer.TYPE;
 JsonAdapter annotation = intType.getAnnotation(JsonAdapter.class);
 assertNotNull(annotation);
 assertNotSame(intType, annotation.value());
 TypeAdapter<?> adapter = factory.create(gson, intType.getTypeToken());
 fail(); // Should throw an exception
 }
}

class MyJsonAdapter extends TypeAdapter<String> {
 @Override
 public String read(JsonElement jsonElement) throws IOException {
 return jsonElement.getAsString();
 }

 @Override
 public void write(JsonWriter jsonWriter, String value) throws IOException {
 jsonWriter.value(value);
 }
}

class MyJsonAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
 return new MyJsonAdapter();
 }
}

class CustomCreationReflectorPreProcessor implements PreProcessor {
 @Override
 public <T> T create(Class<T> rawType, TypeToken<? extends T> annotationType, FieldAttributes fieldAttributes,
     int modifiers, Processor processor) {
 if (TypeAdapterFactory.class.isAssignableFrom(rawType)) {
 return processor.getProcessorContext().getServicefinder()
     .findService(TypeAdapterFactory.class, null);
 }
 return null;
 }
}