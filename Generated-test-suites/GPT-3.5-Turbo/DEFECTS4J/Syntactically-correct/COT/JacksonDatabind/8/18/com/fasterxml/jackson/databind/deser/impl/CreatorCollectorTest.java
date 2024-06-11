import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.setDefaultCreator(creator);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addStringCreator(creator, true);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addIntCreator(creator, true);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addLongCreator(creator, true);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addDoubleCreator(creator, true);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addBooleanCreator(creator, true);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[1];
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[1];
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), true);
        creatorCollector.addIncompeteParameter(parameter);
        assertSame(parameter, creatorCollector._incompleteParameter);
    }
}