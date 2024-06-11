import static org.junit.Assert.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.Visitor;

public class ClosureReverseAbstractInterpreterTest {

 private final JSTypeRegistry typeRegistry = new JSTypeRegistry();
 private final CodingConvention convention = new CodingConvention();
 private final ClosureReverseAbstractInterpreter interpreter =
     new ClosureReverseAbstractInterpreter(convention, typeRegistry);

 @org.junit.Test
 public void testIsDef() {
 Node condition = createNode("goog.isDef(x)");
 JSType expectedType = interpreter.getType(condition, null);
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isDef").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsDefFalse() {
 Node condition = createNode("goog.isDef(undefined)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isDef").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsDefAndNotNull() {
 Node condition = createNode("goog.isDefAndNotNull(x)");
 JSType expectedType = interpreter.getType(condition, null);
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isDefAndNotNull").apply(typeRestriction);
 assertEquals(getRestrictedWithoutUndefined(expectedType), restrictedType);
 }

 @org.junit.Test
 public void testIsDefAndNotNullFalse() {
 Node condition = createNode("goog.isDefAndNotNull(null)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isDefAndNotNull").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsString() {
 Node condition = createNode("goog.isString(x)");
 JSType expectedType = interpreter.getType(condition, null);
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isString").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsStringFalse() {
 Node condition = createNode("goog.isString(1)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isString").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsBoolean() {
 Node condition = createNode("goog.isBoolean(x)");
 JSType expectedType = interpreter.getType(condition, null);
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isBoolean").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsBooleanFalse() {
 Node condition = createNode("goog.isBoolean(1)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isBoolean").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsNumber() {
 Node condition = createNode("goog.isNumber(x)");
 JSType expectedType = interpreter.getType(condition, null);
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isNumber").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsNumberFalse() {
 Node condition = createNode("goog.isNumber(x.toString)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isNumber").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsFunction() {
 Node condition = createNode("goog.isFunction(x)");
 JSType expectedType = interpreter.getType(condition, null);
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isFunction").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsFunctionFalse() {
 Node condition = createNode("goog.isFunction(null)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isFunction").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsArrayTrue() {
 Node condition = createNode("goog.isArray(x)");
 JSType expectedType = ARRAY_TYPE;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isArray").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsArrayFalse() {
 Node condition = createNode("goog.isArray(x.toString)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isArray").apply(typeRestriction);
 assertNull(restrictedType);
 }

 @org.junit.Test
 public void testIsObjectTrue() {
 Node condition = createNode("goog.isObject(x)");
 JSType expectedType = OBJECT_TYPE;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
 JSType restrictedType = interpreter.restricters.get("isObject").apply(typeRestriction);
 assertEquals(expectedType, restrictedType);
 }

 @org.junit.Test
 public void testIsObjectFalse() {
 Node condition = createNode("goog.isObject(1)");
 JSType expectedType = null;
 TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
 JSType restrictedType = interpreter.restricters.get("isObject").apply(typeRestriction);
 assertNull(restrictedType);
 }

 private Node createNode(String nodeString) {
 Node node = new Node();
 node.setType(STRING);
 node.setString(nodeString);
 return node;
 }

 private JSType getRestrictedWithoutUndefined(JSType type) {
 return type.isUndefined() ? NULL_TYPE : type;
 }

 private JSType getRestrictedWithoutNull(JSType type) {
 return type.isNull() ? ARRAY_TYPE : type;
 }
}