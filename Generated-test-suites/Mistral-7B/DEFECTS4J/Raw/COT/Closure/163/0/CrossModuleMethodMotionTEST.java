import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Logger;
import com.google.javascript.jscomp.ModuleGraph;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private ModuleGraph moduleGraph;
  private Logger logger;
  private AnalyzePrototypeProperties analyzer;
  private JSModule deepestCommonModuleRef;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    idGenerator = mock(IdGenerator.class);
    moduleGraph = mock(ModuleGraph.class);
    logger = mock(Logger.class);
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(moduleGraph.getModuleCount()).thenReturn(2);
    when(moduleGraph.dependsOn(deepestCommonModuleRef, null)).thenReturn(true);
  }

  @Test
  public void testProcess_NullExternRoot() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(null, null);
    // No action required since there are no arguments
  }

  @Test
  public void testProcess_NullRoot() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(mock(Node.class), null);
    // No action required since there are no arguments
  }

  @Test
  public void testProcess_EmptyModuleGraph() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(mock(Node.class), mock(Node.class));
    // No action required since there are no modules
  }

  @Test
  public void testProcess_OneModuleGraph() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(mock(Node.class), mock(Node.class));
    // No action required since there is only one module
  }

  @Test
  public void testMoveMethods_NullAnalyzer() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(null);
  }

  @Test
  public void testMoveMethods_EmptyNameInfoCollection() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(Collections.emptyList());
  }

  @Test
  public void testMoveMethods_NullNameInfo() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.moveMethods(Collections.singletonList(null));
  }

  @Test
  public void testMoveMethods_NonFunctionProperty() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    NameInfo nameInfo = mock(NameInfo.class);
    Symbol symbol = mock(Property.class);
    when(symbol.getValue()).thenReturn(mock(Node.class));
    when(nameInfo.getDeclarations()).thenReturn(Arrays.asList(symbol));
    pass.moveMethods(Arrays.asList(nameInfo));
  }

  @Test
  public void testMoveMethods_AlreadyStubbedMethod() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    NameInfo nameInfo = mock(NameInfo.class);
    Symbol symbol = mock(Property.class);
    when(symbol.getValue()).thenReturn(mock(Node.class));
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(nameInfo.getDeclarations()).thenReturn(Arrays.asList(symbol));
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);
    pass.moveMethods(Arrays.asList(nameInfo));
  }

  @Test
  public void testMoveMethods_MoveFunctionProperty() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    NameInfo nameInfo = mock(NameInfo.class);
    Symbol symbol = mock(Property.class);
    Node value = mock(Node.class);
    when(symbol.getValue()).thenReturn(value);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(nameInfo.getDeclarations()).thenReturn(Arrays.asList(symbol));
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    ArgumentCaptor<Node> stubCallCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> unstubCallCaptor = ArgumentCaptor.forClass(Node.class);

    pass.moveMethods(Arrays.asList(nameInfo));

    verify(compiler, times(1)).getNodeForCodeInsertion(deepestCommonModuleRef);
    verify(compiler, times(1)).parseSyntheticCode(STUB_DECLARATIONS);
    verify(compiler, times(1)).reportCodeChange();

    verify(valueParent, times(1)).replaceChild(value, stubCallCaptor.capture());
    Node stubCall = stubCallCaptor.getValue();
    assertSame(STUB_METHOD_NAME, stubCall.getFirstChild().getFirstChild().getString());

    verify(deepestCommonModuleRef, times(1)).addChildToFront(
        unstubCallCaptor.capture());
    Node unstubCall = unstubCallCaptor.getValue();
    assertSame(UNSTUB_METHOD_NAME, unstubCall.getFirstChild().getString());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMethods_NullDeepestCommonModuleRef() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    NameInfo nameInfo = mock(NameInfo.class);
    Symbol symbol = mock(Property.class);
    when(symbol.getValue()).thenReturn(mock(Node.class));
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);
    when(nameInfo.getDeclarations()).thenReturn(Arrays.asList(symbol));
    pass.moveMethods(Arrays.asList(nameInfo));
  }

  @Test
  public void testIdGenerator_NewId() {
    IdGenerator idGenerator = new IdGenerator();
    int id = idGenerator.newId();
    assertEquals(0, id);

    id = idGenerator.newId();
    assertEquals(1, id);
  }

  @Test
  public void testIdGenerator_HasGeneratedAnyIds() {
    IdGenerator idGenerator = new IdGenerator();
    assertFalse(idGenerator.hasGeneratedAnyIds());

    idGenerator.newId();
    assertTrue(idGenerator.hasGeneratedAnyIds());
  }
}