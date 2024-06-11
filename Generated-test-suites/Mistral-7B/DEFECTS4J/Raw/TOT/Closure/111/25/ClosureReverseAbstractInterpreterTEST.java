package com.google.javascript.jscomp.type.test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.TypeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClosureReverseAbstractInterpreterTest {

 private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
 private static final CodingConvention codingConvention = CodingConvention.getInstance();

 @Test
 public void testIsDef() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 codingConvention, typeRegistry);

 // Positive test - when condition outcome is true
 JSType type = interpreter.restrictParameter(
 NodeFactory.createName("param"), TypeUtil.createUndefinedType(), null,
 interpreter.restricters.get("isDef"), true);
 assertEquals(TypeUtil.createBooleanType(), type);

 // Negative test - when condition outcome is false and parameter type is null
 type = interpreter.restrictParameter(
 NodeFactory.createName("param"), null, null, interpreter.restricters.get("isDef"), false);
 assertNull(type);

 // Negative test - when condition outcome is false and parameter type is not undefined
 type = interpreter.restrictParameter(
 NodeFactory.createName("param"), TypeUtil.createStringType(), null,
 interpreter.restricters.get("isDef"), false);
 assertNull(type);
 }

 @Test
 public void testIsDefAndNotNull() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 codingConvention, typeRegistry);

 // Positive test - when condition outcome is true
 JSType type = interpreter.restrictParameter(
 NodeFactory.createName("param"), TypeUtil.createStringType(), null,
 interpreter.restricters.get("isDefAndNotNull"), true);
 assertEquals(TypeUtil.createStringType(), type);

 // Negative test - when condition outcome is false
 type = interpreter.restrictParameter(
 NodeFactory.createName("param"), TypeUtil.createStringType(), null,
 interpreter.restricters.get("isDefAndNotNull"), false);
 assertNull(type);

 // Negative test - when parameter type is null
 type = interpreter.restrictParameter(
 NodeFactory.createName("param"), null, null, interpreter.restricters.get("isDefAndNotNull"), false);
 assertNull(type);
 }

 //... Add test cases for other methods in the class like isNull, isString, isBoolean, isNumber, isFunction, isArray, and isObject

 //... Add test cases for error scenarios, edge cases, and typical use cases

}