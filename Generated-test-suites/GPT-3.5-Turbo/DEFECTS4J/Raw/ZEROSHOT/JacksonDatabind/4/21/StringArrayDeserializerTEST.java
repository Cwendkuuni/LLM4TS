package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testConstructor() {
        StringArrayDeserializer instance = StringArrayDeserializer.instance;
        assertNotNull(instance);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = new JsonParser() {
            @Override
            public ObjectCodec getCodec() {
                return null;
            }
            @Override
            public void setCodec(ObjectCodec codec) {
            }
            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }
            @Override
            public JsonToken getCurrentToken() {
                return null;
            }
            @Override
            public String getText() throws IOException {
                return null;
            }
            @Override
            public char[] getTextCharacters() throws IOException {
                return new char[0];
            }
            @Override
            public int getTextLength() throws IOException {
                return 0;
            }
            @Override
            public int getTextOffset() throws IOException {
                return 0;
            }
        };
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = new JsonParser() {
            @Override
            public ObjectCodec getCodec() {
                return null;
            }
            @Override
            public void setCodec(ObjectCodec codec) {
            }
            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }
            @Override
            public JsonToken getCurrentToken() {
                return null;
            }
            @Override
            public String getText() throws IOException {
                return null;
            }
            @Override
            public char[] getTextCharacters() throws IOException {
                return new char[0];
            }
            @Override
            public int getTextLength() throws IOException {
                return 0;
            }
            @Override
            public int getTextOffset() throws IOException {
                return 0;
            }
        };
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt, Object intoValue) throws IOException {
                return null;
            }
            @Override
            public JsonDeserializer<?> findDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public String getPropertyName() {
                return null;
            }
            @Override
            public String getTypeId() {
                return null;
            }
            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
            @Override
            public TypeIdResolver getTypeIdResolver() {
                return null;
            }
            @Override
            public JsonDeserializer<Object> getDefaultImpl() {
                return null;
            }
        };
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jp = new JsonParser() {
            @Override
            public ObjectCodec getCodec() {
                return null;
            }
            @Override
            public void setCodec(ObjectCodec codec) {
            }
            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }
            @Override
            public JsonToken getCurrentToken() {
                return null;
            }
            @Override
            public String getText() throws IOException {
                return null;
            }
            @Override
            public char[] getTextCharacters() throws IOException {
                return new char[0];
            }
            @Override
            public int getTextLength() throws IOException {
                return 0;
            }
            @Override
            public int getTextOffset() throws IOException {
                return 0;
            }
        };
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        BeanProperty property = new BeanProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
            @Override
            public Type getType() {
                return null;
            }
            @Override
            public PropertyName getFullName() {
                return null;
            }
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }
            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
                return null;
            }
            @Override
            public boolean isRequired() {
                return false;
            }
            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }
            @Override
            public ValueDeserializer<?> getValueDeserializer() {
                return null;
            }
            @Override
            public AnnotatedMember getAccessor() {
                return null;
            }
            @Override
            public AnnotatedMember getMutator() {
                return null;
            }
            @Override
            public Property getProperty() {
                return null;
            }
            @Override
            public JavaType getType() {
                return null;
            }
            @Override
            public PropertyName getWrapperName() {
                return null;
            }
            @Override
            public boolean isTypeId() {
                return false;
            }
        };
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(ctxt, property);
        assertNotNull(result);
    }
}