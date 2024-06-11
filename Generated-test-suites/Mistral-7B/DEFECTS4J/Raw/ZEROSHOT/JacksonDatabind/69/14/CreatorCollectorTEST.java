package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.DeserializerPropertyName;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.NestedCreatorProperty;
import com.fasterxml.jackson.databind.introspect.ValuePropertyAnnotation;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    @InjectMocks
    private CreatorCollector collector;

    private BeanDescription beanDesc;
    private MapperConfig<?> config;
    private DeserializationConfig deserializationConfig;
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        config = Mockito.mock(MapperConfig.class);
        deserializationConfig = Mockito.mock(DeserializationConfig.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        MockitoAnnotations.initMocks(this);
        beanDesc = new BeanDescription(new AnnotatedClass(TestClass.class), new AnnotationIntrospector());
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertNull(instantiator.getDelegateType());
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        String value = "test";

        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getParameterName(""), value);
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        int value = 1;

        collector.addIntCreator(creator, false);
        collector.addIntCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getIntCreator(), value);
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        long value = 1L;

        collector.addLongCreator(creator, false);
        collector.addLongCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getLongCreator(), value);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        double value = 1.0;

        collector.addDoubleCreator(creator, false);
        collector.addDoubleCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getDoubleCreator(), value, 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        boolean value = true;

        collector.addBooleanCreator(creator, false);
        collector.addBooleanCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getBooleanCreator(), value);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty delegateArg1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty delegateArg2 = Mockito.mock(SettableBeanProperty.class);

        collector.addDelegatingCreator(creator, false, new SettableBeanProperty[]{delegateArg1, delegateArg2});

        JavaType delegateType = collector._computeDelegateType(creator, new SettableBeanProperty[0]);
        assertEquals(delegateType.getRawClass(), creator.getParameterType(0).getComponentType());

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getDelegateType(), delegateType);
        assertEquals(instantiator.getDelegateArgs(), new SettableBeanProperty[]{delegateArg1, delegateArg2});
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty arrayDelegateArg1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty arrayDelegateArg2 = Mockito.mock(SettableBeanProperty.class);

        collector.addDelegatingCreator(creator, false, new SettableBeanProperty[]{arrayDelegateArg1, arrayDelegateArg2});

        JavaType arrayDelegateType = collector._computeDelegateType(creator, new SettableBeanProperty[0]);
        assertTrue(arrayDelegateType.isArray());

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getDelegateType(), arrayDelegateType);
        assertEquals(instantiator.getDelegateArgs(), new SettableBeanProperty[]{arrayDelegateArg1, arrayDelegateArg2});
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty propertyBasedArg1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propertyBasedArg2 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propertyBasedArg3 = Mockito.mock(SettableBeanProperty.class);

        collector.addPropertyCreator(creator, false, new SettableBeanProperty[]{propertyBasedArg1, propertyBasedArg2, propertyBasedArg3});

        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertEquals(instantiator.getClass(), StdValueInstantiator.class);
        assertEquals(instantiator.getPropertyBasedArgs(), new SettableBeanProperty[]{propertyBasedArg1, propertyBasedArg2, propertyBasedArg3});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyCreatorWithDuplicateNames() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty propertyBasedArg1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propertyBasedArg2 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propertyBasedArg3 = Mockito.mock(SettableBeanProperty.class);

        collector.addPropertyCreator(creator, false, new SettableBeanProperty[]{propertyBasedArg1, propertyBasedArg2, propertyBasedArg3});
        collector.addPropertyCreator(creator, false, new SettableBeanProperty[]{propertyBasedArg1, propertyBasedArg3});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyCreatorWithIncompleteParameter() throws Exception {
        AnnotatedParameter incompleteParameter = Mockito.mock(AnnotatedParameter.class);

        collector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), false, new SettableBeanProperty[0]);
        collector.addIncompeteParameter(incompleteParameter);
        collector.constructValueInstantiator(deserializationConfig);
    }

    @Test
    public void testCanInstantiateUsingDefault() throws Exception {
        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertTrue(instantiator.canInstantiateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        assertTrue(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCreatingValueUsingDefault() throws Exception {
        ValueInstantiator instantiator = collector.constructValueInstantiator(deserializationConfig);
        Object value = instantiator.createUsingDefault(deserializationConfig);
        assertNotNull(value);
    }

    static class TestClass {
        @JsonCreator
        public TestClass(@JsonProperty("str") String str,
                         @JsonProperty("int") int intValue,
                         @JsonProperty("long") long longValue,
                         @JsonProperty("double") double doubleValue,
                         @JsonProperty("boolean") boolean booleanValue,
                         @JsonProperty("delegate") DelegateClass delegate,
                         @JsonProperty("arrayDelegate") DelegateClass[] arrayDelegate,
                         @JsonProperty("propertyBased") Map<String, Object> propertyBased) {
            // empty constructor
        }

        @JsonProperty
        public String str;
        @JsonProperty
        public int intValue;
        @JsonProperty
        public long longValue;
        @JsonProperty
        public double doubleValue;
        @JsonProperty
        public boolean booleanValue;
        @JsonProperty
        public DelegateClass delegate;
        @JsonProperty
        public DelegateClass[] arrayDelegate;
        @JsonProperty
        public Map<String, Object> propertyBased;

        static class DelegateClass {
            @JsonCreator
            public DelegateClass(@JsonProperty("arg1") String arg1, @JsonProperty("arg2") String arg2) {
                // empty constructor
            }

            @JsonProperty
            public String arg1;
            @JsonProperty
            public String arg2;
        }
    }

}

class AnnotatedWithParams extends AnnotatedMethod {
    // empty constructor
}