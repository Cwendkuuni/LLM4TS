import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    @Test
    public void testHasNextValueWithEmptyIterator() throws Exception {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWhenObjectIsNotArray() throws Exception {
        String input = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = new JsonFactory().createParser(input);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWhenObjectIsEmptyArray() throws Exception {
        String input = "[]";
        JsonParser parser = new JsonFactory().createParser(input);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValueWithEmptyIterator() throws Exception {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();

        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextValueWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);

        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextValueWhenObjectIsNotArray() throws Exception {
        String input = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = new JsonFactory().createParser(input);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        assertTrue(iterator.hasNextValue());

        Object value = iterator.nextValue();
        assertEquals("{\"name\":\"John\",\"age\":30}", value.toString());

        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValueWhenObjectIsEmptyArray() throws Exception {
        String input = "[]";
        JsonParser parser = new JsonFactory().createParser(input);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadAllWithEmptyIterator() throws Exception {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertEquals(0, iterator.readAll().size());
    }

    @Test
    public void testReadAllWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertEquals(0, iterator.readAll().size());
    }

    @Test
    public void testReadAllWhenObjectIsNotArray() throws Exception {
        String input = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = new JsonFactory().createParser(input);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        assertEquals(1, iterator.readAll().size());
    }

    @Test
    public void testReadAllWhenObjectIsEmptyArray() throws Exception {
        String input = "[]";
        JsonParser parser = new JsonFactory().createParser(input);
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        assertEquals(0, iterator.readAll().size());
    }

    @Test
    public void testGetParserWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchemaWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocationWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(iterator.getCurrentLocation());
    }

    @Test
    public void testRemove() throws Exception {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();

        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testCloseWithNullParser() throws Exception {
        MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator.close();
        assertNull(iterator.getParser());
    }
}