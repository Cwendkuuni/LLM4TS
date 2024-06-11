
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyName;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;

    @Before
    public void setup() {
        propDef = new BeanPropertyDefinition();
        type = new JavaType();
        typeDeser = new TypeDeserializer();
        contextAnnotations = new Annotations();
        method = new AnnotatedMethod();
        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithNullProvider() {
        assertNull(setterlessProperty.withNullProvider(null));
    }

    @Test
    public void testWithSameNullProvider() {
        NullValueProvider nvp = new NullValueProvider();
        assertSame(setterlessProperty.withNullProvider(nvp), setterlessProperty);
    }

    @Test
    public void testWithDifferentNullProvider() {
        NullValueProvider nvp1 = new NullValueProvider();
        NullValueProvider nvp2 = new NullValueProvider();
        assertNotSame(setterlessProperty.withNullProvider(nvp1), setterlessProperty.withNullProvider(nvp2));
    }

    @Test
    public void testWithSameName() {
        PropertyName newName = new PropertyName();
        assertSame(setterlessProperty.withName(newName), setterlessProperty);
    }

    @Test
    public void testWithDifferentName() {
        PropertyName newName1 = new PropertyName();
        PropertyName newName2 = new PropertyName();
        assertNotSame(setterlessProperty.withName(newName1), setterlessProperty.withName(newName2));
    }

    @Test
    public void testWithSameValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer();
        assertSame(setterlessProperty.withValueDeserializer(deser), setterlessProperty);
    }

    @Test
    public void testWithDifferentValueDeserializer() {
        JsonDeserializer<?> deser1 = new JsonDeserializer();
        JsonDeserializer<?> deser2 = new JsonDeserializer();
        assertNotSame(setterlessProperty.withValueDeserializer(deser1), setterlessProperty.withValueDeserializer(deser2));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        assertSame(setterlessProperty.deserializeSetAndReturn(p, ctxt, instance), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        assertSame(setterlessProperty.setAndReturn(instance, value), instance);
    }

}
