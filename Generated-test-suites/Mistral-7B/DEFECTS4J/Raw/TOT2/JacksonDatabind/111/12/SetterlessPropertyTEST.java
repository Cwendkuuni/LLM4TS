import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SetterlessPropertyTest {

    @Test
    void testConstructor() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        Assertions.assertNotNull(setterlessProperty);
        Assertions.assertEquals(propDef, setterlessProperty.getPropDefinition());
        Assertions.assertEquals(type, setterlessProperty.getType());
        Assertions.assertEquals(typeDeser, setterlessProperty.getTypeDeser());
        Assertions.assertEquals(contextAnnotations, setterlessProperty.getContextAnnotations());
        Assertions.assertEquals(method, setterlessProperty.getAnnotated());
    }
}