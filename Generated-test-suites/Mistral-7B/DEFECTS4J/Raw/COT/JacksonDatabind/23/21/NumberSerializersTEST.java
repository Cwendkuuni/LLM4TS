package com.fasterxml.jackson.databind.ser.std.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    

     @Test
     public void testAddAll() {
         Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
         NumberSerializers.addAll(allDeserializers);
         assertEquals(10, allDeserializers.size());
     }

     // Test each serializer class here by creating instances and testing their serialization

     @Test
     public void testShortSerializer() {
         ShortSerializer shortSerializer = new ShortSerializer();

         JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
         shortSerializer.serialize(-127, gen, new SerializerProvider());
         String serializedShort = gen.getOutputStream().toString();
         assertEquals("-127", serializedShort);
     }

     @Test(expected = JsonProcessingException.class)
     public void testShortSerializerWithException() {
         ShortSerializer shortSerializer = new ShortSerializer();

         JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
         shortSerializer.serialize(30000, gen, new SerializerProvider());
     }

     // Test other serializers similarly

     @Test
     public void testBaseClassGetSchema() {
         Base<Short> shortSerializer = new ShortSerializer();
         JsonNode schema = shortSerializer.getSchema(new SerializerProvider(), Short.class);
         assertEquals("number", schema.getClass().getSimpleName());
     }

     @Test
     public void testBaseClassAcceptJsonFormatVisitor() {
         Base<Short> shortSerializer = new ShortSerializer();
         JsonFormatVisitorWrapper visitor = new JsonMockFormatVisitorWrapper();
         shortSerializer.acceptJsonFormatVisitor(visitor, Short.class);
         assertTrue(visitor.isNumberFormatCalled());
     }

     @Test
     public void testBaseClassCreateContextual() throws JsonMappingException {
         Base<Short> shortSerializer = new ShortSerializer();
         BeanProperty property = new BeanProperty(Short.class, "test");
         JsonSerializer<?> serializer = shortSerializer.createContextual(new SerializerProvider(), property);
         assertEquals(ToStringSerializer.instance, serializer);
     }

    
}