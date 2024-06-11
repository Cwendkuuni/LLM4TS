import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public class InnerClassPropertyTest {

    @Test
    public void testDeserializeAndSet_NullToken_ShouldSetNullValue() {
        // arrange
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        // act
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // assert
        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_ValueTokenAndNoValueTypeDeserializer_ShouldSetNewInstanceAndDeserialize() {
        // arrange
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        // act
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // assert
        assertNotNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_ValueTokenAndValueTypeDeserializer_ShouldSetNullValue() {
        // arrange
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        // act
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // assert
        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeSetAndReturn_ShouldSetAndReturnDeserializedValue() {
        // arrange
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        Object value = new Object();
        // act
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        // assert
        assertEquals(value, result);
    }

    @Test
    public void testSet_ShouldSetPropertyValue() {
        // arrange
        SettableBeanProperty delegate = new SettableBeanProperty(null, null, null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Object instance = new Object();
        Object value = new Object();
        // act
        innerClassProperty.set(instance, value);
        // assert
        assertEquals(value, innerClassProperty.get(instance));
    }

    @Test
    public void testSetAndReturn_ShouldSetAndReturnPropertyValue() {
        // arrange
        SettableBeanProperty delegate = new SettableBeanProperty(null, null, null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Object instance = new Object();
        Object value = new Object();
        // act
        Object result = innerClassProperty.setAndReturn(instance, value);
        // assert
        assertEquals(value, innerClassProperty.get(instance));
        assertEquals(value, result);
    }

    @Test
    public void testReadResolve_ShouldReturnNewInnerClassPropertyInstance() {
        // arrange
        InnerClassProperty src = new InnerClassProperty(null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, null);
        // act
        Object result = innerClassProperty.readResolve();
        // assert
        assertNotSame(innerClassProperty, result);
        assertTrue(result instanceof InnerClassProperty);
    }

    @Test
    public void testWriteReplace_WithNoAnnotatedConstructor_ShouldReturnNewInnerClassPropertyInstance() {
        // arrange
        InnerClassProperty src = new InnerClassProperty(null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, null);
        // act
        Object result = innerClassProperty.writeReplace();
        // assert
        assertNotSame(innerClassProperty, result);
        assertTrue(result instanceof InnerClassProperty);
    }

}