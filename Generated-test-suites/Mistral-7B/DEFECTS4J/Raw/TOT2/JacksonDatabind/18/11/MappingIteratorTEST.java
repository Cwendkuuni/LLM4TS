import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider.Status;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.core.BaseValueTypeDeserializer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonDeserializer<String> deserializer;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        reader = mapper.readerForListOf(String.class);
        deserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonMappingException {
                return p.getValueAsString();
            }
        };
    }

    @Test
    public void testBasicFunctionality() throws Exception {
        List<String> list = reader.readValue(new StringReader("[\"apple\", \"banana\", \"cherry\"]"));
        MappingIterator<String> iterator = list.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("apple", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("banana", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("cherry", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testEdgeCases() throws Exception {
        List<String> list = reader.readValue(new StringReader("[]"));
        MappingIterator<String> iterator = list.iterator();
        Assert.assertFalse(iterator.hasNext());

        list = reader.readValue(new StringReader("null"));
        iterator = list.iterator();
        Assert.assertFalse(iterator.hasNext());

        list = reader.readValue(new StringReader("{\"data\": []}"));
        iterator = list.iterator();
        Assert.assertFalse(iterator.hasNext());

        list = reader.readValue(new StringReader("{\"data\": null}"));
        iterator = list.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testExceptionHandling() throws Exception {
        List<String> list = reader.readValue(new StringReader("{\"data\": [\"apple\", \"invalid\"]}"));
        MappingIterator<String> iterator = list.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("apple", iterator.next());

        try {
            iterator.hasNext();
            fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals(JsonMappingException.ClassId.FIELD_VALUE_INVALID, e.getClassId());
            Assert.assertEquals("Cannot deserialize value of type java.lang.String from Object value (token: START_OBJECT)", e.getOriginalMessage());
        }

        List<String> emptyList = new ArrayList<>();
        iterator = emptyList.iterator();
        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // Expected
        }
    }
}