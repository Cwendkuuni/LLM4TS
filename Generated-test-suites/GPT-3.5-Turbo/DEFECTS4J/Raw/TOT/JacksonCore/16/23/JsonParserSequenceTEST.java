
package com.fasterxml.jackson.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    public JsonParserSequenceTest(JsonParserSequence jsonParserSequence) {
        this.jsonParserSequence = jsonParserSequence;
    }

    @Parameterized.Parameters
    public static Collection<JsonParserSequence> data() {
        JsonParserSequence sequence1 = new JsonParserSequence(new JsonParser[]{new DummyJsonParser(), new DummyJsonParser()});
        JsonParserSequence sequence2 = JsonParserSequence.createFlattened(new DummyJsonParser(), new DummyJsonParser());
        return Arrays.asList(sequence1, sequence2);
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        assertFalse(jsonParserSequence.delegate().isOpen());
    }

    @Test
    public void testNextToken() throws IOException {
        jsonParserSequence.switchToNext();
        assertTrue(jsonParserSequence.nextToken() != null);
    }

    @Test
    public void testContainedParsersCount() {
        int containedParsersCount = jsonParserSequence.containedParsersCount();
        assertEquals(2, containedParsersCount);
    }

}
