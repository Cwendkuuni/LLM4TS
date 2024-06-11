import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {

    private ScopedAliases scopedAliases;

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
                    @Override
                    public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position) {
                        return new CompilerOptions.AliasTransformation();
                    }
                });
        scopedAliases.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        Node root = new Node();
        Node originalRoot = new Node();
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
                    @Override
                    public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position) {
                        return new CompilerOptions.AliasTransformation();
                    }
                });
        scopedAliases.hotSwapScript(root, originalRoot);
    }

    @Test
    public void testEnterScope() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
                    @Override
                    public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position) {
                        return new CompilerOptions.AliasTransformation();
                    }
                });
        scopedAliases.enterScope(t);
    }

    @Test
    public void testExitScope() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
                    @Override
                    public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position) {
                        return new CompilerOptions.AliasTransformation();
                    }
                });
        scopedAliases.exitScope(t);
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
                    @Override
                    public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position) {
                        return new CompilerOptions.AliasTransformation();
                    }
                });
        boolean result = scopedAliases.shouldTraverse(t, n, parent);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
                    @Override
                    public CompilerOptions.AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position) {
                        return new CompilerOptions.AliasTransformation();
                    }
                });
        scopedAliases.visit(t, n, parent);
    }
}