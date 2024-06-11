package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class MappingIteratorTest {
    private JsonParser parser;
    private DeserializationContext context;
    private ObjectMapper mapper;
    private MappingIterator<String> iterator;

    @Before
    public void setUp() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
        parser = jsonFactory.createParser("[]");
        iterator = new MappingIterator<String>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, context, mapper.findValueDeserializer(String.class), false, null);
    }

    @After
    public void tearDown() throws Exception {
        if (parser != null) {
            parser.close();
        }
    }

    @Test
    public void testHasNext_WhenIteratorIsNotClosed_ThenShouldReturnTrue() {
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNext_WhenIteratorIsClosed_ThenShouldReturnFalse() {
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext_WhenIteratorHasNextValue_ThenShouldReturnNextValue() throws IOException {
        assertTrue(iterator.hasNext());
        String nextValue = iterator.next();
        assertNotNull(nextValue);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_WhenIteratorHasNoMoreValues_ThenShouldThrowNoSuchElementException() throws IOException {
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testRemove_WhenNotSupported_ThenThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValue_WhenParserHasNoMoreTokens_ThenShouldThrowNoSuchElementException() throws IOException {
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
        iterator.nextValue();
    }

    @Test
    public void testHasNextValue_WhenParserHasToken_ThenShouldReturnTrue() throws IOException {
        parser.nextToken();
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValue_WhenParserHasTokenButHasBeenChecked_ThenShouldReturnFalse() throws IOException {
        parser.nextToken();
        iterator.hasNextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testNextValue_WhenParserHasNoMoreTokens_ThenShouldThrowIOException() throws IOException {
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
        iterator.nextValue();
        assertThrows(IOException.class, iterator::nextValue);
    }

    @Test
    public void testNextValue_WhenParserHasNewToken_ThenShouldReturnNextValue() throws IOException {
        parser.nextToken();
        String nextValue = iterator.nextValue();
        assertNotNull(nextValue);
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testReadAll_WhenIteratorHasValues_ThenShouldReturnListWithAllValues() throws IOException {
        List<String> list = new ArrayList<>();
        iterator.readAll(list);
        for (int i = 0; i < 5; i++) {
            String value = iterator.nextValue();
            list.add(value);
        }
        assertFalse(iterator.hasNextValue());
        assertEquals(5, list.size());
    }

    @Test
    public void testReadAll_WhenIteratorIsEmpty_ThenShouldReturnEmptyList() throws IOException {
        List<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertFalse(iterator.hasNextValue());
        assertEquals(0, list.size());
    }

    @Test
    public void testGetParser_WhenIteratorIsNotClosed_ThenShouldReturnUnderlyingParser() {
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParser_WhenIteratorIsClosed_ThenShouldReturnNull() {
        iterator.close();
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema_WhenParserIsSchemaAware_ThenShouldReturnParserSchema() {
        ObjectMapper mapper = new ObjectMapper() {
            @Override
            public JsonFactory createJsonFactory() {
                return new JsonFactory() {
                    @Override
                    public JsonParser createParser(String contentInUtf8) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(File file) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(String contentInUtf8, InputSource inputSource) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(byte[] bytes, int offset, int length) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(byte[] bytes) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(File source) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(InputStream src) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public JsonParser createParser(URL url) throws IOException {
                        return mock(JsonParser.class);
                    }

                    @Override
                    public FormatSchema getParserSchema() {
                        return mock(FormatSchema.class);
                    }
                };
            }
        };
        MappingIterator<String> iterator = new MappingIterator<>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                mapper.getJsonFactory().createParser("[]"), context, mapper.findValueDeserializer(String.class), false, null);
        FormatSchema parserSchema = iterator.getParserSchema();
        assertNotNull(parserSchema);
    }

    @Test
    public void testGetParserSchema_WhenParserIsNotSchemaAware_ThenShouldReturnNull() {
        ObjectMapper mapper = new ObjectMapper();
        MappingIterator<String> iterator = new MappingIterator<>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                mapper.getJsonFactory().createParser("[]"), context, mapper.findValueDeserializer(String.class), false, null);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation_WhenParserHasLocation_ThenShouldReturnCurrentParserLocation() throws IOException {
        JsonLocation location = mock(JsonLocation.class);
        when(parser.getCurrentLocation()).thenReturn(location);
        JsonLocation currentLocation = iterator.getCurrentLocation();
        assertEquals(location, currentLocation);
    }

    @Test
    public void testGetCurrentLocation_WhenParserHasNoLocation_ThenShouldReturnNull() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentLocation()).thenReturn(null);
        JsonLocation currentLocation = iterator.getCurrentLocation();
        assertNull(currentLocation);
    }

    @Test(expected = JsonMappingException.class)
    public void testNextValue_WhenDeserializerThrowsMappingException_ThenShouldThrowMappingException() throws IOException {
        SerializationConfig config = new SerializationConfig();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserialize(parser, context)).thenThrow(new JsonMappingException("Test error"));
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public TypeDeserializer getContentDeserializer(DeserializationContext context, BeanDescription beanDesc) {
                return typeDeserializer;
            }
        };
        iterator = new MappingIterator<>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, context, deserializer, false, null);
        assertFalse(iterator.hasNextValue());
        iterator.nextValue();
        assertThrows(JsonMappingException.class, iterator::nextValue);
    }

    @Test(expected = IOException.class)
    public void testNextValue_WhenDeserializerThrowsIOException_ThenShouldThrowIOException() throws IOException {
        SerializationConfig config = new SerializationConfig();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserialize(parser, context)).thenThrow(new IOException("Test error"));
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public TypeDeserializer getContentDeserializer(DeserializationContext context, BeanDescription beanDesc) {
                return typeDeserializer;
            }
        };
        iterator = new MappingIterator<>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, context, deserializer, false, null);
        assertFalse(iterator.hasNextValue());
        iterator.nextValue();
        assertThrows(IOException.class, iterator::nextValue);
    }

    @Test
    public void testNextValue_WhenDeserializerDoesNotConsumeToken_ThenShouldConsumeToken() throws IOException {
        SerializationConfig config = new SerializationConfig();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserialize(parser, context)).thenReturn(null);
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public TypeDeserializer getContentDeserializer(DeserializationContext context, BeanDescription beanDesc) {
                return typeDeserializer;
            }
            @Override
            public NullValueProvider nullValueProvider() {
                return null;
            }
        };
        iterator = new MappingIterator<>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                parser, context, deserializer, false, null);
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
        assertTrue(parser.getCurrentLocation().isCurrentlyParsingValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMappingException_WhenExceptionIsJsonMappingException_ThenShouldReturnException() {
        JsonMappingException mappingException = new JsonMappingException("Test error");
        Object result = iterator._handleMappingException(mappingException);
        assertEquals(mappingException, result);
    }

    @Test(expected = IOException.class)
    public void testHandleIOException_WhenExceptionIsIOException_ThenShouldReturnException() {
        IOException ioException = new IOException("Test error");
        Object result = iterator._handleIOException(ioException);
        assertEquals(ioException, result);
    }

    @Test
    public void testEmptyIterator_WhenIteratorIsEmpty_ThenShouldReturnInstance() {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertTrue(emptyIterator instanceof MappingIterator);
        assertTrue(emptyIterator instanceof Iterator);
        assertTrue(emptyIterator instanceof Closeable);
    }

    @Test
    public void testClose_WhenIteratorIsClosed_ThenParserShouldBeClosed() throws IOException {
        parser.nextToken();
        iterator.close();
        parser.close();
    }
}