import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    traversal = new Traversal();

    when(compiler.reportCodeChange()).thenReturn(true);
    when(transformationHandler.logAliasTransformation(
        anyString(), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation("testAlias", "testQualifiedName"));
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newScope();

    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(
                Node.newBlock(
                    Node.newStatement(
                        Node.newExpressionStatement(
                            Node.newCall(
                                Node.newQualifiedName("goog", "dom"),
                                Lists.newArrayList(
                                    Node.newIdentifier("createElement"),
                                    Node.newString("DIV")
                                )
                            )
                        )
                    )
                ),
                Lists.newArrayList()
            )
        )
    );

    Node rootBefore = rootNode.cloneTree();

    scopedAliases.process(null, rootNode);

    // Check if the scoped aliases were applied.
    assertEquals("goog.dom.createElement(DIV)",
        rootNode.toString(Printer.plainJS));

    // Check if the alias definitions were removed.
    assertFalse(rootNode.hasChild(googScopeCall));

    // Check if the scopes were collapsed.
    Node googScopeBlock = googScopeCall.getLastChild();
    assertNull(googScopeBlock.getParent());

    // Check if the code change was reported.
    compiler.resetReportedCodeChange();
    assertEquals(1, compiler.getReportedCodeChangeCount());

    // Verify the alias transformation.
    assertEquals("testAlias", traversal.getAliasTransformations().get(0).name);
    assertEquals("testQualifiedName", traversal.getAliasTransformations().get(0).qualifiedName);
  }
}