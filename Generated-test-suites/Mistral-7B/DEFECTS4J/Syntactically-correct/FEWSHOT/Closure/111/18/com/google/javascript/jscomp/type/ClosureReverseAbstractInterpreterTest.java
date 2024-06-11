package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ClosureReverseAbstractInterpreterTest {

  private final Node conditionNode;
  private final JSTypeRegistry typeRegistry;
  private final JSType expectedResult;
  private final boolean outcome;

  public ClosureReverseAbstractInterpreterTest(Node conditionNode,
      JSTypeRegistry typeRegistry, JSType expectedResult, boolean outcome) {
    this.conditionNode = conditionNode;
    this.typeRegistry = typeRegistry;
    this.expectedResult = expectedResult;
    this.outcome = outcome;
  }

  @Parameterized.Parameters(name = "{index}: conditionNode={0}, outcome={1}, expectedResult={2}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.STRING),

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        false, Type.VOID,

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.NUMBER),

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.BOOLEAN),

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.FUNCTION),

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.ARRAY),

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.OBJECT),

        new Node(1, null, "goog.isDef", "x"), null, new JSTypeRegistry(), null,
        false, null,

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        true, new ObjectType(Type.NULL),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        false, new ObjectType(Type.STRING),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        false, new ObjectType(Type.NUMBER),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        false, new ObjectType(Type.BOOLEAN),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        false, new ObjectType(Type.FUNCTION),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        false, new ObjectType(Type.ARRAY),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        false, new ObjectType(Type.OBJECT),

        new Node(1, null, "goog.isNull", "x"), null, new JSTypeRegistry(), null,
        true, null,

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, true, new ObjectType(Type.STRING),

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, true, new ObjectType(Type.NUMBER),

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, true, new ObjectType(Type.BOOLEAN),

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, true, new ObjectType(Type.FUNCTION),

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, true, new ObjectType(Type.ARRAY),

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, true, new ObjectType(Type.OBJECT),

        new Node(1, null, "goog.isDefAndNotNull", "x"), null, new JSTypeRegistry(),
        null, false, null
    });
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD,
            typeRegistry);
    FlowScope blindScope = new FlowScope();
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, outcome);
    JSType actualResult = flowScope.getBinding(parameter.getIdentifierName())
        .getType();
    assertNotNull(actualResult);
    assertEquals(expectedResult, actualResult);
  }
}