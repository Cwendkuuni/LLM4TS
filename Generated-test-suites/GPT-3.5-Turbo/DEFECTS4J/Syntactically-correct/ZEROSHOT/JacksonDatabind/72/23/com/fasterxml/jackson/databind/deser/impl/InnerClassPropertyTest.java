import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InnerClassPropertyTest {

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        Object bean = null;
        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        Object bean = null;
        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        Object bean = null;
        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        Object instance = null;
        innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testSet() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        Object instance = null;
        Object value = null;
        innerClassProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        Object instance = null;
        Object value = null;
        innerClassProperty.setAndReturn(instance, value);
    }
}