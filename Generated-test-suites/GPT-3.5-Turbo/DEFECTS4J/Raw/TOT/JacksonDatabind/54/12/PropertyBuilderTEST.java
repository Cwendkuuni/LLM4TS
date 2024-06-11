import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDescription;

    @Before
    public void setUp() {
        config = new SerializationConfig();
        beanDescription = new BeanDescription(BeanPropertiesTestClass.class, Annotations.emptyAnnotations(), null);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(beanDescription, null, null, null);
        JavaType declaredType = JavaType.construct(BeanPropertiesTestClass.class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationTypeWithStaticTyping() throws Exception {
        Annotated a = null;
        boolean useStaticTyping = true;
        JavaType declaredType = JavaType.construct(BeanPropertiesTestClass.class);
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        Assert.assertEquals(declaredType, serializationType);
    }

    @Test
    public void testFindSerializationTypeWithoutStaticTyping() throws Exception {
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = JavaType.construct(BeanPropertiesTestClass.class);
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        Assert.assertNull(serializationType);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String name = "testProperty";
        AnnotatedMember member = null;
        JavaType type = JavaType.construct(String.class);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultValueForPrimitiveType() throws Exception {
        JavaType type = JavaType.construct(int.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValueForWrapperType() throws Exception {
        JavaType type = JavaType.construct(Integer.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValueForStringType() throws Exception {
        JavaType type = JavaType.construct(String.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultValueForContainerType() throws Exception {
        JavaType type = config.getTypeFactory().constructCollectionType(List.class, BeanPropertiesTestClass.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValueForReferenceType() throws Exception {
        JavaType type = JavaType.construct(BeanPropertiesTestClass.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testThrowWrapped() throws Exception {
        Exception exception = new Exception("Test exception");
        String propName = "testProperty";
        Object defaultBean = new BeanPropertiesTestClass();
        
        try {
            propertyBuilder._throwWrapped(exception, propName, defaultBean);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            Assert.assertEquals("Failed to get property 'testProperty' of default BeanPropertiesTestClass instance", e.getMessage());
        }
    }
}

class BeanPropertiesTestClass {
    private String testProperty;

    public String getTestProperty() {
        return testProperty;
    }

    public void setTestProperty(String testProperty) {
        this.testProperty = testProperty;
    }
}