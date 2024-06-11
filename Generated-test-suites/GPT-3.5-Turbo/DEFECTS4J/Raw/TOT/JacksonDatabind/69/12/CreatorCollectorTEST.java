import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

  @Test
  public void testDefaultCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    creatorCollector.setDefaultCreator(creator);
    assertSame(creator, creatorCollector.getDefaultCreator());
  }

  @Test
  public void testStringCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    creatorCollector.addStringCreator(creator);
    assertSame(creator, creatorCollector.getStringCreator());
  }

  @Test
  public void testIntCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    creatorCollector.addIntCreator(creator);
    assertSame(creator, creatorCollector.getIntCreator());
  }

  @Test
  public void testLongCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    creatorCollector.addLongCreator(creator);
    assertSame(creator, creatorCollector.getLongCreator());
  }

  @Test
  public void testDoubleCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    creatorCollector.addDoubleCreator(creator);
    assertSame(creator, creatorCollector.getDoubleCreator());
  }

  @Test
  public void testBooleanCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    creatorCollector.addBooleanCreator(creator);
    assertSame(creator, creatorCollector.getBooleanCreator());
  }

  @Test
  public void testDelegatingCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    SettableBeanProperty[] injectables = Mockito.mock(SettableBeanProperty[].class);
    creatorCollector.addDelegatingCreator(creator, injectables);
    assertSame(creator, creatorCollector.getDelegatingCreator());
    assertSame(injectables, creatorCollector.getDelegatingCreatorArgs());
  }

  @Test
  public void testPropertyCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
    SettableBeanProperty[] properties = Mockito.mock(SettableBeanProperty[].class);
    creatorCollector.addPropertyCreator(creator, properties);
    assertSame(creator, creatorCollector.getPropertyCreator());
    assertSame(properties, creatorCollector.getPropertyCreatorArgs());
  }

  @Test
  public void testIncompleteParameter(){
    CreatorCollector creatorCollector = new CreatorCollector();
    AnnotatedParameter parameter = Mockito.mock(AnnotatedParameter.class);
    creatorCollector.addIncompleteParameter(parameter);
    assertSame(parameter, creatorCollector.getIncompleteParameter());
  }

}