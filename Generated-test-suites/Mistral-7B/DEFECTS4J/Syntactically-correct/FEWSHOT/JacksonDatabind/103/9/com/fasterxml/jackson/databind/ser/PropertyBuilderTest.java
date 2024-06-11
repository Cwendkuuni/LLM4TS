package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        try {
            JavaType result = pb.findSerializationType(am, false, declaredType);
            assertNull(result);
        } catch (JsonMappingException e) {
            // expected
        }

        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        Mockito.when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        Mockito.when(annotation.getType()).thenReturn(Integer.class);
        try {
            JavaType result = pb.findSerializationType(am, false, declaredType);
            assertEquals(Integer.class, result);
        } catch (JsonMappingException e) {
            // not expected
            fail();
        }

        Mockito.when(annotation.getType()).thenReturn(String[].class);
        try {
            JavaType result = pb.findSerializationType(am, false, declaredType);
            assertEquals(String[].class, result);
        } catch (JsonMappingException e) {
            // not expected
            fail();
        }

        JsonInclude annotation2 = Mockito.mock(JsonInclude.class);
        Mockito.when(am.getAnnotation(JsonInclude.class)).thenReturn(annotation2);
        Mockito.when(annotation2.include()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        try {
            JavaType result = pb.findSerializationType(am, false, declaredType);
            assertNull(result);
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testBuildWriter() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);

        Mockito.when(propDef.getAccessor()).thenReturn(am);

        try {
            BeanPropertyWriter result = pb.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        Mockito.when(am.getRawType()).thenReturn(Integer.class);
        try {
            BeanPropertyWriter result = pb.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        Mockito.when(am.getRawType()).thenReturn(String.class);
        try {
            BeanPropertyWriter result = pb.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            // not expected
            fail();
        }
    }

    @Test
    public void testgetDefaultBean() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        try {
            Object result = pb.getDefaultBean();
            fail();
        } catch (Exception e) {
            // expected
        }

        PropertyBuilder pb2 = new PropertyBuilder(null, null) {
            @Override
            Object getDefaultBean() {
                return new Object();
            }
        };
        Object result = pb2.getDefaultBean();
        assertNotNull(result);
    }

    @Test
    public void testgetPropertyDefaultValue() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        String name = "name";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        try {
            Object result = pb.getPropertyDefaultValue(name, member, type);
            fail();
        } catch (Exception e) {
            // expected
        }

        Object defaultBean = new Object();
        try {
            Object result = pb.getPropertyDefaultValue(name, member, type);
            fail();
        } catch (Exception e) {
            try {
                Object result2 = member.getValue(defaultBean);
                assertEquals(result2, result);
            } catch (Exception ex) {
                // not expected
                fail();
            }
        }

        try {
            Object result = pb.getPropertyDefaultValue(name, member, type);
            assertEquals(BeanUtil.getDefaultValue(type), result);
        } catch (Exception e) {
            // not expected
            fail();
        }
    }

    @Test
    public void test_throwWrapped() {
        PropertyBuilder pb = new PropertyBuilder(null, null);
        Exception e = new Exception("test");
        String propName = "name";
        Object defaultBean = new Object();

        try {
            pb._throwWrapped(e, propName, defaultBean);
            fail();
        } catch (IllegalArgumentException e2) {
            assertEquals("Failed to get property 'name' of default Object@..." +
                    "instance", e2.getMessage());
        }
    }
}