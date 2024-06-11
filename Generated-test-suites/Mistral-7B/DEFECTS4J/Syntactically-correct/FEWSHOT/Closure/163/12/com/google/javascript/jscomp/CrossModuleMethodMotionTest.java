package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.rhino.IR;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Test class for CrossModuleMethodMotion.
 */
public class CrossModuleMethodMotionTest {

  private static final Logger logger =
      Logger.getLogger(CrossModuleMethodMotionTest.class.getName());

  private static final String STUB_METHOD_NAME = "JSCompiler_stubMethod";
  private static final String UNSTUB_METHOD_NAME = "JSCompiler_unstubMethod";

  @SuppressWarnings("unchecked")
  @Test
  public void testCrossModuleMethodMotion() {
    // Set up mocks
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    Logger mockLogger = mock(Logger.class);
    IdGenerator idGenerator = new IdGenerator();

    when(moduleA.getName()).thenReturn("moduleA");
    when(moduleB.getName()).thenReturn("moduleB");
    when(passManager.getModuleGraph()).thenReturn(new JSModuleGraph() {
      @Override
      public Collection<JSModule> getModules() {
        return new ArrayList<JSModule>() {{
          add(moduleA);
          add(moduleB);
        }};
      }

      @Override
      public JSModule getModule(String name) {
        if (name.equals("moduleA")) {
          return moduleA;
        }
        if (name.equals("moduleB")) {
          return moduleB;
        }
        return null;
      }
    });

    when(passManager.getLogger()).thenReturn(mockLogger);

    // Create CrossModuleMethodMotion instance
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        mock(AbstractCompiler.class), idGenerator, true);

    // Test process method
    Node externRoot = createNode("// externs for moduleA.js");
    Node root = createNode("// moduleA.js");

    pass.process(externRoot, root);

    assertTrue(idGenerator.hasGeneratedAnyIds());

    // Test moveMethods method
    Node nameInfoNode = createNode("{name: 'nameInfo', deepestCommonModuleRef: moduleA}");
    NameInfo nameInfo = new NameInfo("nameInfo");
    nameInfo.setDeepestCommonModuleRef(moduleA);

    when(analyzer.getAllNameInfo()).thenReturn(new ArrayList<NameInfo>() {{
      add(nameInfo);
    }});

    when(analyzer.getNameInfo(nameInfoNode)).thenReturn(nameInfo);

    pass.moveMethods(new ArrayList<NameInfo>() {{
      add(nameInfo);
    }});

    Node nameInfoParent = nameInfoNode.getParent();
    Node stubCallNode = nameInfoParent.getFirstChild();

    assertTrue(stubCallNode.isCallExpr());
    assertTrue(stubCallNode.getCallSite().getName().equals(STUB_METHOD_NAME));
    assertTrue(stubCallNode.getFirstChild().isNumberLiteral());
    assertTrue(stubCallNode.getLastChild().isFunctionExpression());

    Node unstubCallNode = nameInfoParent.getChild(1);
    assertTrue(unstubCallNode.isCallExpr());
    assertTrue(unstubCallNode.getCallSite().getName().equals(UNSTUB_METHOD_NAME));
    assertTrue(unstubCallNode.getFirstChild().isNumberLiteral());

    Node unstubBodyNode = unstubCallNode.getLastChild();
    assertTrue(unstubBodyNode.isBlock());

    Node unstubFunctionNode = unstubBodyNode.getFirstChild();
    assertTrue(unstubFunctionNode.isFunctionExpression());
    assertTrue(unstubFunctionNode.getFirstChild().isVarDeclList());
    assertTrue(unstubFunctionNode.getFirstChild().getLastChild().isFunctionDeclaration());

    // Test process method with null deepestCommonModuleRef
    nameInfo.setDeepestCommonModuleRef(null);

    pass.process(externRoot, root);

    compilerVerifyError(DiagnosticType.error(
        "JSC_INTERNAL_ERROR_MODULE_DEPEND",
        "null deepest common module"));
  }

  /**
   * Helper method to create Node object.
   * @param code String the node code
   * @return Node object created from the code
   */
  private Node createNode(String code) {
    Node root = new Node();
    Node ast = compiler.parse(code, new CompilerOptions(), root);
    return ast;
  }

  /**
   * Helper method to verify an error message from the logger.
   * @param diagnosticType DiagnosticType the diagnostic type
   */
  private void compilerVerifyError(DiagnosticType diagnosticType) {
    assertTrue(logger.getLevel().intValue() <= Logger.SEVERE.intValue());
    List<Object> messages = new ArrayList<Object>() {{
      add(diagnosticType.getMessage());
    }};
    List<Throwable> thrown = new ArrayList<Throwable>();
    logger.log(Logger.SEVERE, messages.toArray());
    assertTrue(thrown.size() == 1);
    assertTrue(thrown.get(0) instanceof CompilerError);
    CompilerError error = (CompilerError) thrown.get(0);
    assertEquals(diagnosticType, error.getDiagnosticType());
  }
}