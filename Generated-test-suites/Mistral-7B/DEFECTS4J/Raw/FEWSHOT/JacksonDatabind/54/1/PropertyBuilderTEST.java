package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.unknownType();

        // Test without annotations
        JavaType result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(result);

        // Test with annotations
        JsonSerialize jsonSerializeAnnot = Mockito.mock(JsonSerialize.class);
        am.setValue(jsonSerializeAnnot);

        // Test with annotations that match declared type
        JavaType annotationType = TypeFactory.constructType(Foo.class);
        when(jsonSerializeAnnot.type()).thenReturn(annotationType);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(annotationType, result);

        // Test with annotations that match super type
        JavaType superType = TypeFactory.constructType(Bar.class);
        when(jsonSerializeAnnot.type()).thenReturn(superType);
        result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(superType, result);

        // Test with annotations and static typing
        result = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(annotationType.withStaticTyping(), result);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(Foo.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.constructType(Foo.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        // Test without annotations
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(bpw);

        // Test with annotations for null serializer
        Object nullSerializer = Mockito.mock(Object.class);
        when(am.getValue(JsonSerialize.NullSerializer.class)).thenReturn(nullSerializer);
        when(am.getValue(JsonSerialize.class)).thenReturn(ser);
        when(typeSer.getValueSerializer(null)).thenReturn(nullSerializer);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertEquals(ser, bpw.getNullSerializer());

        // Test with annotations for unwrapping
        NameTransformer unwrapper = Mockito.mock(NameTransformer.class);
        when(am.getValue(JsonUnwrapped.class)).thenReturn(unwrapper);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertEquals(unwrapper, bpw.getUnwrappingNameTransformer());

        // Test with property inclusion
        JsonInclude.Include inclusion = JsonInclude.Include.NON_DEFAULT;
        when(propDef.getSerializationConfig().findPropertyInclusion(declaredType)).thenReturn(inclusion);
        Object valueToSuppress = "defaultValue";
        when(propertyBuilder.getPropertyDefaultValue(propDef.getName(), am, declaredType)).thenReturn(valueToSuppress);
        when(propertyBuilder.getDefaultValue(declaredType)).thenReturn("anotherValue");
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertEquals(valueToSuppress, bpw.getValueToSuppress());
        assertEquals(inclusion, bpw.getSerializationInclusion());
        assertEquals(false, bpw.suppressNulls());

        // Test with empty value and writeEmptyJsonArray setting
        declaredType = TypeFactory.constructType(Collection.class);
        when(declaredType.isContainerType()).thenReturn(true);
        when(propDef.getName()).thenReturn("myList");
        when(config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertEquals(BeanPropertyWriter.MarkerForEmpty.class, bpw.getValueToSuppress().getClass());

        // Test with empty value and writeEmptyJsonArray setting enabled
        when(config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(true);
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(bpw.getValueToSuppress());
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class Foo {
        private Integer intValue;
        private String stringValue;
        private Bar bar;

        @JsonSerialize(using = IntSerializer.class)
        public Integer getIntValue() {
            return intValue;
        }

        @JsonSerialize(using = StringSerializer.class)
        public String getStringValue() {
            return stringValue;
        }

        @JsonSerialize(using = BarSerializer.class)
        public Bar getBar() {
            return bar;
        }
    }

    static class Bar {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class IntSerializer extends JsonSerializer<Integer> {
    }

    static class StringSerializer extends JsonSerializer<String> {
    }

    static class BarSerializer extends JsonSerializer<Bar> {
    }
}