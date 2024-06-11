import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("prop");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotationInstance();
        AnnotatedMethod method = new AnnotatedMethod(SetterlessProperty.class, "testMethod");
        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(setterlessProperty);
    }

    @Test
    public void testCopyConstructor() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition("prop"), new TypeDescriptor.Instance("java.util.List<java.lang.String>"), null, null, null);
        JsonDeserializer<?> deser = new JacksonAnnotationIntDeserializer();
        NullValueProvider nva = null;
        SetterlessProperty setterlessProperty = new SetterlessProperty(src, deser, nva);
        assertEquals(src, setterlessProperty);
    }

    @Test
    public void testRenameConstructor() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition("prop"), new TypeDescriptor.Instance("java.util.List<java.lang.String>"), null, null, null);
        PropertyName newName = new PropertyName("newProp");
        SetterlessProperty setterlessProperty = new SetterlessProperty(src, newName);
        assertEquals(src, setterlessProperty);
        assertEquals(newName, setterlessProperty.getName());
    }
}