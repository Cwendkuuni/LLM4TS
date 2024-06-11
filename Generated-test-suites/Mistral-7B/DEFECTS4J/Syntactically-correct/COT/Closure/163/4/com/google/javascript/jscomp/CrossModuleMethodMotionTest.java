import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    idGenerator = Mockito.mock(IdGenerator.class);
    moduleGraph = Mockito.mock(JSModuleGraph.class);

    Mockito.when(compiler.getModuleGraph()).thenReturn(moduleGraph);
  }

  @Test
  public void testProcess_NoModules() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    Node root = Mockito.mock(Node.class);
    pass.process(Mockito.mock(Node.class), root);
    // No-op since there are no modules
  }

  @Test
  public void testProcess_OneModule() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    Node root = Mockito.mock(Node.class);
    JSModule module = Mockito.mock(JSModule.class);
    Mockito.when(moduleGraph.getModuleCount()).thenReturn(1);
    Mockito.when(moduleGraph.getModule(0)).thenReturn(module);

    pass.process(Mockito.mock(Node.class), root);
    // No methods moved since there is only one module
  }

  @Test
  public void testProcess_TwoModules() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    Node root = Mockito.mock(Node.class);
    JSModule module1 = Mockito.mock(JSModule.class);
    JSModule module2 = Mockito.mock(JSModule.class);
    Mockito.when(moduleGraph.getModuleCount()).thenReturn(2);
    Mockito.when(moduleGraph.getModule(0)).thenReturn(module1);
    Mockito.when(moduleGraph.getModule(1)).thenReturn(module2);

    Node functionNode = Mockito.mock(Node.class);
    Mockito.when(functionNode.isFunction()).thenReturn(true);

    NameInfo nameInfo = Mockito.mock(NameInfo.class);
    Mockito.when(nameInfo.isReferenced()).thenReturn(true);
    Mockito.when(nameInfo.readsClosureVariables()).thenReturn(false);
    Mockito.when(nameInfo.getDeepestCommonModuleRef()).thenReturn(module1);
    Mockito.when(nameInfo.getDeclarations()).thenReturn(Mockito.mock(Collection.class));

    Iterator<Symbol> declarationsIterator = Mockito.mock(Iterator.class);
    Mockito.when(declarationsIterator.hasNext()).thenReturn(true);
    Mockito.when(declarationsIterator.next()).thenReturn(Mockito.mock(Symbol.class));

    Mockito.when(nameInfo.getDeclarations().iterator()).thenReturn(declarationsIterator);

    Property property = Mockito.mock(Property.class);
    Mockito.when(property.getValue()).thenReturn(functionNode);

    Mockito.when(nameInfo.getDeclarations().get(0)).thenReturn(property);

    Mockito.when(module1.dependsOn(ArgumentMatchers.eq(module2), ArgumentMatchers.any())).thenReturn(true);

    pass.process(Mockito.mock(Node.class), root);

    // Only one method can be moved
    Mockito.verify(compiler, times(1)).report(ArgumentMatchers.isA(JSError.class));
  }

  @Test
  public void testMoveMethods() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);

    NameInfo nameInfo = Mockito.mock(NameInfo.class);
    Mockito.when(nameInfo.isReferenced()).thenReturn(true);
    Mockito.when(nameInfo.readsClosureVariables()).thenReturn(false);
    Mockito.when(nameInfo.getDeepestCommonModuleRef()).thenReturn(Mockito.mock(JSModule.class));
    Mockito.when(nameInfo.getDeclarations()).thenReturn(Mockito.mock(Collection.class));

    Iterator<Symbol> declarationsIterator = Mockito.mock(Iterator.class);
    Mockito.when(declarationsIterator.hasNext()).thenReturn(true);
    Mockito.when(declarationsIterator.next()).thenReturn(Mockito.mock(Symbol.class));

    Property property = Mockito.mock(Property.class);
    Mockito.when(property.getValue()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(nameInfo.getDeclarations().iterator()).thenReturn(declarationsIterator);
    Mockito.when(nameInfo.getDeclarations().get(0)).thenReturn(property);

    Node functionNode = Mockito.mock(Node.class);
    Mockito.when(functionNode.isFunction()).thenReturn(true);
    Mockito.when(property.getValue()).thenReturn(functionNode);

    JSModule deepestCommonModuleRef = Mockito.mock(JSModule.class);
    JSModule module = Mockito.mock(JSModule.class);

    Mockito.when(moduleGraph.getModuleCount()).thenReturn(1);
    Mockito.when(moduleGraph.getModule(0)).thenReturn(module);
    Mockito.when(moduleGraph.dependsOn(deepestCommonModuleRef, module)).thenReturn(true);

    IdGenerator idGenerator = Mockito.mock(IdGenerator.class);
    Mockito.when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    CrossModuleMethodMotion passInstance = new CrossModuleMethodMotion(compiler, idGenerator, false);
    passInstance.moveMethods(Mockito.arrayOf(nameInfo));

    Mockito.verify(compiler, times(1)).reportCodeChange();

    Mockito.when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);

    Node declarations = Mockito.mock(Node.class);
    Mockito.when(compiler.parseSyntheticCode(ArgumentMatchers.anyString())).thenReturn(declarations);

    CrossModuleMethodMotion passInstance2 = new CrossModuleMethodMotion(compiler, idGenerator, false);
    passInstance2.moveMethods(Mockito.arrayOf(nameInfo));

    Mockito.verify(compiler, times(1)).getNodeForCodeInsertion(ArgumentMatchers.eq(null));
    Mockito.verify(compiler, times(1)).addChildrenToFront(ArgumentMatchers.any(Node.class), ArgumentMatchers.any(Node.class));
  }
}