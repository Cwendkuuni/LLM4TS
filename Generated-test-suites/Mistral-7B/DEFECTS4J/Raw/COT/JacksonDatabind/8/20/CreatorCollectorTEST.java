package com.example.test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private DeserializationConfig config;

    @Before
    public void setup() throws IOException {
        beanDescription = Mockito.mock(BeanDescription.class);
        config = Mockito.mock(DeserializationConfig.class);
        creatorCollector = new CreatorCollector(beanDescription, false);
    }

    // Test the constructor
    @Test
    public void testCreatorCollectorConstructor() {
        assertNotNull(creatorCollector);
    }

    // Test setDefaultCreator method
    @Test
    public void testSetDefaultCreator() throws Exception {
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(defaultCreator);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    // Test addStringCreator method
    @Test
    public void testAddStringCreator() throws Exception {
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator, false);
        assertNotNull(creatorCollector.getCreator(_CreatorCollector.C_STRING));
    }

    // Test addIntCreator method
    @Test
    public void testAddIntCreator() throws Exception {
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(intCreator, false);
        assertNotNull(creatorCollector.getCreator(_CreatorCollector.C_INT));
    }

    // Test addLongCreator method
    @Test
    public void testAddLongCreator() throws Exception {
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(longCreator, false);
        assertNotNull(creatorCollector.getCreator(_CreatorCollector.C_LONG));
    }

    // Test addDoubleCreator method
    @Test
    public void testAddDoubleCreator() throws Exception {
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(doubleCreator, false);
        assertNotNull(creatorCollector.getCreator(_CreatorCollector.C_DOUBLE));
    }

    // Test addBooleanCreator method
    @Test
    public void testAddBooleanCreator() throws Exception {
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(booleanCreator, false);
        assertNotNull(creatorCollector.getCreator(_CreatorCollector.C_BOOLEAN));
    }

    // Test addDelegatingCreator method
    @Test
    public void testAddDelegatingCreator() throws Exception {
        AnnotatedWithParams delegatingCreator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = Mockito.mock(CreatorProperty.class);
        creatorCollector.addDelegatingCreator(delegatingCreator, false, injectables);
        assertNotNull(creatorCollector.getDelegatingCreator());
    }

    // Test addPropertyCreator method
    @Test
    public void testAddPropertyCreator() throws Exception {
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = Mockito.mock(CreatorProperty.class);
        properties[1] = Mockito.mock(CreatorProperty.class);
        creatorCollector.addPropertyCreator(propertyCreator, false, properties);
        assertNotNull(creatorCollector.getPropertyBasedCreator());
    }

    // Test getCreator method
    @Test
    public void testGetCreator() throws Exception {
        Method getCreatorMethod = CreatorCollector.class.getDeclaredMethod("getCreator", int.class);
        getCreatorMethod.setAccessible(true);
        for (int i = 0; i < _CreatorCollector.C_MAX; i++) {
            assertNull(getCreatorMethod.invoke(creatorCollector, i));
        }
    }

    // Test hasNonDefaultCreator method
    @Test
    public void testHasNonDefaultCreator() throws Exception {
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(defaultCreator);
        assertFalse(creatorCollector.hasNonDefaultCreator());

        AnnotatedWithParams nonDefaultCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(nonDefaultCreator, true);
        assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    // Test configureFromObjectSettings method
    @Test
    public void testConfigureFromObjectSettings() throws Exception {
        JavaType delegateType = Mockito.mock(JavaType.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = Mockito.mock(CreatorProperty.class);
        creatorCollector.constructValueInstantiator(config);
        creatorCollector.configureFromObjectSettings(_creators[C_DEFAULT], _creators[C_DELEGATE], delegateType, _delegateArgs, _creators[C_PROPS], _propertyBasedArgs);
        // Check if the correct creators were configured
        for (int i = 0; i < _CreatorCollector.C_MAX; i++) {
            AnnotatedWithParams creator = _creators[i];
            if (creator != null) {
                Class<?> creatorClass = creator.getClass();
                if (i == C_DELEGATE) {
                    assertTrue(creatorClass.isInstance(creatorCollector.getDelegatingCreator()));
                    assertTrue(Arrays.stream(injectables).allMatch(c -> creator.equals(c)));
                    assertNotNull(delegateType);
                } else if (i == C_PROPS) {
                    assertTrue(creatorClass.isInstance(creatorCollector.getPropertyBasedCreator()));
                    assertTrue(Arrays.stream(creatorCollector.getPropertyBasedCreator().getProperties()).allMatch(p -> p != null));
                }
            }
        }
    }

    // Test configureFromStringCreator method
    @Test
    public void testConfigureFromStringCreator() throws Exception {
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.configureFromStringCreator(_creators[C_STRING]);
        assertNotNull(creatorCollector.getStringCreator());
    }

    // Test configureFromIntCreator method
    @Test
    public void testConfigureFromIntCreator() throws Exception {
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.configureFromIntCreator(_creators[C_INT]);
        assertNotNull(creatorCollector.getIntCreator());
    }

    // Test configureFromLongCreator method
    @Test
    public void testConfigureFromLongCreator() throws Exception {
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.configureFromLongCreator(_creators[C_LONG]);
        assertNotNull(creatorCollector.getLongCreator());
    }

    // Test configureFromDoubleCreator method
    @Test
    public void testConfigureFromDoubleCreator() throws Exception {
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.configureFromDoubleCreator(_creators[C_DOUBLE]);
        assertNotNull(creatorCollector.getDoubleCreator());
    }

    // Test configureFromBooleanCreator method
    @Test
    public void testConfigureFromBooleanCreator() throws Exception {
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.configureFromBooleanCreator(_creators[C_BOOLEAN]);
        assertNotNull(creatorCollector.getBooleanCreator());
    }

    // Test configureIncompleteParameter method
    @Test
    public void testConfigureIncompleteParameter() throws Exception {
        AnnotatedParameter incompleteParameter = Mockito.mock(AnnotatedParameter.class);
        creatorCollector.addIncompeteParameter(incompleteParameter);
        assertNotNull(creatorCollector.getIncompleteParameter());
    }
}