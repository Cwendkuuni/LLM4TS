import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class MappingIteratorTest {

    @Test
    public void testMappingIteratorHasNext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("[\"element1\", \"element2\", \"element3\"]");
        MappingIterator<String> iterator = reader.readValues(parser);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("element1", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("element2", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("element3", iterator.next());
        Assert.assertFalse(iterator.hasNext());
        
        parser.close();
    }

    @Test
    public void testMappingIteratorEmptyIterator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonFactory jsonFactory = mapper.getFactory();
        MappingIterator<String> iterator = reader.readValues(jsonFactory.createParser("[]"));

        Assert.assertFalse(iterator.hasNext());
        
        reader.close();
        jsonFactory.close();
        mapper.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMappingIteratorNoSuchElement() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("[\"element1\", \"element2\"]");
        MappingIterator<String> iterator = reader.readValues(parser);

        iterator.next();
        iterator.next();
        iterator.next(); // This should throw NoSuchElementException
        
        parser.close();
        reader.close();
        jsonFactory.close();
        mapper.close();
    }

    @Test(expected = IOException.class)
    public void testMappingIteratorIOException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("{\"invalid_json\"}");
        MappingIterator<String> iterator = reader.readValues(parser);

        parser.close();
        reader.close();
        jsonFactory.close();
        mapper.close();
    }

    @Test
    public void testMappingIteratorCloseParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(String.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser("[\"element1\", \"element2\", \"element3\"]");

        MappingIterator<String> iterator = reader.readValues(parser);
        iterator.close();

        parser = jsonFactory.createParser("[\"element4\", \"element5\", \"element6\"]");
        iterator = reader.readValues(parser);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("element4", iterator.next());
        
        parser.close();
        reader.close();
        jsonFactory.close();
        mapper.close();
    }
}