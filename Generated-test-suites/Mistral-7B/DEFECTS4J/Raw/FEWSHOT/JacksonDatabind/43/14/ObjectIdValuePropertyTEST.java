package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructTestObjectIdReader;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.Testbase;

public class ObjectIdValuePropertyTest extends Testbase {

    private ObjectIdReader _objectIdReader;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        _objectIdReader = constructTestObjectIdReader();
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = createParser("{\"id\": 123}");
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        Object target = new Object() {};

        property.deserializeAndSet(p, new TestContext(), target);

        assertEquals(123L, p.getValueAsLong());
        assertSame(target, property.getBean());
    }

    @Test
    public void testDeserializeAndSetNullId() throws Exception {
        JsonParser p = createParser("{\"id\": null}");
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        Object target = new Object() {};

        property.deserializeAndSet(p, new TestContext(), target);

        assertNull(p.getValueAsLong());
        assertNull(property.getBean());
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws Exception {
        JsonParser p = createParser("{\"id\": null}");
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));

        Object result = property.deserializeSetAndReturn(p, new TestContext(), new Object() {});

        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = createParser("{\"id\": 123}");
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));

        Object result = property.deserializeSetAndReturn(p, new TestContext(), new Object() {});

        assertEquals(123L, p.getValueAsLong());
        assertSame(result, property.getBean());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        Object target = new Object() {};
        Object id = new Object() {};

        Object result = property.setAndReturn(target, id);

        assertSame(target, property.getBean());
        assertSame(id, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnOnPropertyWithNoIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        Object target = new Object() {};

        property.setAndReturn(target, new Object() {});
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        JsonParser p = createParser("{\"id\": 123}");
        Object target = new Object() {};

        Object id = property.deserializeSetAndReturn(p, new TestContext(), target);
        ReadableObjectId roid = property.findObjectId(id, new ObjectIdReaderForString(), new ObjectIdGenerator(), null);

        assertSame(id, roid.getId());
        assertSame(target, roid.getBindValue());
    }

    @Test
    public void testSetIdProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.idProperty).thenReturn(new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null)));
        property.setIdProperty(idReader);

        assertEquals(idReader, property.getIdReader());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetIdPropertyNull() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, createPropertyMetadata(null));
        property.setIdProperty(null);
    }
}