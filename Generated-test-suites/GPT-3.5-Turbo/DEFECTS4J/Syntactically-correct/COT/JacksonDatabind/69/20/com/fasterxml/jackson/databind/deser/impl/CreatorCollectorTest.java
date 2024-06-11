import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;

public class CreatorCollectorTest {

    private CreatorCollector creator;

    @Before
    public void setup() {
        creator = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        creator.setDefaultCreator(null);
        assertNotNull(creator._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        creator.addStringCreator(null, false);
        assertNotNull(creator._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        creator.addIntCreator(null, false);
        assertNotNull(creator._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        creator.addLongCreator(null, false);
        assertNotNull(creator._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        creator.addDoubleCreator(null, false);
        assertNotNull(creator._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        creator.addBooleanCreator(null, false);
        assertNotNull(creator._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        creator.addDelegatingCreator(null, false, null);
        assertNotNull(creator._creators[CreatorCollector.C_DELEGATE]);
        assertNotNull(creator._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        creator.addPropertyCreator(null, false, null);
        assertNotNull(creator._creators[CreatorCollector.C_PROPS]);
        assertNotNull(creator._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        creator.addIncompeteParameter(null);
        assertNotNull(creator._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creator._creators[CreatorCollector.C_DEFAULT] = null;
        assertFalse(creator.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        creator._creators[CreatorCollector.C_DELEGATE] = null;
        assertFalse(creator.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creator._creators[CreatorCollector.C_PROPS] = null;
        assertFalse(creator.hasPropertyBasedCreator());
    }

    @Test
    public void testConstructValueInstantiator() {
        assertNotNull(creator.constructValueInstantiator(null));
    }
}