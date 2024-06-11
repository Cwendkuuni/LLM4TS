package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(
                null, null, null, null, null, null);
        String json = "[1, 2, 3]";
        Collection<Object> result = new ArrayList<>();
        deserializer.deserialize(new FakeJsonParser(json), new FakeDeserializationContext(), result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }


    static class FakeJsonParser extends JsonParser {

        private String json;

        public FakeJsonParser(String json) {
            this.json = json;
        }

        @Override
        public boolean isExpectedStartArrayToken() throws IOException {
            return true;
        }

        @Override
        public String getText() throws IOException {
            return json;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            return JsonToken.END_ARRAY;
        }

        @Override
        public JsonToken getCurrentToken() {
            return JsonToken.START_ARRAY;
        }
        
        // Other unimplemented methods...

    }
    
    static class FakeDeserializationContext extends DeserializationContext {

        // Unimplemented methods...
        
    }
}