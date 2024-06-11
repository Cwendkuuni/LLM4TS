package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        Object dummy = new Object() {};
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.isAnnotated(JsonSerialize.class)).thenReturn(true);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize() {
            @JsonSerialize.Using(value = SerializerFactory.class)
            public Class<? extends JsonSerializer<?>> serializerType() {
                return StringSerializer.class;
            }
        });
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        JavaType actualType = propertyBuilder.findSerializationType(am, false, null);
        assertEquals(StringSerializer.class, actualType.getRawClass());

        // Test with static typing
        when(am.isAnnotated(JsonSerialize.class)).thenReturn(true);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize() {
            @JsonSerialize.Using(value = SerializerFactory.class)
            public Class<? extends JsonSerializer<?>> serializerType() {
                return MyCustomSerializer.class;
            }
        });
        propertyBuilder = new PropertyBuilder(null, beanDesc);
        actualType = propertyBuilder.findSerializationType(am, true, null);
        assertEquals(MyCustomSerializer.class, actualType.getRawClass());

        // Test with no annotations
        when(am.isAnnotated(JsonSerialize.class)).thenReturn(false);
        propertyBuilder = new PropertyBuilder(null, beanDesc);
        assertNull(propertyBuilder.findSerializationType(am, false, null));

        // Test with null type
        propertyBuilder = new PropertyBuilder(null, beanDesc);
        assertNull(propertyBuilder.findSerializationType(am, false, null));
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription();
        Object defaultBean = new Object() {};
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        propertyBuilder._defaultBean = defaultBean;
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder._defaultBean = null;
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        Object defaultBean = new Object() {};
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("testProperty");
        when(member.getValue(defaultBean)).thenReturn("defaultValue");
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        propertyBuilder._defaultBean = defaultBean;
        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("testProperty", member, new JavaType() {}));

        propertyBuilder._defaultBean = null;
        assertNull(propertyBuilder.getPropertyDefaultValue("testProperty", member, new JavaType() {}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithException() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        Object defaultBean = new Object() {};
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("testProperty");
        when(member.getValue(defaultBean)).thenThrow(new Exception());
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        propertyBuilder._defaultBean = defaultBean;
        propertyBuilder.getPropertyDefaultValue("testProperty", member, new JavaType() {});
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(new JavaType() {}).getValueInclusion());

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_NULL);
        assertEquals(JsonInclude.Include.NON_NULL, propertyBuilder.getDefaultValue(new JavaType() {}).getValueInclusion());
    }
}