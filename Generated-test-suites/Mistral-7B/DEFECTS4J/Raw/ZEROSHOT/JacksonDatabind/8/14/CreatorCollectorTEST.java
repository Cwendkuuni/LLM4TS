package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreatorCollectorTest {

    private BeanDescription beanDescription;
    private DeserializationConfig config;
    private ObjectMapper objectMapper;
    private DeserializationContext context;
    private CreatorCollector collector;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        config = objectMapper.getConfig();
        beanDescription = mock(BeanDescription.class);
        context = mock(DeserializationContext.class);
        collector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        JavaType type = mock(JavaType.class);
        when(beanDescription.getType()).thenReturn(type);
        collector.constructValueInstantiator(config);
        verify(context).configureFromObjectSettings(_creatorFor(null), _creatorFor(null), null, null, _creatorFor(null), null);
    }

    @Test
    public void testDefaultConstructorWithCollectionType() throws Exception {
        JavaType type = mock(JavaType.class);
        when(beanDescription.getType()).thenReturn(type);
        when(type.getRawClass()).thenReturn(ArrayList.class);
        collector.constructValueInstantiator(config);
        verify(context).configureFromObjectSettings(_creatorFor(null), null, null, null, _creatorFor(null), null);
    }

    @Test
    public void testDefaultConstructorWithMapType() throws Exception {
        JavaType type = mock(JavaType.class);
        when(beanDescription.getType()).thenReturn(type);
        when(type.getRawClass()).thenReturn(LinkedHashMap.class);
        collector.constructValueInstantiator(config);
        verify(context).configureFromObjectSettings(_creatorFor(null), null, null, null, _creatorFor(null), null);
    }

    @Test
    public void testDefaultConstructorWithHashmapType() throws Exception {
        JavaType type = mock(JavaType.class);
        when(beanDescription.getType()).thenReturn(type);
        when(type.getRawClass()).thenReturn(HashMap.class);
        collector.constructValueInstantiator(config);
        verify(context).configureFromObjectSettings(_creatorFor(null), null, null, null, _creatorFor(null), null);
    }

    @Test
    public void testNoArgumentConstructor() throws Exception {
        AnnotatedWithParams constructor = _creatorFor(mock(AnnotatedMethod.class));
        collector.setDefaultCreator(constructor);
        collector.constructValueInstantiator(config);
        verify(context).configureFromObjectSettings(constructor, null, null, null, null, null);
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addStringCreator(creator, false);
        collector.constructValueInstantiator(config);
        verify(context).configureFromStringCreator(creator);
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addIntCreator(creator, false);
        collector.constructValueInstantiator(config);
        verify(context).configureFromIntCreator(creator);
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addLongCreator(creator, false);
        collector.constructValueInstantiator(config);
        verify(context).configureFromLongCreator(creator);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addDoubleCreator(creator, false);
        collector.constructValueInstantiator(config);
        verify(context).configureFromDoubleCreator(creator);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addBooleanCreator(creator, false);
        collector.constructValueInstantiator(config);
        verify(context).configureFromBooleanCreator(creator);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = _creatorFor(mock(AnnotatedParameter.class));
        collector.addDelegatingCreator(creator, false, injectables);
        collector.constructValueInstantiator(config);
        verify(context).configureFromObjectSettings(_creatorFor(null), creator, null, injectables, null, null);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = _creatorFor(mock(AnnotatedParameter.class), "name");
        collector.addPropertyCreator(creator, false, properties);
        collector.constructValueInstantiator(config);
        verify(context).configureFromPropertyBasedCreator(creator, properties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addIntCreator(creator, false);
        collector.addIntCreator(creator, false);
        collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = _creatorFor(mock(AnnotatedParameter.class), "name");
        collector.addPropertyCreator(creator, false, properties);
        CreatorProperty[] newProperties = new CreatorProperty[1];
        newProperties[0] = _creatorFor(mock(AnnotatedParameter.class), "name");
        collector.addPropertyCreator(creator, false, newProperties);
        collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
        collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateBooleanCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addBooleanCreator(creator, false);
        collector.addBooleanCreator(creator, false);
        collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIntCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addIntCreator(creator, false);
        collector.addIntCreator(creator, false);
        collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateLongCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addLongCreator(creator, false);
        collector.addLongCreator(creator, false);
        collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDoubleCreator() throws Exception {
        AnnotatedWithParams creator = _creatorFor(mock(AnnotatedMethod.class));
        collector.addDoubleCreator(creator, false);
        collector.addDoubleCreator(creator, false);
        collector.constructValueInstantiator(config);
    }

    private AnnotatedWithParams _creatorFor(AnnotatedMember member) {
        when(member.getName()).thenReturn("creator");
        when(member.isConstructor()).thenReturn(false);
        return new AnnotatedWithParams(member, null);
    }

    private AnnotatedWithParams _creatorFor(AnnotatedParameter parameter) {
        when(parameter.getName()).thenReturn("name");
        return new AnnotatedWithParams(null, new AnnotatedMethod(mock(AnnotatedElement.class), _creatorClassFor(String.class)));
    }

    private AnnotatedWithParams _creatorFor(AnnotatedElement element) {
        when(element.getType()).thenReturn(TypeDeserializer.emptyValueDeserializer().getType(config));
        return new AnnotatedWithParams(null, new AnnotatedMethod(element, _creatorClassFor(String.class)));
    }

    private AnnotatedClass _creatorClassFor(Class<?> clazz) {
        when(beanDescription.findCreatorFromAnnotations(eq(JavaType.constructType(clazz)), anyBoolean())).thenReturn(mock(AnnotatedMethod.class));
        return mock(AnnotatedClass.class);
    }

    private static class AnnotatedWithParams {
        private final AnnotatedMember _member;
        private final AnnotatedMethod _method;

        public AnnotatedWithParams(AnnotatedMember member, AnnotatedMethod method) {
            _member = member;
            _method = method;
        }
    }

    private static class AnnotatedParameter {
        private final String name;

        public AnnotatedParameter(String name) {
            this.name = name;
        }
    }

    private static class AnnotatedMethod {
        private final AnnotatedElement _element;
        private final Class<?> _clazz;

        public AnnotatedMethod(AnnotatedElement element, Class<?> clazz) {
            _element = element;
            _clazz = clazz;
        }
    }

    private static class AnnotatedElement {
    }

    @JsonCreator
    public static class TestBean {
        @JsonProperty
        private String name;

        public TestBean() { }

        public TestBean(String name) {
            this.name = name;
        }
    }
}