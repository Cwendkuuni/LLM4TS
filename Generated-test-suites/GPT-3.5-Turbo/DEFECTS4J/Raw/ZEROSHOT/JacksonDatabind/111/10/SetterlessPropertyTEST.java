import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNotNull(property);
        assertEquals(propDef, property.getMetadata());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }

    @Test
    public void testConstructor2() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        SetterlessProperty property1 = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty property2 = new SetterlessProperty(property1, (JsonDeserializer<?>) null, null);

        assertNotNull(property2);
        assertEquals(property1.getMetadata(), property2.getMetadata());
        assertEquals(property1.getType(), property2.getType());
        assertEquals(property1.getTypeDeserializer(), property2.getTypeDeserializer());
        assertEquals(property1.getContextAnnotations(), property2.getContextAnnotations());
        assertEquals(property1.getMember(), property2.getMember());
    }

    @Test
    public void testConstructor3() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        PropertyName newName = Mockito.mock(PropertyName.class);

        SetterlessProperty property1 = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty property2 = new SetterlessProperty(property1, newName);

        assertNotNull(property2);
        assertEquals(newName, property2.getFullName());
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        NullValueProvider nvp = Mockito.mock(NullValueProvider.class);

        SetterlessProperty property1 = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty property2 = (SetterlessProperty) property1.withNullProvider(nvp);

        assertNotNull(property2);
        assertEquals(nvp, property2.getNullProvider());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = Mockito.mock(SetterlessProperty.class);
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = Mockito.mock(SetterlessProperty.class);
        Object instance = new Object();
        Object value = new Object();

        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }
}