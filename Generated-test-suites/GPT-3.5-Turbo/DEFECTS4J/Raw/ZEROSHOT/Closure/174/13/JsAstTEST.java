import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();

        Node astRoot = jsAst.getAstRoot(compiler);

        assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.clearAst();

        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        InputId inputId = jsAst.getInputId();

        assertNotNull(inputId);
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile returnedSourceFile = jsAst.getSourceFile();

        assertEquals(sourceFile, returnedSourceFile);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile newSourceFile = new SourceFile("newTestFile.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);

        assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        SourceFile sourceFile = new SourceFile("testFile.js", "var x = 5;");
        JsAst jsAst = new JsAst(sourceFile);
        AbstractCompiler compiler = new AbstractCompiler();

        jsAst.parse(compiler);

        assertNotNull(jsAst.getAstRoot(null));
    }
}