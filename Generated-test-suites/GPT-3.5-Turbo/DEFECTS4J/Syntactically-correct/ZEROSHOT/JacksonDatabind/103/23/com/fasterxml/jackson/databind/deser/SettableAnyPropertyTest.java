import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    private SettableAnyProperty property;

    @Before
    public void setUp() {
        BeanProperty beanProperty = null;
        AnnotatedMember annotatedMember = null;
        JavaType javaType = null;
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer typeDeserializer = null;
        property = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, typeDeserializer);
    }

    @Test
    public void testGetProperty() {
        BeanProperty expected = null;
        BeanProperty actual = property.getProperty();
        assertEquals(expected, actual);
    }

    @Test
    public void testHasValueDeserializer() {
        boolean expected = false;
        boolean actual = property.hasValueDeserializer();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetType() {
        JavaType expected = null;
        JavaType actual = property.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = null;
        try {
            property.deserializeAndSet(p, ctxt, instance, propName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserialize() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        try {
            Object expected = null;
            Object actual = property.deserialize(p, ctxt);
            assertEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        Object instance = null;
        Object propName = null;
        Object value = null;
        try {
            property.set(instance, propName, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}