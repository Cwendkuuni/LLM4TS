import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

    private VarCheck varCheck;

    @Before
    public void setUp() {
        varCheck = new VarCheck();
    }

    @Test
    public void testProcess_ExternalVariables_ErrorMessage() {
        Node externs = new Node(Token.VAR, new Node(Token.NAME, "externalVar1"));
        Node root = new Node();
        varCheck.process(externs, root);
        assertEquals("Error: Variable externalVar1 is undefined", varCheck.getError());
    }

    @Test
    public void testVisit_ValidDeclaration_NoError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "varName");
        Node parent = new Node(Token.VAR);
        varCheck.visit(t, n, parent);
        assertEquals("", varCheck.getError());
    }

    @Test
    public void testVisit_UndefinedVariable_ErrorMessage() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "var");
        Node parent = new Node(Token.VAR);
        varCheck.visit(t, n, parent);
        assertEquals("Error: Variable var is undefined", varCheck.getError());
    }

    @Test
    public void testVisit_ModuleDependency_ViolationErrorMessage() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "var");
        Node parent = new Node(Token.VAR);
        CompilerInput currInput = new CompilerInput();
        currInput.setModule(new JSModule("currModule"));
        CompilerInput varInput = new CompilerInput();
        varInput.setModule(new JSModule("varModule"));
        t.setInput(currInput);
        Scope scope = new Scope();
        scope.declare("var", n, null, varInput);
        varCheck.visit(t, n, parent);
        assertEquals("Error: Module currModule cannot reference var, defined in module varModule, since varModule loads after currModule", varCheck.getError());
    }

    @Test
    public void testVisit_MissingModuleDependency_WarningMessage() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "var");
        Node parent = new Node(Token.VAR);
        CompilerInput currInput = new CompilerInput();
        currInput.setModule(new JSModule("currModule"));
        CompilerInput varInput = new CompilerInput();
        varInput.setModule(new JSModule("varModule"));
        t.setInput(currInput);
        varCheck.visit(t, n, parent);
        assertEquals("Warning: Missing module dependency; module currModule should depend on module varModule because it references var", varCheck.getError());
    }

    @Test
    public void testVisit_InvalidFunctionDeclaration_ErrorMessage() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "");
        Node parent = new Node(Token.VAR, new Node(Token.FUNCTION));
        varCheck.visit(t, n, parent);
        assertEquals("Error: Syntax error: function declaration must have a name", varCheck.getError());
    }

    @Test
    public void testCreateSynthesizedExternVar_VariableNameConstant_SetIsConstantNameProperty() {
        String varName = "CONSTANT_VAR";
        varCheck.createSynthesizedExternVar(varName);
        Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
        Node synthesizedVar = synthesizedExternsRoot.getFirstChild();
        assertTrue(synthesizedVar.getBooleanProp(Node.IS_CONSTANT_NAME));
    }

    @Test
    public void testGetSynthesizedExternsInput_NoSynthesizedExternsInput_ReturnsNewExternInput() {
        CompilerInput synthesizedExternsInput = varCheck.getSynthesizedExternsInput();
        assertNotNull(synthesizedExternsInput);
    }

    @Test
    public void testGetSynthesizedExternsInput_SynthesizedExternsInputExists_ReturnsExistingExternInput() {
        CompilerInput existingInput = varCheck.getSynthesizedExternsInput();
        CompilerInput synthesizedExternsInput = varCheck.getSynthesizedExternsInput();
        assertEquals(existingInput, synthesizedExternsInput);
    }

    @Test
    public void testGetSynthesizedExternsRoot_NoSynthesizedExternsRoot_ReturnsNewExternRoot() {
        Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
        assertNotNull(synthesizedExternsRoot);
    }

    @Test
    public void testGetSynthesizedExternsRoot_SynthesizedExternsRootExists_ReturnsExistingExternRoot() {
        Node existingRoot = varCheck.getSynthesizedExternsRoot();
        Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
        assertEquals(existingRoot, synthesizedExternsRoot);
    }

}