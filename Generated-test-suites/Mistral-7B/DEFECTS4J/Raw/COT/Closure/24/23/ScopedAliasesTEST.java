import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
  }

  @Test
  public void testProcess_NoGoogScopeBlock() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString("let x = 1;");
    scopedAliases.process(null, root);
    // No check here as no goog.scope block is present
  }

  @Test
  public void testProcess_ValidGoogScopeBlock() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");

    ArgumentCaptor<AliasTransformation> transformationCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    scopedAliases.process(null, root);
    when(transformationHandler.logAliasTransformation(eq(root.getSourceFileName()), any()))
        .thenReturn(transformationCaptor.capture());

    AliasTransformation transformation = transformationCaptor.getValue();
    List<String> aliases = transformation.getAliases();
    assertThat(aliases).containsExactly("dom", "DIV");
  }

  @Test
  public void testProcess_MultipleGoogScopeBlocks() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var dom2 = goog.dom;" +
        "    var DIV2 = dom2.TagName.DIV;" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "  goog.dom.createElement(DIV2);" +
        "});");

    ArgumentCaptor<AliasTransformation> transformationCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    scopedAliases.process(null, root);
    when(transformationHandler.logAliasTransformation(eq(root.getSourceFileName()), any()))
        .thenReturn(transformationCaptor.capture());

    AliasTransformation transformation = transformationCaptor.getValue();
    List<String> aliases = transformation.getAliases();
    assertThat(aliases).containsExactly("dom", "DIV", "dom2", "DIV2");
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_MissingFunction() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString("goog.scope { var dom = goog.dom; }");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCaptor.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_USED_IMPROPERLY);
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_WrongParameters() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString("goog.scope(1, function() {});");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCaptor.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_HAS_BAD_PARAMETERS);
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_ThisKeyword() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString("goog.scope(function() { this.test = 1; });");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCaptor.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_REFERENCES_THIS);
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_ReturnKeyword() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString("goog.scope(function() { return 1; });");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCaptor.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_USES_RETURN);
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_ThrowKeyword() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString("goog.scope(function() { throw new Error(); });");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCaptor.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_USES_THROW);
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_RedefinedAlias() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCaptor.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_ALIAS_REDEFINED);
  }

  @Test
  public void testProcess_InvalidGoogScopeBlock_NonAliasLocal() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.createString(
        "goog.scope(function() {" +
        "  var x = 1;" +
        "});");

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> errorNodeCapture = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    scopedAliases.process(null, root);
    compiler.reportErrorExpectingNone(errorCaptor.capture(), errorNodeCapture.capture(), errorMessageCaptor.capture());

    assertThat(errorCaptor.getValue()).isEqualTo(GOOG_SCOPE_NON_ALIAS_LOCAL);
  }
}