import com.google.gson.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;
 private ConstructorConstructor constructorConstructor;

 @Test
 public void testCreateNullTypeToken() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor());
 assertNull(factory.create(mockGson(), null));
 }

 @Test
 public void testCreateNullAnnotation() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor());
 assertNull(factory.create(mockGson(), typeToken));
 }

 @Test
 public void testCreateTypeAdapter() {
 String json = "\"abc\"";
 TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter> targetType =
 new TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter>() {};
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor());
 TypeAdapter<JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter> expectedTypeAdapter =
 mock(TypeAdapter.class);
 when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);
 JsonAdapter annotation = Mockito.spy(new JsonAdapter() {
 @Override public Class<? extends TypeAdapter<?>> value() {
 return TypeAdapter.class;
 }

 @Override public JsonAdapter getAnnotation(FieldInfo fieldInfo) {
 return this;
 }
 });
 when(expectedTypeAdapter.getClass()).thenReturn(TypeAdapter.class);
 when(mockConstructorConstructor().get(TypeToken.get(TypeAdapter.class))).thenReturn(expectedTypeAdapter);

 when(mockGson().getAdapter(TypeToken.get(String.class))).thenReturn(mock(TypeAdapter.class));
 when(mockGson().getAdapter(TypeToken.get(TypeAdapter.class))).thenReturn(expectedTypeAdapter);

 when(factory.create(mockGson(), targetType)).thenReturn(expectedTypeAdapter);

 String result = factory.getTypeAdapter(mockGson(), targetType).fromJson(json, JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter.class);
 assertEquals("abc", result);
 }

 @Test
 public void testCreateTypeAdapterFactory() {
 String json = "\"[1,2,3]\":[1,2,3]";
 TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTest_ListAdapter> targetType =
 new TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTest_ListAdapter>() {};
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor());
 TypeAdapterFactory expectedTypeAdapterFactory = mock(TypeAdapterFactory.class);
 when(expectedTypeAdapterFactory.create(mockGson(), TypeToken.get(int[].class))).thenReturn(mock(TypeAdapter.class));
 when(expectedTypeAdapterFactory.create(mockGson(), TypeToken.get(List.class))).thenReturn(expectedTypeAdapterFactory);
 when(expectedTypeAdapterFactory.create(mockGson(), targetType)).thenReturn(expectedTypeAdapterFactory);

 when(mockGson().getAdapter(TypeToken.get(int[].class))).thenReturn(mock(TypeAdapter.class));
 when(mockGson().getAdapter(TypeToken.get(List.class))).thenReturn(expectedTypeAdapterFactory);

 when(mockConstructorConstructor().get(TypeToken.get(TypeAdapterFactory.class))).thenReturn(expectedTypeAdapterFactory);

 JsonAdapter annotation = Mockito.spy(new JsonAdapter() {
 @Override public Class<? extends TypeAdapterFactory<?>> value() {
 return TypeAdapterFactory.class;
 }

 @Override public JsonAdapter getAnnotation(FieldInfo fieldInfo) {
 return this;
 }
 });
 when(annotation.value()).thenReturn(TypeAdapterFactory.class);
 when(annotation.getClass()).thenReturn(JsonAdapter.class);

 TypeAdapter<JsonAdapterAnnotationTypeAdapterFactoryTest_ListAdapter> result =
 factory.getTypeAdapter(mockGson(), targetType);
 assertEquals(expectedTypeAdapterFactory, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateInvalidTypeAdapter() {
 String json = "\"abc\"";
 TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter> targetType =
 new TypeToken<JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter>() {};
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor());
 JsonAdapter annotation = new JsonAdapter() {};
 annotation.value() = new Object() {};

 when(mockGson().getAdapter(TypeToken.get(String.class))).thenReturn(mock(TypeAdapter.class));
 when(mockGson().getAdapter(TypeToken.get(Object.class))).thenReturn(mock(TypeAdapter.class));

 when(factory.create(mockGson(), targetType)).thenThrow(new IllegalArgumentException());
 }

 @Test
 private ConstructorConstructor mockConstructorConstructor() {
 return mock(ConstructorConstructor.class);
 }

 @Test
 private Gson mockGson() {
 return mock(Gson.class);
 }
}

 class JsonAdapterAnnotationTypeAdapterFactoryTest_StringAdapter {
}

 class JsonAdapterAnnotationTypeAdapterFactoryTest_ListAdapter {
}