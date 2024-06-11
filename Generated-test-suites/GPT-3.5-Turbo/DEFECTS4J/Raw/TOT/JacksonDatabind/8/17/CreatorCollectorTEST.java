import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        creatorCollector = new CreatorCollector(beanDesc, canFixAccess);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = {new CreatorProperty()};
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        Assert.assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = {new CreatorProperty()};
        creatorCollector.addPropertyCreator(creator, true, properties);
        Assert.assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        Assert.assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        Assert.assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testConstructValueInstantiator() {
        CreatorCollector.ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator();
        Assert.assertTrue(valueInstantiator instanceof CreatorCollector.Vanilla);
    }

    @Test
    public void testHasDefaultCreator() {
        Assert.assertFalse(creatorCollector.hasDefaultCreator());
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        Assert.assertTrue(creatorCollector.hasDefaultCreator());
    }
}