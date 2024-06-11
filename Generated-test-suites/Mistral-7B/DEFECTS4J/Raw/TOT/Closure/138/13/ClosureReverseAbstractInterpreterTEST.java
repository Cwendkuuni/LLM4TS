import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Visitor;
import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

 private static JSTypeRegistry typeRegistry;
 private static CodingConvention codingConvention;
 private static ClosureReverseAbstractInterpreter interpreter;

 @BeforeClass
 public static void setup() {
 typeRegistry = new JSTypeRegistry();
 codingConvention = new CodingConvention();
 interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
 }

 @Test
 public void testIsDef() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 TypeRestriction restriction = new TypeRestriction(type, false);
 JSType expected = null;
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isDef"), false).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(FunctionType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 restriction = new TypeRestriction(type, true);
 expected = typeRegistry.getNativeType(FunctionType.class);
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isDef"), true).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsDefAndNotNull() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(ObjectType.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isDefAndNotNull"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(NullType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isDefAndNotNull"), false).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNotNull() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 TypeRestriction restriction = new TypeRestriction(type, false);
 JSType expected = null;
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isNotNull"), false).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(NullType.class);
 restriction = new TypeRestriction(type, true);
 expected = typeRegistry.getNativeType(NullType.class);
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isNotNull"), true).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsString() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(String.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isString"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(String.class);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isString"), false).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsBoolean() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(Boolean.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isBoolean"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(Boolean.class);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isBoolean"), false).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNumber() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(Number.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isNumber"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(Number.class);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isNumber"), false).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsFunction() {
 JSType type = typeRegistry.getNativeType(ObjectType.class);
 visitor.visit(type, interpreter.restrictToNotObjectVisitor);
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(FunctionType.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isFunction"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(FunctionType.class);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isFunction"), false).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsArray() {
 JSType type = null;
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(ArrayType.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isArray"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(ArrayType.class);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isArray"), false).getPreciseType();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsObject() {
 JSType type = null;
 TypeRestriction restriction = new TypeRestriction(type, true);
 JSType expected = typeRegistry.getNativeType(ObjectType.class);
 JSType actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isObject"), true).getPreciseType();
 assertEquals(expected, actual);

 type = typeRegistry.getNativeType(ObjectType.class);
 restriction = new TypeRestriction(type, false);
 expected = null;
 actual = interpreter.restrictParameter(new Node(), type, null,
 interpreter.restrictors.get("isObject"), false).getPreciseType();
 assertEquals(expected, actual);
 }
}