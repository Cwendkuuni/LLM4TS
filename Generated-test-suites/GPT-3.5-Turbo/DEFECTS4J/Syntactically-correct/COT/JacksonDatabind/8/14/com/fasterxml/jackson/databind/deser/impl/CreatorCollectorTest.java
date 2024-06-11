import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[0];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    }
    
    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[0];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
    
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertSame(parameter, creatorCollector._incompleteParameter);
    }
}