package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Rule
  public TestName testName = new TestName();

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> testName.getMethodName());

    String code = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c;" +
        "}";

    Node externs = Node.externalCall(null, null, "foo");
    Node root = Node.parse(code);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    Var objVar = null;
    for (Var var : scope.getVars()) {
      if (var.name.equals("obj")) {
        objVar = var;
        break;
      }
    }

    assertNotNull(objVar);

    List<Reference> references = null;
    for (Iterator<Var> it = scope.getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith("JSCompiler_object_inline_")) {
        references = v.getReferenceCollection();
        break;
      }
    }

    assertNotNull(references);

    // Check that each reference corresponds to a property in the original object
    Map<String, Node> expectedProperties = Maps.newLinkedHashMap();
    expectedProperties.put("a", Node.number(1));
    expectedProperties.put("b", Node.number(2));
    expectedProperties.put("c", Node.number(3));

    for (Reference ref : references.getReferences()) {
      Node refNode = ref.getNode();
      String propertyName = refNode.getLastChild().getString();
      Node propertyNode = expectedProperties.get(propertyName);
      assertNotNull(propertyNode);
      assertSame(propertyNode, refNode.getFirstChild());
    }
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> testName.getMethodName());

    String code = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c;" +
        "  obj = {" +
        "    a: 4," +
        "    b: 5," +
        "    c: 6" +
        "  };" +
        "}";

    Node externs = Node.externalCall(null, null, "foo");
    Node root = Node.parse(code);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    Var objVar = null;
    for (Var var : scope.getVars()) {
      if (var.name.equals("obj")) {
        objVar = var;
        break;
      }
    }

    assertNotNull(objVar);

    List<Reference> references = null;
    for (Iterator<Var> it = scope.getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith("JSCompiler_object_inline_")) {
        references = v.getReferenceCollection();
        break;
      }
    }

    assertNotNull(references);

    // Check that each reference corresponds to a property in the original object
    Map<String, Node> expectedProperties = Maps.newLinkedHashMap();
    expectedProperties.put("a", Node.number(4));
    expectedProperties.put("b", Node.number(5));
    expectedProperties.put("c", Node.number(6));

    for (Reference ref : references.getReferences()) {
      Node refNode = ref.getNode();
      String propertyName = refNode.getLastChild().getString();
      Node propertyNode = expectedProperties.get(propertyName);
      assertNotNull(propertyNode);
      assertSame(propertyNode, refNode.getFirstChild());
    }

    // Check that the initial values have been stored in the corresponding variables
    Map<String, Node> actualProperties = Maps.newLinkedHashMap();
    for (Var var : scope.getVars()) {
      if (var.name.startsWith("JSCompiler_object_inline_")) {
        List<Reference> refs = var.getReferenceCollection().getReferences();
        assertNotNull(refs);
        assertNotNull(refs.get(0).getAssignedValue());
        actualProperties.put(var.name.substring(23), refs.get(0).getAssignedValue());
      }
    }

    assertNotNull(actualProperties.get("a"));
    assertNotNull(actualProperties.get("b"));
    assertNotNull(actualProperties.get("c"));

    Map<String, Object> expectedInitialValues = Maps.newLinkedHashMap();
    expectedInitialValues.put("a", 1);
    expectedInitialValues.put("b", 2);
    expectedInitialValues.put("c", 3);

    for (Map.Entry<String, Node> entry : actualProperties.entrySet()) {
      Object actualInitialValue = entry.getValue().toJavaScriptValue(null);
      assertNotNull(actualInitialValue);
      assertEquals(expectedInitialValues.get(entry.getKey()), actualInitialValue);
    }
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> testName.getMethodName());

    String code = "function foo() {" +
        "  var obj = {" +
        "    a: obj" +
        "  };" +
        "}";

    Node externs = Node.externalCall(null, null, "foo");
    Node root = Node.parse(code);

    inlineObjectLiterals.process(externs, root);

    // The code should be unchanged, as the self-referential assignment
    // is not inlineable
    assertEquals(code, root.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithDuplicateProperties() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> testName.getMethodName());

    String code = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    a: 2," +
        "    b: 3" +
        "  };" +
        "}";

    Node externs = Node.externalCall(null, null, "foo");
    Node root = Node.parse(code);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    List<Reference> references = null;
    for (Iterator<Var> it = scope.getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith("JSCompiler_object_inline_")) {
        references = v.getReferenceCollection().getReferences();
        break;
      }
    }

    assertNotNull(references);

    // Check that there is only one reference for each property
    Set<String> propertyNames = Sets.newHashSet();
    for (Reference ref : references.getReferences()) {
      assertNotNull(ref.getNode());
      String propertyName = ref.getNode().getLastChild().getString();
      assertNotNull(propertyName);
      propertyNames.add(propertyName);
    }

    assertEquals(3, propertyNames.size());
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> testName.getMethodName());

    String code = "function foo() {" +
        "  var obj = {};" +
        "}";

    Node externs = Node.externalCall(null, null, "foo");
    Node root = Node.parse(code);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    List<Reference> references = null;
    for (Iterator<Var> it = scope.getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith("JSCompiler_object_inline_")) {
        references = v.getReferenceCollection().getReferences();
        break;
      }
    }

    assertNotNull(references);
    assertNotNull(references.get(0).getAssignedValue());
    assertNull(references.get(0).getAssignedValue().getFirstChild());
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObjectAndProperties() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> testName.getMethodName());

    String code = "function foo() {" +
        "  var obj = {" +
        "  };" +
        "  obj.a = 1;" +
        "}";

    Node externs = Node.externalCall(null, null, "foo");
    Node root = Node.parse(code);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    List<Reference> references = null;
    for (Iterator<Var> it = scope.getVars().iterator(); it.hasNext();) {
      Var v = it.next();
      if (v.name.startsWith("JSCompiler_object_inline_")) {
        references = v.getReferenceCollection().getReferences();
        break;
      }
    }

    assertNotNull(references);
    assertNotNull(references.get(0).getAssignedValue());
    assertNull(references.get(0).getAssignedValue().getFirstChild());

    List<Reference> propertyReferences = null;
    for (Reference ref : references) {
      if (ref.isLvalue()) {
        propertyReferences = ref.getReferenceCollection().getReferences();
        break;
      }
    }

    assertNotNull(propertyReferences);
    assertNotNull(propertyReferences.get(0).getAssignedValue());
    assertNotNull(propertyReferences.get(0).getAssignedValue().getFirstChild());
  }
}