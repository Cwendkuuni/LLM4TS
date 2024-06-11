import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;

public class FieldPropertyTest {

    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeserializer = new JsonStringDeserializer();
        Annotations contextAnnotations = new StandardAnnotationInstance();
        AnnotatedField annotatedField = new AnnotatedField(SomeClass.class, "someField", SomeAnnotation.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(SomeClass.class, "someField", javaType, null);
        fieldProperty = new FieldProperty(propDef, javaType, typeDeserializer, contextAnnotations, annotatedField);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fieldProperty);
    }
}