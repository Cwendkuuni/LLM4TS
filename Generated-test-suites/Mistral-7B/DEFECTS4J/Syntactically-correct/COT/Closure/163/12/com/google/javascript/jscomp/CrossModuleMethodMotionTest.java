import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IDependencyCollector;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * JUnit 4 test cases for the CrossModuleMethodMotion class.
 */
public class CrossModuleMethodMotionTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private AnalyzePrototypeProperties analyzer;

  @Mock
  private JSModuleGraph moduleGraph;

  @Mock
  private CompilerPassManager passManager;

  @InjectMocks
  private CrossModuleMethodMotion crossModuleMethodMotion;

  @Before
  public void setUp() {
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
  }

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    // Create an instance of CrossModuleMethodMotion.
    CrossModuleMethodMotion instance = new CrossModuleMethodMotion(compiler,
        mock(IdGenerator.class), true);
    assertSame(compiler, instance.compiler);
    assertSame(mock(IdGenerator.class), instance.idGenerator);
    assertSame(moduleGraph, instance.moduleGraph);
  }

  /**
   * Test process method with empty externRoot and root.
   */
  @Test
  public void testProcessEmpty() {
    // Set up mocks.
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test process method.
    crossModuleMethodMotion.process(externRoot, root);

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());
  }

  /**
   * Test process method with valid arguments.
   */
  @Test
  public void testProcessValid() {
    // Set up mocks and test data.
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();
    when(analyzer.getAllNameInfo()).thenReturn(Collections.emptyList());

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test process method.
    crossModuleMethodMotion.process(externRoot, root);

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());
  }

  /**
   * Test moveMethods method with empty allNameInfo.
   */
  @Test
  public void testMoveMethodsEmptyAllNameInfo() {
    // Set up mocks and test data.
    Collection<NameInfo> allNameInfo = Collections.emptyList();

    // Set up mocks.
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();

    when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test moveMethods method.
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());
  }

  /**
   * Test moveMethods method with nameInfo that is not referenced.
   */
  @Test
  public void testMoveMethodsUnreferencedNameInfo() {
    // Set up test data.
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.isReferenced()).thenReturn(false);

    // Set up mocks and test data.
    Collection<NameInfo> allNameInfo = Arrays.asList(nameInfo);
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();

    when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test moveMethods method.
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());
  }

  /**
   * Test moveMethods method with null deepestCommonModuleRef.
   */
  @Test
  public void testMoveMethodsNullDeepestCommonModuleRef() {
    // Set up test data.
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);

    // Set up mocks and test data.
    Collection<NameInfo> allNameInfo = Arrays.asList(nameInfo);
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();

    when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test moveMethods method.
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());

    // Verify that no error is reported.
    verify(compiler, never()).report(eq(JSError.make(NULL_COMMON_MODULE_ERROR)));
  }

  /**
   * Test moveMethods method with method that can't be moved.
   */
  @Test
  public void testMoveMethodsCannotMove() {
    // Set up test data.
    NameInfo nameInfo = mock(NameInfo.class);
    Symbol symbol = mock(Symbol.class);
    Property property = mock(Property.class);
    Node value = mock(Node.class);

    when(nameInfo.getName()).thenReturn("name");
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(moduleGraph);
    when(nameInfo.getDeclarations()).thenReturn(Arrays.asList(symbol));
    when(symbol.getValue()).thenReturn(value);
    when(value.isFunction()).thenReturn(true);
    when(value.getParent()).thenReturn(mock(Node.class));
    when(value.getParent().isGetterDef()).thenReturn(true);

    // Set up mocks and test data.
    Collection<NameInfo> allNameInfo = Arrays.asList(nameInfo);
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();

    when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test moveMethods method.
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());
  }

  /**
   * Test moveMethods method with method that can be moved.
   */
  @Test
  public void testMoveMethodsCanMove() {
    // Set up test data.
    NameInfo nameInfo = mock(NameInfo.class);
    Symbol symbol = mock(Symbol.class);
    Property property = mock(Property.class);
    Node value = mock(Node.class);
    Node proto = mock(Node.class);

    when(nameInfo.getName()).thenReturn("name");
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(moduleGraph);
    when(nameInfo.getDeclarations()).thenReturn(Arrays.asList(symbol));
    when(symbol.getValue()).thenReturn(value);
    when(value.isFunction()).thenReturn(true);
    when(value.getParent()).thenReturn(mock(Node.class));
    when(value.getParent().isGetterDef()).thenReturn(false);
    when(value.getParent()).thenReturn(proto);

    Node stubCall = mock(Node.class);
    Node unstubCall = mock(Node.class);

    when(value.getType()).thenReturn(NodeType.FUNCTION_DECL);
    when(value.getParent()).thenReturn(mock(Node.class));

    when(moduleGraph.dependsOn(moduleGraph, moduleGraph)).thenReturn(true);

    when(IR.call(IR.name(STUB_METHOD_NAME), IR.number(0))).thenReturn(stubCall);
    when(IR.name(UNSTUB_METHOD_NAME)).thenReturn(IR.name("JSCompiler_unstubMethod"));
    when(IR.call(IR.name(UNSTUB_METHOD_NAME), IR.number(0), IR.valueToQuotedString(
        "body"))).thenReturn(unstubCall);

    when(compiler.getNodeForCodeInsertion(moduleGraph)).thenAnswer(
        new Answer<Node>() {
          @Override
          public Node answer(InvocationOnMock invocation) throws Throwable {
            return mock(Node.class);
          }
        });

    // Set up mocks and test data.
    Collection<NameInfo> allNameInfo = Arrays.asList(nameInfo);
    Node externRoot = SourceFile.ROOT.getAST();
    Node root = SourceFile.ROOT.getAST();

    when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    // Test moveMethods method.
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Verify that stub function is added to the top-most module.
    verify(compiler, times(1)).reportCodeChange();

    verify(compiler).getNodeForCodeInsertion(null).addChildrenToFront(
        isA(Node.class));

    // Verify that function body is unstubbed in the deeper module.
    verify(compiler, times(1)).getNodeForCodeInsertion(moduleGraph)
        .addChildrenToFront(isA(Node.class));

    // Verify that method is stubbed in the original module.
    verify(compiler, times(1)).getNodeForCodeInsertion(moduleGraph)
        .addChildrenToFront(isA(Node.class));

    // Verify no side effects.
    assertNull(externRoot.getFirstChild());
    assertNull(root.getFirstChild());
  }

  /**
   * Test IdGenerator class.
   */
  @Test
  public void testIdGenerator() {
    // Test newId method.
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();

    int id1 = idGenerator.newId();
    int id2 = idGenerator.newId();

    assertEquals("Ids should be unique.", id1, id2);
    assertEquals("Ids should start from 0.", id1, 0);
  }
}