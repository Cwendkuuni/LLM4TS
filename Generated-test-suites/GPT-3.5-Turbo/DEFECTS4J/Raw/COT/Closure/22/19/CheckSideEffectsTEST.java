import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.List;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);

        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.process(externs, root);

        List<Node> problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new Compiler();
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checker.hotSwapScript(scriptRoot, originalRoot);

        List<Node> problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }

    @Test
    public void testVisit_noOpStatement() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.VOID);
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }

    @Test
    public void testVisit_noOpStatementWithQualifiedNameAndJSDocInfo() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.NAME);
        n.setJSDocInfo(new JSDocInfo(true));
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }

    @Test
    public void testVisit_noOpStatementWithQualifiedNameAndNoJSDocInfo() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.NAME);
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }

    @Test
    public void testVisit_noOpStatementWithString() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.STRING);
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());
        assertEquals(1, problemNodes.size());
    }

    @Test
    public void testVisit_noOpStatementWithSimpleOp() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.GT);
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());
        assertEquals(1, problemNodes.size());
    }

    @Test
    public void testVisit_noOpStatementWithBlockParent() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.BLOCK);
        Node n = new Node(Token.STRING);
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());
        assertEquals(1, problemNodes.size());
    }

    @Test
    public void testVisit_noOpStatementWithExprResultParent() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        NodeTraversal t = new NodeTraversal(compiler, checker);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.STRING);
        Node grandparent = new Node(Token.COMMA);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(n);

        checker.visit(t, n, parent);

        List<Node> problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());
        assertEquals(1, problemNodes.size());
    }

    @Test
    public void testProtectSideEffects_emptyProblemNodes() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        List<Node> problemNodes = checker.problemNodes;

        checker.protectSideEffects();

        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testProtectSideEffects_withProblemNodes() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        Node n1 = new Node(Token.STRING);
        Node n2 = new Node(Token.STRING);
        Node n3 = new Node(Token.STRING);
        Node parent1 = new Node(Token.COMMA);
        Node parent2 = new Node(Token.COMMA);
        parent1.addChildToBack(n1);
        parent2.addChildToBack(n2);
        parent2.addChildToBack(n3);
        checker.problemNodes.add(parent1);
        checker.problemNodes.add(parent2);

        checker.protectSideEffects();

        assertEquals(2, checker.problemNodes.size());

        Node externs = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
        Node var = externs.getNext();
        Node name = var.getFirstChild();
        assertTrue(name.isName());
        assertEquals("JSCOMPILER_PRESERVE", name.getString());

        Node replacement1 = parent1.getParent();
        assertTrue(replacement1.isCall());
        assertEquals(name, replacement1.getFirstChild());
        assertEquals(n1, replacement1.getLastChild());
        assertEquals(2, replacement1.getChildCount());

        Node replacement2 = parent2.getParent();
        assertTrue(replacement2.isCall());
        assertEquals(name, replacement2.getFirstChild());
        assertEquals(parent2, replacement2.getLastChild());
        assertEquals(2, replacement2.getChildCount());
    }

    @Test
    public void testProtectSideEffects_withProblemNodes_noAncestors() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        Node n1 = new Node(Token.STRING);
        Node parent1 = new Node(Token.STMT_LIST);
        parent1.addChildToBack(n1);
        checker.problemNodes.add(parent1);

        checker.protectSideEffects();

        assertEquals(1, checker.problemNodes.size());

        Node externs = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
        Node var = externs.getNext();
        Node name = var.getFirstChild();
        assertTrue(name.isName());
        assertEquals("JSCOMPILER_PRESERVE", name.getString());

        Node replacement1 = parent1.getParent();
        assertTrue(replacement1.isCall());
        assertEquals(name, replacement1.getFirstChild());
        assertEquals(parent1, replacement1.getLastChild());
        assertEquals(2, replacement1.getChildCount());
    }

    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        checker.addExtern();

        Node externs = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
        assertTrue(externs.hasChildren());
        Node var = externs.getFirstChild();
        assertTrue(var.isVar());
        Node name = var.getFirstChild();
        assertTrue(name.isName());
        assertEquals("JSCOMPILER_PRESERVE", name.getString());
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);

        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        Node script = new Node(Token.SCRIPT);
        root.addChildToBack(script);
        Node call = new Node(Token.CALL);
        script.addChildToBack(call);
        Node name = new Node(Token.NAME, new Node(Token.STRING, new Node(Token.STRING, new Node(Token.STRING, new Node(Token.NAME, "JSCOMPILER_PRESERVE")))));
        call.addChildToBack(name);
        Node expr = new Node(Token.NAME, new Node(Token.STRING, "expr"));
        call.addChildToBack(expr);

        stripper.process(externs, root);

        assertEquals(expr, call.getFirstChild());
    }
}