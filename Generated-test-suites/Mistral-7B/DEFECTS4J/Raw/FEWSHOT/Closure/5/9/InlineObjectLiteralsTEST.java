package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Test case for object literal with no references
    String code1 = "var obj1 = {prop1: \"value1\"};";

    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is the name of the AbstractCompiler instance
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);

    Node externs = Node.createString("", null);
    Node root = Node.createText(code1, null);
    inlineObjectLiterals.process(externs, root);

    List<String> expectedCode1 = Lists.newArrayList(
        "var prop1_1 = \"value1\";",
        code1.substring(0, code1.lastIndexOf("=")), // Exclude the object initialization
        "var obj1 = {};"
    );

    Node expectedRoot1 = Node.createText(
        String.join("\n", expectedCode1),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot1.getString(), root.getString());

    // Test case for object literal with references
    String code2 = "var obj2 = {\n" +
        "  prop1: \"value1\",\n" +
        "  prop2: obj1.prop1,\n" +
        "};";

    Var obj1Var = new Var("obj1");
    Var obj2Var = new Var("obj2");

    Scope scope = new Scope();
    scope.addVar(obj1Var);
    scope.addVar(obj2Var);

    Node externs2 = Node.createString("", scope);
    Node root2 = Node.createText(code2, scope);

    inlineObjectLiterals.process(externs2, root2);

    List<String> expectedCode2 = Lists.newArrayList(
        "var prop1_1 = \"value1\";",
        "var prop1_2 = prop1_1;", // Forward reference
        "var obj1 = {\n" +
        "  prop1: prop1_1,\n" +
        "};",
        "var obj2 = {\n" +
        "  prop1: prop1_2,\n" +
        "};"
    );

    Node expectedRoot2 = Node.createText(
        String.join("\n", expectedCode2),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot2.getString(), root2.getString());

    // Test case for object literal with self-referential assignment
    String code3 = "var obj3 = {\n" +
        "  prop1: obj3.prop1,\n" +
        "};";

    Var obj3Var = new Var("obj3");

    Scope scope3 = new Scope();
    scope3.addVar(obj3Var);

    Node externs3 = Node.createString("", scope3);
    Node root3 = Node.createText(code3, scope3);

    inlineObjectLiterals.process(externs3, root3);

    List<String> expectedCode3 = Lists.newArrayList(
        "var prop1_1 = {};", // Invalid object literal assignment
        "var obj3 = {};"
    );

    Node expectedRoot3 = Node.createText(
        String.join("\n", expectedCode3),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot3.getString(), root3.getString());

    // Test case for object literal with missing property reference
    String code4 = "var obj4 = {\n" +
        "  prop1: obj1.prop1,\n" +
        "  prop2: missingProp,\n" +
        "};";

    Var obj4Var = new Var("obj4");

    Scope scope4 = new Scope();
    scope4.addVar(obj4Var);

    Node externs4 = Node.createString("", scope4);
    Node root4 = Node.createText(code4, scope4);

    inlineObjectLiterals.process(externs4, root4);

    List<String> expectedCode4 = Lists.newArrayList(
        "var obj4 = {};"
    );

    Node expectedRoot4 = Node.createText(
        String.join("\n", expectedCode4),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot4.getString(), root4.getString());

    // Test case for object literal with missing property and self-referential assignment
    String code5 = "var obj5 = {\n" +
        "  prop1: obj3.prop1,\n" +
        "  prop2: missingProp,\n" +
        "};";

    Var obj5Var = new Var("obj5");

    Scope scope5 = new Scope();
    scope5.addVar(obj5Var);

    Node externs5 = Node.createString("", scope5);
    Node root5 = Node.createText(code5, scope5);

    inlineObjectLiterals.process(externs5, root5);

    List<String> expectedCode5 = Lists.newArrayList(
        "var obj5 = {};"
    );

    Node expectedRoot5 = Node.createText(
        String.join("\n", expectedCode5),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot5.getString(), root5.getString());

    // Test case for object literal with multiple references
    String code6 = "var obj6 = {\n" +
        "  prop1: obj1.prop1,\n" +
        "  prop2: obj1.prop1,\n" +
        "  prop3: obj2.prop1,\n" +
        "};";

    Var obj1Var6 = new Var("obj1");
    Var obj2Var6 = new Var("obj2");
    Var obj6Var = new Var("obj6");

    Scope scope6 = new Scope();
    scope6.addVar(obj1Var6);
    scope6.addVar(obj2Var6);
    scope6.addVar(obj6Var);

    Node externs6 = Node.createString("", scope6);
    Node root6 = Node.createText(code6, scope6);

    inlineObjectLiterals.process(externs6, root6);

    List<String> expectedCode6 = Lists.newArrayList(
        "var prop1_1 = \"value1\";",
        "var prop1_2 = prop1_1;", // Forward reference
        "var obj1 = {\n" +
        "  prop1: prop1_1,\n" +
        "};",
        "var prop1_3 = prop1_1;", // Forward reference
        "var obj2 = {\n" +
        "  prop1: prop1_2,\n" +
        "};",
        "var obj6 = {\n" +
        "  prop1: prop1_3,\n" +
        "  prop2: prop1_1,\n" +
        "  prop3: prop1_2,\n" +
        "};"
    );

    Node expectedRoot6 = Node.createText(
        String.join("\n", expectedCode6),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot6.getString(), root6.getString());

    // Test case for object literal with initial values
    String code7 = "var obj7 = {\n" +
        "  prop1: \"value1\",\n" +
        "  prop2: 123,\n" +
        "};";

    Var obj7Var = new Var("obj7");

    Scope scope7 = new Scope();
    scope7.addVar(obj7Var);

    Node externs7 = Node.createString("", scope7);
    Node root7 = Node.createText(code7, scope7);

    inlineObjectLiterals.process(externs7, root7);

    List<String> expectedCode7 = Lists.newArrayList(
        "var prop1_1 = \"value1\";",
        "var prop2_1 = 123;",
        "var obj7 = {\n" +
        "  prop1: prop1_1,\n" +
        "  prop2: prop2_1,\n" +
        "};"
    );

    Node expectedRoot7 = Node.createText(
        String.join("\n", expectedCode7),
        null
    );

    // Check the generated code against the expected code
    assertEquals(expectedRoot7.getString(), root7.getString());
  }

  @Test
  public void testIsInlinableObject() {
    // Test case for an object literal with no references
    List<Reference> refs1 = Lists.newArrayList();

    boolean expectedResult1 = InlineObjectLiterals.isInlinableObject(refs1);

    assertTrue(expectedResult1);

    // Test case for an object literal with one reference to a property
    List<Reference> refs2 = Lists.newArrayList(
        new Reference(null, null, null, null, null, null, null, false, null)
    );

    String propName = "prop1";
    refs2.get(0).getNode().setLastChild(
        NodeUtil.newNameNode(propName, null)
    );

    boolean expectedResult2 = InlineObjectLiterals.isInlinableObject(refs2);

    assertFalse(expectedResult2);

    // Test case for an object literal with one reference to a variable
    List<Reference> refs3 = Lists.newArrayList(
        new Reference(null, null, null, null, null, null, null, false, null)
    );

    Var var1 = new Var("var1");
    refs3.get(0).getNode().setLastChild(NodeUtil.newNameNode(var1.name, null));

    boolean expectedResult3 = InlineObjectLiterals.isInlinableObject(refs3);

    assertFalse(expectedResult3);

    // Test case for an object literal with one reference to a property and one reference to a variable
    List<Reference> refs4 = Lists.newArrayList(
        new Reference(null, null, null, null, null, null, null, false, null),
        new Reference(null, null, null, null, null, null, null, false, null)
    );

    refs4.get(0).getNode().setLastChild(
        NodeUtil.newNameNode("prop1", null)
    );
    refs4.get(1).getNode().setLastChild(NodeUtil.newNameNode(var1.name, null));

    boolean expectedResult4 = InlineObjectLiterals.isInlinableObject(refs4);

    assertFalse(expectedResult4);
  }

  @Test
  public void testComputeVarList() {
    // Test case for an object literal with no references
    Var objVar = new Var("obj");

    ReferenceCollection referenceInfo1 = new ReferenceCollection(
        null, null, null, null, null, null, null, null, null, null
    );

    Map<String, String> expectedVarList1 = Maps.newLinkedHashMap();

    Map<String, String> varList1 = InlineObjectLiterals.computeVarList(
        objVar, referenceInfo1
    );

    assertEquals(expectedVarList1, varList1);

    // Test case for an object literal with one property reference
    List<Reference> refs1 = Lists.newArrayList(
        new Reference(null, null, null, null, null, null, null, false, null)
    );

    String propName1 = "prop1";
    refs1.get(0).getNode().setLastChild(
        NodeUtil.newNameNode(propName1, null)
    );

    Map<String, String> expectedVarList2 = Maps.newLinkedHashMap();
    expectedVarList2.put("prop1", "prop1_" + Integer.toString(System.identityHashCode(this)));

    Map<String, String> varList2 = InlineObjectLiterals.computeVarList(
        objVar, new ReferenceCollection(null, null, null, null, refs1, null, null, null, null, null)
    );

    assertEquals(expectedVarList2, varList2);

    // Test case for an object literal with one variable reference
    List<Reference> refs2 = Lists.newArrayList(
        new Reference(null, null, null, null, null, null, null, false, null)
    );

    Var var1 = new Var("var1");
    refs2.get(0).getNode().setLastChild(NodeUtil.newNameNode(var1.name, null));

    Map<String, String> expectedVarList3 = Maps.newLinkedHashMap();
    expectedVarList3.put(var1.name, "var1_" + Integer.toString(System.identityHashCode(this)));

    Map<String, String> varList3 = InlineObjectLiterals.computeVarList(
        objVar, new ReferenceCollection(null, null, null, null, refs2, null, null, null, null, null)
    );

    assertEquals(expectedVarList3, varList3);

    // Test case for an object literal with one property reference and one variable reference
    List<Reference> refs3 = Lists.newArrayList(
        new Reference(null, null, null, null, null, null, null, false, null),
        new Reference(null, null, null, null, null, null, null, false, null)
    );

    refs3.get(0).getNode().setLastChild(
        NodeUtil.newNameNode("prop1", null)
    );
    refs3.get(1).getNode().setLastChild(NodeUtil.newNameNode(var1.name, null));

    Map<String, String> expectedVarList4 = Maps.newLinkedHashMap();
    expectedVarList4.put("prop1", "prop1_" + Integer.toString(System.identityHashCode(this)));
    expectedVarList4.put(var1.name, "var1_" + Integer.toString(System.identityHashCode(this)));

    Map<String, String> varList4 = InlineObjectLiterals.computeVarList(
        objVar, new ReferenceCollection(null, null, null, null, refs3, null, null, null, null, null)
    );

    assertEquals(expectedVarList4, varList4);
  }
}