import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Iterators;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticsListener;
import com.google.javascript.jscomp.DiagnosticsReceiver;
import com.google.javascript.jscomp.DiagnosticsWriter;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private JSModuleGraph moduleGraph;
  private IdGenerator idGenerator;
  private AnalyzePrototypeProperties analyzer;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    moduleGraph = mock(JSModuleGraph.class);
    idGenerator = mock(IdGenerator.class);
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    passManager = new CompilerPassManager(compiler, new DiagnosticsReceiver() {},
        new DiagnosticsWriter());
  }

  @Test
  public void testProcess_NoModules_DoesNothing() {
    when(moduleGraph.getModuleCount()).thenReturn(0);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(Node.NULL, Node.NULL);
    verify(compiler).report(Mockito.any(JSError.class));
  }

  @Test
  public void testProcess_OneModule_DoesNothing() {
    when(moduleGraph.getModuleCount()).thenReturn(1);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(Node.NULL, Node.NULL);
    verify(compiler, never()).report(Mockito.any(JSError.class));
  }

  @Test
  public void testProcess_MultipleModules() {
    when(moduleGraph.getModuleCount()).thenReturn(2);

    Node externRoot = Node.NULL;
    Node root = Node.NULL;
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(externRoot, root);

    verify(compiler, times(1)).reportCodeChange();
    verify(compiler).report(Mockito.never(JSError.class));
  }

  @Test
  public void testMoveMethods_NoNameInfos() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(Collections.emptyList());
  }

  @Test
  public void testMoveMethods_EmptyNameInfos() {
    when(analyzer.getAllNameInfo()).thenReturn(Collections.emptyList());
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(analyzer.getAllNameInfo());
    verify(compiler, never()).reportCodeChange();
    verify(compiler).report(Mockito.never(JSError.class));
  }

  @Test
  public void testMoveMethods_NullDeepestCommonModuleRef() {
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(Node.NULL);

    Iterator<NameInfo> allNameInfo = Iterators.singletonIterator(nameInfo);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(allNameInfo);

    verify(compiler).report(JSError.make(CrossModuleMethodMotion.NULL_COMMON_MODULE_ERROR));
  }

  @Test
  public void testMoveMethods_NoMethodsToMove() {
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.readsClosureVariables()).thenReturn(true);

    Iterator<NameInfo> allNameInfo = Iterators.singletonIterator(nameInfo);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(allNameInfo);
    verify(compiler, never()).reportCodeChange();
  }

  @Test
  public void testMoveMethods_MoveFunction() {
    NameInfo nameInfo = mock(NameInfo.class);
    JSModule deepestCommonModuleRef = mock(JSModule.class);

    Property propertySymbol = mock(Property.class);
    when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(propertySymbol));
    when(propertySymbol.getValue()).thenReturn(Node.functionLiteral(null));

    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(moduleGraph.dependsOn(deepestCommonModuleRef, Node.NULL)).thenReturn(true);

    Iterator<NameInfo> allNameInfo = Iterators.singletonIterator(nameInfo);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(allNameInfo);

    ArgumentCaptor<Node> stubCallCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> unstubCallCaptor = ArgumentCaptor.forClass(Node.class);

    verify(compiler).reportCodeChange();

    verify(compiler).getNodeForCodeInsertion(deepestCommonModuleRef)
        .addChildrenToFront(stubCallCaptor.capture());

    Node stubCall = stubCallCaptor.getValue();
    assertEquals("JSCompiler_stubMethod(id)", stubCall.getFirstChild().getFirstChild().toString());

    verify(compiler).getNodeForCodeInsertion(deepestCommonModuleRef)
        .addChildrenToFront(unstubCallCaptor.capture());

    Node unstubCall = unstubCallCaptor.getValue();
    assertEquals("JSCompiler_unstubMethod(id, body)", unstubCall.toString());

    Node functionNode = nameInfo.getDeclarations().iterator().next().getValue();
    Node originalFunction = functionNode.getFirstChild();

    verify(functionNode).replaceChild(originalFunction, stubCall);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMethods_MoveGetOrSet() {
    NameInfo nameInfo = mock(NameInfo.class);
    JSModule deepestCommonModuleRef = mock(JSModule.class);

    Property propertySymbol = mock(Property.class);
    when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(propertySymbol));
    when(propertySymbol.getValue()).thenReturn(Node.getprop(Node.NULL, "get"));

    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(moduleGraph.dependsOn(deepestCommonModuleRef, Node.NULL)).thenReturn(true);

    Iterator<NameInfo> allNameInfo = Iterators.singletonIterator(nameInfo);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(allNameInfo);

    verify(compiler).report(JSError.make(CrossModuleMethodMotion.NULL_COMMON_MODULE_ERROR));
  }

  @Test
  public void testIdGenerator_GenerateNewIds() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    int id = pass.idGenerator.newId();
    assertTrue(id > 0);
  }
}