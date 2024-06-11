import org.junit.Test;

import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        // Test for the constructor that takes a SettableBeanProperty and a Constructor
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testDeserializationAndSet() {
        // Test for the method deserializeAndSet
        // Case: JsonToken.VALUE_NULL
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object bean = new Object();

        jsonParser.setCurrentToken(JsonToken.VALUE_NULL);
        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        assertNull(innerClassProperty.get(bean));

        // Case: _valueTypeDeserializer != null
        JsonDeserializer<?> valueDeserializer = new JsonDeserializer<>();
        innerClassProperty.setValueDeserializer(valueDeserializer);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        assertEquals(valueDeserializer.getNullValue(deserializationContext), innerClassProperty.get(bean));

        // Case: _creator.newInstance(bean) throws Exception
        Object value = new Object();
        innerClassProperty.setValueDeserializer(null);
        innerClassProperty.set(bean, value);

        jsonParser.setCurrentToken(JsonToken.VALUE_STRING);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        assertEquals(value, innerClassProperty.get(bean));

        // Case: _creator is null
        innerClassProperty._creator = null;

        try {
            innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Test for the method deserializeSetAndReturn
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();

        jsonParser.setCurrentToken(JsonToken.VALUE_NULL);

        assertEquals(value, innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testSetAndReturn() {
        // Test for the method setAndReturn
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();

        assertEquals(value, innerClassProperty.setAndReturn(instance, value));
    }

    @Test
    public void testReadResolve() {
        // Test for the method readResolve
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor();
        innerClassProperty._annotated = annotatedConstructor;

        assertEquals(new InnerClassProperty(innerClassProperty, annotatedConstructor), innerClassProperty.readResolve());
    }

    @Test
    public void testWriteReplace() {
        // Test for the method writeReplace
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor();
        innerClassProperty._annotated = annotatedConstructor;

        assertEquals(innerClassProperty, innerClassProperty.writeReplace());
    }

}