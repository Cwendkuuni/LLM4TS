import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {
  
  // Create an instance of CreatorCollector with a BeanDescription and MapperConfig
  CreatorCollector creatorCollector = new CreatorCollector(beanDescription, mapperConfig);
  
  // Test the setDefaultCreator method
  @Test
  public void testSetDefaultCreator() {
    // Create an instance of AnnotatedWithParams for the default creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    
    // Assert that the default creator has been set
    assertEquals(creator, creatorCollector.getDefaultCreator());
  }
  
  // Test the addStringCreator method
  @Test
  public void testAddStringCreator() {
    // Create an instance of AnnotatedWithParams for the string creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addStringCreator(creator);
    
    // Assert that the string creator has been added
    assertTrue(creatorCollector.hasStringCreator());
    assertEquals(creator, creatorCollector.getStringCreator());
  }
  
  // Test the addIntCreator method
  @Test
  public void testAddIntCreator() {
    // Create an instance of AnnotatedWithParams for the int creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addIntCreator(creator);
    
    // Assert that the int creator has been added
    assertTrue(creatorCollector.hasIntCreator());
    assertEquals(creator, creatorCollector.getIntCreator());
  }
  
  // Test the addLongCreator method
  @Test
  public void testAddLongCreator() {
    // Create an instance of AnnotatedWithParams for the long creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addLongCreator(creator);
    
    // Assert that the long creator has been added
    assertTrue(creatorCollector.hasLongCreator());
    assertEquals(creator, creatorCollector.getLongCreator());
  }
  
  // Test the addDoubleCreator method
  @Test
  public void testAddDoubleCreator() {
    // Create an instance of AnnotatedWithParams for the double creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addDoubleCreator(creator);
    
    // Assert that the double creator has been added
    assertTrue(creatorCollector.hasDoubleCreator());
    assertEquals(creator, creatorCollector.getDoubleCreator());
  }
  
  // Test the addBooleanCreator method
  @Test
  public void testAddBooleanCreator() {
    // Create an instance of AnnotatedWithParams for the boolean creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addBooleanCreator(creator);
    
    // Assert that the boolean creator has been added
    assertTrue(creatorCollector.hasBooleanCreator());
    assertEquals(creator, creatorCollector.getBooleanCreator());
  }
  
  // Test the addDelegatingCreator method
  @Test
  public void testAddDelegatingCreator() {
    // Create an instance of AnnotatedWithParams for the delegating creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] injectables = new SettableBeanProperty[1];
    creatorCollector.addDelegatingCreator(creator, injectables);
    
    // Assert that the delegating creator has been added
    assertTrue(creatorCollector.hasDelegatingCreator());
    assertEquals(creator, creatorCollector.getDelegatingCreator());
    assertArrayEquals(injectables, creatorCollector.getDelegatingCreatorInjectables());
  }
  
  // Test the addPropertyCreator method
  @Test
  public void testAddPropertyCreator() {
    // Create an instance of AnnotatedWithParams for the property creator
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] properties = new SettableBeanProperty[1];
    creatorCollector.addPropertyCreator(creator, properties);
    
    // Assert that the property creator has been added
    assertTrue(creatorCollector.hasPropertyCreator());
    assertEquals(creator, creatorCollector.getPropertyCreator());
    assertArrayEquals(properties, creatorCollector.getPropertyCreatorProperties());
  }
  
  // Test the addIncompeteParameter method
  @Test
  public void testAddIncompleteParameter() {
    // Create an instance of AnnotatedParameter for the incomplete parameter
    AnnotatedParameter parameter = new AnnotatedParameter();
    creatorCollector.addIncompleteParameter(parameter);
    
    // Assert that the incomplete parameter has been added
    assertEquals(parameter, creatorCollector.getIncompleteParameter());
  }
}