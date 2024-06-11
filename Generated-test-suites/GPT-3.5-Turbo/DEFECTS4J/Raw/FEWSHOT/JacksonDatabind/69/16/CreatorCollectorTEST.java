import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {
    
    private CreatorCollector creatorCollector;
    
    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector();
    }
    
    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompeteParameter(parameter);
        assertNotNull(creatorCollector._incompleteParameter);
    }
}