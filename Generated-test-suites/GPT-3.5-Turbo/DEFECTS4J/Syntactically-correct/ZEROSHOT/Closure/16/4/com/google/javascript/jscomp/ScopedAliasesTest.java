import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScopedAliasesTest {

    private ScopedAliases scopedAliases;
    private AbstractCompiler compiler;
    private PreprocessorSymbolTable preprocessorSymbolTable;
    private CompilerOptions.AliasTransformationHandler transformationHandler;

    @BeforeEach
    void setUp() {
        compiler = new AbstractCompiler();
        preprocessorSymbolTable = new PreprocessorSymbolTable();
        transformationHandler = new CompilerOptions.AliasTransformationHandler();
        scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    }

    @Test
    void process() {
        Node externs = new Node();
        Node root = new Node();
        scopedAliases.process(externs, root);
    }

    @Test
    void hotSwapScript() {
        Node root = new Node();
        Node originalRoot = new Node();
        scopedAliases.hotSwapScript(root, originalRoot);
    }

    @Test
    void enterScope() {
        NodeTraversal t = new NodeTraversal(compiler);
        scopedAliases.enterScope(t);
    }

    @Test
    void exitScope() {
        NodeTraversal t = new NodeTraversal(compiler);
        scopedAliases.exitScope(t);
    }

    @Test
    void shouldTraverse() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node();
        Node parent = new Node();
        assertNotEquals(true, scopedAliases.shouldTraverse(t, n, parent));
    }

    @Test
    void visit() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node();
        Node parent = new Node();
        scopedAliases.visit(t, n, parent);
    }
}