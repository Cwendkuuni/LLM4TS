package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for CreatorCollector class.
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstruction() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        assertEquals(beanDesc, collector.getBeanDescription());
        assertTrue(collector.canFixAccess());
        assertNull(collector.getDefaultCreator());
        assertArrayEquals(new AnnotatedWithParams[8], collector.getCreators());
        assertFalse(collector.hasNonDefaultCreator());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testConstructValueInstantiator() throws Exception {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(createTestConfig());
        assertInstanceOf(StdValueInstantiator.class, instantiator);
        assertEquals(beanDesc.getType(), instantiator.getType());
        assertNull(instantiator.getDefaultCreator());
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams();
        collector.setDefaultCreator(creator);
        AnnotatedWithParams result = collector.getDefaultCreator();
        assertEquals(creator, result);
        assertTrue(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("StringCreator");
        collector.addStringCreator(creator, false);
        AnnotatedWithParams result = collector.getStringCreator();
        assertEquals(creator, result);
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("IntCreator");
        collector.addIntCreator(creator, false);
        AnnotatedWithParams result = collector.getIntCreator();
        assertEquals(creator, result);
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("LongCreator");
        collector.addLongCreator(creator, false);
        AnnotatedWithParams result = collector.getLongCreator();
        assertEquals(creator, result);
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("DoubleCreator");
        collector.addDoubleCreator(creator, false);
        AnnotatedWithParams result = collector.getDoubleCreator();
        assertEquals(creator, result);
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("BooleanCreator");
        collector.addBooleanCreator(creator, false);
        AnnotatedWithParams result = collector.getBooleanCreator();
        assertEquals(creator, result);
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("DelegateCreator");
        CreatorProperty[] injectables = new CreatorProperty[] { createTestCreatorProperty("injectable1"), createTestCreatorProperty("injectable2") };
        collector.addDelegatingCreator(creator, false, injectables);
        AnnotatedWithParams result = collector.getDelegatingCreator();
        assertEquals(creator, result);
        assertEquals(injectables, collector.getDelegateArgs());
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("PropertyCreator");
        CreatorProperty[] properties = new CreatorProperty[] { createTestCreatorProperty("property1"), createTestCreatorProperty("property2") };
        collector.addPropertyCreator(creator, false, properties);
        AnnotatedWithParams result = collector.getPropertyCreator();
        assertEquals(creator, result);
        assertEquals(properties, collector.getPropertyBasedArgs());
        assertFalse(Arrays.equals(new AnnotatedWithParams[8], collector.getCreators()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("Creator1");
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateExplicitCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("Creator1");
        collector.addStringCreator(creator, true);
        collector.addStringCreator(creator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDuplicateExplicitCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = createTestAnnotatedWithParams("Creator1");
        collector.setStringCreator(creator);
        collector.setStringCreator(creator);
    }

    @Test
    public void testGetCreators() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.setDefaultCreator(createTestAnnotatedWithParams("DefaultCreator"));
        collector.addStringCreator(createTestAnnotatedWithParams("StringCreator1"), false);
        collector.addStringCreator(createTestAnnotatedWithParams("StringCreator2"), false);
        collector.addIntCreator(createTestAnnotatedWithParams("IntCreator"), false);
        collector.addLongCreator(createTestAnnotatedWithParams("LongCreator"), false);
        collector.addDoubleCreator(createTestAnnotatedWithParams("DoubleCreator"), false);
        collector.addBooleanCreator(createTestAnnotatedWithParams("BooleanCreator"), false);
        collector.addDelegatingCreator(createTestAnnotatedWithParams("DelegateCreator"), false, new CreatorProperty[0]);
        collector.addPropertyCreator(createTestAnnotatedWithParams("PropertyCreator"), false, new CreatorProperty[] { createTestCreatorProperty("property1"), createTestCreatorProperty("property2") });
        AnnotatedWithParams[] creators = collector.getCreators();
        List<AnnotatedWithParams> expected = Arrays.asList(
                createTestAnnotatedWithParams("DefaultCreator"),
                createTestAnnotatedWithParams("StringCreator1"),
                createTestAnnotatedWithParams("StringCreator2"),
                createTestAnnotatedWithParams("IntCreator"),
                createTestAnnotatedWithParams("LongCreator"),
                createTestAnnotatedWithParams("DoubleCreator"),
                createTestAnnotatedWithParams("BooleanCreator"),
                createTestAnnotatedWithParams("DelegateCreator")
        );
        assertEquals(Collections.unmodifiableList(expected), Arrays.asList(creators));
    }

    @Test
    public void testHasNonDefaultCreator() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.setDefaultCreator(createTestAnnotatedWithParams("DefaultCreator"));
        collector.addStringCreator(createTestAnnotatedWithParams("StringCreator"), false);
        assertTrue(collector.hasNonDefaultCreator());

        collector.setDefaultCreator(null);
        collector.addIntCreator(createTestAnnotatedWithParams("IntCreator"), false);
        assertTrue(collector.hasNonDefaultCreator());

        collector.setDefaultCreator(createTestAnnotatedWithParams("DefaultCreator"));
        collector.addDelegatingCreator(createTestAnnotatedWithParams("DelegateCreator"), false, new CreatorProperty[0]);
        assertTrue(collector.hasNonDefaultCreator());

        collector.setDefaultCreator(null);
        collector.addPropertyCreator(createTestAnnotatedWithParams("PropertyCreator"), false, new CreatorProperty[] { createTestCreatorProperty("property1"), createTestCreatorProperty("property2") });
        assertTrue(collector.hasNonDefaultCreator());

        collector.setDefaultCreator(createTestAnnotatedWithParams("DefaultCreator"));
        assertFalse(collector.hasNonDefaultCreator());
    }

    @Test
    public void testGetDelegateArgs() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addDelegatingCreator(createTestAnnotatedWithParams("DelegateCreator"), false, new CreatorProperty[] { createTestCreatorProperty("injectable1"), createTestCreatorProperty("injectable2") });
        CreatorProperty[] result = collector.getDelegateArgs();
        CreatorProperty[] expected = new CreatorProperty[] { createTestCreatorProperty("injectable1"), createTestCreatorProperty("injectable2") };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetPropertyBasedArgs() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addPropertyCreator(createTestAnnotatedWithParams("PropertyCreator"), false, new CreatorProperty[] { createTestCreatorProperty("property1"), createTestCreatorProperty("property2") });
        CreatorProperty[] result = collector.getPropertyBasedArgs();
        CreatorProperty[] expected = new CreatorProperty[] { createTestCreatorProperty("property1"), createTestCreatorProperty("property2") };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetIncompleteParameter() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedParameter parameter = createTestAnnotatedParameter();
        collector.addIncompleteParameter(parameter);
        AnnotatedParameter result = collector.getIncompleteParameter();
        assertEquals(parameter, result);
    }

    private static BeanDescription createTestBeanDescription() {
        JavaType type = TypeBindings.TypeFactory.constructType(TestBean.class);
        BeanDescription beanDesc = new BeanDescription(type, null, null, null, null);
        return beanDesc;
    }

    private static AnnotatedWithParams createTestAnnotatedWithParams() {
        return createTestAnnotatedWithParams("Creator");
    }

    private static AnnotatedWithParams createTestAnnotatedWithParams(String name) {
        AnnotatedClass ac = new AnnotatedClass(TestBean.class);
        AnnotatedElement anno = new AnnotatedElement() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[] { new JsonCreator() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return JsonCreator.class;
                    }
                    @Override
                    public String value() {
                        return name;
                    }
                }};
            }
        };
        return new AnnotatedWithParams(anno);
    }

    private static AnnotatedParameter createTestAnnotatedParameter() {
        AnnotatedMember am = new AnnotatedMember() {
            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[] { new JsonProperty() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return JsonProperty.class;
                    }
                    @Override
                    public String name() {
                        return "property";
                    }
                }};
            }
        };
        return new AnnotatedParameter(am);
    }

    private static CreatorProperty createTestCreatorProperty(String name) {
        return new CreatorProperty() {
            @Override
            public String getName() {
                return name;
            }
        };
    }

    private static DeserializationConfig createTestConfig() {
        return new DeserializationConfig();
    }

    private static ObjectMapper createTestObjectMapper() {
        return new ObjectMapper();
    }

    private static class TestBean {
        @JsonCreator
        public TestBean(String s, int i, long l, double d, boolean b) {
        }
    }
}