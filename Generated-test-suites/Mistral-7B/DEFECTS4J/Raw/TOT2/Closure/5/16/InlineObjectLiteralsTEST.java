import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = mock(Compiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("function foo() {}");
    Node root = Node.newText("var obj = {a: 1}; foo();");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    // Check that 'obj.a' has been replaced with 'JSCompiler_object_inline_a'
    Scope scope = callback.getScope();
    Node getProp = scope.getVar("obj").getChildAtIndex(1);
    Node name = getProp.getLastChild();
    assertEquals("JSCompiler_object_inline_a", name.getString());

    // Check that 'obj' is still a valid object literal
    Node object = scope.getVar("obj").getFirstChild();
    assertEquals(Token.OBJECT_LITERAL, object.getTokenType());
    for (Node child = object.getFirstChild(); child != null; child = child.getNext()) {
      assertEquals(Token.PROPERTY, child.getTokenType());
      Node key = child.getFirstChild();
      Node value = child.getNext();
      assertEquals("a", key.getString());
      assertEquals(Token.NUMBER, value.getTokenType());
      assertEquals(1, value.getFirstChild().getNumber().intValue());
    }
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() {
    Compiler compiler = mock(Compiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("function foo() {}");
    Node root = Node.newText("var obj = {a: 1, b: 2}; foo();");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    // Check that 'obj.a' and 'obj.b' have been replaced with 'JSCompiler_object_inline_a' and 'JSCompiler_object_inline_b'
    Scope scope = callback.getScope();
    Node getPropA = scope.getVar("obj").getChildAtIndex(1).getChildAtIndex(0);
    Node nameA = getPropA.getLastChild();
    assertEquals("JSCompiler_object_inline_a", nameA.getString());

    Node getPropB = scope.getVar("obj").getChildAtIndex(1).getChildAtIndex(1);
    Node nameB = getPropB.getLastChild();
    assertEquals("JSCompiler_object_inline_b", nameB.getString());

    // Check that 'obj' is still a valid object literal
    Node object = scope.getVar("obj").getFirstChild();
    assertEquals(Token.OBJECT_LITERAL, object.getTokenType());
    for (Node child = object.getFirstChild(); child != null; child = child.getNext()) {
      assertEquals(Token.PROPERTY, child.getTokenType());
      Node key = child.getFirstChild();
      Node value = child.getNext();
      if (key.getString().equals("a")) {
        assertEquals("JSCompiler_object_inline_a", child.getLastChild().getString());
        Node valueA = value.getFirstChild();
        assertEquals(Token.NUMBER, valueA.getTokenType());
        assertEquals(1, valueA.getFirstChild().getNumber().intValue());
      } else if (key.getString().equals("b")) {
        assertEquals("JSCompiler_object_inline_b", child.getLastChild().getString());
        Node valueB = value.getFirstChild();
        assertEquals(Token.NUMBER, valueB.getTokenType());
        assertEquals(2, valueB.getFirstChild().getNumber().intValue());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    Compiler compiler = mock(Compiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("function foo() {}");
    Node root = Node.newText("var a = {b: {c: a}}; foo();");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    // Check that the cycle has been detected and ignored
    Scope scope = callback.getScope();
    Node object = scope.getVar("a").getFirstChild();
    assertEquals(Token.OBJECT_LITERAL, object.getTokenType());
    for (Node child = object.getFirstChild(); child != null; child = child.getNext()) {
      assertEquals(Token.PROPERTY, child.getTokenType());
      Node key = child.getFirstChild();
      Node value = child.getNext();
      if (key.getString().equals("b")) {
        assertEquals(Token.OBJECT_LITERAL, value.getTokenType());
        for (Node grandChild = value.getFirstChild(); grandChild != null; grandChild = grandChild.getNext()) {
          Node greatGrandChild = grandChild.getNext();
          if (greatGrandChild != null && greatGrandChild.isName()) {
            assertEquals("a", greatGrandChild.getString());
          }
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithMissingProperty() {
    Compiler compiler = mock(Compiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("function foo() {}");
    Node root = Node.newText("var obj = {a: 1}; obj.missing; foo();");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    // Check that 'obj.missing' has not been replaced with a variable name
    Scope scope = callback.getScope();
    Node getProp = scope.getVar("obj").getChildAtIndex(1);
    Node getPropMissing = getProp.getNext();
    Node nameMissing = getPropMissing.getLastChild();
    assertFalse("obj.missing should not have been replaced", nameMissing.isName());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperty() {
    Compiler compiler = mock(Compiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("function foo() {}");
    Node root = Node.newText("var obj = {foo: function() {}, a: 1}; foo();");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    // Check that 'obj.foo' has not been replaced with a variable name
    Scope scope = callback.getScope();
    Node getProp = scope.getVar("obj").getChildAtIndex(1);
    Node getPropFoo = getProp.getNext();
    Node function = getPropFoo.getFirstChild();
    assertTrue("obj.foo should not have been replaced", function.isFunction());
  }

  @Test
  public void testInlineObjectLiteralsWithES5PropertyAccess() {
    Compiler compiler = mock(Compiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("function foo() {}");
    Node root = Node.newText("var obj = {a: {b: 1}}; obj.a.b++; foo();");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    // Check that 'obj.a.b' has not been replaced with a variable name
    Scope scope = callback.getScope();
    Node getPropA = scope.getVar("obj").getChildAtIndex(1);
    Node getPropB = getPropA.getNext();
    Node getProp = getPropB.getNext();
    Node nameB = getProp.getLastChild();
    assertFalse("obj.a.b should not have been replaced", nameB.isName());
  }

  private static class InliningBehavior implements Behavior {
    // Empty implementation
  }
}