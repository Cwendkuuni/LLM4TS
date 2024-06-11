import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

  @Test
  public void testHasDefaultCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    assertFalse(creatorCollector.hasDefaultCreator());
    creatorCollector.setDefaultCreator(null);
    assertFalse(creatorCollector.hasDefaultCreator());
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    assertTrue(creatorCollector.hasDefaultCreator());
  }

  @Test
  public void testHasDelegatingCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    assertFalse(creatorCollector.hasDelegatingCreator());
    creatorCollector.addDelegatingCreator(null, false, null);
    assertFalse(creatorCollector.hasDelegatingCreator());
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addDelegatingCreator(creator, false, null);
    assertTrue(creatorCollector.hasDelegatingCreator());
  }

  @Test
  public void testHasPropertyBasedCreator() {
    CreatorCollector creatorCollector = new CreatorCollector();
    assertFalse(creatorCollector.hasPropertyBasedCreator());
    creatorCollector.addPropertyCreator(null, false, null);
    assertFalse(creatorCollector.hasPropertyBasedCreator());
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addPropertyCreator(creator, false, null);
    assertTrue(creatorCollector.hasPropertyBasedCreator());
  }
}