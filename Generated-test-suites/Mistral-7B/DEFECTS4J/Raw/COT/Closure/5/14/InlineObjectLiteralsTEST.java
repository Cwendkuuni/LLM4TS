import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private static final String VAR_PREFIX = "JSCompiler_object_inline_";
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private Behavior behavior;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    behavior = new InlineObjectLiterals.InliningBehavior();
  }

  @Test
  public void testInlineSimpleObject() {
    // Given
    Node root = Node.newText("var obj = {a: 1}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    Node expected = Node.newText("var a = 1, obj = {}");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testInlineNestedObject() {
    // Given
    Node root = Node.newText("var obj = {a: {b: 1}}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    Node expected = Node.newText("var a = {}, b = 1, obj = {}");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testInlineObjectWithFunctionProperty() {
    // Given
    Node root = Node.newText("var obj = {get Foo() {return 1}}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    Node expected = Node.newText("var Foo = 1, obj = {}");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testInlineObjectWithSelfReference() {
    // Given
    Node root = Node.newText("var obj = {a: obj}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    Node expected = Node.newText("var obj = {a: undefined}, a = {}", root.toString());
    assertEquals(expected.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testInlineVariableThatIsForbidden() {
    // Given
    Node root = Node.newText("var x = 1; var obj = {x: 1}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    fail("Variable x should be forbidden from being inlined.");
  }

  @Test(expected = AssertionError.class)
  public void testInlineGlobalVariable() {
    // Given
    Node root = Node.newText("var obj = {window: 1}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    fail("Global variables should be forbidden from being inlined.");
  }

  @Test(expected = AssertionError.class)
  public void testInlineExportedVariable() {
    // Given
    Node root = Node.newText("var obj = {exported: 1}");

    // When
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(null, root);

    // Then
    fail("Exported variables should be forbidden from being inlined.");
  }
}