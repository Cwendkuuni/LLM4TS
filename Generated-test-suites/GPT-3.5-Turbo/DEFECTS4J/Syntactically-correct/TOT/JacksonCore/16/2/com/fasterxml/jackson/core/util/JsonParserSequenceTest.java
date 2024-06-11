import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;


public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Test
    public void createFlattened_TwoNonSequenceParsers_ReturnsJsonParserSequence() {
        // Arrange
        JsonParser first = new DummyParser();
        JsonParser second = new DummyParser();

        // Act
        jsonParserSequence = JsonParserSequence.createFlattened(first, second);

        // Assert
        Assert.assertNotNull(jsonParserSequence);
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void createFlattened_FirstSequenceParserAndSecondNonSequenceParser_ReturnsJsonParserSequence() {
        // Arrange
        JsonParser first = new JsonParserSequence(new JsonParser[] { new DummyParser() });
        JsonParser second = new DummyParser();

        // Act
        jsonParserSequence = JsonParserSequence.createFlattened(first, second);

        // Assert
        Assert.assertNotNull(jsonParserSequence);
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void createFlattened_FirstNonSequenceParserAndSecondSequenceParser_ReturnsJsonParserSequence() {
        // Arrange
        JsonParser first = new DummyParser();
        JsonParser second = new JsonParserSequence(new JsonParser[] { new DummyParser() });

        // Act
        jsonParserSequence = JsonParserSequence.createFlattened(first, second);

        // Assert
        Assert.assertNotNull(jsonParserSequence);
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void createFlattened_TwoSequenceParsers_ReturnsJsonParserSequence() {
        // Arrange
        JsonParser first = new JsonParserSequence(new JsonParser[] { new DummyParser() });
        JsonParser second = new JsonParserSequence(new JsonParser[] { new DummyParser() });

        // Act
        jsonParserSequence = JsonParserSequence.createFlattened(first, second);

        // Assert
        Assert.assertNotNull(jsonParserSequence);
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void nextToken_SingleParserWithTokens_ReturnsTokens() throws IOException, JsonParseException {
        // Arrange
        JsonParser parser = new DummyParser();
        jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });

        // Act
        JsonToken token = jsonParserSequence.nextToken();

        // Assert
        Assert.assertNotNull(token);
    }

    @Test
    public void nextToken_MultipleParsersWithTokens_ReturnsTokensFromAllParsers() throws IOException, JsonParseException {
        // Arrange
        JsonParser first = new DummyParser(JsonToken.START_OBJECT);
        JsonParser second = new DummyParser(JsonToken.FIELD_NAME);
        jsonParserSequence = JsonParserSequence.createFlattened(first, second);

        // Act
        JsonToken token1 = jsonParserSequence.nextToken();
        JsonToken token2 = jsonParserSequence.nextToken();

        // Assert
        Assert.assertNotNull(token1);
        Assert.assertNotNull(token2);
        Assert.assertEquals(JsonToken.START_OBJECT, token1);
        Assert.assertEquals(JsonToken.FIELD_NAME, token2);
    }

    private class DummyParser extends JsonParser {
        private JsonToken token;

        public DummyParser() {
            this.token = null;
        }

        public DummyParser(JsonToken token) {
            this.token = token;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return token;
        }

        // Implement other abstract methods as per needed for the test scenarios
    }
}