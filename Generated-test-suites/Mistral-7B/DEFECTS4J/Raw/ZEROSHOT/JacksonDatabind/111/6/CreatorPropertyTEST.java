package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    private static final Object MOCK_BEAN_INSTANCE = new Object() {};
    private static final Object MOCK_INJECTABLE_VALUE = new Object() {};
    private static final JsonDeserializer<Object> MOCK_VALUE_DESERIALIZER = mock(JsonDeserializer.class);
    private static final NullValueProvider MOCK_NULL_PROVIDER = mock(NullValueProvider.class);
    private static final Object MOCK_ANNOTATED_PARAMETER = mock(AnnotatedParameter.class);
    private static final Object MOCK_CONTEXT = mock(DeserializationContext.class);
    private static final Object MOCK_INJECTABLE_VALUE_ID = "injectableValueId";
    private static final int MOCK_CREATOR_INDEX = 0;

    @SuppressWarnings("unchecked")
    private CreatorProperty createTestSubject(PropertyName name, JavaType type, Object injectableValueId) {
        return new CreatorProperty(name, type, null, null, null, (AnnotatedParameter) MOCK_ANNOTATED_PARAMETER, MOCK_CREATOR_INDEX,
                injectableValueId, new PropertyMetadata());
    }

    @Test
    public void test_Constructor() throws Exception {
        // Basic test
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        assertSame(name, testSubject.getName());
        assertSame(type, testSubject.getType());
        assertNull(testSubject._annotated);
        assertNull(testSubject._injectableValueId);
        assertNull(testSubject._fallbackSetter);
        assertNull(testSubject._creatorIndex);
        assertFalse(testSubject.isIgnorable());
    }

    @Test
    public void test_Constructor_WithInjectableValueId() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, MOCK_INJECTABLE_VALUE_ID);
        assertSame(name, testSubject.getName());
        assertSame(type, testSubject.getType());
        assertSame(MOCK_INJECTABLE_VALUE_ID, testSubject.getInjectableValueId());
        assertNull(testSubject._annotated);
        assertNull(testSubject._fallbackSetter);
        assertNull(testSubject._creatorIndex);
        assertFalse(testSubject.isIgnorable());
    }

    @Test
    public void test_GettersAndSetters() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);

        // Test getters
        assertSame(name, testSubject.getName());
        assertSame(type, testSubject.getType());
        assertNull(testSubject.getAnnotation(Object.class));
        assertSame(MOCK_CREATOR_INDEX, testSubject.getCreatorIndex());
        assertNull(testSubject._injectableValueId);
        assertNull(testSubject._fallbackSetter);
        assertFalse(testSubject.isIgnorable());

        // Test setters
        final Object newName = propertyName("newName");
        testSubject.setName(newName);
        assertSame(newName, testSubject.getName());
        testSubject.setType(simpleType(Object.class));
        assertSame(simpleType(Object.class), testSubject.getType());
        testSubject.setAnnotated(MOCK_ANNOTATED_PARAMETER);
        assertSame(MOCK_ANNOTATED_PARAMETER, testSubject.getAnnotated());
        testSubject.setCreatorIndex(1);
        assertSame(1, testSubject.getCreatorIndex());
        testSubject.setInjectableValueId(MOCK_INJECTABLE_VALUE_ID);
        assertSame(MOCK_INJECTABLE_VALUE_ID, testSubject.getInjectableValueId());
        testSubject.setFallbackSetter(mock(SettableBeanProperty.class));
        assertSame(mock(SettableBeanProperty.class), testSubject.getFallbackSetter());
        testSubject.setIgnorable(true);
        assertTrue(testSubject.isIgnorable());
    }

    @Test
    public void test_Inject() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, MOCK_INJECTABLE_VALUE_ID);

        // Test finding and injecting injectable value
        when(MOCK_CONTEXT.findInjectableValue(eq(MOCK_INJECTABLE_VALUE_ID), eq(testSubject), eq(MOCK_BEAN_INSTANCE)))
                .thenReturn(MOCK_INJECTABLE_VALUE);
        testSubject.inject(MOCK_CONTEXT, MOCK_BEAN_INSTANCE);
        assertEquals(MOCK_INJECTABLE_VALUE, testSubject.get(MOCK_BEAN_INSTANCE));

        // Test error when no injectable value found
        when(MOCK_CONTEXT.findInjectableValue(eq(MOCK_INJECTABLE_VALUE_ID), eq(testSubject), eq(MOCK_BEAN_INSTANCE)))
                .thenThrow(new JsonMappingException("Injectable value not found"));
        try {
            testSubject.inject(MOCK_CONTEXT, MOCK_BEAN_INSTANCE);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Injectable value not found", e.getMessage());
        }
    }

    @Test
    public void test_DeserializeAndSet() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        final JsonParser p = createParser("{\"name\": \"value\"}");
        final DeserializationContext ctxt = createContext();
        testSubject.deserializeAndSet(p, ctxt, MOCK_BEAN_INSTANCE);
        assertEquals("value", testSubject.get(MOCK_BEAN_INSTANCE));
    }

    @Test
    public void test_DeserializeSetAndReturn() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        final JsonParser p = createParser("{\"name\": \"value\"}");
        final DeserializationContext ctxt = createContext();
        Object value = testSubject.deserializeSetAndReturn(p, ctxt, MOCK_BEAN_INSTANCE);
        assertEquals("value", value);
        assertEquals("value", testSubject.get(MOCK_BEAN_INSTANCE));
    }

    @Test
    public void test_Set() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        testSubject.set(MOCK_BEAN_INSTANCE, "value");
        assertEquals("value", testSubject.get(MOCK_BEAN_INSTANCE));
    }

    @Test
    public void test_SetAndReturn() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        Object value = testSubject.setAndReturn(MOCK_BEAN_INSTANCE, "value");
        assertEquals("value", value);
        assertEquals("value", testSubject.get(MOCK_BEAN_INSTANCE));
    }

    @Test
    public void test_FindInjectableValue() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, MOCK_INJECTABLE_VALUE_ID);
        when(MOCK_CONTEXT.findInjectableValue(eq(MOCK_INJECTABLE_VALUE_ID), any(), eq(MOCK_BEAN_INSTANCE)))
                .thenReturn(MOCK_INJECTABLE_VALUE);
        assertSame(MOCK_INJECTABLE_VALUE, testSubject.findInjectableValue(MOCK_CONTEXT, MOCK_BEAN_INSTANCE));

        // Test error when no injectable value found
        when(MOCK_CONTEXT.findInjectableValue(eq(MOCK_INJECTABLE_VALUE_ID), any(), eq(MOCK_BEAN_INSTANCE)))
                .thenThrow(new JsonMappingException("Injectable value not found"));
        try {
            testSubject.findInjectableValue(MOCK_CONTEXT, MOCK_BEAN_INSTANCE);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Injectable value not found", e.getMessage());
        }
    }

    @Test
    public void test_MarkAsIgnorable() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        testSubject.markAsIgnorable();
        assertTrue(testSubject.isIgnorable());
    }

    @Test
    public void test_ToString() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, "injectableValueId");
        assertEquals("[creator property, name 'name'; inject id 'injectableValueId']", testSubject.toString());
    }

    @Test(expected = IOException.class)
    public void test_VerifySetter_WhenNoSetterDefined() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        testSubject.deserializeAndSet(createParser("{}"), createContext(), MOCK_BEAN_INSTANCE);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void test_VerifySetter_WhenNoSetterDefined_WithParser() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        testSubject.deserializeSetAndReturn(createParser("{}"), createContext(), MOCK_BEAN_INSTANCE);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void test_VerifySetter_WhenNoSetterDefined_WithSetter() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        testSubject.set(MOCK_BEAN_INSTANCE, "value");
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void test_VerifySetter_WhenNoSetterDefined_WithSetterAndReturn() throws Exception {
        final PropertyName name = propertyName("name");
        final JavaType type = basicMapType();
        final CreatorProperty testSubject = createTestSubject(name, type, null);
        testSubject.setAndReturn(MOCK_BEAN_INSTANCE, "value");
        fail("Expected IOException");
    }

    private static ObjectNode createParser(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new StringReader(content), ObjectNode.class);
    }

    private static DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationConfig().withoutFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}