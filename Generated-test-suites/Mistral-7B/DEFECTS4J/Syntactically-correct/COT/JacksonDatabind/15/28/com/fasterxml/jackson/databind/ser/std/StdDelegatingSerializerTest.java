import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Type;

class StdDelegatingSerializerTest {

 private static final ObjectNode EMPTY_JSON_OBJECT = ObjectMapper.findDefaultInstance()
         .createObjectNode();

 @BeforeAll
 static void setup() {
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     objectMapper.configure(SerializerFeatures.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 void testStdDelegatingSerializerConstructorWithConverter() throws IOException {
     StdDelegatingSerializer serializer = new StdDelegatingSerializer(
             new MyConverter<>());
     assertNull(serializer.getDelegateType());
     assertNull(serializer.getDelegateSerializer());
     assertEquals(Object.class, serializer.getType());
 }

 @Test
 void testStdDelegatingSerializerConstructorWithClass() throws IOException {
     StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<>(
             String.class, new MyConverter<String>());
     assertEquals(String.class, serializer.getType());
     assertNull(serializer.getDelegateType());
     assertNull(serializer.getDelegateSerializer());
 }

 @Test
 void testStdDelegatingSerializerConstructorWithConverterDelegateTypeAndDelegateSerializer() throws IOException {
     JsonSerializer<Integer> delegateSerializer = mock(JsonSerializer.class);
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>(), JavaType.construct(Integer.class), delegateSerializer);
     assertEquals(Object.class, serializer.getType());
     assertEquals(JavaType.construct(Integer.class), serializer.getDelegateType());
     assertEquals(delegateSerializer, serializer.getDelegateSerializer());
 }

 @Test
 void testResolveWithNullDelegateSerializer() throws JsonMappingException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>(), null, null);
     SerializerProvider provider = mock(SerializerProvider.class);
     serializer.resolve(provider);
     verify(provider, times(1)).findValueSerializer(JavaType.construct(Object.class));
 }

 @Test
 void testResolveWithDelegateSerializer() throws JsonMappingException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>(), null, mock(JsonSerializer.class));
     SerializerProvider provider = mock(SerializerProvider.class);
     serializer.resolve(provider);
     verifyNoInteractions(provider);
 }

 @Test
 void testCreateContextualWithNullDelegateSerializer() throws JsonMappingException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>(), null, null);
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     JsonSerializer<Integer> intSerializer = mock(JsonSerializer.class);
     JsonSerializer<String> strSerializer = mock(JsonSerializer.class);
     when(objectMapper.findValueSerializer(JavaType.construct(Integer.class)))
             .thenReturn(intSerializer);
     when(objectMapper.findValueSerializer(JavaType.construct(String.class)))
             .thenReturn(strSerializer);
     assertEquals(serializer, serializer.createContextual(
             objectMapper.getSerializerProvider(), mock(BeanProperty.class)));
     verify(intSerializer, never()).handleSecondaryContextualization(
             any(), any());
     verify(strSerializer, never()).handleSecondaryContextualization(
             any(), any());
 }

 @Test
 void testCreateContextualWithDelegateSerializer() throws JsonMappingException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>(), null, mock(JsonSerializer.class));
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     JsonSerializer<Integer> intSerializer = mock(JsonSerializer.class);
     JsonSerializer<String> strSerializer = mock(JsonSerializer.class);
     when(objectMapper.findValueSerializer(JavaType.construct(Integer.class)))
             .thenReturn(intSerializer);
     when(objectMapper.findValueSerializer(JavaType.construct(String.class)))
             .thenReturn(strSerializer);
     JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
     assertEquals(serializer, serializer.createContextual(
             objectMapper.getSerializerProvider(), mock(BeanProperty.class)));
     verify(intSerializer, never()).handleSecondaryContextualization(
             any(), any());
     verify(strSerializer, never()).handleSecondaryContextualization(
             any(), any());
     verify(delegateSerializer, never()).handleSecondaryContextualization(
             any(), any());
 }

 @Test
 void testConvertValueNullValue() {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     assertNull(serializer.convertValue(null));
 }

 @Test
 void testSerializeWithNullValue() throws IOException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     JsonGenerator gen = objectMapper.getFactory()
             .createGenerator(new ByteArrayOutputStream(), null);
     serializer.serialize(null, gen, objectMapper.getSerializerProvider());
     assertTrue(gen.getOutputBuffer().isEmpty());
 }

 @Test
 void testSerializeValue() throws IOException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     JsonGenerator gen = objectMapper.getFactory()
             .createGenerator(new ByteArrayOutputStream(), null);
     Object value = new Object();
     Object delegateValue = new Object();
     when(serializer.convertValue(value)).thenReturn(delegateValue);
     when(serializer.getDelegateSerializer())
             .thenReturn(mock(JsonSerializer.class));
     serializer.serialize(value, gen, objectMapper.getSerializerProvider());
     delegateValue.getClass().getMethod("writeTo", JsonGenerator.class,
             SerializerProvider.class).invoke(delegateValue, gen,
             objectMapper.getSerializerProvider());
     assertFalse(gen.getOutputBuffer().isEmpty());
 }

 @Test
 void testGetSchema() throws JsonMappingException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Object.class);
     assertTrue(schema.isNull());
 }

 @Test
 void testAcceptJsonFormatVisitor() throws JsonMappingException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
     serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Object.class));
     verify(visitor, never()).visitObjectController(any());
 }

 @Test
 void testIsEmptyWithNullValue() throws IOException {
     StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     assertFalse(serializer.isEmpty(null));
 }

 @Test
 void testIsEmptyWithEmptyList() throws IOException {
     StdDelegatingSerializer<List<Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     List<Integer> list = new ArrayList<>();
     assertFalse(serializer.isEmpty(list));
 }

 @Test
 void testIsEmptyWithEmptyMap() throws IOException {
     StdDelegatingSerializer<Map<String, Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     Map<String, Integer> map = new HashMap<>();
     assertFalse(serializer.isEmpty(map));
 }

 @Test
 void testIsEmptyWithNullDelegatorProvider() throws IOException {
     StdDelegatingSerializer<Map<String, Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     assertFalse(serializer.isEmpty(null, serializer.getSerializerProvider()));
 }

 @Test
 void testIsEmptyWithEmptyListFromDelegatorProvider() throws IOException {
     StdDelegatingSerializer<List<Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     List<Integer> list = new ArrayList<>();
     when(objectMapper.getSerializerProvider().isEnabled(
             SerializerFeature.FAIL_ON_EMPTY_BEANS))
             .thenReturn(false);
     when(objectMapper.getSerializerProvider().isEmpty(list))
             .thenReturn(true);
     assertTrue(serializer.isEmpty(list, objectMapper.getSerializerProvider()));
 }

 @Test
 void testIsEmptyWithEmptyMapFromDelegatorProvider() throws IOException {
     StdDelegatingSerializer<Map<String, Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     Map<String, Integer> map = new HashMap<>();
     when(objectMapper.getSerializerProvider().isEnabled(
             SerializerFeature.FAIL_ON_EMPTY_BEANS))
             .thenReturn(false);
     when(objectMapper.getSerializerProvider().isEmpty(map))
             .thenReturn(true);
     assertTrue(serializer.isEmpty(map, objectMapper.getSerializerProvider()));
 }

 @Test
 void testIsEmptyWithNonEmptyListFromDelegatorProvider() throws IOException {
     StdDelegatingSerializer<List<Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     List<Integer> list = new ArrayList<>();
     list.add(1);
     when(objectMapper.getSerializerProvider().isEnabled(
             SerializerFeature.FAIL_ON_EMPTY_BEANS))
             .thenReturn(false);
     when(objectMapper.getSerializerProvider().isEmpty(list))
             .thenReturn(false);
     assertFalse(serializer.isEmpty(list, objectMapper.getSerializerProvider()));
 }

 @Test
 void testIsEmptyWithNonEmptyMapFromDelegatorProvider() throws IOException {
     StdDelegatingSerializer<Map<String, Integer>> serializer = new StdDelegatingSerializer<>(
             new MyConverter<>());
     ObjectMapper objectMapper = ObjectMapper.findDefaultInstance();
     Map<String, Integer> map = new HashMap<>();
     map.put("key", 1);
     when(objectMapper.getSerializerProvider().isEnabled(
             SerializerFeature.FAIL_ON_EMPTY_BEANS))
             .thenReturn(false);
     when(objectMapper.getSerializerProvider().isEmpty(map))
             .thenReturn(false);
     assertFalse(serializer.isEmpty(map, objectMapper.getSerializerProvider()));
 }
}

class MyConverter<T> implements Converter<Object, T> {
 @Override
 public T convert(Object value) {
     if (value instanceof Number) {
         return (T) value;
     }
     throw new RuntimeException("Converter can handle only Number type.");
 }
}