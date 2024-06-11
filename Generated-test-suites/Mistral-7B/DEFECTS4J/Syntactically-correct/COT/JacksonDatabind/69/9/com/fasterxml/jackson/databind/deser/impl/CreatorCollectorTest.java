package com.example; // Replace this with the actual package name

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {
    private static final String CLASS_NAME = "com.test.YourTestClass"; // Replace this with the actual class name
    private static final String INVALID_CLASS_NAME = "com.test.InvalidClass"; // Replace this with the actual name of an invalid class
    private MapperConfig<?> config;
    private ObjectMapper mapper;
    private AnnotatedClass annotatedClass;

    @Before
    public void setup() throws Exception {
        config = new MapperConfig(new AnnotationIntrospector());
        mapper = new ObjectMapper(config);
        annotatedClass = mapper.getTypeFactory().constructAnnotatedClass(CLASS_NAME);
    }

    @Test
    public void testDefaultCreator() {
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        BeanDescription beanDesc = annotatedClass.getDefaultMember().getAnnotations().get(0).getAnnotation(BeanDescription.class);
        collector.setDefaultCreator(beanDesc.getRawBeanDescriptor().getConstructors().get(0).getParameterlessConstructor());
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedMethod stringCreator = annotatedClass.findDeclaredMember(new Class[] { String.class }, "stringConstructor"); // Replace this with the actual method name and parameter type
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addStringCreator(new AnnotatedWithParams(stringCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedMethod intCreator = annotatedClass.findDeclaredMember(new Class[] { int.class }, "intConstructor"); // Replace this with the actual method name and parameter type
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addIntCreator(new AnnotatedWithParams(intCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedMethod longCreator = annotatedClass.findDeclaredMember(new Class[] { long.class }, "longConstructor"); // Replace this with the actual method name and parameter type
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addLongCreator(new AnnotatedWithParams(longCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        AnnotatedMethod doubleCreator = annotatedClass.findDeclaredMember(new Class[] { double.class }, "doubleConstructor"); // Replace this with the actual method name and parameter type
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addDoubleCreator(new AnnotatedWithParams(doubleCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedMethod booleanCreator = annotatedClass.findDeclaredMember(new Class[] { boolean.class }, "booleanConstructor"); // Replace this with the actual method name and parameter type
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addBooleanCreator(new AnnotatedWithParams(booleanCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        AnnotatedMethod delegatingCreator = annotatedClass.findDeclaredMember(new Class[] { YourDelegateClass.class }, "delegateConstructor"); // Replace this with the actual method name, class name and parameter type
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty(annotatedClass, "injectableProperty"); // Replace this with the actual property name
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addDelegatingCreator(new AnnotatedWithParams(delegatingCreator), false, injectables);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        AnnotatedMethod propertyCreator = annotatedClass.findDeclaredMember(new Class[] {}, "propertyConstructor"); // Replace this with the actual method name and parameter type
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = new SettableBeanProperty(annotatedClass, "property1"); // Replace this with the actual property name
        properties[1] = new SettableBeanProperty(annotatedClass, "property2"); // Replace this with the actual property name
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addPropertyCreator(new AnnotatedWithParams(propertyCreator), false, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateCreator() throws Exception {
        AnnotatedMethod stringCreator = annotatedClass.findDeclaredMember(new Class[] { String.class }, "stringConstructor"); // Replace this with the actual method name and parameter type
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addStringCreator(new AnnotatedWithParams(stringCreator), false);
        collector.addStringCreator(new AnnotatedWithParams(stringCreator), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreatorClass() throws Exception {
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addStringCreator(new AnnotatedWithParams(new AnnotatedElement() {
            @Override
            public AnnotatedType<AnnotatedType<?>> getAnnotatedType() {
                return null;
            }

            @Override
            public Type getDeclaringType() {
                return Annotation.class;
            }

            @Override
            public AnnotatedType[] getAnnotationTypes() {
                return new AnnotatedType[0];
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public Annotation[] getDeclaredAnnotations() {
                return new Annotation[0];
            }

            @Override
            public Annotation[] getMemberAnnotations() {
                return new Annotation[0];
            }

            @Override
            public String getName() {
                return INVALID_CLASS_NAME;
            }
        }), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(annotatedClass.getAnnotationMembers(), config);
        collector.addStringCreator(null, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
    }
}

class AnnotatedWithParams {
    private final AnnotatedElement _element;

    public AnnotatedWithParams(AnnotatedElement element) {
        _element = element;
    }

    public AnnotatedElement getAnnotated() {
        return _element;
    }

    public Type getRawParameterType(int index) {
        AnnotatedMethod method = (AnnotatedMethod) _element;
        ParameterizedType parameterType = (ParameterizedType) method.getGenericParameterTypes()[0];
        return parameterType.getActualTypeArguments()[index];
    }
}