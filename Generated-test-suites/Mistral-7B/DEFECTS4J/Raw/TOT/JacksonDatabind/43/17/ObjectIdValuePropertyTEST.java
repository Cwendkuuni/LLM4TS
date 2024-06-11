import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> mockValueDeserializer;
 private Object testInstance;

 @Before
 public void setup() throws Exception {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockValueDeserializer = mock(JsonDeserializer.class);
 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
 objectIdValueProperty.setName("idProperty");
 TestObject testObject = new TestObject();
 testInstance = testObject;
 doReturn(testObject).when(mockObjectIdReader).getIdProperty();
 doReturn(mockValueDeserializer).when(mockObjectIdReader).getValueDeserializer();
 doReturn(new SimpleBeanPropertyFilter(SimpleBeanPropertyFilter.FilterFlag.ALL, NameTransformer.IDENTITY)).when(mockObjectIdReader).getFilterId();
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = "123";
 JsonParser mockParser = mock(JsonParser.class);
 ObjectIdValueReader mockObjectIdValueReader = new ObjectIdValueReader() {
 @Override
 public ObjectIdValueProperty findValueProperty(DeserializationContext ctxt, BeanProperty property) {
 return objectIdValueProperty;
 }
 };
 ObjectReader mockObjectReader = mock(ObjectReader.class);
 when(mockParser.readValueAs(ObjectIdReader.class)).thenReturn(mockObjectIdValueReader);
 when(mockObjectIdReader.findPropertyName("idProperty")).thenReturn("id");
 when(mockObjectIdReader.getIdType()).thenReturn(Long.class);
 when(mockObjectReader.readValueAs(Long.class)).thenReturn(1L);

 objectIdValueProperty.deserializeAndSet(mockParser, mockObjectIdValueReader.getDeserializationContext(), testInstance);
 assertEquals("123", testInstance.getId());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 objectIdValueProperty.set(testInstance, "id");
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object id = "456";
 Object newId = "789";
 Object setValue = new Object();
 doReturn(newId).when(testInstance).getId();
 doReturn(setValue).when(testInstance).getName();
 doReturn(id).when(mockObjectIdReader).getIdProperty().get(testInstance);
 doReturn(new SettableBeanPropertyMock()).when(testInstance).getSettableBeanProperty("name");

 Object result = objectIdValueProperty.setAndReturn(testInstance, id);
 assertEquals(id, result);
 doCallRealMethod().when(testInstance).setId(newId);
 objectIdValueProperty.setAndReturn(testInstance, newId);
 assertEquals(newId, testInstance.getId());
 }

 @Test
 public void testNullId() throws Exception {
 ObjectReader mockObjectReader = mock(ObjectReader.class);
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.readValueAs(ObjectIdReader.class)).thenReturn(mockObjectReader);
 when(mockObjectReader.readValueAs(Long.class)).thenReturn(null);

 objectIdValueProperty.deserializeAndSet(mockParser, mockObjectReader.getDeserializationContext(), testInstance);
 assertNull(testInstance.getId());
 }

 private class ObjectIdValueReader implements ObjectIdReader {
 @Override
 public ObjectIdValueProperty findValueProperty(DeserializationContext ctxt, BeanProperty property) {
 return objectIdValueProperty;
 }
 }

 private class SettableBeanPropertyMock extends SettableBeanProperty {
 @Override
 public Object setAndReturn(Object instance, Object value) throws IOException {
 return value;
 }
 }

 private static class TestObject {
 private Object id;

 public Object getId() {
 return id;
 }

 public void setId(Object id) {
 this.id = id;
 }
}

}