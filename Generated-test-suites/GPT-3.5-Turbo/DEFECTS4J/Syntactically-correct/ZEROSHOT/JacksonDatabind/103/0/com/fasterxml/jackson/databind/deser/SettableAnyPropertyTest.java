import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        JsonDeserializer<Object> typeDeser = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = null;
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        // assert something
    }

    @Test
    public void testDeserialize() throws Exception {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        JsonDeserializer<Object> typeDeser = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

        JsonParser p = null;
        DeserializationContext ctxt = null;
        settableAnyProperty.deserialize(p, ctxt);
        // assert something
    }

    @Test
    public void testSet() throws Exception {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        JsonDeserializer<Object> typeDeser = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

        Object instance = null;
        Object propName = null;
        Object value = null;
        settableAnyProperty.set(instance, propName, value);
        // assert something
    }

    @Test
    public void testThrowAsIOE() throws Exception {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        JsonDeserializer<Object> typeDeser = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

        Exception e = null;
        Object propName = null;
        Object value = null;
        try {
            settableAnyProperty.throwAsIOE(e, propName, value);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof JsonMappingException);
            // assert something
        }
    }

    @Test
    public void testAnySetterReferring() throws Exception {
        SettableAnyProperty parent = null;
        UnresolvedForwardReference reference = null;
        Class<?> type = null;
        Object instance = null;
        String propName = null;
        SettableAnyProperty.AnySetterReferring anySetterReferring = new SettableAnyProperty.AnySetterReferring(parent, reference, type, instance, propName);

        Object id = null;
        Object value = null;
        try {
            anySetterReferring.handleResolvedForwardReference(id, value);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof IllegalArgumentException);
            // assert something
        }
    }
}