package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new DefaultDeserializationContext(deserializer, null, null);
        assertNull(deserializer.deserialize(p, ctxt));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new DefaultDeserializationContext(deserializer, null, null);
        TypeDeserializer typeDeserializer = new DefaultTypeDeserializer();
        assertNull(deserializer.deserializeWithType(p, ctxt, typeDeserializer));
    }
    
    private class DefaultDeserializationContext extends DeserializationContext {
        
        protected DefaultDeserializationContext(Deserializer<Object> deserializer, InjectableValues injectableValues, ContextAttributes attributes) {
            super(deserializer, injectableValues, attributes);
        }
        
        @Override
        public Object readValue(JsonParser p, ResolvedType type) throws IOException {
            return null;
        }
        
    }

    private class DefaultTypeDeserializer extends TypeDeserializer {
        
        protected DefaultTypeDeserializer() {
            super(null, null, null, null);
        }
        
        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
        
    }
}