
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.RenameLabels;

public class RenameLabelsTest {
    private AbstractCompiler compiler;
    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        compiler = new Compiler();
        renameLabels = new RenameLabels(compiler);
    }

    @Test
    public void testProcess() {
        // Generate test code
        String code = "function testFunction() {\n" +
                "  a: {\n" +
                "    b: {\n" +
                "      foo();\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "  a: {\n" +
                "    b: break a;\n" +
                "  }\n" +
                "}";
        // Compile code
        JsAst ast = JsAst.parse(code);
        compiler.compile(new JsAst[] { ast }, new CompilerOptions());
        // Process labels
        renameLabels.process(null, ast.getAstRoot(compiler));
        // Check if labels are renamed correctly
        String expectedCode = "function testFunction() {\n" +
                "  a1: {\n" +
                "    b1: {\n" +
                "      foo();\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "  a2: {\n" +
                "    b2: break a2;\n" +
                "  }\n" +
                "}";
        assertEquals(expectedCode, ast.toSource());
    }
}
