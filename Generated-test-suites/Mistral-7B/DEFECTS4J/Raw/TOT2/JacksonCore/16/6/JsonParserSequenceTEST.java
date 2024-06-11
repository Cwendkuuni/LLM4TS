import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testNextTokenNormalFlow() throws IOException, JsonParseException {
        JsonParser parser1 = new JsonFactory().createParser("{ \"name\": \"John\" }");
        JsonParser parser2 = new JsonFactory().createParser("{ \"age\": 25 }");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        JsonToken token = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", token.asString());

        jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", token.asString());

        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", token.asString());

        jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(25, token.intValue());

        jsonParserSequence.nextToken(); // End of object
        Assert.assertEquals(JsonToken.END_OBJECT, token);
    }
}