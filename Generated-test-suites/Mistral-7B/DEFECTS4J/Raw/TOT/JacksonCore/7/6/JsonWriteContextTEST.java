package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPreprets;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonWriteContextTest {

    @Test
    public void testJsonWriteContextRootContext() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.appendDesc(new StringBuilder(64));
        String result = context.toString();
        assertEquals("/", result);

        context.reset();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("/", result);

        context.setCurrentValue("test");
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("/test", result);
    }

    @Test
    public void testJsonWriteContextObjectContext() throws JsonProcessingException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();
        context.appendDesc(new StringBuilder(64));

        context.writeFieldName("field1");
        context.setCurrentValue(1);
        context.appendDesc(new StringBuilder(64));
        String result = context.toString();
        assertEquals("{\"field1\":1}", result);

        context.writeFieldName("field2");
        context.setCurrentValue("test");
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("{\"field1\":1,\"field2\":\"test\"}", result);

        context.reset();
        context.writeFieldName("field1");
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("{\"field1\":}", result);

        context.writeFieldName("field2");
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("{\"field2\":}", result);

        context.reset();
        context.writeFieldName("field1");
        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("{\"field1\":}", result);

        context.reset();
        context.writeFieldName("field1");
        context.writeFieldName("field2");
        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("{\"field1\":{\"field2\":}}", result);
    }

    @Test
    public void testJsonWriteContextArrayContext() throws JsonProcessingException {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.appendDesc(new StringBuilder(64));

        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        String result = context.toString();
        assertEquals("[0]", result);

        context.writeValue();
        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("[0,0]", result);

        context.writeValue("test");
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("[0,\"test\"]", result);

        context.reset();
        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("[0]", result);

        context.writeValue();
        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("[0,0]", result);

        context.reset();
        context.writeValue();
        context.writeValue();
        context.writeValue();
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("[0,0,0]", result);

        context.reset();
        context.writeValue();
        context.writeValue("test");
        context.writeValue(1);
        context.appendDesc(new StringBuilder(64));
        result = context.toString();
        assertEquals("[0,\"test\",1]", result);
    }

    @Test
    public void testJsonWriteContextWithDupDetector() throws JsonProcessingException {
        DupDetector dupDetector = new DefaultPreprets.SimpleStringDupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context = context.withDupDetector(dupDetector);

        context.writeFieldName("field1");
        context.setCurrentValue(1);
        context.writeFieldName("field1");
        context.setCurrentValue("test");
        context.appendDesc(new StringBuilder(64));
        String result = context.toString();

        try {
            result = context.toString();
            Assert.fail();
        } catch (JsonProcessingException e) {
            String expectedMessage = "Duplicate field 'field1'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}