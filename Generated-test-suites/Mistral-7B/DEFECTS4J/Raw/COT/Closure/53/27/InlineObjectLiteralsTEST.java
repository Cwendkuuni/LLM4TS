import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.Reference.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // When
    Node externs = Node.newText("{}");
    Node root = Node.newText("{}");
    inlineObjectLiterals.process(externs, root);

    // Then
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);
    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    ReferenceMap referenceMap = referenceCollection.getReferenceMap();

    // Check that a simple object literal is inlined
    Var v = referenceMap.getVariable("x");
    List<Reference> refs = referenceMap.getReferences(v);
    assertEquals(1, refs.size());
    Reference ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "x")},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with a single property is inlined
    Var w = referenceMap.getVariable("w");
    refs = referenceMap.getReferences(w);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "w"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with multiple properties is inlined
    Var z = referenceMap.getVariable("z");
    refs = referenceMap.getReferences(z);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "z"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.INT_LITERAL, 1)),
        new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.INT_LITERAL, 2)),
        new Node(Token.ASSIGN, new Node(Token.NAME, "c"), new Node(Token.INT_LITERAL, 3))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with a getter is handled correctly
    Var y = referenceMap.getVariable("y");
    refs = referenceMap.getReferences(y);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "y"),
        new Node(Token.GETPROP, new Node(Token.NAME, "x"), new Node(Token.NAME, "get"))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with a setter is handled correctly
    Var u = referenceMap.getVariable("u");
    refs = referenceMap.getReferences(u);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "u"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "set")),
        new Node(Token.SETPROP, new Node(Token.NAME, "y"), new Node(Token.INT_LITERAL, 5))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with a method is handled correctly
    Var t = referenceMap.getVariable("t");
    refs = referenceMap.getReferences(t);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "t"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "myMethod"))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with nested objects is handled correctly
    Var q = referenceMap.getVariable("q");
    refs = referenceMap.getReferences(q);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    Node nestedObject = ((Node) ref.getNode().getFirstChild()).getFirstChild();
    assertEquals(new Object[]{Token.OBJECTLIT, new Node(Token.NAME, "q"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.OBJECTLIT,
            new Node(Token.NAME, "nested"),
            new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.INT_LITERAL, 1)),
            new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.INT_LITERAL, 2))))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with a property with a space is handled correctly
    Var r = referenceMap.getVariable("r");
    refs = referenceMap.getReferences(r);
    assertEquals(1, refs.size());
    ref = refs.get(0);
    assertEquals(new Object[]{Token.OBJECTLITERAL, new Node(Token.NAME, "r"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.STRING_LITERAL, "my property"))},
        ref.getNode().getChildren().toArray());

    // Check that an object literal with cyclic references is rejected
    Var cyclicObject = new Var("cyclicObj", Node.newText("{x: {x: {} }}"), null);
    ReferenceCollection cyclicReferenceCollection = callback.process(externs, Node.newText("{x: cyclicObj}"));
    assertEquals(0, cyclicReferenceCollection.size());

    // Check that an object literal with an exported property is not inlined
    Var exportedObject = new Var("exportedObj", Node.newText("{exportedProp: 1}"), null);
    Reference exportedRef = referenceMap.getReferences(exportedObject).get(0);
    assertTrue(exportedRef.getNode().getChildren()[0].getType() == Token.EXPORT_ASSIGN);

    // Check that a variable that is used directly in a function call is not inlined
    Var usedInFunctionCall = new Var("usedInFunctionCall", Node.newText("{}"), null);
    Node functionCallNode = Node.newText("myFunction(usedInFunctionCall)");
    Reference usedInFunctionCallRef = referenceMap.getReferences(usedInFunctionCall).get(0);
    Node usedInFunctionCallNode = usedInFunctionCallRef.getNode();
    Node functionCall = ((Node) usedInFunctionCallNode.getParent()).getFirstChild();
    assertFalse(NodeUtil.isVariableAssignment(functionCall.getParent()));

    // Check that a variable that is used in an assignment is inlined
    Var usedInAssignment = new Var("usedInAssignment", Node.newText("{}"), null);
    Node assignmentNode = Node.newText("x = usedInAssignment");
    Reference usedInAssignmentRef = referenceMap.getReferences(usedInAssignment).get(0);
    Node usedInAssignmentNode = usedInAssignmentRef.getNode();
    Node assignment = ((Node) usedInAssignmentNode.getParent()).getParent();
    assertTrue(NodeUtil.isVariableAssignment(assignment));

    // Check that a variable that is used in a getter is inlined
    Var usedInGetter = new Var("usedInGetter", Node.newText("{}"), null);
    Node getterNode = Node.newText("x.getter = usedInGetter.property");
    Reference usedInGetterRef = referenceMap.getReferences(usedInGetter).get(0);
    Node usedInGetterNode = usedInGetterRef.getNode();
    Node getter = ((Node) usedInGetterNode.getParent()).getParent();
    assertTrue(getter.getType() == Token.GETPROP);
    assertEquals("property", getter.getLastChild().getString());

    // Check that a variable that is used in a setter is inlined
    Var usedInSetter = new Var("usedInSetter", Node.newText("{}"), null);
    Node setterNode = Node.newText("x.setter = usedInSetter.property = 5");
    Reference usedInSetterRef = referenceMap.getReferences(usedInSetter).get(0);
    Node usedInSetterNode = usedInSetterRef.getNode();
    Node setter = ((Node) usedInSetterNode.getParent()).getParent();
    assertTrue(setter.getType() == Token.SETPROP);
    assertEquals("property", setter.getFirstChild().getString());

    // Check that a variable that is used in an assignment with a getter is inlined
    Var usedInGetterAssignment = new Var("usedInGetterAssignment", Node.newText("{}"), null);
    Node getterAssignmentNode = Node.newText("x.getter = usedInGetterAssignment");
    Reference usedInGetterAssignmentRef = referenceMap.getReferences(usedInGetterAssignment).get(0);
    Node usedInGetterAssignmentNode = usedInGetterAssignmentRef.getNode();
    Node getterAssignment = ((Node) usedInGetterAssignmentNode.getParent()).getParent();
    Node assignment = ((Node) getterAssignment.getParent()).getFirstChild();
    assertTrue(NodeUtil.isVariableAssignment(assignment));

    // Check that a variable with a self-referential assignment is not inlined
    Var selfReferentialVariable = new Var("selfReferentialVariable", Node.newText("{x: x}"), null);
    ReferenceCollection selfReferentialReferenceCollection = callback.process(externs, Node.newText("{}"));
    assertEquals(0, selfReferentialReferenceCollection.size());

    // Check that a variable with a circular reference is not inlined
    Var circularVariable = new Var("circularVariable", Node.newText("{x: {x: circularVariable}}"), null);
    Reference circularReference = referenceMap.getReferences(circularVariable).get(0);
    assertFalse(circularReference.getNode().getType() == Token.OBJECTLIT);
  }
}

class InliningBehavior implements Behavior {
  // Implement the InliningBehavior class here (already implemented in the given code)
}