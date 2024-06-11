
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

    @Test
    public void testConstructor() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);

        Assert.assertNotNull(jsAst.getInputId());
        Assert.assertEquals(sourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);

        Node astRoot = jsAst.getAstRoot(compiler);

        Assert.assertNotNull(astRoot);
        Mockito.verify(compiler).setOldParseTree(Mockito.eq(sourceFile.getName()), Mockito.any());
    }

    @Test
    public void testClearAst() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);
        jsAst.clearAst();

        Assert.assertNull(jsAst.getAstRoot(null));
        Mockito.verify(sourceFile).clearCachedSource();
    }

    @Test
    public void testGetInputId() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);

        InputId inputId = jsAst.getInputId();

        Assert.assertNotNull(inputId);
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile result = jsAst.getSourceFile();

        Assert.assertEquals(sourceFile, result);
    }

    @Test
    public void testSetSourceFile() {
        SourceFile sourceFile = Mockito.mock(SourceFile.class);
        JsAst jsAst = new JsAst(sourceFile);

        SourceFile newSourceFile = Mockito.mock(SourceFile.class);
        jsAst.setSourceFile(newSourceFile);

        Assert.assertEquals(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse() {
        // Test parse method when source file has a parse error
        AbstractCompiler compiler1 = Mockito.mock(AbstractCompiler.class);
        SourceFile sourceFile1 = Mockito.mock(SourceFile.class);
        JsAst jsAst1 = new JsAst(sourceFile1);
        JsErrorReporter errorReporter = Mockito.mock(JsErrorReporter.class);
        Mockito.when(compiler1.getDefaultErrorReporter()).thenReturn(errorReporter);
        Mockito.when(sourceFile1.getName()).thenReturn("test.js");
        Mockito.when(sourceFile1.getCode()).thenReturn("var x;");
        Mockito.when(compiler1.hasHaltingErrors()).thenReturn(true);

        // Call parse method
        jsAst1.parse(compiler1);

        // Assertions
        Assert.assertNotNull(jsAst1.getAstRoot(null));
        Mockito.verify(compiler1).report(ArgumentMatchers.any(JSError.class));

        // Test parse method when source file has an IOException
        AbstractCompiler compiler2 = Mockito.mock(AbstractCompiler.class);
        SourceFile sourceFile2 = Mockito.mock(SourceFile.class);
        JsAst jsAst2 = new JsAst(sourceFile2);
        Mockito.when(sourceFile2.getName()).thenReturn("test.js");
        Mockito.when(sourceFile2.getCode()).thenThrow(new IOException());

        // Call parse method
        jsAst2.parse(compiler2);

        // Assertions
        Assert.assertNotNull(jsAst2.getAstRoot(null));
        Mockito.verify(compiler2).report(ArgumentMatchers.any(JSError.class));

        // Test parse method when source file is valid
        AbstractCompiler compiler3 = Mockito.mock(AbstractCompiler.class);
        SourceFile sourceFile3 = Mockito.mock(SourceFile.class);
        JsAst jsAst3 = new JsAst(sourceFile3);
        Mockito.when(sourceFile3.getName()).thenReturn("test.js");
        Mockito.when(sourceFile3.getCode()).thenReturn("console.log('Hello, world!');");

        // Call parse method
        jsAst3.parse(compiler3);

        // Assertions
        Assert.assertNotNull(jsAst3.getAstRoot(null));
        Mockito.verify(compiler3).prepareAst(Mockito.any(Node.class));
        Mockito.verify(jsAst3.getAstRoot(null)).setStaticSourceFile(sourceFile3);
    }
}
