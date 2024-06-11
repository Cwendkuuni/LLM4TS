package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.ser.*;

import org.junit.Test;

/**
 * Test class for PropertyBuilder.
 */
public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() {
        // Test constructor
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);

        // Test getClassAnnotations()
        Annotations actualAnnotations = propertyBuilder.getClassAnnotations();
        assertEquals("Test annotations for PropertyBuilder", 0, actualAnnotations.size());

        // Test buildWriter() with default parameters
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class, null);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = new JsonSerializer<String>() {};
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(actualWriter);

        // Test buildWriter() with custom parameters
        typeSer = new TypeSerializer() {};
        contentTypeSer = new TypeSerializer() {};
        am = new AnnotatedMember() {};
        defaultUseStaticTyping = true;
        BeanPropertyWriter actualWriterCustom = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(actualWriterCustom);

        // Test findSerializationType() with no annotations
        Annotated aNoAnnotations = new Annotated() {};
        JavaType actualTypeNoAnnotations = propertyBuilder.findSerializationType(aNoAnnotations, false, declaredType);
        assertNull(actualTypeNoAnnotations);

        // Test findSerializationType() with JsonSerialize annotation
        Annotated aJsonSerialize = new Annotated() {};
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize() {};
        aJsonSerialize.setValue(JsonSerialize.Field.CLASS, jsonSerializeAnnotation.getClass());
        JavaType actualTypeJsonSerialize = propertyBuilder.findSerializationType(aJsonSerialize, false, declaredType);
        assertEquals("Expected JsonSerialize annotation type", jsonSerializeAnnotation.getClass(), actualTypeJsonSerialize.getRawClass());

        // Test findSerializationType() with JsonTypeId annotation
        Annotated aJsonTypeId = new Annotated() {};
        JsonTypeId jsonTypeIdAnnotation = new JsonTypeId() {};
        aJsonTypeId.setValue(JsonTypeId.Field.CLASS, jsonTypeIdAnnotation.getClass());
        JavaType actualTypeJsonTypeId = propertyBuilder.findSerializationType(aJsonTypeId, false, declaredType);
        assertEquals("Expected JsonTypeId annotation type", jsonTypeIdAnnotation.getClass(), actualTypeJsonTypeId.getRawClass());

        // Test getPropertyDefaultValue() with String property
        String name = "testProperty";
        JavaType typeString = TypeFactory.defaultInstance().constructType(String.class);
        AnnotatedMember memberString = new AnnotatedMember() {};
        Object defaultBeanString = new Object() {};
        Object actualDefaultValueString = propertyBuilder.getPropertyDefaultValue(name, memberString, typeString);
        assertEquals("Expected empty string as default value for String property", "", actualDefaultValueString);

        // Test getPropertyDefaultValue() with int property
        String nameInt = "testProperty";
        JavaType typeInt = TypeFactory.defaultInstance().constructType(int.class);
        AnnotatedMember memberInt = new AnnotatedMember() {};
        Object defaultBeanInt = new Object() {};
        Object actualDefaultValueInt = propertyBuilder.getPropertyDefaultValue(nameInt, memberInt, typeInt);
        assertEquals("Expected 0 as default value for int property", 0, actualDefaultValueInt.intValue());

        // Test getPropertyDefaultValue() with nullable Object property
        String nameObject = "testProperty";
        JavaType typeObject = TypeFactory.defaultInstance().constructType(Object.class);
        AnnotatedMember memberObject = new AnnotatedMember() {};
        Object defaultBeanObject = null;
        Object actualDefaultValueObject = propertyBuilder.getPropertyDefaultValue(nameObject, memberObject, typeObject);
        assertNull("Expected null as default value for Object property", actualDefaultValueObject);

        // Test getDefaultValue() with String type
        JavaType typeStringDefaultValue = TypeFactory.defaultInstance().constructType(String.class);
        Object actualDefaultValueStringDefault = propertyBuilder.getDefaultValue(typeStringDefaultValue);
        assertEquals("Expected empty string as default value for String type", "", actualDefaultValueStringDefault);

        // Test getDefaultValue() with int type
        JavaType typeIntDefaultValue = TypeFactory.defaultInstance().constructType(int.class);
        Object actualDefaultValueIntDefault = propertyBuilder.getDefaultValue(typeIntDefaultValue);
        assertEquals("Expected 0 as default value for int type", 0, actualDefaultValueIntDefault.intValue());

        // Test getDefaultValue() with nullable Object type
        JavaType typeObjectDefaultValue = TypeFactory.defaultInstance().constructType(Object.class);
        Object actualDefaultValueObjectDefault = propertyBuilder.getDefaultValue(typeObjectDefaultValue);
        assertNull("Expected null as default value for Object type", actualDefaultValueObjectDefault);

        // Test _throwWrapped() with NullPointerException
        Exception eNullPointerException = new NullPointerException();
        String propNameNullPointerException = "testProperty";
        Object defaultBeanNullPointerException = new Object() {};
        Object actualWrappedNullPointerException = propertyBuilder._throwWrapped(eNullPointerException, propNameNullPointerException, defaultBeanNullPointerException);
        assertSame("Expected NullPointerException as wrapped exception", eNullPointerException, actualWrappedNullPointerException);

        // Test _throwWrapped() with ArrayIndexOutOfBoundsException
        Exception eArrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException();
        String propNameArrayIndexOutOfBoundsException = "testProperty";
        Object defaultBeanArrayIndexOutOfBoundsException = new Object() {};
        Object actualWrappedArrayIndexOutOfBoundsException = propertyBuilder._throwWrapped(eArrayIndexOutOfBoundsException, propNameArrayIndexOutOfBoundsException, defaultBeanArrayIndexOutOfBoundsException);
        assertSame("Expected ArrayIndexOutOfBoundsException as wrapped exception", eArrayIndexOutOfBoundsException, actualWrappedArrayIndexOutOfBoundsException);

        // Test _throwWrapped() with IOException
        Exception eIOException = new IOException();
        String propNameIOException = "testProperty";
        Object defaultBeanIOException = new Object() {};
        Object actualWrappedIOException = propertyBuilder._throwWrapped(eIOException, propNameIOException, defaultBeanIOException);
        assertSame("Expected IOException as wrapped exception", eIOException, actualWrappedIOException);
    }
}