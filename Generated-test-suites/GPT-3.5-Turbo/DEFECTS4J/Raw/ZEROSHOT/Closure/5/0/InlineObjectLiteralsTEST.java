import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() throws Exception {
    inlineObjectLiterals = new InlineObjectLiterals();
  }

  @After
  public void tearDown() throws Exception {
    
  }

  @Test
  public void testProcess() {
    // create input nodes
    Node externs = null;
    Node root = null;

    // call the process method
    inlineObjectLiterals.process(externs, root);
    
    // add assertion here

  }

  // add more test methods here

}