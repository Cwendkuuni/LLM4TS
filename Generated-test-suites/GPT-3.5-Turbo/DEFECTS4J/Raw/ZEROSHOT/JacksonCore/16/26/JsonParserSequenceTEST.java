package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testContainedParsersCount() {
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] {});
        assertEquals(0, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testCreateFlattened() {
        JsonParser first = null;
        JsonParser second = null;
        JsonParserSequence result = JsonParserSequence.createFlattened(first, second);
        assertNull(result);
    }

    // TODO: Write more test methods to comprehensively test all methods in JsonParserSequence

}