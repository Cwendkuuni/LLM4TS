import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(JUnitParams.JUnit4TestRunnerImpl.class)
public class ScopedAliasesTest {

  @Parameters(name = "{index}: test{0}")
  public static Iterable<Object[]> data() {
    return Lists.newArrayList(new Object[][]{
        {"test_valid_google_scope.js"},
        {"test_invalid_google_scope.js"},
        {"test_alias_redefined.js"},
        {"test_alias_used_improperly.js"},
        {"test_alias_uses_return.js"},
        {"test_alias_uses_throw.js"}
    });
  }

  private static final String SCOPING_METHOD_NAME = ScopedAliases.SCOPING_METHOD_NAME;

  @Nullable
  private static AbstractCompiler compiler;
  @Nullable
  private static PreprocessorSymbolTable preprocessorSymbolTable;
  @Nullable
  private static AliasTransformationHandler transformationHandler;

  @Nullable
  private static Node rootNode;

  @Nullable
  private static ScopedAliases testee;

  @Nullable
  private static List<AliasTransformation> transformations;

  @Nullable
  private static DiagnosticType diagnosticType;

  @Nullable
  private static String fileName;

  @Nullable
  private static SourcePosition sourcePosition;

  @Nullable
  private static Node node;

  @Nullable
  private static Node expectedNode;

  @Nullable
  private static String expectedString;

  @Test
  public void testScopedAliases() {
    
    setup();
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();
    

    // Test valid goog.scope
    testValidGoogleScope();

    // Test invalid goog.scope
    testInvalidGoogleScope();

    // Test alias redefined
    testAliasRedefined();

    // Test alias used improperly
    testAliasUsedImproperly();

    // Test alias uses return
    testAliasUsesReturn();

    // Test alias uses throw
    testAliasUsesThrow();
  }

  private void setup() {
    compiler = new CompilerOptions().getCompiler();
    preprocessorSymbolTable = compiler.getPreprocessorSymbolTable();
    transformationHandler = new TestAliasTransformationHandler();
  }

  private void teardown() {
    if (transformations != null) {
      for (AliasTransformation transformation : transformations) {
        transformation.dispose();
      }
    }
    if (rootNode != null) {
      compiler.disposeTree(rootNode);
    }
    if (compiler != null) {
      compiler.dispose();
    }
  }

  @Test
  public void testValidGoogleScope() {
    setup();
    rootNode = NodePrinter.parseJsFile("test_valid_google_scope.js");
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();

    assertNull(diagnosticType);
  }

  @Test
  public void testInvalidGoogleScope() {
    setup();
    rootNode = NodePrinter.parseJsFile("test_invalid_google_scope.js");
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();

    assertNotNull(diagnosticType);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnosticType);
  }

  @Test
  public void testAliasRedefined() {
    setup();
    rootNode = NodePrinter.parseJsFile("test_alias_redefined.js");
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();

    assertNotNull(diagnosticType);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnosticType);
  }

  @Test
  public void testAliasUsedImproperly() {
    setup();
    rootNode = NodePrinter.parseJsFile("test_alias_used_improperly.js");
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();

    assertNotNull(diagnosticType);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnosticType);
  }

  @Test
  public void testAliasUsesReturn() {
    setup();
    rootNode = NodePrinter.parseJsFile("test_alias_uses_return.js");
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();

    assertNotNull(diagnosticType);
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnosticType);
  }

  @Test
  public void testAliasUsesThrow() {
    setup();
    rootNode = NodePrinter.parseJsFile("test_alias_uses_throw.js");
    testee = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    testee.process(null, rootNode);
    teardown();

    assertNotNull(diagnosticType);
    assertEquals(GOOG_SCOPE_USES_THROW, diagnosticType);
  }
}

class TestAliasTransformationHandler implements AliasTransformationHandler {
  private final List<AliasTransformation> transformations = Lists.newArrayList();

  @Override
  public AliasTransformation logAliasTransformation(String fileName, SourcePosition sourcePosition) {
    AliasTransformation aliasTransformation = new TestAliasTransformation();
    aliasTransformation.setFileName(fileName);
    aliasTransformation.setSourcePosition(sourcePosition);
    transformations.add(aliasTransformation);
    return aliasTransformation;
  }

  public List<AliasTransformation> getTransformations() {
    return transformations;
  }

  public void dispose() {
    for (AliasTransformation transformation : transformations) {
      transformation.dispose();
    }
    transformations.clear();
  }
}

class TestAliasTransformation implements AliasTransformation {
  private String fileName;

  private SourcePosition sourcePosition;

  public void dispose() {
    // No-op
  }

  public String getAlias() {
    return null;
  }

  public String getQualifiedName() {
    return null;
  }

  public String getReplacement() {
    return null;
  }

  public void setAlias(String alias) {
    // No-op
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setSourcePosition(SourcePosition sourcePosition) {
    this.sourcePosition = sourcePosition;
  }

  @Override
  public String toString() {
    return String.format(
        "AliasTransformation[fileName=%s, sourcePosition=%s]", fileName, sourcePosition);
  }
}