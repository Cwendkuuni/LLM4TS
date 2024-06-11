import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        CompilerOptions options = new CompilerOptions();
        AbstractCompiler compiler = new Compiler();
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    }

    @Test
    public void testProcess_ValidInput_CollapseDeclarations() {
        String input = "var a;\n" +
                "var b = 1;\n" +
                "var c = 2;\n";
        String expectedOutput = "var a, b = 1, c = 2;\n";
        Node root = createAST(input);
        collapseVariableDeclarations.process(null, root);
        String output = generateCode(root);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testProcess_ValidInput_CollapseAssignments() {
        String input = "a = true;\n" +
                "b = true;\n" +
                "var c = true;\n";
        String expectedOutput = "var c = b = a = true;\n";
        Node root = createAST(input);
        collapseVariableDeclarations.process(null, root);
        String output = generateCode(root);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testProcess_NoVariableDeclarations_NoChange() {
        String input = "var a = 1;\n" +
                "var b = 2;\n" +
                "var c = 3;\n";
        String expectedOutput = "var a = 1;\n" +
                "var b = 2;\n" +
                "var c = 3;\n";
        Node root = createAST(input);
        collapseVariableDeclarations.process(null, root);
        String output = generateCode(root);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testProcess_IfStatement_VariableDeclarationsNotCollapsed() {
        String input = "if (a) {\n" +
                "    var x = 1;\n" +
                "    var y = 2;\n" +
                "} else {\n" +
                "    var z = 3;\n" +
                "}\n";
        String expectedOutput = "if (a) {\n" +
                "    var x = 1;\n" +
                "    var y = 2;\n" +
                "} else {\n" +
                "    var z = 3;\n" +
                "}\n";
        Node root = createAST(input);
        collapseVariableDeclarations.process(null, root);
        String output = generateCode(root);
        assertEquals(expectedOutput, output);
    }

    private Node createAST(String code) {
        CompilerOptions options = new CompilerOptions();
        AstFactory astFactory = new RhinoErrorReporter();
        SourceFile sourceFile = SourceFile.fromCode("test.js", code);
        Node root = new JsAst(SourceFile.fromCode("externs.js", "")).getAstRoot(astFactory);
        astFactory.setAstRoot(root);
        new JsAst(SourceFile.fromCode("input.js", code)).getAstRoot(astFactory);

        (new JsDocInfoBuilder(false)).build();

        Node externs = options.devMode == DevMode.EVERY_PASS ? new Node(Token.BLOCK) : null;

        NodeTraversal.traverse(compiler, root, new ProcessClosurePrimitives(compiler, options));
        return root;
    }

    private String generateCode(Node node) {
        CodePrinter.Builder builder = new CodePrinter.Builder(node).setPrettyPrint(true).setLineBreak(true);
        return builder.build();
    }
}