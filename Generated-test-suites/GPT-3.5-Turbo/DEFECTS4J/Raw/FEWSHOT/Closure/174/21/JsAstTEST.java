import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner.ParseResult;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticSourceFile;
import com.google.javascript.rhino.testing.TestErrorReporter;

public class JsAstTest {
    
    private JsAst jsAst;
    private AbstractCompiler compiler;
    private StaticSourceFile sourceFile;
    
    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        sourceFile = mock(StaticSourceFile.class);
        jsAst = new JsAst(sourceFile);
    }
    
    @Test
    public void testConstructor() {
        assertEquals("filename", jsAst.getInputId().getId());
        assertEquals(sourceFile, jsAst.getSourceFile());
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testSetSourceFile() {
        SourceFile file = mock(SourceFile.class);
        when(file.getName()).thenReturn("filename");
        jsAst.setSourceFile(file);
        assertEquals(file, jsAst.getSourceFile());
    }
    
    @Test
    public void testParse_Success() throws IOException {
        String code = "var a = 1;";
        ParseResult result = mock(ParseResult.class);
        Node ast = IR.root().srcref(0, code.length());

        when(sourceFile.getName()).thenReturn("filename");
        when(sourceFile.getCode()).thenReturn(code);
        when(compiler.getParserConfig()).thenReturn(mock(ParserConfig.class));
        when(compiler.getDefaultErrorReporter()).thenReturn(mock(TestErrorReporter.class));
        when(result.ast).thenReturn(ast);

        jsAst.parse(compiler);

        verify(compiler).setOldParseTree("filename", result.oldAst);
        assertEquals(ast, jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testParse_IOException() throws IOException {
        String code = "var a = 1;";

        when(sourceFile.getName()).thenReturn("filename");
        when(sourceFile.getCode()).thenReturn(code);
        when(compiler.getParserConfig()).thenReturn(mock(ParserConfig.class));
        when(compiler.getDefaultErrorReporter()).thenReturn(mock(TestErrorReporter.class));
        doThrow(new IOException()).when(compiler).report(JSError.make(eq(AbstractCompiler.READ_ERROR), eq("filename")));

        jsAst.parse(compiler);

        verify(compiler, never()).setOldParseTree(eq("filename"), any(Node.class));
        assertNull(jsAst.getAstRoot(compiler));
    }
    
    @Test
    public void testParse_NullRoot() throws IOException {
        String code = "";

        when(sourceFile.getName()).thenReturn("filename");
        when(sourceFile.getCode()).thenReturn(code);
        when(compiler.getParserConfig()).thenReturn(mock(ParserConfig.class));
        when(compiler.getDefaultErrorReporter()).thenReturn(mock(TestErrorReporter.class));

        jsAst.parse(compiler);

        assertNull(jsAst.getAstRoot(compiler));
    }

    @Test
    public void testParse_HaltingErrors() throws IOException {
        String code = "var a = 1;";
        ParseResult result = mock(ParseResult.class);
        Node ast = IR.root().srcref(0, code.length());

        when(sourceFile.getName()).thenReturn("filename");
        when(sourceFile.getCode()).thenReturn(code);
        when(compiler.getParserConfig()).thenReturn(mock(ParserConfig.class));
        when(compiler.getDefaultErrorReporter()).thenReturn(mock(TestErrorReporter.class));
        when(result.ast).thenReturn(ast);
        when(compiler.hasHaltingErrors()).thenReturn(true);

        jsAst.parse(compiler);

        verify(compiler, never()).setOldParseTree(eq("filename"), any(Node.class));
        assertNull(jsAst.getAstRoot(compiler));
    }

}