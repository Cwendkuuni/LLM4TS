import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
    
    private CreatorCollector creatorCollector;
    
    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(BeanDescription, MapperConfig);
    }
    
    @Test
    public void testSetDefaultCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.setDefaultCreator(creator);
        
        // Assert
        boolean hasDefaultCreator = creatorCollector.hasDefaultCreator();
        assertTrue(hasDefaultCreator);
    }
    
    @Test
    public void testAddStringCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        
        // Act
        creatorCollector.addStringCreator(creator, explicit);
        
        // Assert
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();
        assertTrue(hasDelegatingCreator);
    }
    
    @Test
    public void testAddIntCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        
        // Act
        creatorCollector.addIntCreator(creator, explicit);
        
        // Assert
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();
        assertTrue(hasDelegatingCreator);
    }
    
    @Test
    public void testAddLongCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        
        // Act
        creatorCollector.addLongCreator(creator, explicit);
        
        // Assert
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();
        assertTrue(hasDelegatingCreator);
    }
    
    @Test
    public void testAddDoubleCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        
        // Act
        creatorCollector.addDoubleCreator(creator, explicit);
        
        // Assert
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();
        assertTrue(hasDelegatingCreator);
    }
    
    @Test
    public void testAddBooleanCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        
        // Act
        creatorCollector.addBooleanCreator(creator, explicit);
        
        // Assert
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();
        assertTrue(hasDelegatingCreator);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        
        // Act
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        
        // Assert
        boolean hasPropertyBasedCreator = creatorCollector.hasPropertyBasedCreator();
        assertTrue(hasPropertyBasedCreator);
    }
    
    @Test
    public void testAddPropertyCreator() {
        // Arrange
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = true;
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        
        // Act
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        
        // Assert
        boolean hasPropertyBasedCreator = creatorCollector.hasPropertyBasedCreator();
        assertTrue(hasPropertyBasedCreator);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        // Arrange
        AnnotatedParameter parameter = new AnnotatedParameter();
        
        // Act
        creatorCollector.addIncompeteParameter(parameter);
        
        // Assert
        assertNotNull(creatorCollector.getIncompleteParameter());
    }
}