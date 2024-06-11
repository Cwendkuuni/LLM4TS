import org.junit.Test;
import static org.junit.Assert.*;

public class RenameVarsTest {

    @Test
    public void testProcess() {
        // create dummy compiler and variables for testing
        AbstractCompiler compiler = new AbstractCompiler();
        String prefix = "test";
        boolean localRenamingOnly = true;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = null;
        RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

        // create dummy nodes and add them to the lists
        Node globalNode = new Node(Token.NAME, "globalNameNode");
        renameVars.globalNameNodes.add(globalNode);
        Node localNode = new Node(Token.NAME, "localNameNode");
        renameVars.localNameNodes.add(localNode);
        String localTempName = "localTemporaryName";
        renameVars.localTempNames.add(localTempName);

        // call the process method
        renameVars.process(null, null);

        // check if names are correctly renamed
        assertEquals(globalNode.getString(), "testGlobalNameNode");
        assertEquals(localNode.getString(), "localTemporaryName");
    }
}