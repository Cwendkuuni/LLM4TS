import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.IDiagnosticReporter;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for {@link CrossModuleMethodMotion}.
 */
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private JSModuleGraph moduleGraph;
  private AnalyzePrototypeProperties analyzer;
  private CompilerPass pass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    moduleGraph = mock(JSModuleGraph.class);
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, true, false);
    pass = new CrossModuleMethodMotion(compiler, analyzer.idGenerator(), true);
  }

  @Test
  public void testNullDeepestCommonModuleError() {
    when(analyzer.getAllNameInfo()).thenReturn(Collections.emptyList());
    when(compiler.getModuleGraph()).thenReturn(null);

    pass.process(mock(Node.class), mock(Node.class));
    Diagnostics diagnostics = compiler.getDiagnostics();
    ArgumentCaptor<Diagnostics.Error> errorCaptor =
        ArgumentCaptor.forClass(Diagnostics.Error.class);

    pass.process(mock(Node.class), mock(Node.class));

    diagnostics.reportError(errorCaptor.capture());
    assertNotNull(errorCaptor.getValue().getLocation());
    assertNotNull(errorCaptor.getValue().getMessage());
  }

  @Test
  public void testMoveMethod() {
    SourceFile root = new SourceFile("root.js", "source file");
    Node rootNode = compiler.parse(root, null);
    Node moduleNode = new SourceFile("module.js", "module file")
        .getTree();

    when(analyzer.getAllNameInfo())
        .thenReturn(Collections.singletonList(new NameInfo("b", rootNode, null) {
          @Override
          public Collection<Symbol> getDeclarations() {
            return Arrays.asList(
                new Property("b", rootNode, IR.getprop(moduleNode, "prototype"))
                    .setStatic(false)
                    .setFunction(true)
                    .setValue(IR.string("function() {\nreturn 42;\n}")),
                new Property("b", rootNode, IR.getprop(moduleNode, "prototype"))
                    .setStatic(false)
                    .setFunction(true)
                    .setValue(IR.name("Math.random"))
                    .setGetter(true)
                    .setSetter(true),
                new Property("b", rootNode, IR.getprop(moduleNode, "prototype"))
                    .setStatic(false)
                    .setFunction(false)
                    .setValue(IR.number(42)));
          }
        }));

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    pass.process(mock(Node.class), rootNode);

    Node bPropertyRoot = IR.getprop(rootNode, "prototype").getFirstChild();
    Node bPropertyModule = bPropertyRoot.getFirstChild();

    assertFalse(bPropertyModule.isGetterDef());
    assertFalse(bPropertyModule.isSetterDef());
    assertFalse(bPropertyModule.isFunction());

    assertNotNull(bPropertyModule.getFirstChild());
    Node stubCall = bPropertyModule.getFirstChild();
    assertNotNull(stubCall.getExpression(0));
    assertNotNull(stubCall.getExpression(1));

    Node unstubCall = IR.getprop(moduleNode, "prototype")
        .getFirstChild()
        .getFirstChild();
    assertNotNull(unstubCall.getExpression(0));
    assertNotNull(unstubCall.getExpression(1));
  }

  @Test
  public void testNoMoveMethod() {
    SourceFile root = new SourceFile("root.js", "source file");
    Node rootNode = compiler.parse(root, null);
    Node moduleNode = new SourceFile("module.js", "module file")
        .getTree();

    when(analyzer.getAllNameInfo())
        .thenReturn(Collections.singletonList(new NameInfo("b", rootNode, null) {
          @Override
          public Collection<Symbol> getDeclarations() {
            return Arrays.asList(
                new Property("b", rootNode, IR.getprop(moduleNode, "prototype"))
                    .setStatic(false)
                    .setFunction(false)
                    .setValue(IR.number(42)));
          }
        }));

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    pass.process(mock(Node.class), rootNode);

    Node bPropertyRoot = IR.getprop(rootNode, "prototype").getFirstChild();
    Node bPropertyModule = bPropertyRoot.getFirstChild();

    assertFalse(bPropertyModule.isGetterDef());
    assertFalse(bPropertyModule.isSetterDef());
    assertFalse(bPropertyModule.isFunction());

    assertNull(bPropertyModule.getFirstChild());
  }

  @Test
  public void testDeclareStubFunctions() {
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(IR.empty());

    pass.process(mock(Node.class), mock(Node.class));

    Node declarations = IR.getprop(IR.getprop(compiler.getDiagnostics()
        .getRootTree(), "externs"), "JSCompiler_stubMap")
        .getLastChild();

    assertNotNull(declarations);
    assertFalse(declarations.isFunction());
    assertFalse(declarations.isStatement());

    Node functionDeclaration = declarations.getFirstChild();
    assertNotNull(functionDeclaration);
    assertFalse(functionDeclaration.isFunction());
    assertFalse(functionDeclaration.isStatement());
  }
}