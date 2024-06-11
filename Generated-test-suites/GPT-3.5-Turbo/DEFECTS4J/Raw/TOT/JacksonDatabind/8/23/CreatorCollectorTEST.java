import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        BeanDescription beanDescription = new BeanDescription(SomeClass.class);
        creatorCollector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.setDefaultCreator(creator);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertTrue(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.addStringCreator(creator, false);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.addIntCreator(creator, false);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.addLongCreator(creator, false);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.addDoubleCreator(creator, false);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.addBooleanCreator(creator, false);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        CreatorProperty[] properties = new CreatorProperty[]{};
        creatorCollector.addDelegatingCreator(creator, false, properties);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        CreatorProperty[] properties = new CreatorProperty[]{};
        creatorCollector.addPropertyCreator(creator, false, properties);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter(null, null, null);
        creatorCollector.addIncompeteParameter(parameter);
        AnnotatedParameter incompleteParameter = creatorCollector.getIncompleteParameter();
        Assert.assertEquals(parameter, incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        Assert.assertFalse(creatorCollector.hasDefaultCreator());
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.setDefaultCreator(creator);
        Assert.assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testConstructValueInstantiator() {
        AnnotatedWithParams creator = new AnnotatedWithParams() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        creatorCollector.setDefaultCreator(creator);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());
        Assert.assertTrue(valueInstantiator.canCreateUsingDefault());
        Assert.assertEquals(creator.getParameterCount(), 0);
    }
}