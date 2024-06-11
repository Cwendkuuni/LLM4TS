package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    @Test
    public void testMappingIterator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReader reader = mapper.reader(List.class);
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeatures(JsonFactory.Feature.AUTO_CLOSE_SOURCE)
                .build();
        JsonParser parser = jsonFactory.createParser("[]");
        DeserializationContext context = mapper.getDeserializationContext();
        StdDeserializer deserializer = new StdDeserializer();
        deserializer.findContentConverter(null, context, List.class);

        MappingIterator<Integer> iterator = (MappingIterator<Integer>) reader.readValues(parser, Integer.class);

        // Test hasNext method
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        // Test next method
        assertEquals(0, iterator.next());
        assertNull(iterator.next());

        // Test close method
        iterator.close();
        assertFalse(iterator.hasNext());

        // Test hasNextValue method
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());

        // Test nextValue method
        assertEquals(0, iterator.nextValue());
        assertNull(iterator.nextValue());

        // Test readAll method
        List<Integer> list = iterator.readAll();
        assertEquals(0, list.get(0));
        assertEquals(0, list.size());

        // Test getParser method
        JsonParser parser2 = iterator.getParser();
        assertTrue(parser2.isCurrentLocationNull());
        JsonToken token = parser2.getCurrentToken();
        assertTrue(token.isNull());

        // Test getParserSchema method
        FormatSchema schema = iterator.getParserSchema();
        assertNull(schema);

        // Test readAll method with provided collection
        List<String> stringList = new ArrayList<>();
        iterator = reader.readValues(parser, String.class);
        iterator.readAll(stringList);
        assertEquals(0, stringList.size());

        mapper.writeValue(parser, new String[]{"foo", "bar"});
        iterator = reader.readValues(parser, String[].class);
        iterator.readAll(stringList);
        assertEquals(2, stringList.size());
        assertEquals("foo", stringList.get(0));
        assertEquals("bar", stringList.get(1));

        // Test getCurrentLocation method
        JsonLocation location = iterator.getCurrentLocation();
        assertEquals(location.getColumnNumber(), 0);
        assertEquals(location.getLineNumber(), 1);
    }
}