import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.IR;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.InstrumentationTestCase;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;

public class CollapseVariableDeclarationsTest extends InstrumentationTestCase {

    private CollapseVariableDeclarations collapseVariableDeclarations;
    private Compiler compiler;

    protected void setUp() throws Exception {
        super.setUp();
        compiler = new Compiler();
        compile(
                "collapse_variable_declarations.js",
                "var a;\n" +
                        "var b = 1;\n" +
                        "var c = 2;\n" +
                        "a = true;\n" +
                        "b = true;\n" +
                        "var c = true;");
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
        compiler.initOptions(compiler.getOptions());
    }

    protected void compile(String fileName, String sourceCode) {
        SourceFile input = SourceFile.fromCode(fileName, sourceCode);
        compiler.compile(SourceFile.fromCode("externs.js", ""), input, compiler.getOptions());
    }

    private class TestCallback implements Callback {
        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node node1) {
            return true;
        }

        @Override
        public void visit(NodeTraversal nodeTraversal, Node node, Node node1) {
            // Do nothing
        }
    }

    @Test
    public void testProcess() {
        compiler.process(new TestCallback());
        collapseVariableDeclarations.process(compiler.getRoot().getFirstChild(), compiler.getRoot());
        String transformedCode = compiler.toSource();
        assertEquals(
                "var a, b = 1, c = 2;\n" +
                        "var a = true;\n" +
                        "var b = true, c = true;",
                transformedCode);
    }
}