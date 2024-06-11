import org.junit.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

  private CreatorCollector creatorCollector;

  @Before
  public void setUp() {
    BeanDescription beanDesc = new BeanDescription();  // instantiate BeanDescription here
    MapperConfig config = new MapperConfig();  // instantiate MapperConfig here
    creatorCollector = new CreatorCollector(beanDesc, config);
  }

  @Test
  public void testSetDefaultCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    creatorCollector.setDefaultCreator(creator);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
  }

  @Test
  public void testAddStringCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    creatorCollector.addStringCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
  }

  @Test
  public void testAddIntCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    creatorCollector.addIntCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
  }

  @Test
  public void testAddLongCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    creatorCollector.addLongCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
  }

  @Test
  public void testAddDoubleCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    creatorCollector.addDoubleCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
  }

  @Test
  public void testAddBooleanCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    creatorCollector.addBooleanCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
  }

  @Test
  public void testAddDelegatingCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    SettableBeanProperty[] injectables = new SettableBeanProperty[1];  // instantiate SettableBeanProperty here
    creatorCollector.addDelegatingCreator(creator, true, injectables);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    assertArrayEquals(injectables, creatorCollector._delegateArgs);
  }

  @Test
  public void testAddPropertyCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();  // instantiate AnnotatedWithParams here
    SettableBeanProperty[] properties = new SettableBeanProperty[1];  // instantiate SettableBeanProperty here
    creatorCollector.addPropertyCreator(creator, true, properties);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
    assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
  }

  @Test
  public void testAddIncompeteParameter() {
    AnnotatedParameter parameter = new AnnotatedParameter();  // instantiate AnnotatedParameter here
    creatorCollector.addIncompeteParameter(parameter);
    assertEquals(parameter, creatorCollector._incompleteParameter);
  }

  @Test
  public void testHasDefaultCreator() {
    creatorCollector._creators[CreatorCollector.C_DEFAULT] = new AnnotatedWithParams();  // set _creators[C_DEFAULT] here
    assertTrue(creatorCollector.hasDefaultCreator());
  }

  @Test
  public void testHasDelegatingCreator() {
    creatorCollector._creators[CreatorCollector.C_DELEGATE] = new AnnotatedWithParams();  // set _creators[C_DELEGATE] here
    assertTrue(creatorCollector.hasDelegatingCreator());
  }
  
  @Test
  public void testHasPropertyBasedCreator() {
    creatorCollector._creators[CreatorCollector.C_PROPS] = new AnnotatedWithParams();  // set _creators[C_PROPS] here
    assertTrue(creatorCollector.hasPropertyBasedCreator());
  }

  @After
  public void tearDown() {
    creatorCollector = null;
  }
}