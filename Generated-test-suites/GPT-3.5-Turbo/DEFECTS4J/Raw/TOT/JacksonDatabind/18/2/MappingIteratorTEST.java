import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class MappingIteratorTest {

    private MappingIterator<Integer> mappingIterator;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testHasNext() throws IOException {
        // Test case 1: No elements
        String json1 = "[]";
        mappingIterator = mapper.readValue(json1, MappingIterator.class);

        Assert.assertFalse(mappingIterator.hasNext());

        // Test case 2: Multiple elements
        String json2 = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json2, MappingIterator.class);

        Assert.assertTrue(mappingIterator.hasNext());

        // Test case 3: Single element
        String json3 = "[1]";
        mappingIterator = mapper.readValue(json3, MappingIterator.class);

        Assert.assertTrue(mappingIterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        // Test case 1: No elements
        String json1 = "[]";
        mappingIterator = mapper.readValue(json1, MappingIterator.class);

        try {
            mappingIterator.next();
            Assert.fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // Expected
        }

        // Test case 2: Multiple elements
        String json2 = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json2, MappingIterator.class);

        Assert.assertEquals(Integer.valueOf(1), mappingIterator.next());
        Assert.assertEquals(Integer.valueOf(2), mappingIterator.next());
        Assert.assertEquals(Integer.valueOf(3), mappingIterator.next());

        try {
            mappingIterator.next();
            Assert.fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // Expected
        }
    }

    @Test
    public void testRemove() throws IOException {
        String json = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json, MappingIterator.class);

        try {
            mappingIterator.remove();
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testClose() throws IOException {
        // Test case 1: Valid close
        String json1 = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json1, MappingIterator.class);

        Assert.assertTrue(mappingIterator.hasNext());
        mappingIterator.close();
        Assert.assertFalse(mappingIterator.hasNext());

        // Test case 2: Close twice
        String json2 = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json2, MappingIterator.class);

        Assert.assertTrue(mappingIterator.hasNext());
        mappingIterator.close();
        mappingIterator.close();
        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        // Test case 1: Empty list
        String json1 = "[]";
        mappingIterator = mapper.readValue(json1, MappingIterator.class);

        Assert.assertEquals(0, mappingIterator.readAll().size());

        // Test case 2: Multiple elements
        String json2 = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json2, MappingIterator.class);

        Assert.assertEquals(3, mappingIterator.readAll().size());

        // Test case 3: Single element
        String json3 = "[1]";
        mappingIterator = mapper.readValue(json3, MappingIterator.class);

        Assert.assertEquals(1, mappingIterator.readAll().size());
    }

    @Test
    public void testGetParser() throws IOException {
        String json = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json, MappingIterator.class);

        Assert.assertNotNull(mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() throws IOException {
        String json = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json, MappingIterator.class);

        Assert.assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        String json = "[1, 2, 3]";
        mappingIterator = mapper.readValue(json, MappingIterator.class);

        Assert.assertNotNull(mappingIterator.getCurrentLocation());
    }

}