import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertTrue(creatorCollector.hasDefaultCreator());
        assertEquals(creatorCollector._creators[0], creator);
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[1], creator);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[2], creator);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[3], creator);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[4], creator);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[5], creator);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[6], creator);
        assertArrayEquals(creatorCollector._delegateArgs, injectables);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertTrue(creatorCollector.hasNonDefaultCreator());
        assertEquals(creatorCollector._creators[7], creator);
        assertArrayEquals(creatorCollector._propertyBasedArgs, properties);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(creatorCollector._incompleteParameter, parameter);
    }

}