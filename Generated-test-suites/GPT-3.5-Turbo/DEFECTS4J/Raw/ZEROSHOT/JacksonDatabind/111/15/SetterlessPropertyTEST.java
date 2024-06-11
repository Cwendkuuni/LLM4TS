import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(property);
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty src = new SetterlessProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        SetterlessProperty property = src.withNullProvider(nva);
        assertNotNull(property);
    }

    @Test
    public void testWithNullValueDeserializer() {
        SetterlessProperty src = new SetterlessProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SetterlessProperty property = src.withValueDeserializer(deser);
        assertNotNull(property);
    }

    @Test
    public void testWithPropertyName() {
        SetterlessProperty src = new SetterlessProperty();
        PropertyName newName = new PropertyName();
        SetterlessProperty property = src.withName(newName);
        assertNotNull(property);
    }

    @Test
    public void testDeserializeAndSet() {
        SetterlessProperty property = new SetterlessProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            assertEquals(instance, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        SetterlessProperty property = new SetterlessProperty();
        try {
            property.set(null, null);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('null')", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty();
        try {
            Object instance = new Object();
            Object value = new Object();
            Object result = property.setAndReturn(instance, value);
            assertEquals(instance, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}