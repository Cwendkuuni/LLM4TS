package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_EXPECT_VALUE;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_EXPECT_NAME;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AS_IS;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AFTER_COMMA;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AFTER_COLON;
import static com.fasterxml.jackson.core.JsonWriteContext.STATUS_OK_AFTER_SPACE;

public class JsonWriteContextTest {

    @Test
    public void testJsonWriteContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("[]", rootContext.toString());

        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        assertEquals("[", arrayContext.toString());

        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        assertEquals("{}", objectContext.toString());

        JsonWriteContext arrayContextWithValue = arrayContext.reset(JsonWriteContext.TYPE_OBJECT);
        arrayContextWithValue.writeValue();
        arrayContextWithValue.writeFieldName("fieldName");
        arrayContextWithValue.writeValue();
        assertEquals("[\"fieldName\":]", arrayContextWithValue.toString());

        JsonWriteContext objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_ARRAY);
        objectContextWithName.writeFieldName("fieldName1");
        objectContextWithName.writeValue();
        objectContextWithName.writeFieldName("fieldName2");
        objectContextWithName.writeValue();
        assertEquals("{\"fieldName1\":, \"fieldName2\":}", objectContextWithName.toString());

        JsonWriteContext objectContextWithValueAndName = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
        objectContextWithValueAndName.writeFieldName("fieldName1");
        objectContextWithValueAndName.writeValue("fieldValue1");
        objectContextWithValueAndName.writeFieldName("fieldName2");
        objectContextWithValueAndName.writeValue("fieldValue2");
        assertEquals("{\"fieldName1\":\"fieldValue1\", \"fieldName2\":\"fieldValue2\"}", objectContextWithValueAndName.toString());

        JsonWriteContext objectContextWithNameAndDupDetector = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
        objectContextWithNameAndDupDetector.withDupDetector(new DupDetector() {
            @Override
            public void reset() {
                // empty
            }

            @Override
            public boolean isDup(String name) {
                if ("fieldName1".equals(name)) {
                    return true;
                }
                return false;
            }
        });
        try {
            objectContextWithNameAndDupDetector.writeFieldName("fieldName1");
            objectContextWithNameAndDupDetector.writeValue("fieldValue1");
            objectContextWithNameAndDupDetector.writeFieldName("fieldName1"); // should throw exception
            fail("Expected JsonGenerationException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }
}

class DupDetector {
    private DupDetector child;

    public boolean isDup(String name) {
        return false;
    }

    public DupDetector child() {
        return child;
    }

    public void reset() {
        // empty
    }
}