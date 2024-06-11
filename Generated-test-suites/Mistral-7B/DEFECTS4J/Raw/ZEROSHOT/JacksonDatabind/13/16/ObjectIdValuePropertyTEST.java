package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerProvider;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonUtil;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() throws Exception {
        // given
        String propertyName = "id";
        Class<Long> idType = Long.class;
        JsonId idAnnotation = new JsonId();
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(idType);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        when(beanPropertyDefinition.getName()).thenReturn(propertyName);
        BeanDeserializerProvider deserializerProvider = mock(BeanDeserializerProvider.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(deserializerProvider.findBeanDeserializer(ArgumentMatchers.eq(idType), ArgumentMatchers.eq(Long.class)))
                .thenReturn(valueDeserializer);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(valueDeserializer.findContentDeserializer(ArgumentMatchers.eq(JsonParser.class), ArgumentMatchers.eq(deserializerProvider), ArgumentMatchers.eq(typeDeserializer), ArgumentMatchers.isA(JsonDeserializer.class)))
                .thenReturn(valueDeserializer);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, beanPropertyDefinition, deserializerProvider);

        // when
        JsonParser jsonParser = mock(JsonParser.class);
        Object instance = new Object();

        // test deserializeAndSet
        objectIdValueProperty.deserializeAndSet(jsonParser, JsonTestConfig.context(), instance);
        Object id = JsonTestConfig.context().findObjectId(instance, objectIdReader.generator, objectIdReader.resolver).getId();
        assertNull("Id should be null after deserialization", id);

        // given
        String idValue = "1";
        Object idValueObject = JsonUtil.parseValueForType(idValue, Long.class);

        // when
        JsonParser jsonParserWithId = mock(JsonParser.class);
        when(jsonParserWithId.getValueAsToken()).thenReturn(JsonTestConfig.context().createValue(idValueObject));
        objectIdValueProperty.deserializeSetAndReturn(jsonParserWithId, JsonTestConfig.context(), instance);

        Object result = instance;
        if (objectIdValueProperty._objectIdReader.idProperty != null) {
            result = objectIdValueProperty._objectIdReader.idProperty.get(instance);
        }
        assertNotNull("Id should not be null after deserialization", result);
        assertEquals("Id should be the same as the deserialized value", idValueObject, result);

        // given
        Object newInstance = new Object();

        // when
        objectIdValueProperty.set(newInstance, idValueObject);

        Object idFromSet = newInstance;
        if (objectIdValueProperty._objectIdReader.idProperty != null) {
            idFromSet = objectIdValueProperty._objectIdReader.idProperty.get(newInstance);
        }
        assertEquals("Id should be the same after setting", idValueObject, idFromSet);

        // test setAndReturn
        Object setAndReturn = objectIdValueProperty.setAndReturn(newInstance, idValueObject);
        assertEquals("Id should be the same after setting and returning", idValueObject, setAndReturn);
        assertEquals("Instance should be the same after setting and returning", newInstance, setAndReturn);

        // test withName
        ObjectIdValueProperty withName = objectIdValueProperty.withName(new PropertyName("newName"));
        assertEquals("Property names should be the same", "newName", withName.getName().asString());

        // test withValueDeserializer
        JsonDeserializer<?> newValueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty withValueDeserializer = new ObjectIdValueProperty(objectIdValueProperty, newValueDeserializer);
        assertEquals("Deserializers should be the same", valueDeserializer, withValueDeserializer.getValueDeserializer());
        withValueDeserializer.setValueDeserializer(newValueDeserializer);
        assertEquals("Deserializers should be the new one", newValueDeserializer, withValueDeserializer.getValueDeserializer());
    }
}