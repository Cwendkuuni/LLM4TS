import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test(expected = JsonParseException.class)
    public void testNextValueWithInvalidJson_shouldThrowJsonParseException() throws IOException {
        MappingIterator<Object> mappingIterator = objectMapper.readerFor(Object.class).readValues("{invalidJson}");
        mappingIterator.nextValue();
    }

    @Test
    public void testHasNextValueWithValidJson_shouldReturnTrue() throws IOException {
        MappingIterator<Integer> mappingIterator = objectMapper.readerFor(Integer.class).readValues("[1, 2, 3]");
        Assert.assertTrue(mappingIterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWithEmptyIterator_shouldThrowNoSuchElementException() throws IOException {
        MappingIterator<Object> mappingIterator = objectMapper.readerFor(Object.class).readValues("[]");
        mappingIterator.nextValue();
    }

    @Test
    public void testNextValueWithValidJson_shouldReturnDeserializedValue() throws IOException {
        MappingIterator<Integer> mappingIterator = objectMapper.readerFor(Integer.class).readValues("[1, 2, 3]");
        Assert.assertEquals(Integer.valueOf(1), mappingIterator.nextValue());
        Assert.assertEquals(Integer.valueOf(2), mappingIterator.nextValue());
        Assert.assertEquals(Integer.valueOf(3), mappingIterator.nextValue());
    }

    @Test
    public void testReadAllWithValidJson_shouldReturnAllDeserializedValues() throws IOException {
        MappingIterator<Integer> mappingIterator = objectMapper.readerFor(Integer.class).readValues("[1, 2, 3]");
        List<Integer> resultList = mappingIterator.readAll(new ArrayList<>());
        Assert.assertEquals(3, resultList.size());
        Assert.assertEquals(Integer.valueOf(1), resultList.get(0));
        Assert.assertEquals(Integer.valueOf(2), resultList.get(1));
        Assert.assertEquals(Integer.valueOf(3), resultList.get(2));
    }

    @Test
    public void testReadAllWithEmptyIterator_shouldReturnEmptyList() throws IOException {
        MappingIterator<Object> mappingIterator = objectMapper.readerFor(Object.class).readValues("[]");
        List<Object> resultList = mappingIterator.readAll(new ArrayList<>());
        Assert.assertTrue(resultList.isEmpty());
    }

    @Test
    public void testGetParser_returnsValidParser() throws IOException {
        MappingIterator<String> mappingIterator = objectMapper.readerFor(String.class).readValues("[\"test\"]");
        Assert.assertNotNull(mappingIterator.getParser());
        Assert.assertTrue(mappingIterator.getParser().hasNext());
        Assert.assertEquals(JsonToken.START_ARRAY, mappingIterator.getParser().nextToken());
        Assert.assertEquals("test", mappingIterator.getParser().nextTextValue());
        Assert.assertEquals(JsonToken.END_ARRAY, mappingIterator.getParser().nextToken());
        Assert.assertFalse(mappingIterator.getParser().hasNext());
    }

    @Test
    public void testGetParserSchema_returnsValidSchema() throws IOException {
        MappingIterator<String> mappingIterator = objectMapper.readerFor(String.class).readValues("[\"test\"]");
        Assert.assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation_returnsValidLocation() throws IOException {
        MappingIterator<String> mappingIterator = objectMapper.readerFor(String.class).readValues("[\"test\"]");
        Assert.assertNotNull(mappingIterator.getCurrentLocation());
        Assert.assertEquals(1, mappingIterator.getCurrentLocation().getLineNr());
        Assert.assertEquals(1, mappingIterator.getCurrentLocation().getColumnNr());
        mappingIterator.nextValue();
        Assert.assertEquals(1, mappingIterator.getCurrentLocation().getLineNr());
        Assert.assertEquals(8, mappingIterator.getCurrentLocation().getColumnNr());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_shouldThrowUnsupportedOperationException() throws IOException {
        MappingIterator<String> mappingIterator = objectMapper.readerFor(String.class).readValues("[\"test\"]");
        mappingIterator.remove();
    }

    @Test(expected = IOException.class)
    public void testClose_withClosedIterator_shouldThrowIOException() throws IOException {
        MappingIterator<String> mappingIterator = objectMapper.readerFor(String.class).readValues("[\"test\"]");
        mappingIterator.close();
        mappingIterator.hasNext();
    }
}