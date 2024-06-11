import org.junit.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParserSequenceTest {
    
    // Test createFlattened(JsonParser first, JsonParser second)
    
    @Test
    public void testCreateFlattenedWithSimpleParsers() {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        JsonParser result = JsonParserSequence.createFlattened(first, second);
        Assert.assertTrue(result instanceof JsonParserSequence);
        Assert.assertEquals(2, ((JsonParserSequence) result).containedParsersCount());
    }
    
    @Test
    public void testCreateFlattenedWithSequenceAndSimpleParsers() {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        JsonParser third = new MockJsonParser();
        JsonParser sequence = JsonParserSequence.createFlattened(first, second);
        JsonParser result = JsonParserSequence.createFlattened(sequence, third);
        Assert.assertTrue(result instanceof JsonParserSequence);
        Assert.assertEquals(3, ((JsonParserSequence) result).containedParsersCount());
    }
    
    @Test
    public void testCreateFlattenedWithSequences() {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        JsonParser third = new MockJsonParser();
        JsonParser fourth = new MockJsonParser();
        JsonParser sequence1 = JsonParserSequence.createFlattened(first, second);
        JsonParser sequence2 = JsonParserSequence.createFlattened(third, fourth);
        JsonParser result = JsonParserSequence.createFlattened(sequence1, sequence2);
        Assert.assertTrue(result instanceof JsonParserSequence);
        Assert.assertEquals(4, ((JsonParserSequence) result).containedParsersCount());
    }
    
    // Test close()
    
    @Test
    public void testCloseWithFirstParserHavingToken() throws IOException {
        JsonParser first = new MockJsonParser(true);
        JsonParser second = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        sequence.close();
        Assert.assertFalse(first.isClosed());
        Assert.assertTrue(second.isClosed());
    }
    
    @Test
    public void testCloseWithFirstParserHavingNoTokenButSecondParserHavingToken() throws IOException {
        JsonParser first = new MockJsonParser(false);
        JsonParser second = new MockJsonParser(true);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        sequence.close();
        Assert.assertTrue(first.isClosed());
        Assert.assertTrue(second.isClosed());
    }
    
    @Test
    public void testCloseWithBothParsersHavingNoTokens() throws IOException {
        JsonParser first = new MockJsonParser(false);
        JsonParser second = new MockJsonParser(false);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        sequence.close();
        Assert.assertTrue(first.isClosed());
        Assert.assertTrue(second.isClosed());
    }
    
    @Test
    public void testCloseWithAllParsersHavingTokens() throws IOException {
        JsonParser first = new MockJsonParser(true);
        JsonParser second = new MockJsonParser(true);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        sequence.close();
        Assert.assertTrue(first.isClosed());
        Assert.assertTrue(second.isClosed());
    }
    
    // Test nextToken()
    
    @Test
    public void testNextTokenWithFirstParserHavingTokens() throws IOException {
        JsonParser first = new MockJsonParser(true);
        JsonParser second = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        JsonToken token = sequence.nextToken();
        Assert.assertNotNull(token);
        Assert.assertEquals(JsonToken.VALUE_TRUE, token);
    }
    
    @Test
    public void testNextTokenWithFirstParserHavingNoTokensButSecondParserHavingTokens() throws IOException {
        JsonParser first = new MockJsonParser(false);
        JsonParser second = new MockJsonParser(true);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        JsonToken token = sequence.nextToken();
        Assert.assertNotNull(token);
        Assert.assertEquals(JsonToken.VALUE_TRUE, token);
    }
    
    @Test
    public void testNextTokenWithBothParsersHavingNoTokens() throws IOException {
        JsonParser first = new MockJsonParser(false);
        JsonParser second = new MockJsonParser(false);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        JsonToken token = sequence.nextToken();
        Assert.assertNull(token);
    }
    
    @Test
    public void testNextTokenWithAllParsersHavingTokens() throws IOException {
        JsonParser first = new MockJsonParser(true);
        JsonParser second = new MockJsonParser(true);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        JsonToken token = sequence.nextToken();
        Assert.assertNotNull(token);
        Assert.assertEquals(JsonToken.VALUE_TRUE, token);
    }
    
    // Test containedParsersCount()
    
    @Test
    public void testContainedParsersCountWithSingleParser() {
        JsonParser parser = new MockJsonParser();
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        int count = sequence.containedParsersCount();
        Assert.assertEquals(1, count);
    }
    
    @Test
    public void testContainedParsersCountWithMultipleParsers() {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        int count = sequence.containedParsersCount();
        Assert.assertEquals(2, count);
    }
    
    // Helper class
    
    private static class MockJsonParser extends JsonParser {
        
        private boolean hasToken;
        private boolean closed;
        
        public MockJsonParser() {
            super(null);
        }
        
        public MockJsonParser(boolean hasToken) {
            super(null);
            this.hasToken = hasToken;
        }
        
        public void close() throws IOException {
            closed = true;
        }
        
        public JsonToken nextToken() throws IOException {
            return hasToken ? JsonToken.VALUE_TRUE : null;
        }
        
        public boolean isClosed() {
            return closed;
        }
    }
}