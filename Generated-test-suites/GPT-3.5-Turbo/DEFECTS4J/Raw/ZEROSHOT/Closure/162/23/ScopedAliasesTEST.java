import org.junit.*;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = null;
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        Node externs = new Node();
        Node root = new Node();
        scopedAliases.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = null;
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        Node root = new Node();
        Node originalRoot = null;
        scopedAliases.hotSwapScript(root, originalRoot);
    }

    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = null;
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        NodeTraversal t = new NodeTraversal();
        scopedAliases.enterScope(t);
    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = null;
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        NodeTraversal t = new NodeTraversal();
        scopedAliases.exitScope(t);
    }

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = null;
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean result = scopedAliases.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = null;
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        scopedAliases.visit(t, n, parent);
    }
}