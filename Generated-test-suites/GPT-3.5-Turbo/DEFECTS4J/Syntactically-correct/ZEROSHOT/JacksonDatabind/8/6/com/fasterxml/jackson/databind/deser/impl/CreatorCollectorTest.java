import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(BeanDescriptionMock.getInstance(), true);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        creatorCollector.setDefaultCreator(creator);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        creatorCollector.addStringCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        creatorCollector.addIntCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        creatorCollector.addLongCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        creatorCollector.addDoubleCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        creatorCollector.addBooleanCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        CreatorProperty[] injectables = CreatorPropertyMock.getInstance();
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(injectables, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = AnnotatedWithParamsMock.getInstance();
        CreatorProperty[] properties = CreatorPropertyMock.getInstance();
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter annotatedParameter = new AnnotatedParameterMock();
        creatorCollector.addIncompeteParameter(annotatedParameter);
        assertSame(annotatedParameter, creatorCollector._incompleteParameter);
    }
}