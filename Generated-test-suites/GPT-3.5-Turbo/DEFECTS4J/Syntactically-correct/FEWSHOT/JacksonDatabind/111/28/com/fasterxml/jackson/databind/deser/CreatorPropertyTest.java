

//Corresponding JUnit 4 Test Cases:
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.CreatorProperty.PropertyMetadata;

public class CreatorPropertyTest {
    
    @Mock
    private PropertyName mockPropertyName;
    
    @Mock
    private JavaType mockJavaType;
    
    @Mock
    private PropertyName mockWrapperName;
    
    @Mock
    private TypeDeserializer mockTypeDeserializer;
    
    @Mock
    private Annotations mockContextAnnotations;
    
    @Mock
    private AnnotatedParameter mockAnnotatedParameter;
    
    @Mock
    private NullValueProvider mockNullValueProvider;
    
    @Mock
    private DeserializationConfig mockDeserializationConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCreatorPropertyConstructorAndGetters() {
        CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockWrapperName,
                mockTypeDeserializer, mockContextAnnotations, mockAnnotatedParameter, 1, "testId",
                PropertyMetadata.STD_OPTIONAL);
        
        assertEquals(mockPropertyName, creatorProperty.getName());
        assertEquals(mockJavaType, creatorProperty.getType());
        assertEquals(mockWrapperName, creatorProperty.getWrapperName());
        assertEquals(mockTypeDeserializer, creatorProperty.getTypeDeserializer());
        assertEquals(mockContextAnnotations, creatorProperty.getMetadata());
        assertEquals(mockAnnotatedParameter, creatorProperty.getMember());
        assertEquals(1, creatorProperty.getCreatorIndex());
        assertEquals("testId", creatorProperty.getInjectableValueId());
    }

    @Test
    public void testCreatorPropertyWithMethods() {
        CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockWrapperName,
                mockTypeDeserializer, mockContextAnnotations, mockAnnotatedParameter, 1, "testId",
                PropertyMetadata.STD_OPTIONAL);
        
        CreatorProperty newCreatorProperty = (CreatorProperty) creatorProperty.withName(mockPropertyName);
        assertEquals(mockPropertyName, newCreatorProperty.getName());
        
        newCreatorProperty = (CreatorProperty) creatorProperty.withValueDeserializer(mockDeserializationConfig);
        assertEquals(mockDeserializationConfig, newCreatorProperty.getValueDeserializer());
        
        newCreatorProperty = (CreatorProperty) creatorProperty.withNullProvider(mockNullValueProvider);
        assertEquals(mockNullValueProvider, newCreatorProperty.getNullProvider());
        
        creatorProperty.fixAccess(mockDeserializationConfig);
        
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);
        
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
        
        assertEquals(mockAnnotatedParameter.getAnnotation(Annotation.class), creatorProperty.getAnnotation(Annotation.class));
        
        assertEquals(mockAnnotatedParameter, creatorProperty.getMember());
        
        assertEquals(1, creatorProperty.getCreatorIndex());
        
        assertFalse(creatorProperty.isIgnorable());
        
        creatorProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        
        creatorProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        
        creatorProperty.set(new Object(), new Object());
        
        creatorProperty.setAndReturn(new Object(), new Object());
        
        assertEquals("testId", creatorProperty.getInjectableValueId());
        
        assertEquals("[creator property, name 'mockPropertyName'; inject id 'testId']", creatorProperty.toString());
    }
    
    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetter() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockWrapperName,
                mockTypeDeserializer, mockContextAnnotations, mockAnnotatedParameter, 1, null,
                PropertyMetadata.STD_OPTIONAL);
        
        creatorProperty._reportMissingSetter(mock(JsonParser.class), mock(DeserializationContext.class));
    }
}

