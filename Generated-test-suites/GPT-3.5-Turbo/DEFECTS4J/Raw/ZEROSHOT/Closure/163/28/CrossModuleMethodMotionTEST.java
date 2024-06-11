import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
  
  private CrossModuleMethodMotion methodMotion;

  @Before
  public void setUp() {
    methodMotion = new CrossModuleMethodMotion();
  }

  @After
  public void tearDown() {
    methodMotion = null;
  }

  @Test
  public void testProcess() {
    // Test with minimum number of modules
    methodMotion.process(new Node(), new Node());

    // Test with more than 1 module
    methodMotion.process(new Node(), new Node());
    methodMotion.process(new Node(), new Node());
    methodMotion.process(new Node(), new Node());
  }

  @Test
  public void testMoveMethods() {
    Node externRoot = new Node();
    Node root = new Node();
    methodMotion.moveMethods(externRoot, root);
  }

  @Test
  public void testHasGeneratedAnyIds() {
    assertFalse(methodMotion.hasGeneratedAnyIds());

    // Generate an id
    methodMotion.newId();
    assertTrue(methodMotion.hasGeneratedAnyIds());
  }

  @Test
  public void testNewId() {
    int id = methodMotion.newId();
    assertEquals(id, 0);
  }
}