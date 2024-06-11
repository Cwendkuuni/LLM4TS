import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {
  
  private ScopedAliases scopedAliases;
  
  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(new AbstractCompiler(),
                                     null,
                                     new CompilerOptions.AliasTransformationHandler() {
                                       @Override
                                       public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName,
                                               SourcePosition<CompilerOptions.AliasTransformation> aliasTransformation) {
                                         return new CompilerOptions.AliasTransformation();
                                       }
                                     });
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node();
    Node root = new Node();
    scopedAliases.process(externs, root);
    // Assert something
  }
  
  @Test
  public void testHotSwapScript() {
    Node root = new Node();
    Node originalRoot = new Node();
    scopedAliases.hotSwapScript(root, originalRoot);
    // Assert something
  }

  @Test
  public void testEnterScope() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null, null, null);
    scopedAliases.enterScope(t);
    // Assert something
  }
  
  @Test
  public void testExitScope() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null, null, null);
    scopedAliases.exitScope(t);
    // Assert something
  }
  
  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null, null, null);
    Node n = new Node();
    Node parent = new Node();
    boolean result = scopedAliases.shouldTraverse(t, n, parent);
    // Assert something
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null, null, null);
    Node n = new Node();
    Node parent = new Node();
    scopedAliases.visit(t, n, parent);
    // Assert something
  }
  
}