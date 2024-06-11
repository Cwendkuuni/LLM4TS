import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyReferring;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

 private ObjectMapper objectMapper;
 private JsonParser jsonParser;
 private ObjectIdReferenceProperty property;
 private Object target;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 jsonParser = mock(JsonParser.class);
 property = new ObjectIdReferenceProperty(mock(SettableBeanProperty.class), null);
 target = new Object();
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = new Object();
 when(jsonParser.readValueAs(Object.class)).thenReturn(id);
 property.deserializeAndSet(jsonParser, mock(DeserializationContext.class), target);
 assertEquals(id, property.get(target));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws Exception {
 when(jsonParser.getValueAsToken()).thenReturn(null);
 Object nullValue = property.deserializeSetAndReturn(jsonParser, mock(DeserializationContext.class), target);
 assertNull(nullValue);
 }

 @Test
 public void testDeserializeSetAndReturnUnresolvedForwardReference() throws Exception {
 Object id = new Object();
 when(jsonParser.readValueAs(Object.class)).thenReturn(id);
 when(jsonParser.getValueAsToken()).thenReturn(mock(JsonToken.ID.class));
 PropertyReferring referring = new PropertyReferring(property, null, null, target);
 property.deserializeSetAndReturn(jsonParser, mock(DeserializationContext.class), target);
 verify(referring).handleResolvedForwardReference(eq(id), any());
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnThrowsIOException() throws Exception {
 when(jsonParser.getValueAsToken()).thenThrow(new IOException());
 property.deserializeSetAndReturn(jsonParser, mock(DeserializationContext.class), target);
 }

 @Test
 public void testSet() throws Exception {
 property.set(target, new Object());
 assertEquals(new Object(), property.get(target));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 assertEquals(target, property.setAndReturn(target, new Object()));
 }
}