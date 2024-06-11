package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class MappingIteratorTest {

    @Mock
    private JsonParser jsonParserMock;

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(null, jsonParserMock, null, null, true, null);
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(null, jsonParserMock, null, null, true, null);
        assertNull(mappingIterator.nextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testThrowNoSuchElement() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(null, null, null, null, true, null);
        mappingIterator._throwNoSuchElement();
    }

    @Test
    public void testHandleMappingException() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(null, null, null, null, true, null);
        try {
            mappingIterator._handleMappingException(new JsonMappingException(""));
        } catch (RuntimeJsonMappingException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testHandleIOException() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(null, null, null, null, true, null);
        try {
            mappingIterator._handleIOException(new IOException(""));
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }
}