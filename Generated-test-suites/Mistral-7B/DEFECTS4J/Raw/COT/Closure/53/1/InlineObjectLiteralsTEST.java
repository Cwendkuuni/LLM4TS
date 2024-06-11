import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPass.*;
import com.google.javascript.rhino.*;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testutils.AbstractCompilerForTest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @SuppressWarnings("unchecked")
  interface BehaviorTest extends Behavior {

    Set<Var> staleVars = Sets.newHashSet();

    @Override
    void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      super.afterExitScope(t, referenceMap);
      // Add your test assertions here after the call to super.
    }
  }

  private static class TestingInlineObjectLiterals
      extends InlineObjectLiterals {

    public TestingInlineObjectLiterals(
        AbstractCompiler compiler,
        Supplier<String> safeNameIdSupplier) {
      super(compiler, safeNameIdSupplier);
    }

    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      super.afterExitScope(t, referenceMap);

      // Test the staleVars set
      for (Var v : staleVars) {
        assertNotNull(v);
        // Add your specific assertions here.
      }
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    InlineObjectLiterals pass = new TestingInlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testInlineSimpleObject() throws Exception {
    SourceFile input = SourceFile.fromCode("test.js",
        "var obj = {a: 1, b: 2};");
    Node root = nodeFactory.compile(input).getRootNode();

    CompilerOptions options = new CompilerOptions();
    options.setAssumptions(Assumptions.forSourceFile(input));
    options.setPrintDetailedSourceMap(true);

    NodeTraversal traversal = nodeTraversalFactory.create(root, options);
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new BehaviorTest() {});
    callback.process(input.getSource(), root);

    // Test the result
    assertTrue(callback.getReferencesCount() > 0);
  }

  @Test
  public void testInlineObjectWithFunction() throws Exception {
    SourceFile input = SourceFile.fromCode("test.js",
        "var obj = {a: 1, b: {fn: function() {}}};");
    Node root = nodeFactory.compile(input).getRootNode();

    CompilerOptions options = new CompilerOptions();
    options.setAssumptions(Assumptions.forSourceFile(input));
    options.setPrintDetailedSourceMap(true);

    NodeTraversal traversal = nodeTraversalFactory.create(root, options);
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new BehaviorTest() {});
    callback.process(input.getSource(), root);

    // Test the result
    assertTrue(callback.getReferencesCount() > 0);

    // Check if the function was inlined
    Iterator<Var> varIter = traversal.getScope().getVars();
    while (varIter.hasNext()) {
      Var v = varIter.next();
      if (v.name.equals("fn")) {
        ReferenceCollection refs = callback.getReferenceMap().getReferences(v);
        assertTrue(refs.references.isEmpty());
      }
    }
  }

  @Test
  public void testInlineObjectWithSelfReference() throws Exception {
    SourceFile input = SourceFile.fromCode("test.js",
        "var obj = {a: 1, b: {fn: function() {this.a = this.b;}}};");
    Node root = nodeFactory.compile(input).getRootNode();

    CompilerOptions options = new CompilerOptions();
    options.setAssumptions(Assumptions.forSourceFile(input));
    options.setPrintDetailedSourceMap(true);

    NodeTraversal traversal = nodeTraversalFactory.create(root, options);
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new BehaviorTest() {});
    callback.process(input.getSource(), root);

    // Test the result
    assertTrue(callback.getReferencesCount() > 0);

    // Check if the self-reference was handled correctly
    Iterator<Var> varIter = traversal.getScope().getVars();
    while (varIter.hasNext()) {
      Var v = varIter.next();
      if (v.name.equals("fn")) {
        ReferenceCollection refs = callback.getReferenceMap().getReferences(v);
        List<Reference> references = refs.references;
        Node referenceNode = references.get(0).getNode();
        Node selfReference = null;
        for (Node child = referenceNode.getFirstChild(); child != null;
             child = child.getNext()) {
          if (child.getType() == Token.GET && child.getLastChild().getType() == Token.THIS) {
            selfReference = child;
            break;
          }
        }
        assertNotNull(selfReference);
        // Check if the self-reference was replaced with a variable
        Node selfVariable = traversal.getScope().getVar(safeNameIdSupplier.get());
        Reference selfVarRef = callback.getReferenceMap().getReference(selfVariable);
        assertEquals(selfReference, selfVarRef.getNode());
      }
    }
  }

  @Test
  public void testInlineObjectWithPropertyAccess() throws Exception {
    SourceFile input = SourceFile.fromCode("test.js",
        "var obj = {a: {b: 1}}; var c = obj.a.b;");
    Node root = nodeFactory.compile(input).getRootNode();

    CompilerOptions options = new CompilerOptions();
    options.setAssumptions(Assumptions.forSourceFile(input));
    options.setPrintDetailedSourceMap(true);

    NodeTraversal traversal = nodeTraversalFactory.create(root, options);
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        new BehaviorTest() {});
    callback.process(input.getSource(), root);

    // Test the result
    assertTrue(callback.getReferencesCount() > 0);

    // Check if the property access was handled correctly
    Iterator<Var> varIter = traversal.getScope().getVars();
    Var cVar = null;
    while (varIter.hasNext()) {
      Var v = varIter.next();
      if (v.name.equals("c")) {
        cVar = v;
        break;
      }
    }
    assertNotNull(cVar);

    ReferenceCollection refs = callback.getReferenceMap().getReferences(cVar);
    List<Reference> references = refs.references;
    Node referenceNode = references.get(0).getNode();
    Node propertyAccess = null;
    for (Node child = referenceNode.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.GETPROP) {
        propertyAccess = child;
        break;
      }
    }
    assertNotNull(propertyAccess);
    Node propertyName = propertyAccess.getLastChild();
    String propertyNameValue = propertyName.getString();
    Node variableNode = traversal.getScope().getVar(safeNameIdSupplier.get());
    String variableNameValue = variableNode.getName();
    assertEquals(propertyNameValue, variableNameValue);
  }
}