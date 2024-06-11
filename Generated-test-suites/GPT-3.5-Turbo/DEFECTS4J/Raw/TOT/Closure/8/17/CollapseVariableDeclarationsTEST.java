
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.WarningLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.BaseJSTypeTestCase;

public class CollapseVariableDeclarationsTest {
    // Test case for process() method
    @Test
    public void testProcess() {
        String sourceCode = "var a;\n" +
                "var b = 1;\n" +
                "var c = 2;";
        String expectedCode = "var a, b = 1, c = 2;";

        AbstractCompiler compiler = createCompiler();
        Node root = parseSourceCode(compiler, sourceCode);

        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        pass.process(null, root);

        String actualCode = compiler.toSource(root);

        Assert.assertEquals(expectedCode, actualCode);
    }

    // Test case for visit() method
    @Test
    public void testVisit() {
        String sourceCode = "function myFunction() {\n" +
                "    var x = 1;\n" +
                "    var y = 2;\n" +
                "    var z = 3;\n" +
                "}";
        String expectedCode = "function myFunction() {\n" +
                "    var x = 1, y = 2, z =3;\n" +
                "}";

        AbstractCompiler compiler = createCompiler();
        Node root = parseSourceCode(compiler, sourceCode);

        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        pass.process(null, root);

        String actualCode = compiler.toSource(root);

        Assert.assertEquals(expectedCode, actualCode);
    }

    // Helper method to create AbstractCompiler instance
    private AbstractCompiler createCompiler() {
        CompilerOptions options = new CompilerOptions();
        options.setWarningLevel(WarningLevel.DEFAULT);
        return new Compiler();
    }

    // Helper method to parse the source code into AST
    private Node parseSourceCode(AbstractCompiler compiler, String sourceCode) {
        Node root = compiler.parseTestCode(sourceCode);
        compiler.reportCodeChange();
        return root;
    }
}
