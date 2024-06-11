
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @Before
    public void setUp() {
        reader = new JsonTreeReader(null);
    }

    @Test
    public void testBeginArray() {
        try {
            reader.beginArray();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testEndArray() {
        try {
            reader.endArray();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testBeginObject() {
        try {
            reader.beginObject();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testEndObject() {
        try {
            reader.endObject();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testHasNext() {
        try {
            boolean hasNext = reader.hasNext();
            assertFalse(hasNext);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testPeek() {
        try {
            JsonToken peek = reader.peek();
            assertEquals(JsonToken.END_DOCUMENT, peek);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextName() {
        try {
            String nextName = reader.nextName();
            assertNull(nextName);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextString() {
        try {
            String nextString = reader.nextString();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextBoolean() {
        try {
            boolean nextBoolean = reader.nextBoolean();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextNull() {
        try {
            reader.nextNull();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextDouble() {
        try {
            double nextDouble = reader.nextDouble();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextLong() {
        try {
            long nextLong = reader.nextLong();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testNextInt() {
        try {
            int nextInt = reader.nextInt();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testClose() {
        try {
            reader.close();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testSkipValue() {
        try {
            reader.skipValue();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testPromoteNameToValue() {
        try {
            reader.promoteNameToValue();
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testGetPath() {
        try {
            String path = reader.getPath();
            assertEquals("$", path);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void testToString() {
        try {
            String string = reader.toString();
            assertEquals("JsonTreeReader", string);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
}
