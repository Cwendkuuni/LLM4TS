package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = mock(SourceFile.class);
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("function main() {}");

    CompilerOptions options = mock(CompilerOptions.class);
    ParserConfig parserConfig = mock(ParserConfig.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    Logger logger = mock(Logger.class);

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCompilerOptions()).thenReturn(options);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getLogger()).thenReturn(logger);

    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);

    ArgumentCaptor<ParserRunner> parserRunnerCaptor = ArgumentCaptor.forClass(ParserRunner.class);
    ParserRunner parserRunner = parserRunnerCaptor.capture(compiler).getValue();

    assertThat(root).isNotNull();
    assertThat(parserRunner.getInputId()).isEqualTo(new SourceName("test.js"));
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = mock(SourceFile.class);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();

    Preconditions.checkState(jsAst.root == null);
    Preconditions.checkState(sourceFile.getCachedSource().isEmpty());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile oldSourceFile = mock(SourceFile.class);
    SourceFile newSourceFile = mock(SourceFile.class);

    JsAst jsAst = new JsAst(oldSourceFile);
    jsAst.setSourceFile(newSourceFile);

    Preconditions.checkState(jsAst.inputId.getName().equals(newSourceFile.getName()));
    Preconditions.checkState(jsAst.sourceFile == newSourceFile);
  }

  @Test
  public void testParse() {
    SourceFile sourceFile = mock(SourceFile.class);
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("function main() {}");

    CompilerOptions options = mock(CompilerOptions.class);
    ParserConfig parserConfig = mock(ParserConfig.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    Logger logger = mock(Logger.class);

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCompilerOptions()).thenReturn(options);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getLogger()).thenReturn(logger);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    ArgumentCaptor<ParserRunner> parserRunnerCaptor = ArgumentCaptor.forClass(ParserRunner.class);
    ParserRunner parserRunner = parserRunnerCaptor.capture(compiler).getValue();

    ArgumentCaptor<Node> oldAstCaptor = ArgumentCaptor.forClass(Node.class);
    compiler.setOldParseTree(sourceFile.getName(), oldAstCaptor.capture());

    Preconditions.checkState(parserRunner.getInputId().equals(new SourceName("test.js")));
    Preconditions.checkState(jsAst.root != null);
    Preconditions.checkState(oldAstCaptor.getValue() != null);
  }
}