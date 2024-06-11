import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class CreatorPropertyTest {

 private CreatorProperty creatorProperty;
 private ObjectMapper objectMapper;
 private DeserializationContext context;
 private JsonDeserializer<String> stringDeserializer;
 private TypeDeserializer typeDeserializer;
 private SettableBeanProperty fallbackSetterMock;

 @Before
 public void setup() {
 creatorProperty = new CreatorProperty(
 new PropertyName("testProperty"),
 new TypeReference<Object>() {},
 new PropertyName("testWrapperName"),
 typeDeserializer,
 mock(Annotations.class),
 mock(AnnotatedParameter.class),
 0,
 null,
 null);
 objectMapper = new ObjectMapper();
 context = mock(DeserializationContext.class);
 stringDeserializer = mock(JsonDeserializer.class);
 fallbackSetterMock = mock(SettableBeanProperty.class);
 }

 @Test
 public void testConstructor() throws Exception {
 // Test construction with valid arguments
 creatorProperty = new CreatorProperty(
 new PropertyName("testProperty"),
 new TypeReference<Object>() {},
 new PropertyName("testWrapperName"),
 typeDeserializer,
 mock(Annotations.class),
 mock(AnnotatedParameter.class),
 0,
 null,
 null);
 // Test construction with null arguments
 creatorProperty = new CreatorProperty(
 null,
 null,
 null,
 null,
 null,
 null,
 0,
 null,
 null);
 }

 @Test
 public void testSetFallbackSetter() throws Exception {
 creatorProperty.setFallbackSetter(fallbackSetterMock);
 assertEquals(fallbackSetterMock, creatorProperty._fallbackSetter);
 }

 @Test
 public void testIsIgnorable() throws Exception {
 creatorProperty.markAsIgnorable();
 assertEquals(true, creatorProperty.isIgnorable());
 }

 @Test
 public void testFindInjectableValue() throws Exception {
 when(context.findInjectableValue(anyObject(), eq(creatorProperty), anyObject())).thenReturn("injectedValue");
 assertEquals("injectedValue", creatorProperty.findInjectableValue(context, mock(Object.class)));
 }

 @Test
 public void testInject() throws Exception {
 when(context.findInjectableValue(anyObject(), eq(creatorProperty), anyObject())).thenReturn("injectedValue");
 assertEquals("injectedValue", creatorProperty.inject(context, mock(Object.class)));
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 when(stringDeserializer.deserialize(anyObject(), anyObject(), eq(context)))
 .thenReturn("deserializedValue");
 when(fallbackSetterMock.set(anyObject(), anyObject()))
 .thenReturn(null);
 creatorProperty.setFallbackSetter(fallbackSetterMock);
 creatorProperty.deserializeAndSet(mock(JsonParser.class), context, mock(Object.class));
 assertEquals("deserializedValue", creatorProperty.getValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 when(stringDeserializer.deserialize(anyObject(), anyObject(), eq(context)))
 .thenReturn("deserializedValue");when(fallbackSetterMock.setAndReturn(anyObject(), anyObject()))
 .thenReturn("returnValue");
 creatorProperty.setFallbackSetter(fallbackSetterMock);
 assertEquals("returnValue", creatorProperty.deserializeSetAndReturn(mock(JsonParser.class), context, mock(Object.class)));
 }

 @Test(expected = IOException.class)
 public void testMissingSetter() throws Exception {
 creatorProperty.deserializeAndSet(mock(JsonParser.class), context, mock(Object.class));
 }

 @Test(expected = IOException.class)
 public void testMissingSetterWithContext() throws Exception {
 creatorProperty.deserializeAndSet(mock(JsonParser.class), context, mock(Object.class));
 }

 @Test
 public void testToString() {
 assertEquals("[creator property, name 'testProperty'; inject id null]", creatorProperty.toString());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(0, creatorProperty.getCreatorIndex());
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotationMock = mock(Annotation.class);
 when(context.findAnnotationForType(_eq(creatorProperty.getType()), anyObject()))
 .thenReturn(annotationMock);
 assertEquals(annotationMock, creatorProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(mock(AnnotatedParameter.class), creatorProperty.getMember());
 }
}