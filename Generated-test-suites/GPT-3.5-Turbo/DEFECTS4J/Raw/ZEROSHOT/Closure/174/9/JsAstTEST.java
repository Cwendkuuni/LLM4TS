import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.testing.*;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.*;
import org.junit.*;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {
    @Test
    public void testGetAstRoot() {
        SourceFile sourceFile = new SourceFile("test.js", "some javascript code");
        JsAst jsAst = new JsAst(sourceFile);

        AbstractCompiler compiler = new JSErrorReporter();
        Node astRoot = jsAst.getAstRoot(compiler);

        Assert.assertNotNull(astRoot);
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = new SourceFile("test.js", "some javascript code");
        JsAst jsAst = new JsAst(sourceFile);

        jsAst.clearAst();

        Node astRoot = jsAst.getAstRoot(new JSErrorReporter());
        Assert.assertNull(astRoot);
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = new SourceFile("test.js", "some javascript code");
        JsAst jsAst = new JsAst(sourceFile);

        InputId inputId = jsAst.getInputId();

        Assert.assertNotNull(inputId);
        Assert.assertEquals("test.js", inputId.getIdName());
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "some javascript code");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile returnedSourceFile = jsAst.getSourceFile();

        Assert.assertEquals("test.js", returnedSourceFile.getName());
        Assert.assertEquals("some javascript code", returnedSourceFile.getCode());
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = new SourceFile("test.js", "some javascript code");
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile newSourceFile = new SourceFile("newFile.js", "new javascript code");
        jsAst.setSourceFile(newSourceFile);

        SourceFile returnedSourceFile = jsAst.getSourceFile();
        Assert.assertEquals("newFile.js", returnedSourceFile.getName());
        Assert.assertEquals("new javascript code", returnedSourceFile.getCode());
    }

    @Test
    public void testParse() {
        SourceFile sourceFile = new SourceFile("test.js", "some javascript code");
        JsAst jsAst = new JsAst(sourceFile);

        Compiler compiler = new Compiler();
        jsAst.parse(compiler);

        Node astRoot = jsAst.getAstRoot(compiler);
        Assert.assertNotNull(astRoot);
    }
}

