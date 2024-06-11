package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonWriteContextTest {

    @Mock
    private JsonWriteContext parent;

    private JsonWriteContext context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new JsonWriteContext(JsonStreamContext.TYPE_ROOT, parent, null);
    }

    @Test
    public void withDupDetector_shouldSetDupDetector() {
        assertNull(context.getDupDetector());

        DupDetector dupDetector = mock(DupDetector.class);

        JsonWriteContext result = context.withDupDetector(dupDetector);

        assertEquals(dupDetector, result.getDupDetector());
    }

    @Test
    public void getCurrentValue_shouldReturnCurrentValue() {
        assertNull(context.getCurrentValue());

        Object currentValue = new Object();
        context.setCurrentValue(currentValue);

        assertEquals(currentValue, context.getCurrentValue());
    }

    @Test
    public void createRootContext_shouldReturnNewInstance() {
        JsonWriteContext result = JsonWriteContext.createRootContext();

        assertNotNull(result);
    }

    @Test
    public void createChildArrayContext_whenChildIsNull_shouldReturnNewInstance() {
        assertNull(context._child);

        JsonWriteContext result = context.createChildArrayContext();

        assertNotNull(result);
        assertNotNull(context._child);
        assertNotSame(context._child, context.createChildArrayContext());
    }

    @Test
    public void createChildArrayContext_whenChildIsNotNull_shouldResetChild() {
        JsonWriteContext child = mock(JsonWriteContext.class);
        context._child = child;

        JsonWriteContext result = context.createChildArrayContext();

        assertSame(child, result);
        verify(child).reset(JsonStreamContext.TYPE_ARRAY);
    }

    @Test
    public void createChildObjectContext_whenChildIsNull_shouldReturnNewInstance() {
        assertNull(context._child);

        JsonWriteContext result = context.createChildObjectContext();

        assertNotNull(result);
        assertNotNull(context._child);
        assertNotSame(context._child, context.createChildObjectContext());
    }

    @Test
    public void createChildObjectContext_whenChildIsNotNull_shouldResetChild() {
        JsonWriteContext child = mock(JsonWriteContext.class);
        context._child = child;

        JsonWriteContext result = context.createChildObjectContext();

        assertSame(child, result);
        verify(child).reset(JsonStreamContext.TYPE_OBJECT);
    }

    @Test
    public void getParent_shouldReturnParent() {
        assertSame(parent, context.getParent());
    }

    @Test
    public void getCurrentName_shouldReturnCurrentName() {
        assertNull(context.getCurrentName());

        String currentName = "name";
        context._currentName = currentName;

        assertEquals(currentName, context.getCurrentName());
    }

    @Test
    public void writeFieldName_whenGotNameIsTrue_shouldReturnExpectValue() {
        context._gotName = true;

        int result = context.writeFieldName("name");

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
    }

    @Test
    public void writeFieldName_whenDupDetectorIsNotNull_shouldCheckDup() {
        DupDetector dupDetector = mock(DupDetector.class);
        context._dups = dupDetector;

        String name = "name";
        context.writeFieldName(name);

        verify(dupDetector).isDup(name);
    }

    @Test(expected = JsonGenerationException.class)
    public void writeFieldName_whenDupDetectorReturnsTrue_shouldThrowException() {
        DupDetector dupDetector = mock(DupDetector.class);
        when(dupDetector.isDup(any())).thenReturn(true);
        context._dups = dupDetector;

        context.writeFieldName("name");
    }

    @Test
    public void writeFieldName_whenIndexIsNegative_shouldReturnOkAsIs() {
        context._index = -1;

        int result = context.writeFieldName("name");

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void writeFieldName_whenIndexIsPositive_shouldReturnOkAfterComma() {
        context._index = 1;

        int result = context.writeFieldName("name");

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void writeValue_whenTypeIsObject_shouldIncrementIndexAndReturnOkAfterColon() {
        context._type = JsonWriteContext.TYPE_OBJECT;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(0, context._index);
    }

    @Test
    public void writeValue_whenTypeIsArray_shouldIncrementIndexAndReturnOkAfterCommaWhenIndexIsNegative() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        context._index = -1;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(0, context._index);
    }

    @Test
    public void writeValue_whenTypeIsArray_shouldIncrementIndexAndReturnOkAfterCommaWhenIndexIsPositive_shouldReturnOkAfterComma() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        context._index = 1;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(2, context._index);
    }

    @Test
    public void writeValue_whenTypeIsRoot_shouldIncrementIndexAndReturnOkAsIsWhenIndexIsZero() {
        context._type = JsonWriteContext.TYPE_ROOT;
        context._index = 0;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(1, context._index);
    }

    @Test
    public void writeValue_whenTypeIsRoot_shouldIncrementIndexAndReturnOkAfterSpaceWhenIndexIsPositive() {
        context._type = JsonWriteContext.TYPE_ROOT;
        context._index = 1;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        assertEquals(2, context._index);
    }

    @Test
    public void appendDesc_whenTypeIsObject_shouldAppendJsonPathWithFieldName() {
        context._type = JsonWriteContext.TYPE_OBJECT;
        context._currentName = "name";

        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);

        assertEquals("{\"name\"}", sb.toString());
    }

    @Test
    public void appendDesc_whenTypeIsObjectAndCurrentNameIsNull_shouldAppendJsonPathWithUnknownFieldName() {
        context._type = JsonWriteContext.TYPE_OBJECT;
        context._currentName = null;

        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);

        assertEquals("{?}", sb.toString());
    }

    @Test
    public void appendDesc_whenTypeIsArray_shouldAppendJsonPathWithArrayIndex() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        context._index = 1;

        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);

        assertEquals("[1]", sb.toString());
    }

    @Test
    public void appendDesc_whenTypeIsRoot_shouldAppendSlash() {
        context._type = JsonWriteContext.TYPE_ROOT;

        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);

        assertEquals("/", sb.toString());
    }

    @Test
    public void toString_shouldReturnJsonPathRepresentation() {
        context._type = JsonWriteContext.TYPE_OBJECT;
        context._currentName = "name";

        String result = context.toString();

        assertEquals("{\"name\"}", result);
    }
}