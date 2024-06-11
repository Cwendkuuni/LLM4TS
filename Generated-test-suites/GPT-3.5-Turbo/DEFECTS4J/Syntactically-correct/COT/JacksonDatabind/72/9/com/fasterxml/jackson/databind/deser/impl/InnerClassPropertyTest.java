import static org.junit.Assert.*;
import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testDeserAndSet() {
        // Test for JsonToken.VALUE_NULL
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();

        jp.setCurrentToken(JsonToken.VALUE_NULL);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));

        // Test for value typeserializer
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        innerClassProperty = new InnerClassProperty();
        innerClassProperty._valueTypeDeserializer = new ValueTypeDeserializer();
        innerClassProperty._valueDeserializer = valueDeser;
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));

        // Test for catch block
        innerClassProperty._creator = new Constructor<>();
        innerClassProperty._creator.setAccessible(true);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty._delegate = new SettableBeanProperty();

        jp.setCurrentToken(JsonToken.VALUE_NULL);
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        assertNull(innerClassProperty.get(instance));

        innerClassProperty._creator = new Constructor<>();
        innerClassProperty._creator.setAccessible(true);
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        assertNull(innerClassProperty.get(instance));
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty._delegate = new SettableBeanProperty();
        innerClassProperty.set(instance, value);
        assertEquals(value, innerClassProperty.get(instance));
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty._delegate = new SettableBeanProperty();
        assertEquals(value, innerClassProperty.setAndReturn(instance, value));
        assertEquals(value, innerClassProperty.get(instance));
    }
}