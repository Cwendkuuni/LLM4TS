package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Unit tests for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    @Test
    public void testDefaultConstruction() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertEquals(JsonInclude.Include.ALWAYS, propertyBuilder._defaultInclusion.getValueInclusion());
        assertFalse(propertyBuilder._useRealPropertyDefaults);
    }

    @Test
    public void testSerializationTypeDetermination() throws JsonMappingException {
        Object obj = new Object() {
            @JsonSerialize(using = MyCustomSerializer.class, as = String.class)
            public String getFoo() {
                return "foo";
            }
        };

        AnnotatedMember member = ReflectionTestUtils.findMethod(obj, "getFoo");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(obj.getClass()));
        JavaType serializationType = propertyBuilder.findSerializationType(member, false, null);
        assertEquals(String.class, serializationType.getRawClass());
        assertTrue(serializationType.isWrapperType());
        assertEquals(MyCustomSerializer.class, serializationType.getRawClass().getDeclaredMethod("create").getAnnotation(JsonSerialize.class).withSerializer(MyCustomSerializer.class));
    }

    @Test
    public void testDefaultValueHandling() throws JsonMappingException {
        class MyBean {
            @JsonSerialize(including = JsonSerialize.Inclusion.NON_DEFAULT)
            String myField;
        }

        AnnotatedMember member = ReflectionTestUtils.findField(MyBean.class, "myField");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(MyBean.class, defaultBean.getClass());
        try {
            String value = (String) member.getValue(defaultBean);
            fail("Expected null as default value for property 'myField'");
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultBeanNull() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(Object.class));
        propertyBuilder.getDefaultBean();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultBeanNoDefaultConstructor() throws InstantiationException, IllegalAccessException {
        class MyBean {
            private MyBean() {
            }
        }

        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));
        propertyBuilder.getDefaultBean();
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(Number.class));
        Object defaultValue = propertyBuilder.getDefaultValue(Number.class);
        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        class MyBean {
            int myField;
        }

        AnnotatedMember member = ReflectionTestUtils.findField(MyBean.class, "myField");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("myField", member, Number.class);
        assertEquals(0, defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueNull() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(Object.class));
        propertyBuilder.getPropertyDefaultValue("myField", null, Number.class);
    }

    @Test
    public void testGetPropertyDefaultValueNoDefaultValue() throws InstantiationException, IllegalAccessException {
        class MyBean {
            MyBean() {
            }
        }

        AnnotatedMember member = ReflectionTestUtils.findField(MyBean.class, "myField");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));
        propertyBuilder.getPropertyDefaultValue("myField", member, Number.class);
        fail("Expected IllegalArgumentException as no default value is defined for property 'myField'");
    }

    @Test
    public void testGetPropertyDefaultValueForCollection() {
        class MyBean {
            List<String> myList;
        }

        AnnotatedMember member = ReflectionTestUtils.findField(MyBean.class, "myList");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("myList", member, Array.class);
        assertTrue(defaultValue instanceof Object[]);
        assertEquals(0, ((Object[]) defaultValue).length);
    }

    @Test
    public void testGetPropertyDefaultValueForArray() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(int[].class));
        Object defaultValue = propertyBuilder.getDefaultValue(int[].class);
        assertArrayEquals(new int[0], (int[]) defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueForArrayCustomSerializer() {
        class MyBean {
            @JsonSerialize(using = MyCustomSerializer.class)
            int[] myArray;
        }

        AnnotatedMember member = ReflectionTestUtils.findField(MyBean.class, "myArray");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(MyBean.class));
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("myArray", member, int[].class);
        assertEquals(MyCustomSerializer.MARKER, defaultValue);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        class MyBean {
            @JsonProperty("foo")
            String foo;
            @JsonProperty("bar")
            int bar;
        }

        SerializerProvider serializerProvider = new StandardSerializerProvider();
        SerializationConfig config = new SerializationConfig(serializerProvider, new AnnotationIntrospector());
        BeanDescription beanDescription = new BeanDescription(MyBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        JavaType declaredType = TypeFactory.type(MyBean.class);
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("foo");
        AnnotatedMember annotatedMember = propDef.getAccessor();
        SerializerProvider prov = new StandardSerializerProvider();
        JsonSerializer<String> ser = new StringSerializer();
        TypeSerializer typeSer = null;
        JsonSerializer<?> contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertEquals("foo", writer.getName());
        assertEquals(String.class, writer.getType().getRawClass());
        assertEquals("", writer.getValueToSuppress());
        assertFalse(writer.suppressNulls());

        propDef = beanDescription.findPropertyDefinition("bar");
        annotatedMember = propDef.getAccessor();
        ser = new IntSerializer();
        typeSer = new IntTypeSerializer();
        contentTypeSer = new IntContentSerializer();
        am = new AnnotatedMemberImpl(annotatedMember) {{
            setRawType(int.class);
        }};
        defaultUseStaticTyping = true;
        writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertEquals("bar", writer.getName());
        assertEquals(int.class, writer.getType().getRawClass());
        assertEquals(Integer.MIN_VALUE, writer.getValueToSuppress());
        assertTrue(writer.suppressNulls());
    }
}