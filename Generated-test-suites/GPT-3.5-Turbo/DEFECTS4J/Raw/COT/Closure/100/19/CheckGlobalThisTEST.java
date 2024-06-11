import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private CheckLevel level;
  
  @Before
  public void setUp() {
    compiler = // Create a mock AbstractCompiler object;
    level = // Create a mock CheckLevel object;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testCheckGlobalThis() {
    // Test case code    
  }

  @Test
  public void testShouldTraverse() {
    // Test case code    
  }

  @Test
  public void testVisit() {
    // Test case code    
  }

  @Test(expected = NullPointerException.class)
  public void testCheckGlobalThis_nullCompiler() {
    checkGlobalThis = new CheckGlobalThis(null, level);
  }

  @Test(expected = NullPointerException.class)
  public void testCheckGlobalThis_nullLevel() {
    checkGlobalThis = new CheckGlobalThis(compiler, null);
  }

  @Test(expected = NullPointerException.class)
  public void testShouldTraverse_nullTraversal() {
    checkGlobalThis.shouldTraverse(null, new Node(), new Node());
  }

  @Test(expected = NullPointerException.class)
  public void testShouldTraverse_nullNode() {
    checkGlobalThis.shouldTraverse(new NodeTraversal(), null, new Node());
  }

  @Test(expected = NullPointerException.class)
  public void testShouldTraverse_nullParent() {
    checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(), null);
  }

  @Test(expected = NullPointerException.class)
  public void testVisit_nullTraversal() {
    checkGlobalThis.visit(null, new Node(), new Node());
  }

  @Test(expected = NullPointerException.class)
  public void testVisit_nullNode() {
    checkGlobalThis.visit(new NodeTraversal(), null, new Node());
  }

  @Test(expected = NullPointerException.class)
  public void testVisit_nullParent() {
    checkGlobalThis.visit(new NodeTraversal(), new Node(), null);
  }

}