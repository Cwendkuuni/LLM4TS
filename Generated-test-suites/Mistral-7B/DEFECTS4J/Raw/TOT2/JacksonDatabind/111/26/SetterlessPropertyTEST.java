import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.bean.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        verify(propDef).getName();
        verify(type).getType();
        verify(typeDeser).findValueDeserializer(eq(type), eq(contextAnnotations));
        verify(contextAnnotations).findAnnotation(AnnotationIntrospector.findSerializationConfig(method, null).getAnnotationIntrospector());
        verify(method).getAnnotations();
        verify(method).getAnnotated();
    }
}