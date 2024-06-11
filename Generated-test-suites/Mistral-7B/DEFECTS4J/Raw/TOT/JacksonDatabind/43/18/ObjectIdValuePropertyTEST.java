import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private AnnotationIntrospector annotationIntrospector;
 private ObjectNode testObjectNode;

 @Before
 public void setup() throws Exception {
 ObjectIdReaderForString stringObjectIdReader = new ObjectIdReaderForString();
 objectIdReader = mock(ObjectIdReader.class);
 when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
 when(objectIdReader.getIdType()).thenReturn(Long.TYPE);
 when(objectIdReader.generator).thenReturn(new StdSerializer<ObjectNode>(ObjectNode.class) {});
 when(objectIdReader.resolver).thenReturn(new StdSerializer<ObjectNode>(ObjectNode.class) {});
 when(objectIdReader.getIdType()).thenReturn(Long.TYPE);
 when(objectIdReader.getDeserializer()).thenReturn(new StdScalarDeserializer(Long.class));
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 annotationIntrospector = new AnnotationIntrospector();
 testObjectNode = new ObjectMapper().createObjectNode();
 }

 @Test
 public void testEmptyId() throws Exception {
 ObjectIdValueProperty property = spy(objectIdValueProperty);
 Object id = null;
 Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
 assertEquals(null, result);
 verifyZeroInteractions(objectIdReader);
 }

 @Test
 public void testValidIdDeserialization() throws Exception {
 ObjectIdValueProperty property = spy(objectIdValueProperty);
 Object id = new Long(1L);
 ObjectNode idNode = testObjectNode.put("id", id.toString());
 Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
 assertEquals(id, result);
 verify(objectIdReader).bindItem(mock(Object.class));
 }

 @Test
 public void testNullIdDeserialization() throws Exception {
 ObjectIdValueProperty property = spy(objectIdValueProperty);
 Object id = null;
 Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
 assertEquals(null, result);
 verifyZeroInteractions(objectIdReader);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 ObjectIdValueProperty property = spy(objectIdValueProperty);
 Object id = new Long(1L);
 Object instance = new Object();
 Object result = property.setAndReturn(instance, id);
 assertSame(instance, result);
 verify(objectIdReader.idProperty).setAndReturn(instance, id);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object instance = new Object();
 Object id = new Long(1L);
 property.setAndReturn(instance, id);
 fail("Should throw UnsupportedOperationException");
 }
}