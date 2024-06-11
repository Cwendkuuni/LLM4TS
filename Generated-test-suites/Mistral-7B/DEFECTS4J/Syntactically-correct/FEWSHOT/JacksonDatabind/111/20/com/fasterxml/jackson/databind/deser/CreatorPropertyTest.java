package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() {
        // Test default constructor
        CreatorProperty cp = new CreatorProperty(null, null, null, null, null, null, -1, null, null);
        assertNull(cp._annotated);
        assertNull(cp._injectableValueId);
        assertNull(cp._fallbackSetter);
        assertEquals(-1, cp._creatorIndex);
        assertFalse(cp._ignorable);

        // Test constructor with annotated parameter
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        when(param.getName()).thenReturn(new PropertyName("test"));
        CreatorProperty cpWithParam = new CreatorProperty(new PropertyName("testName"), null, null, null, null, param, 0, null, null);
        assertEquals("testName", cpWithParam.getName().getSimpleName());
        assertEquals(param, cpWithParam._annotated);
        assertNull(cpWithParam._injectableValueId);
        assertNull(cpWithParam._fallbackSetter);
        assertEquals(0, cpWithParam._creatorIndex);
        assertFalse(cpWithParam._ignorable);

        // Test constructor with injectable value id
        Object injectableValueId = new Object();
        CreatorProperty cpWithInjectableValueId = new CreatorProperty(new PropertyName("testName"), null, null, null, null, null, 0, injectableValueId, null);
        assertEquals(injectableValueId, cpWithInjectableValueId._injectableValueId);
        assertNull(cpWithInjectableValueId._annotated);
        assertNull(cpWithInjectableValueId._fallbackSetter);
        assertEquals(0, cpWithInjectableValueId._creatorIndex);
        assertFalse(cpWithInjectableValueId._ignorable);

        // Test constructor with fallback setter
        SettableBeanProperty fallbackSetter = Mockito.mock(SettableBeanProperty.class);
        when(fallbackSetter.getName()).thenReturn(new PropertyName("testName"));
        CreatorProperty cpWithFallbackSetter = new CreatorProperty(new PropertyName("testName"), null, null, null, null, null, 0, null, fallbackSetter);
        assertEquals(fallbackSetter, cpWithFallbackSetter._fallbackSetter);
        assertNull(cpWithFallbackSetter._annotated);
        assertNull(cpWithFallbackSetter._injectableValueId);
        assertEquals(0, cpWithFallbackSetter._creatorIndex);
        assertFalse(cpWithFallbackSetter._ignorable);

        // Test copy constructor
        CreatorProperty cpWithParamCopy = new CreatorProperty(cpWithParam, new PropertyName("testNameCopy"));
        assertEquals("testNameCopy", cpWithParamCopy.getName().getSimpleName());
        assertEquals(param, cpWithParamCopy._annotated);
        assertNull(cpWithParamCopy._injectableValueId);
        assertNull(cpWithParamCopy._fallbackSetter);
        assertEquals(0, cpWithParamCopy._creatorIndex);
        assertFalse(cpWithParamCopy._ignorable);

        // Test copy constructor with deserializer
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);
        CreatorProperty cpWithDeserializerCopy = new CreatorProperty(cpWithParam, deserializer, null);
        assertEquals(deserializer, cpWithDeserializerCopy._valueDeserializer);
        assertEquals(param, cpWithDeserializerCopy._annotated);
        assertNull(cpWithDeserializerCopy._injectableValueId);
        assertNull(cpWithDeserializerCopy._fallbackSetter);
        assertEquals(0, cpWithDeserializerCopy._creatorIndex);
        assertFalse(cpWithDeserializerCopy._ignorable);

        // Test copy constructor with null provider
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        CreatorProperty cpWithNullProviderCopy = new CreatorProperty(cpWithParam, null, nva);
        assertNull(cpWithNullProviderCopy._valueDeserializer);
        assertEquals(param, cpWithNullProviderCopy._annotated);
        assertNull(cpWithNullProviderCopy._injectableValueId);
        assertNull(cpWithNullProviderCopy._fallbackSetter);
        assertEquals(0, cpWithNullProviderCopy._creatorIndex);
        assertFalse(cpWithNullProviderCopy._ignorable);
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        Object beanInstance = new Object();
        CreatorProperty cp = new CreatorProperty(new PropertyName("testName"), null, null, null, null, null, 0, null, null);
        when(cp._injectableValueId).thenReturn("testValueId");

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        when(context.findInjectableValue("testValueId", cp, beanInstance)).thenReturn(new Object());

        cp.inject(context, beanInstance);
        verify(beanInstance).set(eq(cp), any());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValue_NoValueId() throws IOException {
        Object beanInstance = new Object();
        CreatorProperty cp = new CreatorProperty(new PropertyName("testName"), null, null, null, null, null, 0, null, null);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        cp.inject(context, beanInstance);
        verifyNoInteractions(beanInstance);

        Annotation a = Mockito.mock(Annotation.class);
        when(cp._annotated.getAnnotation(eq(a.getClass()))).thenReturn(a);
        context.reportBadDefinition(eq(getClassOf(beanInstance)), argThat(m -> m.getMessage().contains("Property")));
    }
}