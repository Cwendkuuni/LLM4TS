
import com.google.common.javascript.compiler.AbstractCompiler;
import com.google.common.javascript.jscomp.SourceAst;
import com.google.common.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

    private JsAst jsAst;
    private SourceFile sourceFile;

    @Before
    public void setup() {
        sourceFile = Mockito.mock(SourceFile.class);
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot_NullRoot_ReturnsParsedRoot() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        jsAst.clearAst();

        Node root = jsAst.getAstRoot(compiler);

        Assert.assertNotNull(root);
        Assert.assertEquals(Node.Token.SCRIPT, root.getToken());
        Assert.assertEquals(sourceFile.getName(), root.getSourceFileName());
    }

    @Test
    public void testGetAstRoot_NonNullRoot_ReturnsRootWithoutParsing() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        Node rootNode = Mockito.mock(Node.class);
        rootNode.setSourceFileName(sourceFile.getName());

        jsAst.clearAst();
        jsAst.root = rootNode;

        Node root = jsAst.getAstRoot(compiler);

        Assert.assertSame(rootNode, root);
        Mockito.verifyZeroInteractions(compiler);
    }

    @Test
    public void testClearAst_ClearsAstRootAndSourceFile() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        jsAst.root = Mockito.mock(Node.class);

        jsAst.clearAst();

        Assert.assertNull(jsAst.root);
        Mockito.verify(sourceFile, Mockito.times(1)).clearCachedSource();
    }

    @Test
    public void testGetInputId_ReturnsInputId() {
        InputId inputId = Mockito.mock(InputId.class);
        Mockito.when(sourceFile.getName()).thenReturn("example.js");
        jsAst = new JsAst(sourceFile);

        InputId receivedInputId = jsAst.getInputId();
        
        Assert.assertEquals(inputId.getIdentifier(), receivedInputId.getIdentifier());
    }

    @Test
    public void testGetSourceFile_ReturnsSourceFile() {
        jsAst = new JsAst(sourceFile);

        SourceFile receivedSourceFile = jsAst.getSourceFile();

        Assert.assertEquals(sourceFile, receivedSourceFile);
    }

    @Test
    public void testSetSourceFile_UpdatesSourceFileCorrectly() {
        SourceFile newSourceFile = Mockito.mock(SourceFile.class);
        Mockito.when(newSourceFile.getName()).thenReturn("example.js");
        jsAst = new JsAst(sourceFile);

        jsAst.setSourceFile(newSourceFile);

        Assert.assertSame(newSourceFile, jsAst.getSourceFile());
    }

    @Test
    public void testParse_SuccessfulParsingWithValidSourceFile_CallsPreparationMethods() throws IOException {
        ParserRunner.ParseResult parseResult = Mockito.mock(ParserRunner.ParseResult.class);
        Node astRoot = Mockito.mock(Node.class);
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        Mockito.when(sourceFile.getName()).thenReturn("example.js");
        Mockito.when(sourceFile.getCode()).thenReturn("var x = 5;");
        Mockito.when(compiler.getParserConfig()).thenReturn(Mockito.mock(Config.class));
        Mockito.when(compiler.getDefaultErrorReporter()).thenReturn(Mockito.mock(ErrorReporter.class));
        Mockito.when(parseResult.ast).thenReturn(astRoot);
        Mockito.when(parseResult.oldAst).thenReturn(Mockito.mock(Node.class));

        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);

        Mockito.verify(compiler, Mockito.times(1)).prepareAst(astRoot);
        Mockito.verify(astRoot, Mockito.times(1)).setStaticSourceFile(sourceFile);
    }

    @Test
    public void testParse_ParseError_CallsCompilerReport() throws IOException {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        Mockito.when(sourceFile.getName()).thenReturn("example.js");
        Mockito.when(sourceFile.getCode()).thenReturn("invalid code");

        JsAst jsAst = new JsAst(sourceFile);
        jsAst.parse(compiler);

        Mockito.verify(compiler, Mockito.times(1)).report(Mockito.any(JSError.class));
    }
}
