import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() throws IOException {
        BeanDescription beanDesc = new BeanDescription();
        DeserializationConfig config = new DeserializationConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);

        Assert.assertEquals("com.fasterxml.jackson.databind.deser.std.StdValueInstantiator", valueInstantiator.getClass().getName());
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[1];
        creatorCollector.addDelegatingCreator(creator, false, injectables);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        Assert.assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[1];
        creatorCollector.addPropertyCreator(creator, false, properties);

        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        Assert.assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);

        Assert.assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        Assert.assertFalse(creatorCollector.hasDefaultCreator());

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);

        Assert.assertTrue(creatorCollector.hasDefaultCreator());
    }
}