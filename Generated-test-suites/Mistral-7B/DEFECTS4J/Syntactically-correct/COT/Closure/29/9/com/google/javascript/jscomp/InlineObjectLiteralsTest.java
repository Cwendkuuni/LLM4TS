import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    safeNameIdSupplier = () -> "test_id";
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testProcess() {
    Node externs = Node.objectLiteral();
    Node root = Node.stringLiteral("test");
    inlineObjectLiterals.process(externs, root);
    // Add assertions for the processed node tree.
  }

  @Test
  public void testIsVarInlineForbidden() {
    Scope scope = new Scope();
    Var v = new Var("testVar", Node.NAME, null, null, null, null, null, false);
    scope.addVar(v);

    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));

    assertTrue(v.isGlobal());
    assertFalse(v.isExtern());
    assertFalse(compiler.getCodingConvention().isExported(v.name));
    assertEquals("testVar", RenameProperties.RENAME_PROPERTY_FUNCTION_NAME);
    assertFalse(staleVars.contains(v));

    assertFalse(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));

    v.setExtern(true);
    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));

    v.setName("exportedVar");
    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));

    v.setName("RenameProperties.RENAME_PROPERTY_FUNCTION_NAME");
    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));

    staleVars.add(v);
    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));
  }

  @Test
  public void testIsInlinableObject() {
    ReferenceCollection referenceInfo = new ReferenceCollection();
    List<Reference> refs = Lists.newArrayList();

    Node object = Node.objectLiteral();
    refs.add(new Reference(object, Behavior.IN, referenceInfo));

    assertTrue(inlineObjectLiterals.new InliningBehavior().isInlinableObject(refs));

    Node objectWithProp = Node.objectLiteral(
        Node.name("prop"),
        Node.objectLiteral(
            Node.name("prop2"),
            Node.numberLiteral(1)
        )
    );
    refs.clear();
    refs.add(new Reference(objectWithProp, Behavior.IN, referenceInfo));

    // Test with a self-referential assignment.
    Node selfRefObject = Node.objectLiteral(
        Node.name("prop"),
        Node.objectLiteral(
            Node.name("prop2"),
            Node.name("prop2")
        )
    );
    refs.clear();
    refs.add(new Reference(selfRefObject, Behavior.IN, referenceInfo));

    assertFalse(inlineObjectLiterals.new InliningBehavior().isInlinableObject(refs));

    // Test with a getter property.
    Node getterObject = Node.objectLiteral(
        Node.name("prop"),
        Node.getter(Node.name("getProp"))
    );
    refs.clear();
    refs.add(new Reference(getterObject, Behavior.IN, referenceInfo));

    // Test ES5 getter/setter.
    Node es5Object = Node.objectLiteral(
        Node.name("prop"),
        Node.setter(Node.name("setProp"))
    );
    refs.clear();
    refs.add(new Reference(es5Object, Behavior.IN, referenceInfo));

    assertFalse(inlineObjectLiterals.new InliningBehavior().isInlinableObject(refs));
  }

  @Test
  public void testSplitObject() {
    Node externs = Node.objectLiteral();
    Node root = Node.stringLiteral("test");
    ReferenceCollection referenceInfo = new ReferenceCollection();

    Var v = new Var("testVar", Node.NAME, null, null, null, null, null, false);
    Scope scope = new Scope();
    scope.addVar(v);

    Node object = Node.objectLiteral();
    referenceInfo.addReference(new Reference(object, Behavior.IN, referenceInfo));
    List<Reference> refs = Lists.newArrayList();
    refs.add(new Reference(object, Behavior.IN, referenceInfo));

    inlineObjectLiterals.new InliningBehavior().splitObject(v, null, null, referenceInfo);

    assertEquals(1, v.getVars().size());
    Node newVar = v.getVars().get(0);
    assertNotNull(newVar.getName());
    assertEquals("JSCompiler_object_inline_prop_test_id", newVar.getName().getString());

    Node objectWithInit = Node.objectLiteral(
        Node.name("prop"),
        Node.numberLiteral(1)
    );
    refs.clear();
    refs.add(new Reference(objectWithInit, Behavior.IN, referenceInfo));
    inlineObjectLiterals.new InliningBehavior().splitObject(v, null, new Reference(objectWithInit, Behavior.INIT, referenceInfo), referenceInfo);

    assertEquals(2, v.getVars().size());
    newVar = v.getVars().get(1);
    assertNotNull(newVar.getName());
    assertEquals("JSCompiler_object_inline_prop2_test_id", newVar.getName().getString());

    Node objectWithInitAndGetter = Node.objectLiteral(
        Node.name("prop"),
        Node.getter(Node.name("getProp")),
        Node.numberLiteral(1)
    );
    refs.clear();
    refs.add(new Reference(objectWithInitAndGetter, Behavior.IN, referenceInfo));
    inlineObjectLiterals.new InliningBehavior().splitObject(v, null, new Reference(objectWithInitAndGetter, Behavior.INIT, referenceInfo), referenceInfo);

    assertEquals(3, v.getVars().size());
    newVar = v.getVars().get(2);
    assertNotNull(newVar.getName());
    assertEquals("JSCompiler_object_inline_getProp_test_id", newVar.getName().getString());

    Node objectWithInitAndSetter = Node.objectLiteral(
        Node.name("prop"),
        Node.setter(Node.name("setProp")),
        Node.numberLiteral(1)
    );
    refs.clear();
    refs.add(new Reference(objectWithInitAndSetter, Behavior.IN, referenceInfo));
    inlineObjectLiterals.new InliningBehavior().splitObject(v, null, new Reference(objectWithInitAndSetter, Behavior.INIT, referenceInfo), referenceInfo);

    assertEquals(4, v.getVars().size());
    newVar = v.getVars().get(3);
    assertNotNull(newVar.getName());
    assertEquals("JSCompiler_object_inline_setProp_test_id", newVar.getName().getString());
  }

  @Test(expected = AssertionError.class)
  public void testSplitObjectWithInvalidObject() {
    Node externs = Node.objectLiteral();
    Node root = Node.stringLiteral("test");
    ReferenceCollection referenceInfo = new ReferenceCollection();

    Var v = new Var("testVar", Node.NAME, null, null, null, null, null, false);
    Scope scope = new Scope();
    scope.addVar(v);

    Node object = Node.call(Node.NAME("getNodeByPath"), Node.array(Node.stringLiteral("test"), Node.numberLiteral(0)));
    referenceInfo.addReference(new Reference(object, Behavior.IN, referenceInfo));
    List<Reference> refs = Lists.newArrayList();
    refs.add(new Reference(object, Behavior.IN, referenceInfo));

    inlineObjectLiterals.new InliningBehavior().splitObject(v, null, null, referenceInfo);
  }
}