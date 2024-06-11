import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testHasNextValue() throws IOException {
        // Initialize
        JavaType type = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = false;
        Object valueToUpdate = null;
        MappingIterator<Object> mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);

        // Test when parser is null
        assertFalse(mi.hasNextValue());

        // Test when parser is not null and there is no next value
        p = new JsonFactory().createParser("");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        assertFalse(mi.hasNextValue());

        // Test when parser is not null and there is a next value
        p = new JsonFactory().createParser("[{\"name\":\"John\", \"age\":30}]");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        assertTrue(mi.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        // Initialize
        JavaType type = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = false;
        Object valueToUpdate = null;
        MappingIterator<Object> mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);

        // Test when there is no next value
        try {
            mi.nextValue();
            fail("NoSuchElementException not thrown");
        } catch (NoSuchElementException e) {
            // Expected
        }

        // Test when there is a next value
        p = new JsonFactory().createParser("[{\"name\":\"John\", \"age\":30}]");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        assertNotNull(mi.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        // Initialize
        JavaType type = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = false;
        Object valueToUpdate = null;
        MappingIterator<Object> mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);

        // Test when there are no values
        List<Object> resultList = mi.readAll();
        assertEquals(0, resultList.size());

        // Test when there are values
        p = new JsonFactory().createParser("[{\"name\":\"John\", \"age\":30}, {\"name\":\"Jane\", \"age\":25}]");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        resultList = mi.readAll();
        assertEquals(2, resultList.size());
    }

    @Test
    public void testGetParser() {
        // Initialize
        JavaType type = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = false;
        Object valueToUpdate = null;
        MappingIterator<Object> mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);

        assertNull(mi.getParser());

        p = new JsonFactory().createParser("");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        assertNotNull(mi.getParser());
    }

    @Test
    public void testGetParserSchema() {
        // Initialize
        JavaType type = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = false;
        Object valueToUpdate = null;
        MappingIterator<Object> mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);

        assertNull(mi.getParserSchema());

        p = new JsonFactory().createParser("");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        assertNull(mi.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        // Initialize
        JavaType type = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = false;
        Object valueToUpdate = null;
        MappingIterator<Object> mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);

        assertNull(mi.getCurrentLocation());

        p = new JsonFactory().createParser("");
        mi = new MappingIterator<Object>(type, p, ctxt, deser, managedParser, valueToUpdate);
        assertNotNull(mi.getCurrentLocation());
    }
}