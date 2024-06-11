
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() {
        BeanDescription beanDesc = new BeanDescription(); // ToDo: Add relevant input arguments
        boolean canFixAccess = true; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);
        DeserializationConfig config = new DeserializationConfig(); // ToDo: Add relevant input arguments
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addStringCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addIntCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addLongCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addDoubleCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addBooleanCreator(creator, explicit);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorProperty[] injectables = new CreatorProperty[] {}; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams(); // ToDo: Add relevant input arguments
        boolean explicit = true; // ToDo: Add relevant input arguments
        CreatorProperty[] properties = new CreatorProperty[] {}; // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
    }

    @Test
    public void testAddIncompleteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter(); // ToDo: Add relevant input arguments
        CreatorCollector creatorCollector = new CreatorCollector();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}
