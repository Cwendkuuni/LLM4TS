import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class SettlerlessPropertyTest_Constructor {

    @Test
    public void testConstructor() {
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(setterlessProperty);
    }
}