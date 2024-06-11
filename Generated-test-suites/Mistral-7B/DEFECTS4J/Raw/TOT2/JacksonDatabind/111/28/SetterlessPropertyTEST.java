import org.junit.Test;
import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

    @Test
    public void testSetterlessPropertyConstruction() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        verify(propDef).getName();
        verify(type).getTypeFromAnnotation(method, JsonTypeDeserializer.Any.class);
        verify(typeDeser).createContextualType(propDef.getType());
        verify(contextAnnotations).findAnnotationValues(method, JsonDeserialize.class);
        verify(method).getAnnotations();
        verify(method).getMethod();
        verifyNoMoreInteractions(propDef, type, typeDeser, contextAnnotations, method);
    }
}