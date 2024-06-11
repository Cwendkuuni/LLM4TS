package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() throws IOException {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription((MapperConfig) config, SimpleType.constructUnsafe(String.class), null), (MapperConfig) config);
        ValueInstantiator actualConstructValueInstantiatorResult = creatorCollector.constructValueInstantiator(config);
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDelegate());
        assertTrue(actualConstructValueInstantiatorResult.canCreateUsingDefault());
        assertTrue(actualConstructValueInstantiatorResult.canCreateFromBoolean());
        assertFalse(actualConstructValueInstantiatorResult.canCreateFromDouble());
        assertFalse(actualConstructValueInstantiatorResult.canCreateFromInstant());
        assertFalse(actualConstructValueInstantiatorResult.canCreateFromLong());
        assertFalse(actualConstructValueInstantiatorResult.canCreateFromObjectWith());
        assertFalse(actualConstructValueInstantiatorResult.canCreateFromString());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingArrayDelegate());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDefault());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDelegate());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDelegate());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDelegate());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDelegate());
        assertEquals(0, actualConstructValueInstantiatorResult.getDefaultCreatorIndex());
        assertNull(actualConstructValueInstantiatorResult.getDelegateCreator());
        assertNull(actualConstructValueInstantiatorResult.getDelegateType(null));
        assertNull(actualConstructValueInstantiatorResult.getFromBooleanCreator());
        assertNull(actualConstructValueInstantiatorResult.getFromDoubleCreator());
        assertNull(actualConstructValueInstantiatorResult.getFromIntCreator());
        assertNull(actualConstructValueInstantiatorResult.getFromLongCreator());
        assertNull(actualConstructValueInstantiatorResult.getFromObjectArguments(null));
        assertNull(actualConstructValueInstantiatorResult.getFromStringCreator());
        assertEquals(null, actualConstructValueInstantiatorResult.getIncompleteParameter());
        assertFalse(actualConstructValueInstantiatorResult.canCreateUsingDefault());
        assertEquals("java.lang.String", actualConstructValueInstantiatorResult.getValueTypeDesc());
    }

}