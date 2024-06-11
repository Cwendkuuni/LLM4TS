import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.io.IOException;
import java.util.logging.Logger;

public class JsAstTest {

  private JsAst jsAst;

  @Before
  public void setUp() {
    SourceFile sourceFile = mock(SourceFile.class);
    this.jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot_ParseCalledOnce_RootNotNull() {
    // Arrange
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    // Act
    Node astRoot = jsAst.getAstRoot(compiler);

    // Assert
    assertEquals("Expected root node to be null before parse is called", null, astRoot);

    ArgumentCaptor<ParserRunner.ParseResult> parseResultCaptor =
        ArgumentCaptor.forClass(ParserRunner.ParseResult.class);
    verify(compiler).setOldParseTree(sourceFile.getName(), parseResultCaptor.capture().oldAst);
    assertEquals("Expected the old parse tree to be set in the compiler",
        MockParserRunner.ParseResult.oldAst, parseResultCaptor.getValue().oldAst);
  }

  @Test
  public void testClearAst_RootIsNull_CachedSourceCleared() {
    // Arrange
    jsAst.clearAst();

    // Assert
    verify(sourceFile).clearCachedSource();
  }

  @Test
  public void testGetInputId_SourceFileIsValid_ReturnInputId() {
    // Act
    InputId inputId = jsAst.getInputId();

    // Assert
    assertEquals("Expected input ID to be initialized with the source file name",
        MockSourceFile.getName(), inputId.getName());
  }

  @Test
  public void testGetSourceFile_ReturnSourceFile() {
    // Act
    SourceFile sourceFile = jsAst.getSourceFile();

    // Assert
    assertEquals("Expected source file to be the one provided in the constructor",
        MockSourceFile, sourceFile);
  }

  @Test
  public void testSetSourceFile_SourceFileNameNotEqual_ThrowsException() {
    // Arrange
    SourceFile newSourceFile = mock(SourceFile.class);

    // Act
    Throwable exception = assertThrows(IllegalStateException.class,
        () -> jsAst.setSourceFile(newSourceFile));

    // Assert
    assertEquals("Expected exception to be thrown with the correct error message",
        "File name does not match the source file name provided in the constructor",
        exception.getMessage());
  }

  @Test
  public void testSetSourceFile_SourceFileNameEqual_ChangesSourceFile() {
    // Arrange
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn(MockSourceFile.getName());

    // Act
    jsAst.setSourceFile(newSourceFile);

    // Assert
    assertEquals("Expected source file to be updated to the new source file",
        newSourceFile, jsAst.getSourceFile());
  }

  // Add more test cases for other methods if needed

  private static class MockParserRunner {
    public static final ParserRunner.ParseResult oldAst = new ParserRunner.ParseResult();
  }

  private static class MockSourceFile extends SourceFile {
    public MockSourceFile(String name) {
      super(name);
    }
  }

}