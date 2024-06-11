
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreatorPropertyTest {
    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object beanInstance;

    @Mock
    private SettableBeanProperty fallbackSetter;

    @Mock
    private Annotation annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty.findInjectableValue(deserializationContext, beanInstance);
        verify(deserializationContext).findInjectableValue("", creatorProperty, beanInstance);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testFindInjectableValueWithNoInjectableValueId() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, null, null);

        creatorProperty.findInjectableValue(deserializationContext, beanInstance);
        verify(deserializationContext, never()).findInjectableValue(null, null, null);
    }

    @Test
    public void testInject() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        when(creatorProperty.findInjectableValue(deserializationContext, beanInstance))
                .thenReturn("testValue");

        creatorProperty.inject(deserializationContext, beanInstance);
        verify(creatorProperty).set(beanInstance, "testValue");
    }

    @Test
    public void testGetAnnotation() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty._annotated = mock(AnnotatedParameter.class);

        when(creatorProperty._annotated.getAnnotation(Annotation.class)).thenReturn(annotation);

        assertEquals(annotation, creatorProperty.getAnnotation(Annotation.class));
        verify(creatorProperty._annotated).getAnnotation(Annotation.class);
        verifyNoMoreInteractions(creatorProperty._annotated);
    }

    @Test
    public void testGetMember() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty._annotated = mock(AnnotatedParameter.class);

        assertEquals(creatorProperty._annotated, creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 2, "", null);

        assertEquals(2, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty._fallbackSetter = fallbackSetter;

        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, beanInstance);

        verify(creatorProperty._fallbackSetter).set(beanInstance, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty._fallbackSetter = fallbackSetter;

        assertNull(creatorProperty.deserializeSetAndReturn(jsonParser, deserializationContext,
                beanInstance));
        verify(creatorProperty._fallbackSetter).setAndReturn(beanInstance, null);
    }

    @Test
    public void testSet() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty._fallbackSetter = fallbackSetter;

        creatorProperty.set(beanInstance, "testValue");
        verify(creatorProperty._fallbackSetter).set(beanInstance, "testValue");
    }

    @Test
    public void testSetAndReturn() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "", null);

        creatorProperty._fallbackSetter = fallbackSetter;

        assertEquals("returnValue", creatorProperty.setAndReturn(beanInstance, "testValue"));
        verify(creatorProperty._fallbackSetter).setAndReturn(beanInstance, "testValue");
    }

    @Test
    public void testGetInjectableValueId() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(null, null, null, null, null,
                null, 0, "injectableValueId", null);

        assertEquals("injectableValueId", creatorProperty.getInjectableValueId());
    }
}
