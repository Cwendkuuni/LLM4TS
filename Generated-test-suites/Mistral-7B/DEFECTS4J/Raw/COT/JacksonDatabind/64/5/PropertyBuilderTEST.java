package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;
    private PropertyBuilder _propertyBuilder;

    @Before
    public void setup() {
        _config = new SerializationConfig();
        _beanDesc = new BeanDescription(Object.class);
        _annotationIntrospector = new StandardAnnotationIntrospector();
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    @Test
    public void testConstructor() {
        assertNotNull(_propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        assertEquals(0, _propertyBuilder.getClassAnnotations().size());
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullSerializer() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullSerializerType() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder.buildWriter(new Provider(), null, null, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullBeanPropertyDefinition() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder.buildWriter(new Provider(), null, null, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullDeclaredType() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder.buildWriter(new Provider(), null, null, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeNullAnnotated() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder.findSerializationType(null, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeInvalidSuperType() {
        Object a = new Object() {
            @JsonSerialize(using = MySerializer.class, as = MyClass.class)
            public int getValue() {
                return 1;
            }
        };
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder.findSerializationType(new AnnotatedMember(a, MyClass.class), false, null);
    }

    @Test
    public void testGetPropertyDefaultValueStringProperty() {
        String defaultBean = new String("defaultValue");
        assertEquals("", _propertyBuilder.getPropertyDefaultValue("propertyName", null, String.class));
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder._defaultBean = defaultBean;
        assertEquals("defaultValue", _propertyBuilder.getPropertyDefaultValue("propertyName", null, String.class));
    }

    @Test
    public void testGetPropertyDefaultValuePrimitiveInt() {
        assertEquals(0, _propertyBuilder.getPropertyDefaultValue("propertyName", null, Integer.TYPE));
    }

    @Test
    public void testGetPropertyDefaultValueNull() {
        assertNull(_propertyBuilder.getPropertyDefaultValue("propertyName", null, Object.class));
    }

    @Test
    public void testGetPropertyDefaultValueReferenceType() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder._defaultBean = new Object();
        assertEquals(_propertyBuilder._defaultBean, _propertyBuilder.getPropertyDefaultValue("propertyName", null, Object.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueInvalidPropertyName() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder._defaultBean = new Object();
        _propertyBuilder.getPropertyDefaultValue("invalidPropertyName", null, Object.class);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPropertyDefaultValueNullMember() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder._defaultBean = new Object();
        _propertyBuilder.getPropertyDefaultValue(null, null, Object.class);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPropertyDefaultValueNullType() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder._defaultBean = new Object();
        _propertyBuilder.getPropertyDefaultValue("propertyName", null, null);
    }

    @Test
    public void testGetDefaultBeanNull() {
        assertNull(_propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetDefaultBeanInstance() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _propertyBuilder._defaultBean = new Object();
        assertEquals(_propertyBuilder._defaultBean, _propertyBuilder.getDefaultBean());
    }
}

class MySerializer extends StdSerializer<MyClass> {
    public MySerializer() {
        this(null);
    }

    public MySerializer(Class<MyClass> t) {
        super(t);
    }
}

class MyClass {
    @JsonSerialize(using = MySerializer.class)
    public int getValue() {
        return 1;
    }
}