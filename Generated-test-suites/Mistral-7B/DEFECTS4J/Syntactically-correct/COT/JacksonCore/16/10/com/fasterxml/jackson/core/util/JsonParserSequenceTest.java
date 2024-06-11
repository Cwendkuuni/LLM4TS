package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class JsonParserSequenceTest {
    

    @Test
    public void testJsonParserSequenceConstructor() {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        Assert.assertNotNull(jsonParserSequence);
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParser sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        Assert.assertNotNull(sequence);
        Assert.assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken_firstParserHasToken() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        JsonToken token = firstParser.nextToken();
        JsonToken nextToken = jsonParserSequence.nextToken();
        Assert.assertEquals(token, nextToken);
    }

    @Test
    public void testNextToken_secondParserHasToken() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        firstParser.nextToken();
        JsonToken token = secondParser.nextToken();
        JsonToken nextToken = jsonParserSequence.nextToken();
        Assert.assertEquals(token, nextToken);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_noMoreParsers() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParser thirdParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, thirdParser });
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
    }

    @Test
    public void testClose() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        jsonParserSequence.close();
        firstParser.close();
        secondParser.close();
    }

    @Test
    public void testSwitchToNext_hasNextParser() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        jsonParserSequence.switchToNext();
        Assert.assertEquals(secondParser, jsonParserSequence.delegate);
    }

    @Test
    public void testSwitchToNext_noNextParser() throws IOException {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        Assert.assertEquals(firstParser, jsonParserSequence.delegate);
    }

    @Test
    public void testAddFlattenedActiveParsers() {
        JsonParser firstParser = createTestParser();
        JsonParser secondParser = createTestParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(firstParser);
        List<JsonParser> result = new ArrayList<>();
        jsonParserSequence.addFlattenedActiveParsers(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(firstParser, result.get(0));

        JsonParser thirdParser = createTestParser();
        JsonParserSequence jsonParserSequence2 = new JsonParserSequence(secondParser);
        jsonParserSequence2.addFlattenedActiveParsers(result);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(firstParser, result.get(0));
        Assert.assertEquals(secondParser, result.get(1));
        Assert.assertEquals(thirdParser, jsonParserSequence2.delegate);
    }

    

    private JsonParser createTestParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"name\":\"John\",\"age\":30}";
        JsonFactory factory = mapper.getFactory();
        return factory.createParser(new ByteArrayInputStream(jsonString.getBytes(Base64Variants.STANDARD)));
    }
}