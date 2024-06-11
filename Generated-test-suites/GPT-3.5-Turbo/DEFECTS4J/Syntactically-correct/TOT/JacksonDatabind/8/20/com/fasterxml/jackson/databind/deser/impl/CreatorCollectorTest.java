import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        creatorCollector.addStringCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        creatorCollector.addLongCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        creatorCollector.addDoubleCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        creatorCollector.addBooleanCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        CreatorProperty[] injectables = {new CreatorProperty(null, null)};
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        CreatorProperty[] properties = {new CreatorProperty(null, null)};
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter parameter = new AnnotatedParameter(null, null, 0, null, null);
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}