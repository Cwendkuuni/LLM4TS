import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations actualAnnotations = propertyBuilder.getClassAnnotations();
        Annotations expectedAnnotations = new Annotations();
        assertTrue(actualAnnotations.getClassAnnotations().length > 0);
        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new SimpleType(String.class);
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        Boolean defaultUseStaticTyping = true;
        JavaType serializationType = propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType);
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, am, null, declaredType, ser, typeSer, serializationType, false, null);
        assertEquals(expectedWriter, actualWriter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithInvalidSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new SimpleType(String.class);
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        Boolean defaultUseStaticTyping = true;
        JavaType serializationType = propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType);
        prov.reportBadPropertyDefinition(beanDesc, propDef, "invalid serialization type");
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = new Annotated();
        JavaType declaredType = new SimpleType(String.class);
        boolean useStaticTyping = true;
        JavaType actualType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertEquals(declaredType, actualType);
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object actualBean = propertyBuilder.getDefaultBean();
        Object expectedBean = new Object();
        assertEquals(expectedBean, actualBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "name";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new SimpleType(String.class);
        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Object expectedDefaultValue = "";
        assertEquals(expectedDefaultValue, actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        JavaType type = new SimpleType(Integer.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        Object expectedDefaultValue = 0;
        assertEquals(expectedDefaultValue, actualDefaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new Exception("Test Exception");
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object actualWrapped = propertyBuilder._throwWrapped(e, "testProperty", new Object());
        throw new AssertionError("Thrown exception not expected");
    }
}