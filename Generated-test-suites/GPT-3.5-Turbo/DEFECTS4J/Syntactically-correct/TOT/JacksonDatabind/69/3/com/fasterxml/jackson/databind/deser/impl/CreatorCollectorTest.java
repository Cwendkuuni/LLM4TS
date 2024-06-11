import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
  
  private CreatorCollector creatorCollector;
  private BeanDescription beanDescription;
  private MapperConfig mapperConfig;
  
  @Before
  public void setUp() {
    beanDescription = new BeanDescription();
    mapperConfig = new MapperConfig();
    creatorCollector = new CreatorCollector(beanDescription, mapperConfig);
  }
  
  @Test
  public void testSetDefaultCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
  }
  
  @Test
  public void testAddStringCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addStringCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
  }
  
  @Test
  public void testAddIntCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addIntCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
  }
  
  @Test
  public void testAddLongCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addLongCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addDoubleCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addBooleanCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] injectables = new SettableBeanProperty[1];
    creatorCollector.addDelegatingCreator(creator, true, injectables);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    assertEquals(injectables, creatorCollector._delegateArgs);
  }
  
  @Test
  public void testAddPropertyCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] properties = new SettableBeanProperty[1];
    creatorCollector.addPropertyCreator(creator, true, properties);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
    assertEquals(properties, creatorCollector._propertyBasedArgs);
  }
  
  @Test
  public void testAddIncompeteParameter() {
    AnnotatedParameter parameter = new AnnotatedParameter();
    creatorCollector.addIncompeteParameter(parameter);
    assertEquals(parameter, creatorCollector._incompleteParameter);
  }
  
  @Test
  public void testHasDefaultCreator() {
    creatorCollector._creators[CreatorCollector.C_DEFAULT] = new AnnotatedWithParams();
    assertTrue(creatorCollector.hasDefaultCreator());
  }
  
  @Test
  public void testHasDelegatingCreator() {
    creatorCollector._creators[CreatorCollector.C_DELEGATE] = new AnnotatedWithParams();
    assertTrue(creatorCollector.hasDelegatingCreator());
  }
  
  @Test
  public void testHasPropertyBasedCreator() {
    creatorCollector._creators[CreatorCollector.C_PROPS] = new AnnotatedWithParams();
    assertTrue(creatorCollector.hasPropertyBasedCreator());
  }
  
}