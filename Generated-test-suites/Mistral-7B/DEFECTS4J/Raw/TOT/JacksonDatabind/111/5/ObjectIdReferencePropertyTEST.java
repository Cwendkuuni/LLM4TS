import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty property;
 private JsonDeserializer<String> deserializer;
 private ObjectNode node;
 private JsonParser parser;
 private DeserializationContext context;
 private Object instance;

 @Before
 public void setup() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(String.class, "id");
 property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(JsonTestClass.class.getDeclaredField("field")), objectIdInfo);
 deserializer = mock(JsonDeserializer.class);
 node = ObjectMapper.getDefaultInstance().createObjectNode();
 parser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 instance = new JsonTestClass();
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 when(parser.getValueAsToken()).thenReturn(node.get("id"));
 when(node.has("id")).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 property.deserializeAndSet(parser, context, instance);
 assertEquals("expectedValue", instance.getField());

 verify(context).handleUnexpectedValue(parser, JsonMappingException.ValueInidingException.class, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullValue() throws Exception {
 when(parser.getValueAsToken()).thenReturn(null);

 property.deserializeAndSet(parser, context, instance);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object value = new Object();
 when(parser.getValueAsToken()).thenReturn(node.get("id"));
 when(node.has("id")).thenReturn(true);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 Object returnedValue = property.deserializeSetAndReturn(parser, context, instance);
 assertNull(returnedValue);
 }

 @Test
 public void testSet() throws Exception {
 property.set(instance, "value");
 assertEquals("value", instance.getField());
 }

 @Test
 public void testSetWithNullValue() throws Exception {
 property.set(instance, null);
 assertNull(instance.getField());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = mock(Annotation.class);
 ObjectField annotatedField = AnnotationIntrospector.findAnnotationOfType(instance.getClass(), JsonTestClass.class, annotation.getClass());
 when(property.getMember()).thenReturn(annotatedField);
 when(annotation.getValue()).thenReturn("value");
 assertEquals("value", property.getAnnotation(annotation.getClass()));
 }

 @Test
 public void testFixAccess() throws Exception {
 property.fixAccess(context);
 verify(context).findBeanProperties(_pojo.getClass());
 }

 @Test
 public void testWithName() {
 property = property.withName(PropertyName.valueOf("newName"));
 assertEquals("newName", property.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
 property = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 property = property.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, property.getNullProvider());
 }

 @Test
 public void testUnresolvedForwardReference() throws Exception {
 Object id = new Object();
 UnresolvedForwardReference reference = new UnresolvedForwardReference(id, this);
 property.deserializeSetAndReturn(parser, context, instance);
 verify(context).handleResolvedForwardReference(id, null);
 }
}

class JsonTestClass {
 private String field;

 public String getField() {
 return field;
 }

 public void setField(String field) {
 this.field = field;
 }
}