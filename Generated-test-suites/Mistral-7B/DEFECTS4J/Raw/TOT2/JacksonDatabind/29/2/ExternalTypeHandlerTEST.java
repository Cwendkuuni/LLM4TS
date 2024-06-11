import com.fasterxml.jackson. core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testObject;
 private JsonParser parser;
 private StandardDeserializationContext context;
 private DeserializationConfig config;

 @Before
 public void setup() throws Exception {
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 SettableBeanProperty prop1 = new StdSerializer<String>(String.class) {}.withoutPropertyName();
 SettableBeanProperty prop2 = new StdSerializer<Integer>(Integer.class) {}.withoutPropertyName();
 builder.addExternal(prop1, new StringDeserializer());
 builder.addExternal(prop2, new IntDeserializer());
 handler = builder.build();
 testObject = new HashMap<>();
 context = new StandardDeserializationContext();
 config = context.withBeanDeserializerFactory(new BeanDeserializerFactory());
 parser = config.getFactory().createParser("[{\"type1\": 1, \"type2\": \"test\"}]");
 }

 @Test
 public void handleTypePropertyValue_success() throws IOException {
 handler.handleTypePropertyValue(parser, context, "type1", testObject);
 assertTrue(handler._typeIds[0] != null);
 assertEquals("1", handler._typeIds[0]);
 }

 @Test(expected = JsonMappingException.class)
 public void handleTypePropertyValue_failure() throws IOException {
 handler.handleTypePropertyValue(parser, context, "typeNonexistent", testObject);
 }

 @Test
 public void handlePropertyValue_success() throws IOException {
 handler.handlePropertyValue(parser, context, "type2", testObject);
 assertTrue(handler._tokens[1] != null);
 }

 @Test(expected = JsonMappingException.class)
 public void handlePropertyValue_failure() throws IOException {
 handler.handlePropertyValue(parser, context, "typeNonexistent", testObject);
 }

 @Test
 public void complete_success() throws IOException {
 handler.complete(parser, context, testObject);
 assertEquals("test", testObject.get("type2"));
 assertEquals(1, testObject.get("type1"));
 }

 @Test
 public void complete_failure() throws IOException {
 parser.nextToken(); // parse past opening bracket
 handler.complete(parser, context, testObject);
 fail("Expected exception, but none was thrown.");
 }

 @Test
 public void completeWithCreator_success() throws IOException {
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new ObjectMapper().getDefaultInstance().getFactory()
 .createValueInjector(HashMap.class);
 handler.complete(parser, context, buffer, creator);
 assertEquals(testObject, creator.build(context, buffer));
 }

 @Test(expected = JsonMappingException.class)
 public void completeWithCreator_failure() throws IOException {
 PropertyBasedCreator creator = new ObjectMapper().getDefaultInstance().getFactory()
 .createValueInjector(HashMap.class);
 handler.complete(parser, context, null, creator);
 fail("Expected exception, but none was thrown.");
 }

 @Test
 public void deserialize_success() throws IOException {
 handler._deserialize(parser, context, 0, null);
 assertEquals("test", handler._properties[0].getProperty().getValue(testObject));
 }

 @Test(expected = JsonMappingException.class)
 public void deserialize_failure() throws IOException {
 handler._deserialize(parser, context, 0, null);
 fail("Expected exception, but none was thrown.");
 }

 @Test
 public void deserializeAndSet_success() throws IOException {
 handler._deserializeAndSet(parser, context, testObject, 0, null);
 assertEquals("test", testObject.get("type2"));
 }

 @Test(expected = JsonMappingException.class)
 public void deserializeAndSet_failure() throws IOException {
 handler._deserializeAndSet(parser, context, testObject, 0, null);
 fail("Expected exception, but none was thrown.");
 }
}