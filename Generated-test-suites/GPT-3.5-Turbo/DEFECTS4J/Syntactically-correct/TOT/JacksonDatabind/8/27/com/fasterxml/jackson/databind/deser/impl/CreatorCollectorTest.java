import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;

public class CreatorCollectorTest {
  
  @Test
  public void testSetDefaultCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
  }
  
  @Test
  public void testAddStringCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addStringCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
  }
  
  @Test
  public void testAddIntCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addIntCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
  }
  
  @Test
  public void testAddLongCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addLongCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addDoubleCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addBooleanCreator(creator, true);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    CreatorProperty[] properties = new CreatorProperty[1];
    creatorCollector.addDelegatingCreator(creator, true, properties);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
  }
  
  @Test
  public void testAddPropertyCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    CreatorProperty[] properties = new CreatorProperty[1];
    creatorCollector.addPropertyCreator(creator, true, properties);
    assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
  }
  
  @Test
  public void testAddIncompeteParameter() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedParameter parameter = new AnnotatedParameter();
    creatorCollector.addIncompeteParameter(parameter);
    assertEquals(parameter, creatorCollector._incompleteParameter);
  }
  
  @Test
  public void testHasDefaultCreator() {
    CreatorCollector creatorCollector = new CreatorCollector(null, true);
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    assertTrue(creatorCollector.hasDefaultCreator());
  }
  
}