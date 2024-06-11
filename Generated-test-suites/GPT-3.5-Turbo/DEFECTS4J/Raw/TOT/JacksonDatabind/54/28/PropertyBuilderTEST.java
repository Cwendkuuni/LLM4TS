import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ToBeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.AnnotationIntrospector.*;
import com.fasterxml.jackson.databind.util.AnnotationIntrospector;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class PropertyBuilderTest {

    @Test
    public void getClassAnnotationsTest() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotations classAnnotations = propertyBuilder.getClassAnnotations();

        assertNotNull(classAnnotations);
   }

    @Test
    public void buildWriterTest() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        try {
            BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType,
                                    ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
                                    
            assertNotNull(bpw);
        } catch (JsonMappingException e) {
            fail("Exception thrown" + e.getMessage());
        }
    }

    @Test
    public void findSerializationTypeTest() {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config);

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

            assertNotNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Exception thrown" + e.getMessage());
        }
    }

    @Test
    public void getDefaultBeanTest() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNull(defaultBean);
    }
    
    @Test
    public void getPropertyDefaultValueTest() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember();
        member.setValue("defaultPropertyValue");
        JavaType type = new JavaType();
        
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNotNull(propertyDefaultValue);
        assertEquals("defaultPropertyValue", propertyDefaultValue);
    }

    @Test
    public void getDefaultValueTest() {
        JavaType numericType = new JavaType(int.class);
        JavaType stringType = new JavaType(String.class);
        JavaType containerType = new JavaType(List.class);
        JavaType referenceType = new JavaType(BeanPropertyWriter.class);
        JavaType objectType = new JavaType(Object.class);

        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object numericDefaultValue = propertyBuilder.getDefaultValue(numericType);
        Object stringDefaultValue = propertyBuilder.getDefaultValue(stringType);
        Object containerDefaultValue = propertyBuilder.getDefaultValue(containerType);
        Object referenceDefaultValue = propertyBuilder.getDefaultValue(referenceType);
        Object objectDefaultValue = propertyBuilder.getDefaultValue(objectType);

        assertEquals(0, numericDefaultValue);
        assertEquals("", stringDefaultValue);
        assertEquals(JsonInclude.Include.NON_EMPTY, containerDefaultValue);
        assertEquals(JsonInclude.Include.NON_EMPTY, referenceDefaultValue);
        assertNull(objectDefaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwWrappedTest() {
        Exception exception = new Exception("Test Exception");
        String propName = "testProperty";
        Object defaultBean = new Object();

        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        propertyBuilder._throwWrapped(exception, propName, defaultBean);
    }
}