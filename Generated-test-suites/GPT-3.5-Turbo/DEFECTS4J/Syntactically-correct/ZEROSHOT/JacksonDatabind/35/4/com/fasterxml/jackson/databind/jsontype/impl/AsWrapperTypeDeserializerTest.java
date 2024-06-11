
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;
    
    @Mock
    private BeanProperty mockProperty;
    
    @Mock
    private JsonDeserializer<Object> mockDeserializer;
    
    @Mock
    private DeserializationContext mockContext;
    
    @Mock
    private JsonParser mockParser;
    
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(JavaType, mockTypeIdResolver, "typePropertyName", true, Object.class);
    }
    
    @Test
    public void testForProperty() {
        TypeDeserializer result = asWrapperTypeDeserializer.forProperty(mockProperty);
        assertTrue(result instanceof AsWrapperTypeDeserializer);
        assertEquals(mockProperty, result.getProperty());
    }
    
    @Test
    public void testGetTypeInclusion() {
        As result = asWrapperTypeDeserializer.getTypeInclusion();
        assertEquals(As.WRAPPER_OBJECT, result);
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(mockParser, mockContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(mockParser, mockContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(mockParser, mockContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(mockParser, mockContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        TokenBuffer buffer = new TokenBuffer(null, false);
        JsonGenerator generator = factory.createGenerator(buffer);
        generator.writeStartObject();
        generator.writeFieldName("typeId");
        generator.writeString("testType");
        generator.writeEndObject();
        generator.close();
        JsonParser parser = factory.createParser(buffer.asParser());
        parser.nextToken();
        Object result = asWrapperTypeDeserializer.deserialize(parser, mockContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        TokenBuffer buffer = new TokenBuffer(null, false);
        JsonGenerator generator = factory.createGenerator(buffer);
        generator.writeStartObject();
        generator.writeFieldName("typeId");
        generator.writeString("testType");
        generator.writeEndObject();
        generator.close();
        JsonParser parser = factory.createParser(buffer.asParser());
        parser.nextToken();
        Object typeId = "testType";
        Object result = asWrapperTypeDeserializer._deserializeWithNativeTypeId(parser, mockContext, typeId);
        assertNotNull(result);
    }

}
