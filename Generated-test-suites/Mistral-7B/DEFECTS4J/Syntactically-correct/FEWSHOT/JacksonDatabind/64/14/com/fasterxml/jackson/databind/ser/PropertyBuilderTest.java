package com.fasterxml.jackson.databind.ser;

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
    public void testFindSerializationType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated a = new Annotated() {{
            setClass(Integer.class);
            setName("test");
            JsonSerialize jsonSerializeAnnotation = new JsonSerialize() {{
                setUseJsonGetter(true);
                setTypeSerializer(new NumberSerializer());
            }};
            setAnnotation(JsonSerialize.class, jsonSerializeAnnotation);
        }};
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Integer[].class);
        JavaType actualType = propertyBuilder.findSerializationType(a, true, declaredType);
        assertTrue(actualType.equals(declaredType.getComponentType()));

        a.setAnnotation(JsonSerialize.class, null);
        actualType = propertyBuilder.findSerializationType(a, true, declaredType);
        assertNull(actualType);

        a.setAnnotation(JsonSerialize.class, jsonSerializeAnnotation);
        actualType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(actualType);

        JsonSerializer serializer = new NumberSerializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        JsonSerialize jsonSerializeAnnotationWithTypeSerializer = new JsonSerialize() {{
            setUseJsonGetter(true);
            setTypeSerializer(typeSerializer);
        }};
        a.setAnnotation(JsonSerialize.class, jsonSerializeAnnotationWithTypeSerializer);
        actualType = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(NumberSerializer.class, actualType.getRawClass());
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanDescription beanDescription = new BeanDescription(Object.class);
        AnnotatedMember member = new AnnotatedMember() {{
            setMemberName("test");
            setMember(new FieldInfo() {{
                setName("test");
                setType(Integer.TYPE);
            }});
            setBeanDescription(beanDescription);
        }};
        JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);
        Object value = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertEquals(0, value);

        member.setMember(new FieldInfo() {{
            setName("test");
            setType(Integer.class);
            setValue(1);
        }});
        value = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertEquals(1, value);

        member.setMemberName("test2");
        value = propertyBuilder.getPropertyDefaultValue("test2", member, type);
        assertNull(value);

        member.setMemberName("test");
        member.setValue(null);
        value = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertNull(value);

        member.setMemberName("test");
        member.setValue(new Integer[0]);
        value = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertEquals(JsonInclude.Include.NON_EMPTY, value);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeArrayInt = TypeFactory.defaultInstance().constructType(Integer[].class);
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType javaTypeClass = TypeFactory.defaultInstance().constructType(String.class);

        Object valueInt = propertyBuilder.getDefaultValue(javaTypeInt);
        assertEquals(0, valueInt);

        Object valueString = propertyBuilder.getDefaultValue(javaTypeString);
        assertEquals("", valueString);

        Object valueArrayInt = propertyBuilder.getDefaultValue(javaTypeArrayInt);
        assertEquals(JsonInclude.Include.NON_EMPTY, valueArrayInt);

        Object valueMap = propertyBuilder.getDefaultValue(javaTypeMap);
        assertEquals(JsonInclude.Include.NON_EMPTY, valueMap);

        Object valueClass = propertyBuilder.getDefaultValue(javaTypeClass);
        assertNull(valueClass);
    }
}