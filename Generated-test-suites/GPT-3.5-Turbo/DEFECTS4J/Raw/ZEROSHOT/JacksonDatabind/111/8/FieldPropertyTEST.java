import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertNotNull(fieldProperty);
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        NullValueProvider nva = mock(NullValueProvider.class);
        FieldProperty newFieldProperty = fieldProperty.withNullProvider(nva);

        assertNotEquals(fieldProperty, newFieldProperty);
        assertEquals(fieldProperty.getPropertyDefinition(), newFieldProperty.getPropertyDefinition());
        assertEquals(fieldProperty.getType(), newFieldProperty.getType());
        assertEquals(fieldProperty.getTypeDeserializer(), newFieldProperty.getTypeDeserializer());
        assertEquals(fieldProperty.getContextAnnotations(), newFieldProperty.getContextAnnotations());
        assertEquals(fieldProperty.getMember(), newFieldProperty.getMember());
        assertEquals(nva, newFieldProperty.getNullProvider());
    }

    // Add more test methods to cover all methods in the FieldProperty class

}