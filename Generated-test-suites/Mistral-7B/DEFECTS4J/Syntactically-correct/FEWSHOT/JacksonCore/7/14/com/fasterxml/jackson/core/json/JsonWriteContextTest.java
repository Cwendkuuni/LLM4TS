package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext rootContextWithDups = JsonWriteContext.createRootContext(new NoOpDupDetector());

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertEquals(null, rootContext.getParent());
        assertNull(rootContext.getDupDetector());
        assertEquals(0, rootContext.getCurrentIndex());
        assertEquals(null, rootContext.getCurrentName());
        assertEquals(null, rootContext.getCurrentValue());
        assertEquals(null, rootContext._parent);
        assertEquals(null, rootContext._child);

        assertEquals(JsonWriteContext.TYPE_ROOT, rootContextWithDups.getType());
        assertNotNull(rootContextWithDups.getParent());
        assertNotNull(rootContextWithDups.getDupDetector());
        assertEquals(0, rootContextWithDups.getCurrentIndex());
        assertEquals(null, rootContextWithDups.getCurrentName());
        assertEquals(null, rootContextWithDups.getCurrentValue());
        assertEquals(null, rootContextWithDups._parent);
        assertEquals(null, rootContextWithDups._child);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        JsonWriteContext childArrayContext = parentContext.createChildArrayContext();
        JsonWriteContext childArrayContextReset = parentContext.createChildArrayContext();

        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(parentContext, childArrayContext.getParent());
        assertNull(childArrayContext.getDupDetector());
        assertEquals(-1, childArrayContext.getCurrentIndex());
        assertEquals(null, childArrayContext.getCurrentName());
        assertEquals(null, childArrayContext.getCurrentValue());
        assertEquals(childArrayContext, parentContext._child);
        assertEquals(null, parentContext._child);

        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContextReset.getType());
        assertEquals(parentContext, childArrayContextReset.getParent());
        assertNull(childArrayContextReset.getDupDetector());
        assertEquals(-1, childArrayContextReset.getCurrentIndex());
        assertEquals(null, childArrayContextReset.getCurrentName());
        assertEquals(null, childArrayContextReset.getCurrentValue());
        assertEquals(childArrayContextReset, parentContext._child);
        assertEquals(childArrayContextReset, parentContext._child);
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        JsonWriteContext childObjectContext = parentContext.createChildObjectContext();
        JsonWriteContext childObjectContextReset = parentContext.createChildObjectContext();

        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(parentContext, childObjectContext.getParent());
        assertNull(childObjectContext.getDupDetector());
        assertEquals(-1, childObjectContext.getCurrentIndex());
        assertEquals(null, childObjectContext.getCurrentName());
        assertEquals(null, childObjectContext.getCurrentValue());
        assertEquals(childObjectContext, parentContext._child);
        assertEquals(null, parentContext._child);

        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContextReset.getType());
        assertEquals(parentContext, childObjectContextReset.getParent());
        assertNull(childObjectContextReset.getDupDetector());
        assertEquals(-1, childObjectContextReset.getCurrentIndex());
        assertEquals(null, childObjectContextReset.getCurrentName());
        assertEquals(null, childObjectContextReset.getCurrentValue());
        assertEquals(childObjectContextReset, parentContext._child);
        assertEquals(childObjectContextReset, parentContext._child);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithExistingName() throws JsonProcessingException {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector() {
            private final String[] names = {"name1", "name2"};
            private int index = 0;

            @Override
            public boolean isDup(String name) {
                return Arrays.equals(name, names[index]);
            }

            @Override
            public DupDetector child() {
                return this;
            }

            @Override
            public void reset() {
                index++;
            }
        });

        parentContext.writeFieldName("name1");
        parentContext.writeFieldName("name1");
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext writeContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        writeContext.writeFieldName("name1");
        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, writeContext.getStatus());

        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, writeContext.getStatus());

        writeContext.setCurrentValue("value1");
        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, writeContext.getStatus());

        writeContext.reset(JsonWriteContext.TYPE_ARRAY);
        writeContext.writeFieldName("name2");
        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, writeContext.getStatus());

        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, writeContext.getStatus());

        writeContext.setCurrentValue("value2");
        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, writeContext.getStatus());

        writeContext.reset(JsonWriteContext.TYPE_OBJECT);
        writeContext.writeFieldName("name3");
        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, writeContext.getStatus());

        writeContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, writeContext.getStatus());
    }
}

class NoOpDupDetector implements DupDetector {
    @Override public boolean isDup(String name) { return false; }

    @Override public DupDetector child() { return null; }

    @Override public void reset() { /* no-op */ }
}