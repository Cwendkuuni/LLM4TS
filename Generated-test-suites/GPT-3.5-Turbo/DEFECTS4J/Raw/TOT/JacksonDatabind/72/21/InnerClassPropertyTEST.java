
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);

        assertNotNull(innerClassProperty);
    }

    @Test
    public void testWithAnnotation() {
        SettableBeanProperty delegate = new SettableBeanProperty();

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty._annotated = new AnnotatedConstructor(null, null, null, null);

        Annotation annotation = innerClassProperty.getAnnotation(Annotation.class);

        assertNotNull(annotation);
    }

    @Test
    public void testDeserializeAndSet_NullValue() {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object bean = new Object();

        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        innerClassProperty._valueDeserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext context) {
                return null;
            }
        };

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_WithValueTypeDeserializer() {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object bean = new Object();

        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        innerClassProperty._valueDeserializer = new JsonDeserializer() {
            @Override
            public Object deserializeWithType(JsonParser jsonParser, DeserializationContext context, ValueTypeDeserializer typeDeserializer) {
                return "testValue";
            }
        };

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        assertEquals("testValue", innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_WithUsualCase() {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object bean = new Object();
        Object deserializedValue = new Object();

        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        innerClassProperty._valueDeserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext context) {
                return deserializedValue;
            }
        };
        innerClassProperty._creator = new Constructor() {
            @Override
            public Object newInstance(Object bean) {
                return new Object();
            }
        };

        innerClassProperty.deserializeAndSet(jsonParser, context, bean);

        assertSame(deserializedValue, innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        innerClassProperty._valueDeserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext context) {
                return "testValue";
            }
        };

        Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, context, instance);

        assertEquals("testValue", innerClassProperty.get(instance));
        assertEquals("testValue", result);
    }

    @Test
    public void testSet() {
        InnerClassProperty delegate = new InnerClassProperty(null, null);
        Object instance = new Object();
        Object value = new Object();

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);

        innerClassProperty.set(instance, value);

        assertSame(value, delegate.get(instance));
    }

    @Test
    public void testSetAndReturn() {
        InnerClassProperty delegate = new InnerClassProperty(null, null);
        Object instance = new Object();
        Object value = new Object();

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);

        Object result = innerClassProperty.setAndReturn(instance, value);

        assertSame(value, delegate.get(instance));
        assertSame(value, result);
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty src = new InnerClassProperty(null, null);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);

        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

        assertEquals(innerClassProperty, innerClassProperty.readResolve());
    }

    @Test
    public void testWriteReplace_WithAnnotatedConstructor() {
        InnerClassProperty src = new InnerClassProperty(null, null);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);

        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

        assertEquals(innerClassProperty, innerClassProperty.writeReplace());
    }

    @Test
    public void testWriteReplace_WithoutAnnotatedConstructor() {
        InnerClassProperty src = new InnerClassProperty(null, null);

        InnerClassProperty innerClassProperty = new InnerClassProperty(src, null);

        assertNotEquals(innerClassProperty, innerClassProperty.writeReplace());
    }

}


