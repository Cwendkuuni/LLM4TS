import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class CreatorCollectorTest {
    CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        BeanDescription beanDescription = new BeanDescription();
        boolean canFixAccess = true;
        creatorCollector = new CreatorCollector(beanDescription, canFixAccess);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.setDefaultCreator(annotatedParameter);
        assertNotNull(creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addStringCreator(annotatedParameter, true);
        assertNotNull(creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addIntCreator(annotatedParameter, true);
        assertNotNull(creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addLongCreator(annotatedParameter, true);
        assertNotNull(creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addDoubleCreator(annotatedParameter, true);
        assertNotNull(creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addBooleanCreator(annotatedParameter, true);
        assertNotNull(creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        CreatorProperty[] injectables = new CreatorProperty[]{};
        creatorCollector.addDelegatingCreator(annotatedParameter, true, injectables);
        assertNotNull(creatorCollector._creators[6]);
        assertNotNull(creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        CreatorProperty[] properties = new CreatorProperty[]{};
        creatorCollector.addPropertyCreator(annotatedParameter, true, properties);
        assertNotNull(creatorCollector._creators[7]);
        assertNotNull(creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(annotatedParameter);
        assertNotNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.setDefaultCreator(annotatedParameter);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
}