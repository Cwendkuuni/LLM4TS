import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreatorCollectorTest {

  @Test
  public void testSetDefaultCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Verify default creator is set correctly
    creatorCollector.setDefaultCreator(creator);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
  }
  
  @Test
  public void testAddStringCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Verify string creator is added correctly
    creatorCollector.addStringCreator(creator, false);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
  }
  
  @Test
  public void testAddIntCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Verify int creator is added correctly
    creatorCollector.addIntCreator(creator, false);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
  }
  
  @Test
  public void testAddLongCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Verify long creator is added correctly
    creatorCollector.addLongCreator(creator, false);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Verify double creator is added correctly
    creatorCollector.addDoubleCreator(creator, false);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Verify boolean creator is added correctly
    creatorCollector.addBooleanCreator(creator, false);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] injectables = new SettableBeanProperty[1];
    
    // Verify delegating creator is added correctly
    creatorCollector.addDelegatingCreator(creator, false, injectables);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    assertEquals(injectables, creatorCollector._delegateArgs);
  }
  
  @Test
  public void testAddPropertyCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] properties = new SettableBeanProperty[1];
    
    // Verify property creator is added correctly
    creatorCollector.addPropertyCreator(creator, false, properties);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
    assertEquals(properties, creatorCollector._propertyBasedArgs);
  }
  
  @Test
  public void testAddIncompeteParameter() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedParameter parameter = new AnnotatedParameter();
    
    // Verify incomplete parameter is added correctly
    creatorCollector.addIncompeteParameter(parameter);
    assertEquals(parameter, creatorCollector._incompleteParameter);
  }
}