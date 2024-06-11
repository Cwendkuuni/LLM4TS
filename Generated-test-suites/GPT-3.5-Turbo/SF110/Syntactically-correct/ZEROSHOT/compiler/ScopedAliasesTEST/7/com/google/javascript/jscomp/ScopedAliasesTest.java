import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    fail("Not yet implemented");
  }

  @Test
  public void testHotSwapScript() {
    fail("Not yet implemented");
  }

  @Test
  public void testEnterScope() {
    fail("Not yet implemented");
  }

  @Test
  public void testExitScope() {
    fail("Not yet implemented");
  }

  @Test
  public void testShouldTraverse() {
    fail("Not yet implemented");
  }

  @Test
  public void testVisit() {
    fail("Not yet implemented");
  }

  @Test
  public void testValidateScopeCall() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSourceRegion() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Node node = new Node(Node.SCRIPT);
    SourcePosition<AliasTransformation> sourceRegion = scopedAliases.getSourceRegion(node);
    assertEquals(0, sourceRegion.getStartPosition().getLine());
    assertEquals(0, sourceRegion.getStartPosition().getColumn());
    assertEquals(Integer.MAX_VALUE, sourceRegion.getEndPosition().getLine());
    assertEquals(Integer.MAX_VALUE, sourceRegion.getEndPosition().getColumn());
  }

  @Test
  public void testFindAliases() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindNamespaceShadows() {
    fail("Not yet implemented");
  }

  @Test
  public void testRenameNamespaceShadows() {
    fail("Not yet implemented");
  }

  @Test
  public void testFixTypeNode() {
    fail("Not yet implemented");
  }

}