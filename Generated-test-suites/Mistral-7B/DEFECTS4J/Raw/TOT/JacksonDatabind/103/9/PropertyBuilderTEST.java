package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private SerializationConfig config;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
    }

    @Test
    public void testEmptyStringProperty() throws IOException {
        BeanDescription beanDesc = mapper.findBeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("value");
        JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<String> ser = new StdSerializer<String>(String.class) {};
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, null, null, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);
    }

    @Test
    public void testStringPropertyWithDefaultValue() throws IOException, IllegalAccessException {
        class MyClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        MyClass defaultInstance = new MyClass();
        Field nameField = MyClass.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(defaultInstance, "defaultValue");

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        JsonSerializer<MyClass> ser = new StdSerializer<MyClass>(MyClass.class) {};
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertEquals("defaultValue", valueToSuppress);
    }

    @Test
    public void testStringPropertyWithNullValue() throws IOException {
        class MyClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        JsonSerializer<MyClass> ser = new StdSerializer<MyClass>(MyClass.class) {};
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertNull(valueToSuppress);

        // Test suppression of null values
        config.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);

        assertTrue(writer.isSuppressNulls());
    }

    @Test
    public void testStringPropertyWithEmptyListValue() throws IOException {
        class MyClass {
            private String name;
            private String[] list;

            public String getName() {
                return name;
            }

            public void setName(String name) {this.name = name;}

            public String[] getList() {
                return list;
            }

            public void setList(String[] list) {this.list = list;}
        }

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("list");
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        JsonSerializer<MyClass> ser = new StdSerializer<MyClass>(MyClass.class) {};
        TypeSerializer typeSer = mapper.getTypeSerializer();
        AnnotatedMember am = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertNotNull(valueToSuppress);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);
    }

    @Test
    public void testStringPropertyWithNonEmptyListValue() throws IOException {
        class MyClass {
            private String name;
            private String[] list;

            public String getName() {
                return name;
            }

            public void setName(String name) {this.name = name;}

            public String[] getList() {
                return list;
            }

            public void setList(String[] list) {this.list = list;}
        }

        MyClass instance = new MyClass();
        instance.setName("name");
        instance.setList(new String[]{"value1", "value2"});

        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("list");
        JavaType declaredType = mapper.getTypeFactory().constructType(MyClass.class);
        JsonSerializer<MyClass> ser = new StdSerializer<MyClass>(MyClass.class) {};
        TypeSerializer typeSer = mapper.getTypeSerializer();
        AnnotatedMember am = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertNotNull(valueToSuppress);
        assertNotEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);
        assertArrayEquals(new String[]{"value1", "value2"}, (String[])valueToSuppress);
    }

    @Test
    public void testStringPropertyWithNonDefaultValueIncludingDefault() throws IOException {
        class MyClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {this.name = name;}
        }

        MyClass defaultInstance = new MyClass();
        Field nameField = MyClass.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(defaultInstance, "defaultValue");

        class TestClass {
            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            private MyClass myClass;

            public MyClass getMyClass() {
                return myClass;
            }

            public void setMyClass(MyClass myClass) {
                this.myClass = myClass;
            }
        }

        TestClass testInstance = new TestClass();
        testInstance.setMyClass(defaultInstance);

        BeanDescription beanDesc = mapper.findBeanDescription(TestClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();

        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("myClass");
        JavaType declaredType = mapper.getTypeFactory().constructType(TestClass.class);
        JsonSerializer<TestClass> ser = new StdSerializer<TestClass>(TestClass.class) {};
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, false);

        Object valueToSuppress = writer.getValueToSuppress();
        assertNotNull(valueToSuppress);
        assertEquals(defaultInstance, valueToSuppress);
    }
}