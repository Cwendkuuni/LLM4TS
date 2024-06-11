import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializer;
 private DeserializationContext context;
 private TypeIdResolver idResolver;

 @Before
 public void setup() {
 idResolver = mock(TypeIdResolver.class);
 context = mock(DeserializationContext.class);
 when(context.getTypeFactory()).thenReturn(new SimpleTypeFactory());
 when(context.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
 when(context.handleUnknownTypeId(TypeFactory.defaultInstance, "unknownTypeId")).thenThrow(new JsonMappingException("Unknown TypeId"));
 typeDeserializer = new TypeDeserializerBase(TypeFactory.defaultInstance, idResolver, "", false, null) {
 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.EXISTING_PROPERTY;
 }
 };
 }

 @Test
 public void testForProperty() throws IOException {
 BeanProperty property = mock(BeanProperty.class);
 when(property.getFullName()).thenReturn("testProperty");
 typeDeserializer = new TypeDeserializerBase(typeDeserializer, property);
 assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNull() {
 typeDeserializer = new TypeDeserializerBase(typeDeserializer, null);
 }

 @Test
 public void testGetTypeInclusion() throws IOException {
 assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializer.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws IOException {
 assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws IOException {
 assertEquals("", typeDeserializer.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() throws IOException {
 assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
 }

 @Test
 public void testGetDefaultImpl() throws IOException {
 assertNull(typeDeserializer.getDefaultImpl());
 }

 @Test
 public void testFindDeserializerNullTypeId() throws IOException {
 assertNull(typeDeserializer._findDeserializer(context, null));
 }

 @Test
 public void testFindDeserializerUnknownTypeId() throws IOException {
 when(idResolver.parseTypeId("unknownTypeId")).thenReturn("UnknownTypeId");
 assertEquals(NullifyingDeserializer.instance, typeDeserializer._findDeserializer(context, "unknownTypeId"));
 }

 @Test
 public void testFindDeserializerKnownTypeId() throws IOException {
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 JavaType type = mock(JavaType.class);
 when(type.getRawClass()).thenReturn(String.class);
 when(idResolver.resolveVisibleTypeId(context, "typeId")).thenReturn(type);
 when(deserializer.deserialize(context.getParser(), context)).thenReturn("Test");
 when(context.findContextualValueDeserializer(type, null)).thenReturn(deserializer);
 assertEquals(deserializer, typeDeserializer._findDeserializer(context, "typeId"));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerUnknownTypeIdException() throws IOException {
 typeDeserializer._handleUnknownTypeId(context, "unknownTypeId");
 }

 @Test
 public void testFindDefaultImplDeserializerNull() throws IOException {
 assertNull(typeDeserializer._findDefaultImplDeserializer(context));
 }

 @Test
 public void testFindDefaultImplDeserializerString() throws IOException {
 String typeName = "java.lang.String";
 when(context.findContextualValueDeserializer(TypeFactory.defaultInstance.constructType(typeName), null))
 .thenReturn(mock(JsonDeserializer.class));
 assertEquals(mock(JsonDeserializer.class), typeDeserializer._findDefaultImplDeserializer(context));
 }

 @Test
 public void testFindDefaultImplDeserializerClass() throws IOException {
 when(context.findValueDeserializer(TypeFactory.defaultInstance.constructType(String.class))).thenReturn(mock(JsonDeserializer.class));
 assertEquals(mock(JsonDeserializer.class), typeDeserializer._findDefaultImplDeserializer(context));
 }

 @Test
 public void testDeserializeWithNativeTypeIdNull() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 assertEquals(null, typeDeserializer._deserializeWithNativeTypeId(parser, context));
 }

 @Test
 public void testDeserializeWithNativeTypeIdString() throws IOException {
 when(context.handleUnknownTypeId(TypeFactory.defaultInstance, "typeId")).thenReturn(mock(Object.class));
 JsonParser parser = mock(JsonParser.class);
 when(parser.getTypeId()).thenReturn("typeId");
 Object result = typeDeserializer._deserializeWithNativeTypeId(parser, context);
 assertEquals(mock(Object.class), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getTypeId()).thenReturn("unknownTypeId");
 typeDeserializer._deserializeWithNativeTypeId(parser, context);
 }
}