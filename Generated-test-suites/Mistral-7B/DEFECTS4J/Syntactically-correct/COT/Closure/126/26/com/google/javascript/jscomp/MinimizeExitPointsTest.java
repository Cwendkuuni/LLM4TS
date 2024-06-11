package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    // Given a simple expression
    Node input = IR.literal(1);
    minimizer.process(null, input);
    // Then no exit points should be minimized
    assertTrue(input.toString().equals("1"));

    // Given a return statement
    input = IR.function("test", null, null, null).addChild(
        IR.returnStatement(IR.literal(2)));
    minimizer.process(null, input);
    // Then the return statement should not be minimized
    assertTrue(input. toString().equals(
        "function test() {" +
        "  return 2;" +
        "}"));

    // Given a break statement outside a loop
    input = IR.block().addChild(IR.label("loopLabel")).addChild(
        IR.breakStatement());
    minimizer.process(null, input);
    // Then the break statement should not be minimized
    assertTrue(input.toString().equals(
        "function() {" +
        "  label loopLabel;" +
        "  break;" +
        "}"));

    // Given a continue statement outside a loop
    input = IR.block().addChild(IR.continueStatement());
    minimizer.process(null, input);
    // Then the continue statement should not be minimized
    assertTrue(input.toString().equals("function() {}"));

    // Given a break statement inside a label
    input = IR.block().addChild(IR.label("loopLabel")).addChild(
        IR.forStatement(
            IR.literal(0),
            IR.literal(1),
            null,
            IR.block().addChild(IR.breakStatement()))).addChild(
        IR.label("anotherLabel"));
    minimizer.process(null, input);
    // Then the break statement should be minimized and moved to the loopLabel
    assertTrue(input.toString().equals(
        "function() {" +
        "  label loopLabel:" +
        "  for (;;) {" +
        "    label anotherLabel;" +
        "    break;" +
        "  }" +
        "}"));

    // Given a continue statement inside a loop
    input = IR.block().addChild(IR.label("loopLabel")).addChild(
        IR.forStatement(
            IR.literal(0),
            IR.literal(1),
            null,
            IR.block().addChild(IR.continueStatement()))).addChild(
        IR.label("anotherLabel"));
    minimizer.process(null, input);
    // Then the continue statement should be minimized and moved to the loopLabel
    assertTrue(input.toString().equals(
        "function() {" +
        "  label loopLabel:" +
        "  for (;;) {" +
        "    label anotherLabel;" +
        "    continue;" +
        "  }" +
        "}"));

    // Given a return statement inside a function
    input = IR.function("test", null, null, null).addChild(
        IR.returnStatement(IR.literal(2)));
    minimizer.process(null, input);
    // Then the return statement should not be minimized
    assertTrue(input.toString().equals(
        "function test() {" +
        "  return 2;" +
        "}"));

    // Given a break statement inside a loop with a false condition
    input = IR.block().addChild(IR.label("loopLabel")).addChild(
        IR.forStatement(
            IR.literal(0),
            IR.literal(1),
            null,
            IR.block().addChild(IR.ifStatement(
                IR.literal(false),
                null,
                IR.block().addChild(IR.breakStatement())))));
    minimizer.process(null, input);
    // Then the break statement should not be minimized
    assertTrue(input.toString().equals(
        "function() {" +
        "  label loopLabel:" +
        "  for (;;) {" +
        "    if (false) {" +
        "      break;" +
        "    }" +
        "  }" +
        "}"));

    // Given a try statement with a catch block and a return statement
    input = IR.tryStatement(
        IR.block().addChild(IR.returnStatement(IR.literal(2))),
        null,
        IR.block().addChild(IR.returnStatement(IR.literal(3))));
    minimizer.process(null, input);
    // Then the first return statement should be minimized and moved to the try block
    assertTrue(input.toString().equals(
        "function() {" +
        "  try {" +
        "    return 2;" +
        "  } catch (e) {" +
        "    return 3;" +
        "  }" +
        "}"));

    // Given a try statement with a finally block and a return statement
    input = IR.tryStatement(
        IR.block().addChild(IR.returnStatement(IR.literal(2))),
        null,
        IR.block().addChild(IR.returnStatement(IR.literal(3))));
    minimizer.process(null, input);
    // Then the first return statement should not be minimized because of the finally block
    assertTrue(input.toString().equals(
        "function() {" +
        "  try {" +
        "    return 2;" +
        "  } finally {" +
        "    return 3;" +
        "  }" +
        "}"));

    // Given a nested try statement with a catch block and a return statement
    input = IR.tryStatement(
        IR.tryStatement(
            IR.block().addChild(IR.returnStatement(IR.literal(2))),
            null,
            IR.block().addChild(IR.returnStatement(IR.literal(3)))),
        null,
        IR.block().addChild(IR.returnStatement(IR.literal(4))));
    minimizer.process(null, input);
    // Then the first return statement should be minimized and moved to the outer try block
    assertTrue(input.toString().equals(
        "function() {" +
        "  try {" +
        "    try {" +
        "      return 2;" +
        "    } catch (e) {" +
        "      return 3;" +
        "    }" +
        "  } catch (e) {" +
        "    return 4;" +
        "  }" +
        "}"));

    // Given a switch statement with a default case and a break statement
    input = IR.switchStatement(IR.identifier("x"),
        IR.caseStatement(IR.literal(1), IR.block().addChild(
            IR.breakStatement())),
        IR.defaultCase(IR.block().addChild(IR.breakStatement())));
    minimizer.process(null, input);
    // Then the break statement should not be minimized because of the default case
    assertTrue(input.toString().equals(
        "switch (x) {" +
        "  case 1:" +
        "    break;" +
        "  default:" +
        "    break;" +
        "}"));
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsThrowsException() {
    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    Node input = IR.block().addChild(IR.label("loopLabel")).addChild(
        IR.forStatement(
            IR.literal(0),
            IR.literal(1),
            null,
            IR.block().addChild(IR.returnStatement(IR.literal(2)))));
    minimizer.process(null, input);
  }
}