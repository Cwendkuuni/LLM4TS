import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class VarCheckTest {

    private VarCheck varCheck;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        varCheck = new VarCheck(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        varCheck.process(externs, root);
        // Check that the process method executed without any exceptions
    }

    @Test
    public void testVisit_WithNameNode() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        Node n = new Node(Token.NAME, "varName");
        Node parent = new Node(Token.VAR, n);
        Set<String> varsToDeclareInExterns = Sets.newHashSet("varName");
        varCheck.varsToDeclareInExterns = varsToDeclareInExterns;
        varCheck.visit(t, n, parent);
        assertFalse(varCheck.varsToDeclareInExterns.contains("varName"));
    }

    @Test
    public void testVisit_WithInvalidNameNode() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        Node n = new Node(Token.NAME, "varName");
        Node parent = new Node(Token.STRING, n);
        Set<String> varsToDeclareInExterns = Sets.newHashSet("varName");
        varCheck.varsToDeclareInExterns = varsToDeclareInExterns;
        varCheck.visit(t, n, parent);
        assertTrue(varCheck.varsToDeclareInExterns.contains("varName"));
        // Check that the visit method adds the variable to varsToDeclareInExterns
    }

    @Test
    public void testCreateSynthesizedExternVar() {
        String varName = "varName";
        Node nameNode = new Node(Token.NAME, varName);
        varCheck.createSynthesizedExternVar(varName);
        assertTrue(varCheck.getSynthesizedExternsRoot().hasChildren());
    }

    @Test
    public void testGetSynthesizedExternsInput() {
        assertNotNull(varCheck.getSynthesizedExternsInput());
    }

    @Test
    public void testGetSynthesizedExternsRoot() {
        assertNotNull(varCheck.getSynthesizedExternsRoot());
    }

}