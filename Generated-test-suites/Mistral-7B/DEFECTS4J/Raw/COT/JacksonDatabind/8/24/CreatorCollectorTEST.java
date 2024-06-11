package com.example.creatorcollector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Annotation;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedNamePropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ValuePropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CreatorCollectorTest {
    private BeanDescription _beanDesc;
    private DeserializationConfig _config;
    private DeserializationContext _context;
    private ObjectMapper _mapper;

    @Before
    public void setup() {
        _config = new DeserializationConfig();
        _context = _config.contextInstance();
        _mapper = new ObjectMapper(_config);
        _beanDesc = _mapper.getTypeFactory().constructType(DummyClass.class);
    }

    @Test
    public void testConstructValueInstantiator_Default() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        ValueInstantiator inst = collector.constructValueInstantiator(_config);
        assertNotNull(inst);
    }

    @Test
    public void testConstructValueInstantiator_HasNonDefaultCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        collector.addIntCreator(new AnnotatedWithParams(new AnnotatedMember(IntCreator.class), null, null));
        ValueInstantiator inst = collector.constructValueInstantiator(_config);
        assertNotNull(inst);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiator_NoCreators() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        ValueInstantiator inst = collector.constructValueInstantiator(_config);
        assertNull(inst);
    }

    @Test
    public void testSetDefaultCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(DummyConstructor.class), null, null);
        collector.setDefaultCreator(creator);
        AnnotatedWithParams result = collector.getCreator(C_DEFAULT);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(DummyConstructor.class, result.getAnnotated().getClass());
    }

    @Test
    public void testAddStringCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(StringCreator.class), null, null);
        collector.addStringCreator(creator, false);
        AnnotatedWithParams result = collector.getCreator(C_STRING);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(StringCreator.class, result.getAnnotated().getClass());
    }

    @Test
    public void testAddIntCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(IntCreator.class), null, null);
        collector.addIntCreator(creator, false);
        AnnotatedWithParams result = collector.getCreator(C_INT);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(IntCreator.class, result.getAnnotated().getClass());
    }

    @Test
    public void testAddBooleanCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(BooleanCreator.class), null, null);
        collector.addBooleanCreator(creator, false);
        AnnotatedWithParams result = collector.getCreator(C_BOOLEAN);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(BooleanCreator.class, result.getAnnotated().getClass());
    }

    @Test
    public void testAddDelegatingCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(DelegateCreator.class), null, null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("injectable", String.class);
        collector.addDelegatingCreator(creator, false, injectables);
        AnnotatedWithParams result = collector.getCreator(C_DELEGATE);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(DelegateCreator.class, result.getAnnotated().getClass());
        assertEquals(1, result.getParameterDescriptors().length);
        assertEquals("injectable", result.getParameterDescriptors()[0].getName());
        assertEquals(String.class, result.getParameterDescriptors()[0].getType());
    }

    @Test
    public void testAddPropertyCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(PropertyCreator.class), null, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("property1", String.class);
        properties[1] = new CreatorProperty("property2", Integer.class);
        collector.addPropertyCreator(creator, false, properties);
        AnnotatedWithParams result = collector.getCreator(C_PROPS);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(PropertyCreator.class, result.getAnnotated().getClass());
        assertEquals(2, result.getParameterDescriptors().length);
        assertEquals("property1", result.getParameterDescriptors()[0].getName());
        assertEquals(String.class, result.getParameterDescriptors()[0].getType());
        assertEquals("property2", result.getParameterDescriptors()[1].getName());
        assertEquals(Integer.class, result.getParameterDescriptors()[1].getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyCreator_DuplicateNames() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMember(PropertyCreator.class), null, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("property1", String.class);
        properties[1] = new CreatorProperty("property1", Integer.class);
        collector.addPropertyCreator(creator, false, properties);
        // this test should fail with an IllegalArgumentException
    }

    @Test
    public void testGetCreator_Default() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        assertNotNull(collector.getCreator(C_DEFAULT));
    }

    @Test
    public void testGetCreator_NoCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        assertNull(collector.getCreator(C_STRING));
    }

    @Test
    public void testFixAccess() throws Exception {
        Class<?> dummyClass = DummyClass.class;
        AnnotatedMember member = new AnnotatedMember(new NestedNamePropertyDefinition("nested", "name", dummyClass, null));
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);
        AnnotatedWithParams result = collector._fixAccess((AnnotatedWithParams) member);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
    }

    @Test
    public void testVerifyNonDup() throws Exception {
        CreatorCollector collector = new CreatorCollector(_beanDesc, true);

        // add the same creator twice, it should be handled as one
        AnnotatedWithParams creator1 = new AnnotatedWithParams(new AnnotatedMember(StringCreator.class), null, null);
        collector.addStringCreator(creator1, false);
        collector.addStringCreator(creator1, false);
        AnnotatedWithParams result = collector.getCreator(C_STRING);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(StringCreator.class, result.getAnnotated().getClass());

        // add a different creator, it should be added as well
        AnnotatedWithParams creator2 = new AnnotatedWithParams(new AnnotatedMember(IntCreator.class), null, null);
        collector.addIntCreator(creator2, false);
        result = collector.getCreator(C_INT);
        assertNotNull(result);
        assertNotNull(result.getAnnotated());
        assertEquals(IntCreator.class, result.getAnnotated().getClass());
    }

    private static class DummyClass {
        // empty class
    }

    private static class DummyConstructor {
        // empty constructor
    }

    private static class StringCreator {
        @java.beans.ConstructorProperties({"string"})
        public StringCreator(String string) {
        }
    }

    private static class IntCreator {
        @java.beans.ConstructorProperties({"int"})
        public IntCreator(int intValue) {
        }
    }

    private static class BooleanCreator {
        @java.beans.ConstructorProperties({"boolean"})
        public BooleanCreator(boolean booleanValue) {
        }
    }

    private static class DelegateCreator {
        @java.beans.ConstructorProperties({"delegate", "injectable"})
        public DelegateCreator(Object delegate, String injectable) {
        }
    }

    private static class PropertyCreator {
        @java.beans.ConstructorProperties({"property1", "property2"})
        public PropertyCreator(String property1, int property2) {
        }
    }
}