package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {
    
    @Test
    public void testFindObjectId() {
        // Arrange
        DefaultDeserializationContext defaultDeserializationContext = new DefaultDeserializationContextImpl(Mockito.mock(DeserializerFactory.class), Mockito.mock(DeserializerCache.class));
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Act
        ReadableObjectId result = defaultDeserializationContext.findObjectId(new Object(), objectMapper.objectIdGeneratorInstance(Mockito.mock(Annotated.class), Mockito.mock(Object.class)), resolver);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        // Arrange
        DefaultDeserializationContext defaultDeserializationContext = new DefaultDeserializationContextImpl(Mockito.mock(DeserializerFactory.class), Mockito.mock(DeserializerCache.class));
        
        // Act & Assert
        defaultDeserializationContext.checkUnresolvedObjectId();
    }
    
    @Test
    public void testDeserializerInstance() {
        // Arrange
        DefaultDeserializationContext defaultDeserializationContext = new DefaultDeserializationContextImpl(Mockito.mock(DeserializerFactory.class), Mockito.mock(DeserializerCache.class));
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Act
        JsonDeserializer<Object> result = defaultDeserializationContext.deserializerInstance(annotated, objectMapper.deserializationConfig());
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        // Arrange
        DefaultDeserializationContext defaultDeserializationContext = new DefaultDeserializationContextImpl(Mockito.mock(DeserializerFactory.class), Mockito.mock(DeserializerCache.class));
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Act
        KeyDeserializer result = defaultDeserializationContext.keyDeserializerInstance(annotated, objectMapper.deserializationConfig());
        
        // Assert
        assertNotNull(result);
    }

    private class DefaultDeserializationContextImpl extends DefaultDeserializationContext {
        
        private static final long serialVersionUID = 1L;

        public DefaultDeserializationContextImpl(DeserializerFactory df, DeserializerCache cache) {
            super(df, cache);
        }

        public DefaultDeserializationContextImpl(DefaultDeserializationContext src,
                DeserializationConfig config, JsonParser jp, InjectableValues values) {
            super(src, config, jp, values);
        }

        public DefaultDeserializationContextImpl(DefaultDeserializationContext src,
                DeserializerFactory factory) {
            super(src, factory);
        }

        public DefaultDeserializationContextImpl(DefaultDeserializationContext src) {
            super(src);
        }

        @Override
        public DefaultDeserializationContext copy() {
            return new DefaultDeserializationContextImpl(this);
        }

        @Override
        public DefaultDeserializationContext createInstance(
                DeserializationConfig config, JsonParser jp, InjectableValues values) {
            return new DefaultDeserializationContextImpl(this, config, jp, values);
        }

        @Override
        public DefaultDeserializationContext with(DeserializerFactory factory) {
            return new DefaultDeserializationContextImpl(this, factory);
        }  
    }
}