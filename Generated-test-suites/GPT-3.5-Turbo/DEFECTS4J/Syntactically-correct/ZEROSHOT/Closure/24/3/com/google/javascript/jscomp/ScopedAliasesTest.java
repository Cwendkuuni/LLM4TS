import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class ScopedAliasesTest {

    private ScopedAliases scopedAliases;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        Compiler compiler = new Compiler();
        CompilerOptions compilerOptions = new CompilerOptions();
        scopedAliases = new ScopedAliases(compiler, null, null);
        externs = new Node(NodeType.EXTERN_EXPR);
        root = new Node(NodeType.ROOT);
    }

    @Test
    public void process_test() {
        scopedAliases.process(externs, root);
    }

    @Test
    public void hotSwapScript_test() {
        scopedAliases.hotSwapScript(root, null);
    }

    @Test
    public void enterScope_test() {
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        scopedAliases.enterScope(nodeTraversal);
    }

    @Test
    public void exitScope_test() {
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        scopedAliases.exitScope(nodeTraversal);
    }

    @Test
    public void shouldTraverse_test() {
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        scopedAliases.shouldTraverse(nodeTraversal, new Node(NodeType.EMPTY), new Node(NodeType.EMPTY));
    }

    @Test
    public void visit_test() {
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        scopedAliases.visit(nodeTraversal, new Node(NodeType.EMPTY), new Node(NodeType.EMPTY));
    }

    private class Compiler implements AbstractCompiler {

        @Override
        public void reportCodeChange() {
        }

        @Override
        public void report(JSError error) {
        }

        @Override
        public Node getRoot() {
            return root;
        }

        @Override
        public Scope getTopScope() {
            return null;
        }

    }

    private class NodeTraversal {

        private final AbstractCompiler compiler;
        private final Node root;

        public NodeTraversal(AbstractCompiler compiler) {
            this.compiler = compiler;
            this.root = scopedAliases.getRoot();
        }

        public Scope getScope() {
            return new Scope();
        }

        public boolean inGlobalScope() {
            return true;
        }

        public JSError makeError(Node n, DiagnosticType error, String... arguments) {
            return new JSError();
        }

        public int getScopeDepth() {
            return 2;
        }
    }

    private class JSError {

    }

    private class NodeType {

        public static final int EMPTY = 1;
        public static final int EXTERN_EXPR = 2;
        public static final int ROOT = 3;
    }

    private class Scope {

        public Var getVar(String name) {
            return new Var();
        }

        public class Var {

            public Node getInitialValue() {
                return new Node(NodeType.EMPTY);
            }

            public Node getNode() {
                return new Node(NodeType.EMPTY);
            }
        }
    }

    private class Node {
        private final int type;

        public Node(int type) {
            this.type = type;
        }
    }

    private class DiagnosticType {

        public static final int ERROR = 1;

        public DiagnosticType(int code, String name, String description, Severity severity) {
        }

        public static DiagnosticType error(String name, String description) {
            return new DiagnosticType(ERROR, name, description, Severity.ERROR);
        }
    }

    private class Severity {
        public static final Severity ERROR = new Severity();
    }
}