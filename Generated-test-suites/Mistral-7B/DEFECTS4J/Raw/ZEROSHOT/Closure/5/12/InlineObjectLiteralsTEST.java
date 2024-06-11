import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testtype.InlineObjectLiteralsTestType;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  public static class TestCompiler extends AbstractCompiler {
    @Override
    protected List<CompilerPass> getPasses() {
      return Collections.singletonList(new InlineObjectLiterals(this, getNameIdSupplier()));
    }
  }

  private TestCompiler compiler;

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, getNameIdSupplier());
  }

  @Override
  protected CompilerOptions getOptions() {
    return new CompilerOptions();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    compiler = new TestCompiler();
  }

  // Test cases

  public void testInlineObjectLiterals() throws Exception {
    String input = "var x = {a: 1, b: {c: 1}};";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has two keys: 'a' and 'b'. 'b' itself is an object with one key: 'c'.
    Set<String> keys = Sets.newLinkedHashSet("a", "b", "c");

    // Check that all keys are identified as inlineable.
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      assertTrue(keys.contains(varName));
    }

    // Check that 'x' has been split into three variables: 'x.a', 'x.b', 'x.b.c'.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(3, scope.size());
    for (int i = 0; i < 3; i++) {
      Var var = scope.getVar(i + 1);
      String varName = VAR_PREFIX + var.name + "_";
      String expectedName = "x_" + i;
      assertEquals(varName, var.name);
      assertEquals(expectedName, var.getDisplayName());
    }
  }

  public void testInlineObjectLiteralsWithDeletedProperties() throws Exception {
    String input = "var x = {a: 1, b: {c: 1}, deleted: null};";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has three keys: 'a', 'b', 'deleted'. 'b' itself is an object with one key: 'c'.
    Set<String> keys = Sets.newLinkedHashSet("a", "b", "c", "deleted");

    // Check that all keys are identified as inlineable except 'deleted'.
    Set<String> inlineableKeys = Sets.newLinkedHashSet("a", "b", "c");
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      assertTrue(inlineableKeys.contains(varName) || varName.equals("deleted"));
    }

    // Check that 'x' has been split into four variables: 'x.a', 'x.b', 'x.b.c', 'x.deleted'.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(4, scope.size());
    for (int i = 0; i < 3; i++) {
      Var var = scope.getVar(i + 1);
      String varName = VAR_PREFIX + var.name + "_";
      String expectedName = "x_" + i;
      assertEquals(varName, var.name);
      assertEquals(expectedName, var.getDisplayName());
    }
    Var deletedVar = scope.getVar(3);
    String deletedVarName = VAR_PREFIX + "deleted_";
    assertEquals(deletedVarName, deletedVar.name);
    assertEquals("deleted", deletedVar.getDisplayName());
  }

  public void testInlineObjectLiteralsWithNestedObjects() throws Exception {
    String input = "var x = {a: {b: {c: 1}}};";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has one key: 'a'. 'a' itself is an object with one key: 'b'. 'b' is an object with one key: 'c'.
    Set<String> keys = Sets.newLinkedHashSet("a", "b", "c");

    // Check that all keys are identified as inlineable.
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      assertTrue(keys.contains(varName));
    }

    // Check that 'x' has been split into three variables: 'x.a', 'x.a.b', 'x.a.b.c'.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(3, scope.size());
    for (int i = 0; i < 3; i++) {
      Var var = scope.getVar(i + 1);
      String varName = VAR_PREFIX + var.name + "_";
      String expectedName = "x_" + i;
      assertEquals(varName, var.name);
      assertEquals(expectedName, var.getDisplayName());
    }
  }

  public void testInlineObjectLiteralsWithSelfReferentialAssignment()
      throws Exception {
    String input = "var x = {a: {b: x}};";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has one key: 'a'. 'a' itself is an object with one key: 'b'. 'b' is a reference to 'x'.
    Set<String> keys = Sets.newLinkedHashSet("a", "b");

    // Check that 'x' has not been split, since it contains a self-referential assignment.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(2, scope.size());
    for (int i = 0; i < 2; i++) {
      Var var = scope.getVar(i + 1);
      String varName = VAR_PREFIX + var.name + "_";

      if (i == 0) {
        String expectedName = "x";
        assertEquals(varName, var.name);
        assertEquals(expectedName, var.getDisplayName());
      } else {
        String expectedName = "x.a_";
        assertEquals(varName, var.name);
        assertEquals(expectedName, var.getDisplayName().substring(0, expectedName.length()));
      }
    }
  }

  public void testInlineObjectLiteralsWithGettersAndSetters() throws Exception {
    String input = "var x = {" +
        "get A() { return 1; }," +
        "set A(val) { }," +
        "get B() { return { a: 1 } };" +
        "}";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has one property: 'B'. 'B' itself is an object with one property: 'a'.
    Set<String> keys = Sets.newLinkedHashSet("B");

    // Check that 'x' has not been split, since it contains getters and setters.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(1, scope.size());
    Var var = scope.getVar(1);
    String varName = VAR_PREFIX + var.name + "_";
    String expectedName = "x";
    assertEquals(varName, var.name);
    assertEquals(expectedName, var.getDisplayName());

    // Check that 'x.B' has been split into two variables: 'x.B_a' and 'x.B_'.
    assertEquals(2, scope.getVar(0).scope.size());
    Var xBvar = scope.getVar(0).scope.getVar(1);
    String xBvarName = VAR_PREFIX + "xB_" + "_";
    String xBexpectedName = "x.B_";
    assertEquals(xBvarName, xBvar.name);
    assertEquals(xBexpectedName, xBvar.getDisplayName());
    Var xBaVar = scope.getVar(0).scope.getVar(2);
    String xBaVarName = VAR_PREFIX + "xB_a_";
    String xBaexpectedName = "x.B.a_";
    assertEquals(xBaVarName, xBaVar.name);
    assertEquals(xBaexpectedName, xBaVar.getDisplayName());
  }

  public void testInlineObjectLiteralsWithUnreachableKeys() throws Exception {
    String input = "var x = {a: 1, b: {c: 1, d: 1}, e: 1};";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has three keys: 'a', 'b', 'e'. 'b' itself is an object with two keys: 'c' and 'd'.
    Set<String> keys = Sets.newLinkedHashSet("a", "b", "c", "d", "e");

    // Check that all keys are identified as inlineable.
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      assertTrue(keys.contains(varName));
    }

    // Check that 'x' has been split into five variables: 'x.a', 'x.b.c', 'x.b.d', 'x.b' and 'x.e'.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(5, scope.size());
    for (int i = 0; i < 5; i++) {
      Var var = scope.getVar(i + 1);
      String varName = VAR_PREFIX + var.name + "_";
      String expectedName = "x_" + (i / 3);
      assertEquals(varName, var.name);
      assertEquals(expectedName, var.getDisplayName());
    }

    // Check that none of the unused variables have been identified as inlineable.
    Reference unusedVarRef = referenceInfo.getInitializingReference();
    String unusedVarName = unusedVarRef.getNode().getString();
    Map<String, String> unusedVars = ImmutableMap.of("e", unusedVarName);
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      if (!keys.contains(varName)) {
        assertFalse(unusedVars.containsKey(varName));
      }
    }
  }

  public void testInlineObjectLiteralsWithInvalidPropertyNames() throws Exception {
    String input = "var x = {a: 1, b: {c: 1, 1: 1}};";

    Node ast = compile(input, true);
    assertNotNull(ast);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(ast, null);

    ReferenceCollection referenceInfo = callback.getReferenceMap().get(0);

    // Object x has two keys: 'a', 'b'. 'b' itself is an object with three keys: 'c' and two invalid ones.
    Set<String> keys = Sets.newLinkedHashSet("a", "b");

    // Check that all keys are identified as inlineable except the invalid ones.
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      assertTrue(keys.contains(varName) || !isValidPropertyName(varName));
    }

    // Object x has been split into three variables: 'x.a', 'x.b_c', and 'x.b_'.
    Scope scope = ast.getFirstChild().getFirstChild().getScope();
    assertEquals(3, scope.size());
    for (int i = 0; i < 3; i++) {
      Var var = scope.getVar(i + 1);
      String varName = VAR_PREFIX + var.name + "_";
      String expectedName = "x_" + i;
      assertEquals(varName, var.name);
      assertEquals(expectedName, var.getDisplayName());
    }

    // Check that none of the invalid variables have been identified as inlineable.
    Reference invalidVarRef = referenceInfo.getReferences("b", 1).get(0);
    String invalidVarName = invalidVarRef.getNode().getString();
    Map<String, String> invalidVars = ImmutableMap.of("b_1", invalidVarName);
    for (Reference ref : referenceInfo.references) {
      String varName = ref.getNode().getString();
      if (!keys.contains(varName) && isValidPropertyName(varName)) {
        fail();
      } else if (!invalidVars.containsKey(varName)) {
        assertTrue(isValidPropertyName(varName));
      }
    }
  }

  private boolean isValidPropertyName(String name) {
    return name.matches("[A-Za-z_$][0-9A-Za-z_$]*");
  }

  // Helper methods

  private Node compile(String input, boolean check) throws Exception {
    CompilerOptions options = getOptions();
    options.setCompileWithSourceMap(true);
    Node ast = compileSingleFile(input, options);
    if (check) {
      checkCompiledCode(ast);
    }
    return ast;
  }
}

class InliningBehavior implements Behavior {
  // ... (the same as in the original class)
}