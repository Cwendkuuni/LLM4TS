import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    scopedAliases.process(externs, root);
    // Add test assertions here
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node root = new Node(Token.ROOT);
    Node originalRoot = new Node(Token.ROOT);
    scopedAliases.hotSwapScript(root, originalRoot);
    // Add test assertions here
  }

  @Test
  public void testTraverse() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node root = new Node(Token.ROOT);
    NodeTraversal.traverse(compiler, root, scopedAliases.new Traversal());
    // Add test assertions here
  }

  @Test
  public void testEnterScope() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node root = new Node(Token.ROOT);
    NodeTraversal traversal = new NodeTraversal(compiler, scopedAliases.new Traversal());
    Node parent = new Node(Token.PARENT_NODE);
    traversal.enterScope(parent);
    // Add test assertions here
  }

  @Test
  public void testExitScope() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node root = new Node(Token.ROOT);
    NodeTraversal traversal = new NodeTraversal(compiler, scopedAliases.new Traversal());
    traversal.exitScope();
    // Add test assertions here
  }

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node root = new Node(Token.ROOT);
    NodeTraversal traversal = new NodeTraversal(compiler, scopedAliases.new Traversal());
    Node node = new Node(Token.NODE);
    Node parent = new Node(Token.PARENT_NODE);
    traversal.shouldTraverse(node, parent);
    // Add test assertions here
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new Compiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> sourceRegion) {
        return new AliasTransformation();
      }
    });
    Node root = new Node(Token.ROOT);
    NodeTraversal traversal = new NodeTraversal(compiler, scopedAliases.new Traversal());
    Node node = new Node(Token.NODE);
    Node parent = new Node(Token.PARENT_NODE);
    traversal.visit(node, parent);
    // Add test assertions here
  }

}