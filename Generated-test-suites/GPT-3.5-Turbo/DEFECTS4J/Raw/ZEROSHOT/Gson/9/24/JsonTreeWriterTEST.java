/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet() {
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = jsonTreeWriter.get();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNonEmptyStack() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        jsonTreeWriter.put(jsonArray);
        jsonTreeWriter.get();
    }

    @Test
    public void testPeek() {
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = jsonTreeWriter.peek();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekWithEmptyStack() throws IOException {
        jsonTreeWriter.peek();
    }

    @Test
    public void testPut() {
        JsonElement jsonElement = new JsonPrimitive("value");
        jsonTreeWriter.put(jsonElement);
        Assert.assertEquals(jsonElement, jsonTreeWriter.peek());
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonTreeWriter.beginArray();
        Assert.assertEquals(jsonArray, jsonTreeWriter.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayWithPendingName() throws IOException {
        jsonTreeWriter.name("name");
        jsonTreeWriter.beginArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithEmptyStack() throws IOException {
        jsonTreeWriter.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithPendingName() throws IOException {
        jsonTreeWriter.name("name");
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonTreeWriter.beginObject();
        Assert.assertEquals(jsonObject, jsonTreeWriter.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectWithPendingName() throws IOException {
        jsonTreeWriter.name("name");
        jsonTreeWriter.beginObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithEmptyStack() throws IOException {
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithPendingName() throws IOException {
        jsonTreeWriter.name("name");
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithEmptyStack() throws IOException {
        jsonTreeWriter.name("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithArrayElement() throws IOException {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.name("name");
    }

    @Test
    public void testValueWithString() throws IOException {
        String value = "test";
        jsonTreeWriter.value(value);
        Assert.assertEquals(new JsonPrimitive(value), jsonTreeWriter.peek());
    }

    @Test
    public void testValueWithNull() throws IOException {
        jsonTreeWriter.value(null);
        Assert.assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
    }

    @Test
    public void testNullValue() throws IOException {
        jsonTreeWriter.nullValue();
        Assert.assertEquals(JsonNull.INSTANCE, jsonTreeWriter.peek());
    }

    @Test
    public void testValueWithBoolean() throws IOException {
        boolean value = true;
        jsonTreeWriter.value(value);
        Assert.assertEquals(new JsonPrimitive(value), jsonTreeWriter.peek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueWithNonLenientDouble() throws IOException {
        jsonTreeWriter.setLenient(false);
        double value = Double.NaN;
        jsonTreeWriter.value(value);
    }

    @Test
    public void testValueWithLenientDouble() throws IOException {
        jsonTreeWriter.setLenient(true);
        double value = Double.NaN;
        jsonTreeWriter.value(value);
        Assert.assertEquals(new JsonPrimitive(value), jsonTreeWriter.peek());
    }

    @Test
    public void testValueWithLong() throws IOException {
        long value = 10L;
        jsonTreeWriter.value(value);
        Assert.assertEquals(new JsonPrimitive(value), jsonTreeWriter.peek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueWithNonLenientNumber() throws IOException {
        jsonTreeWriter.setLenient(false);
        double value = Double.NaN;
        jsonTreeWriter.value(value);
    }

    @Test
    public void testValueWithLenientNumber() throws IOException {
        jsonTreeWriter.setLenient(true);
        double value = Double.NaN;
        jsonTreeWriter.value(value);
        Assert.assertEquals(new JsonPrimitive(value), jsonTreeWriter.peek());
    }

    @Test
    public void testFlush() throws IOException {
        // Testing flush is not applicable for JsonTreeWriter class
    }

    @Test(expected = IOException.class)
    public void testCloseWithNonEmptyStack() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeWriter.close();
        Assert.assertEquals(jsonTreeWriter.peek(), JsonTreeWriter.SENTINEL_CLOSED);
    }
}