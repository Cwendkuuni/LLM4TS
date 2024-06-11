import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopedAliasesTest {
    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
        scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        scopedAliases.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        Node root = new Node();
        Node originalRoot = new Node();
        scopedAliases.hotSwapScript(root, originalRoot);
    }

    @Test
    public void testAliasedNode() {
        Node aliasReference = new Node();
        Node aliasDefinition = new Node();
        ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
        aliasedNode.applyAlias();
    }

    @Test
    public void testAliasedTypeNode() {
        Node typeReference = new Node();
        String aliasName = "aliasName";
        ScopedAliases.AliasedTypeNode aliasedTypeNode = new ScopedAliases.AliasedTypeNode(typeReference, aliasName);
        aliasedTypeNode.applyAlias();
    }

    @Test
    public void testTraversal() {
        NodeTraversal traversal = new NodeTraversal();
        ScopedAliases.Traversal scopedAliasesTraversal = new ScopedAliases.Traversal();
        traversal.traverse(null, null, scopedAliasesTraversal);

        assertFalse(scopedAliasesTraversal.hasErrors());
        assertEquals(0, scopedAliasesTraversal.getAliasUsages().size());
        assertEquals(0, scopedAliasesTraversal.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliasesTraversal.getScopeCalls().size());
    }
}