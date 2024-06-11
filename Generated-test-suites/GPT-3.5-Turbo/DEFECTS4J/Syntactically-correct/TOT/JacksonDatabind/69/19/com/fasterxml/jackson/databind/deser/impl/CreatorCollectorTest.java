import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        creatorCollector = new CreatorCollector();
    }
    
    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(creator);
        assertNotNull(creatorCollector.getDefaultCreator());
    }
    
    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(creator, true);
        assertNotNull(creatorCollector.getStringCreator());
    }
    
    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(creator, true);
        assertNotNull(creatorCollector.getIntCreator());
    }
    
    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(creator, true);
        assertNotNull(creatorCollector.getLongCreator());
    }
    
    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(creator, true);
        assertNotNull(creatorCollector.getDoubleCreator());
    }
    
    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(creator, true);
        assertNotNull(creatorCollector.getBooleanCreator());
    }
    
    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertNotNull(creatorCollector.getDelegatingCreator());
        assertNotNull(creatorCollector.getDelegateArgs());
    }
    
    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertNotNull(creatorCollector.getPropertyCreator());
        assertNotNull(creatorCollector.getPropertyBasedArgs());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(parameter);
        assertNotNull(creatorCollector.getIncompleteParameter());
    }

}