import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    parserConfig = Mockito.mock(ParserConfig.class);
    sourceFile = Mockito.mock(SourceFile.class);
  }

  @Test
  public void testJsAstConstructor() {
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getInputId());
    assertNotNull(jsAst.getSourceFile());
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetAstRoot() {
    JsAst jsAst = new JsAst(sourceFile);
    Node root = Mockito.mock(Node.class);
    jsAst.root = root;
    Mockito.when(compiler.getParserConfig()).thenReturn(parserConfig);

    Node actualRoot = jsAst.getAstRoot(compiler);
    assertNotNull(actualRoot);
    Mockito.verify(compiler).prepareAst(root);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = Mockito.mock(SourceFile.class);
    jsAst.setSourceFile(newSourceFile);
    Mockito.verify(sourceFile).clearCachedSource();
    Mockito.verify(jsAst).sourceFile = newSourceFile;
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFileDifferentFileName() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = Mockito.mock(SourceFile.class);
    Mockito.when(newSourceFile.getName()).thenReturn("differentFileName");
    jsAst.setSourceFile(newSourceFile);
  }

  @Test
  public void testParseWithNullCompiler() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithIOException() {
    JsAst jsAst = new JsAst(sourceFile);
    Mockito.when(sourceFile.getCode()).thenThrow(new IOException());
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }
}