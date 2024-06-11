import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class RenameVarsTest {

    private RenameVars renameVars;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        externs = new Node(Token.JSDOC_INFO);
        root = new Node(Token.JSDOC_INFO);
        AbstractCompiler compiler = new AbstractCompiler();
        renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    }

    @Test
    public void testProcess() {
        renameVars.process(externs, root);
    }

    @Test
    public void testGetNewGlobalName() {
        Node node = new Node(Token.NAME, "oldName");
        String newName = renameVars.getNewGlobalName(node);
    }

    @Test
    public void testGetNewLocalName() {
        Node node = new Node(Token.NAME, "oldName");
        String newName = renameVars.getNewLocalName(node, 0);
    }

    @Test
    public void testGetPseudoName() {
        String pseudoName = renameVars.getPseudoName("s");
    }

    @Test
    public void testReusePreviouslyUsedVariableMap() {
        renameVars.reusePreviouslyUsedVariableMap();
    }

    @Test
    public void testAssignNames() {
        Set<RenameVars.Assignment> varsToRename = new HashSet<>();
        renameVars.assignNames(varsToRename);
    }

    @Test
    public void testFinalizeNameAssignment() {
        RenameVars.Assignment assignment = new RenameVars.Assignment("oldName", null);
        renameVars.finalizeNameAssignment(assignment, "newName");
    }

    @Test
    public void testGetVariableMap() {
        VariableMap variableMap = renameVars.getVariableMap();
    }

    @Test
    public void testOkToRenameVar() {
        boolean isOkToRename = renameVars.okToRenameVar("name", false);
    }
}