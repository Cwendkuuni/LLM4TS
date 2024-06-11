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
        CreatorProperty property = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty());
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property._ignorable);

        // Test constructor with annotated parameter
        AnnotatedParameter annotatedParameter = Mockito.mock(AnnotatedParameter.class);
        when(annotatedParameter.getName()).thenReturn(PropertyName.construct("testProperty"));
        CreatorProperty propertyWithAnnotatedParameter = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, annotatedParameter, 0, null, PropertyMetadata.empty());
        assertEquals(annotatedParameter, propertyWithAnnotatedParameter._annotated);

        // Test constructor with injectable value id
        Object injectableValueId = new Object() {};
        CreatorProperty propertyWithInjectableValueId = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, injectableValueId, PropertyMetadata.empty());
        assertEquals(injectableValueId, propertyWithInjectableValueId._injectableValueId);

        // Test constructor with fallback setter
        SettableBeanProperty fallbackSetter = Mockito.mock(SettableBeanProperty.class);
        CreatorProperty propertyWithFallbackSetter = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty(), fallbackSetter);
        assertEquals(fallbackSetter, propertyWithFallbackSetter._fallbackSetter);

        // Test constructor with creator index
        CreatorProperty propertyWithCreatorIndex = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 1, null, PropertyMetadata.empty());
        assertEquals(1, propertyWithCreatorIndex._creatorIndex);

        // Test constructor with ignorable flag
        CreatorProperty propertyWithIgnorableFlag = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty(), null, true);
        assertTrue(propertyWithIgnorableFlag._ignorable);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty());
        SettableBeanProperty fallbackSetter = Mockito.mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        Object beanInstance = new Object() {};
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        when(context.findInjectableValue(Mockito.any(), Mockito.any(), Mockito.eq(beanInstance))).thenReturn(new Object() {});

        CreatorProperty property = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty());
        property.inject(context, beanInstance);
        verify(context).findInjectableValue(Mockito.any(), Mockito.eq(property), Mockito.eq(beanInstance));
    }

    @Test
    public void testInject() throws IOException {
        Object beanInstance = new Object() {};
        Object injectableValue = new Object() {};
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        when(context.findInjectableValue(Mockito.any(), Mockito.any(), Mockito.eq(beanInstance))).thenReturn(injectableValue);

        CreatorProperty property = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty());
        property.inject(context, beanInstance);
        verify(beanInstance).getClass();
        verify(property).set(Mockito.eq(beanInstance), Mockito.eq(injectableValue));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object beanInstance = new Object() {};
        CreatorProperty property = Mockito.spy(new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty()));
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(property._fallbackSetter.getClass()).thenReturn(JsonDeserializer.class);
        doAnswer(invocationOnMock -> property._fallbackSetter.set(beanInstance, invocationOnMock.getArgument(0))).when(property._fallbackSetter).set(beanInstance, Mockito.any());

        property.deserializeAndSet(jsonParser, context, beanInstance);
        verify(jsonParser).nextToken();
        verify(jsonParser).getValueAsString();
        verify(context).findContextualValue("testProperty", beanInstance, JsonDeserializer.class);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object beanInstance = new Object() {};
        CreatorProperty property = Mockito.spy(new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty()));
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(property._fallbackSetter.getClass()).thenReturn(JsonDeserializer.class);
        when(property._fallbackSetter.setAndReturn(beanInstance, Mockito.any())).thenReturn(new Object() {});

        Object result = property.deserializeSetAndReturn(jsonParser, context, beanInstance);
        verify(jsonParser).nextToken();
        verify(jsonParser).getValueAsString();
        verify(context).findContextualValue("testProperty", beanInstance, JsonDeserializer.class);
        assertEquals(result, property._fallbackSetter.setAndReturn(beanInstance, Mockito.any()));
    }

    @Test
    public void testSet() throws IOException {
        Object beanInstance = new Object() {};
        CreatorProperty property = Mockito.spy(new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty()));

        doAnswer(invocationOnMock -> property._fallbackSetter.set(beanInstance, invocationOnMock.getArgument(0))).when(property).set(beanInstance, Mockito.any());

        property.set(beanInstance, new Object() {});
        verify(beanInstance).getClass();
        verify(property)._fallbackSetter.set(beanInstance, new Object() {});
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object beanInstance = new Object() {};
        CreatorProperty property = Mockito.spy(new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty()));

        when(property._fallbackSetter.setAndReturn(beanInstance, Mockito.any())).thenReturn(new Object() {});

        Object result = property.setAndReturn(beanInstance, new Object() {});
        verify(beanInstance).getClass();
        verify(property)._fallbackSetter.setAndReturn(beanInstance, new Object() {});
        assertEquals(result, new Object() {});
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        CreatorProperty property = new CreatorProperty(PropertyName.construct("testProperty"), JavaType.construct(), PropertyName.construct(""), null, null, null, 0, null, PropertyMetadata.empty());
        property.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), new Object() {});
    }
}