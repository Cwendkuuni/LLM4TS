import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsCompilerOptions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ParserRunner;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner.ParseResult;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

@RunWith(MockitoJUnitRunner.class)
public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = Mockito.spy(new AbstractCompiler() {
      @Override
      public void report(JSError error) {
        fail("Should not call report method.");
      }
    });
    sourceFile = Mockito.mock(SourceFile.class);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot_NullCompiler() {
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetAstRoot_ParsedSuccessfully() {
    ParserRunner.ParseResult parseResult = Mockito.mock(ParserRunner.ParseResult.class);
    Node expectedAst = Mockito.mock(Node.class);

    Mockito.when(parserRunner.parse(sourceFile, sourceFile.getCode(), compiler.getParserConfig(),
        compiler.getDefaultErrorReporter(), logger_)).thenReturn(parseResult);
    Mockito.when(parseResult.ast).thenReturn(expectedAst);

    Node actualAst = jsAst.getAstRoot(compiler);
    assertEquals(expectedAst, actualAst);

    ArgumentCaptor<Node> astCaptor = ArgumentCaptor.forClass(Node.class);
    Mockito.verify(compiler).setOldParseTree(sourceFile.getName(), Mockito.any(Node.class));
  }

  @Test
  public void testGetAstRoot_ParsingFailed() {
    jsAst.parse(compiler);

    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testClearAst() {
    jsAst = new JsAst(sourceFile);
    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(compiler));
    assertEquals("", sourceFile.getCode());
  }

  @Test
  public void testGetInputId() {
    Mockito.when(sourceFile.getName()).thenReturn("test.js");

    assertEquals(new InputId("test.js"), jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    String newFileName = "newFile.js";
    SourceFile newSourceFile = Mockito.mock(SourceFile.class);
    Mockito.when(newSourceFile.getName()).thenReturn(newFileName);

    jsAst.setSourceFile(newSourceFile);

    assertEquals(newFileName, jsAst.getSourceFile().getName());
    Preconditions.checkState(newFileName.equals(jsAst.fileName));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFile_DifferentFileName() {
    String newFileName = "newFile.js";
    SourceFile newSourceFile = Mockito.mock(SourceFile.class);
    Mockito.when(newSourceFile.getName()).thenReturn("differentFile.js");

    jsAst.setSourceFile(newSourceFile);
  }
}