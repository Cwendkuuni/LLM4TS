package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParserSequence parserSequence;

    @Before
    public void setUp() throws IOException {
        parser1 = new JsonTestParser(new StringReader("{\"key1\":\"value1\"}"));
        parser2 = new JsonTestParser(new StringReader("{\"key2\":\"value2\"}"));
        parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testNextTokenBasic() throws IOException {
        JsonToken token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value1", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key2", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value2", token.getText().toString());

        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = IOException.class)
    public void testNextTokenIOException() throws IOException {
        parser1 = new JsonTestParser(new StringReader("{\"key1\":\"value1\"")); // incomplete JSON
        parser2 = new JsonTestParser(new StringReader("{\"key2\":\"value2\"}"));
        parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        parserSequence.nextToken();
        parserSequence.nextToken();

        parserSequence.nextToken(); // should throw an IOException
    }

    @Test
    public void testNextTokenAfterIOException() throws IOException {
        parser1 = new JsonTestParser(new StringReader("{\"key1\":\"value1\"")); // incomplete JSON
        parser2 = new JsonTestParser(new StringReader("{\"key2\":\"value2\"}"));
        parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText().toString());

        try {
            parserSequence.nextToken(); // should throw an IOException
        } catch (IOException e) {
            // expected
        }

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertEquals(parser2, parserSequence.delegate);

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key2", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value2", token.getText().toString());

        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testClose() throws IOException {
        parserSequence.close();
        assertNull(parserSequence.delegate);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParserSequence newParserSequence = JsonParserSequence.createFlattened(parser1, parserSequence);

        JsonToken token = newParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertEquals(parser1, newParserSequence.delegate);

        token = newParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText().toString());

        newParserSequence.switchToNext();

        token = newParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertEquals(parser2, newParserSequence.delegate);

        token = newParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key2", token.getText().toString());

        token = newParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value2", token.getText().toString());

        token = newParserSequence.nextToken();
        assertNull(token);
    }
}

class JsonTestParser extends JsonParser {

    public JsonTestParser(Reader reader) {
        super(null);
        this._currentLocation = new Location();
        this._inputSrcFactory = new StandardInputSourceFactory();
        this._inputSrc = _inputSrcFactory.createSource(reader, null);
        this._nextRootValue = null;
        this._inputDelegate = new InputSourcePacketDelegator(_inputSrc, null, null, null);
        this._parseState = new ObjectParserState();
        this._parseState.init(this);
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        JsonToken token = super.nextToken();
        if (token == null) {
            if (_nextRootValue != null) {
                token = _nextRootValue;
                _nextRootValue = null;
            } else {
                throw new IOException("No token to return");
            }
        }
        return token;
    }

    public void setNextRootValue(JsonToken nextRootValue) {
        _nextRootValue = nextRootValue;
    }
}