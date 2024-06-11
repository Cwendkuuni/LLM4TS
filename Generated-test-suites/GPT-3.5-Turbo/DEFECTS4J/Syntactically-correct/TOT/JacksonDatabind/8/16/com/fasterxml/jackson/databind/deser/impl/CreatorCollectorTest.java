import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: No-arguments constructor
        AnnotatedWithParams creator = new NoArgsConstructorCreator();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);

        // Test case 2: Factory method
        creator = new FactoryMethodCreator();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Single string constructor
        AnnotatedWithParams creator = new SingleStringConstructorCreator();
        creatorCollector.addStringCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Single int constructor
        AnnotatedWithParams creator = new SingleIntConstructorCreator();
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Single long constructor
        AnnotatedWithParams creator = new SingleLongConstructorCreator();
        creatorCollector.addLongCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Single double constructor
        AnnotatedWithParams creator = new SingleDoubleConstructorCreator();
        creatorCollector.addDoubleCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Single boolean constructor
        AnnotatedWithParams creator = new SingleBooleanConstructorCreator();
        creatorCollector.addBooleanCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Delegating constructor with injectable properties
        AnnotatedWithParams creator = new DelegatingConstructorCreator();
        CreatorProperty[] injectables = new CreatorProperty[] {
                new CreatorProperty("property1", null, null, null),
                new CreatorProperty("property2", null, null, null)
        };
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Property-based creator with properties
        AnnotatedWithParams creator = new PropertyBasedCreator();
        CreatorProperty[] properties = new CreatorProperty[] {
                new CreatorProperty("property1", null, null, null),
                new CreatorProperty("property2", null, null, null)
        };
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompleteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test case 1: Incomplete parameter
        AnnotatedParameter parameter = new IncompleteParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}

class BeanDescription {}

class NoArgsConstructorCreator implements AnnotatedWithParams {
    public NoArgsConstructorCreator() {}
}

class FactoryMethodCreator implements AnnotatedWithParams {
    public static NoArgsConstructorCreator create() {
        return new NoArgsConstructorCreator();
    }
}

class SingleStringConstructorCreator implements AnnotatedWithParams {
    public SingleStringConstructorCreator(String s) {}
}

class SingleIntConstructorCreator implements AnnotatedWithParams {
    public SingleIntConstructorCreator(int i) {}
}

class SingleLongConstructorCreator implements AnnotatedWithParams {
    public SingleLongConstructorCreator(long l) {}
}

class SingleDoubleConstructorCreator implements AnnotatedWithParams {
    public SingleDoubleConstructorCreator(double d) {}
}

class SingleBooleanConstructorCreator implements AnnotatedWithParams {
    public SingleBooleanConstructorCreator(boolean b) {}
}

class DelegatingConstructorCreator implements AnnotatedWithParams {
    public DelegatingConstructorCreator(BeanDescription bean, int i, boolean b) {}
}

class PropertyBasedCreator implements AnnotatedWithParams {
    public PropertyBasedCreator(BeanDescription bean, int i, boolean b) {}
}

class IncompleteParameter implements AnnotatedParameter {}