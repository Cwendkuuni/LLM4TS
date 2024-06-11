
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {
    @Test
    public void testUndefinedVarError() {
        VarCheck varCheck = new VarCheck(new AbstractCompiler());
        NodeTraversal traversal = new NodeTraversal(new AbstractPostOrderCallback() {
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.NAME) {
                    // Simulating an undefined variable
                    t.getScope().getVar(n.getString()).removeDeclaredType();
                    varCheck.visit(t, n, parent);
                }
            }
        });
        Node scopeRoot = new Node(Token.BLOCK);
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"));
        scopeRoot.addChildToBack(varNode);

        traversal.traverse(null, scopeRoot);
        assertTrue(traversal.getScope().getVar("x").isTypeInferred());
    }

    @Test
    public void testViolatedModuleDepError() {
        AbstractCompiler compiler = new AbstractCompiler();
        JSModuleGraph moduleGraph = compiler.getModuleGraph();
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addDependency(module1, module2);
        
        VarCheck varCheck = new VarCheck(compiler);
        Scope scope = new Scope();
        // Creating a reference from module2 to a variable defined in module1
        scope.declare("x", new Node(Token.NAME, "x"), null, null);
        varCheck.getSynthesizedExternsRoot().addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));

        // Simulating a reference to var x in module2
        NodeTraversal traversal = new NodeTraversal(new AbstractPostOrderCallback() {
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.NAME && n.getString().equals("x")) {
                    varCheck.visit(t, n, parent);
                }
            }
        });
        Node root = new Node(Token.BLOCK);
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"));
        root.addChildToBack(varNode);

        traversal.traverse(null, root);
        assertEquals(1, traversal.getCompiler().getWarningCount());
    }

    @Test
    public void testMissingModuleDepWarning() {
        AbstractCompiler compiler = new AbstractCompiler();
        JSModuleGraph moduleGraph = compiler.getModuleGraph();
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addDependency(module1, module2);

        VarCheck varCheck = new VarCheck(compiler);
        varCheck.getSynthesizedExternsRoot().addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));

        // Simulating a reference to var x in module2
        NodeTraversal traversal = new NodeTraversal(new AbstractPostOrderCallback() {
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.NAME && n.getString().equals("x")) {
                    varCheck.visit(t, n, parent);
                }
            }
        });
        Node root = new Node(Token.BLOCK);
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"));
        root.addChildToBack(varNode);

        traversal.traverse(null, root);
        assertEquals(1, traversal.getCompiler().getWarningCount());
    }

    @Test
    public void testStrictModuleDepError() {
        AbstractCompiler compiler = new AbstractCompiler();
        JSModuleGraph moduleGraph = compiler.getModuleGraph();
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addDependency(module2, module1);

        VarCheck varCheck = new VarCheck(compiler);
        varCheck.getSynthesizedExternsRoot().addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));

        // Simulating a reference to var x in module2
        NodeTraversal traversal = new NodeTraversal(new AbstractPostOrderCallback() {
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.NAME && n.getString().equals("x")) {
                    varCheck.visit(t, n, parent);
                }
            }
        });
        Node root = new Node(Token.BLOCK);
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"));
        root.addChildToBack(varNode);

        traversal.traverse(null, root);
        assertEquals(1, traversal.getCompiler().getErrorCount());
    }

    @Test
    public void testInvalidFunctionDecl() {
        VarCheck varCheck = new VarCheck(new AbstractCompiler());
        NodeTraversal traversal = new NodeTraversal(new AbstractPostOrderCallback() {
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.FUNCTION &&
                    n.getFirstChild().getString().isEmpty()) {
                    varCheck.visit(t, n, parent);
                }
            }
        });
        Node root = new Node(Token.BLOCK);
        Node functionNode = new Node(Token.FUNCTION);
        root.addChildToBack(functionNode);

        traversal.traverse(null, root);
        assertEquals(1, traversal.getCompiler().getErrorCount());
    }
}
