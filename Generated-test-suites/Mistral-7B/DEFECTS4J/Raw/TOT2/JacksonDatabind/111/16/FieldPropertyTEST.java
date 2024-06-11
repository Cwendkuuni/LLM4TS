import org.junit.Test;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);

        new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        verify(propDef).getName();
        verify(type).getRawClass();
        verify(typeDeser).getTypeIdResolver();
        verify(contextAnnotations).getSerializationConfig();
        verify(field).getAnnotated();
        verify(field).getName();
        verify(field).getAnnotated().getClass();
    }
}