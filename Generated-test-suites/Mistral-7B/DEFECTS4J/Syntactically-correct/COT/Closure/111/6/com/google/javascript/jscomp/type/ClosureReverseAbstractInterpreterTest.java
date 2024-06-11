import static com.google.javascript.jscomp.CodingConvention.Standard;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.TypeRestriction;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    private JSTypeRegistry typeRegistry;
    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setUp() {
        typeRegistry = new JSTypeRegistry();
        interpreter = new ClosureReverseAbstractInterpreter(Standard, typeRegistry);
    }

    @Test
    public void testIsDef_true() {
        Node condition = NodeOperations.createCall(null, "goog.isDef",
            NodeOperations.createString("myVariable"));
        JSType expectedType = typeRegistry.createTopType();
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myVariable"), expectedType, flowScope,
            interpreter.restricters.get("isDef"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsDef_false() {
        Node condition = NodeOperations.createCall(null, "goog.isDef",
            NodeOperations.createNull());
        JSType expectedType = JSTypeFactory.NULL_TYPE;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myVariable"), expectedType, flowScope,
            interpreter.restricters.get("isDef"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsDefAndNotNull_true() {
        Node condition = NodeOperations.createCall(null, "goog.isDefAndNotNull",
            NodeOperations.createName("myVariable"));
        JSType expectedType = typeRegistry.createTopType();
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myVariable"), expectedType, flowScope,
            interpreter.restricters.get("isDefAndNotNull"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsDefAndNotNull_false() {
        Node condition = NodeOperations.createCall(null, "goog.isDefAndNotNull",
            NodeOperations.createNull());
        JSType expectedType = JSTypeFactory.NULL_VOID;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myVariable"), expectedType, flowScope,
            interpreter.restricters.get("isDefAndNotNull"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsString_true() {
        Node condition = NodeOperations.createCall(null, "goog.isString",
            NodeOperations.createString("hello"));
        JSType expectedType = JSTypeFactory.STRING;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myString"), expectedType, flowScope,
            interpreter.restricters.get("isString"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsString_false() {
        Node condition = NodeOperations.createCall(null, "goog.isString",
            NodeOperations.createNumber(1));
        JSType expectedType = null;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myString"), expectedType, flowScope,
            interpreter.restricters.get("isString"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsBoolean_true() {
        Node condition = NodeOperations.createCall(null, "goog.isBoolean",
            NodeOperations.createBoolean(true));
        JSType expectedType = JSTypeFactory.BOOL;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myBoolean"), expectedType, flowScope,
            interpreter.restricters.get("isBoolean"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsBoolean_false() {
        Node condition = NodeOperations.createCall(null, "goog.isBoolean",
            NodeOperations.createNumber(1));
        JSType expectedType = null;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myBoolean"), expectedType, flowScope,
            interpreter.restricters.get("isBoolean"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsNumber_true() {
        Node condition = NodeOperations.createCall(null, "goog.isNumber",
            NodeOperations.createNumber(1));
        JSType expectedType = JSTypeFactory.NUMBER;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myNumber"), expectedType, flowScope,
            interpreter.restricters.get("isNumber"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsNumber_false() {
        Node condition = NodeOperations.createCall(null, "goog.isNumber",
            NodeOperations.createString("hello"));
        JSType expectedType = null;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myNumber"), expectedType, flowScope,
            interpreter.restricters.get("isNumber"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsFunction_true() {
        Node condition = NodeOperations.createCall(null, "goog.isFunction",
            NodeOperations.createCall(null, "myFunction", NodeOperations.createNull()));
        JSType expectedType = JSTypeFactory.FUNCTION;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myFunction"), expectedType, flowScope,
            interpreter.restricters.get("isFunction"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsFunction_false() {
        Node condition = NodeOperations.createCall(null, "goog.isFunction",
            NodeOperations.createNumber(1));
        JSType expectedType = null;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myFunction"), expectedType, flowScope,
            interpreter.restricters.get("isFunction"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsArray_true() {
        Node condition = NodeOperations.createCall(null, "goog.isArray",
            NodeOperations.createArray(NodeOperations.createNumber(1), NodeOperations.createNumber(2)));
        JSType expectedType = JSTypeFactory.ARRAY;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myArray"), expectedType, flowScope,
            interpreter.restricters.get("isArray"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsArray_false() {
        Node condition = NodeOperations.createCall(null, "goog.isArray",
            NodeOperations.createString("hello"));
        JSType expectedType = null;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myArray"), expectedType, flowScope,
            interpreter.restricters.get("isArray"), false);

        assertNull(actualType);
    }

    @Test
    public void testIsObject_true() {
        Node condition = NodeOperations.createCall(null, "goog.isObject",
            NodeOperations.createObjectLiteral(NodeOperations.createStringProperty("property", NodeOperations.createNumber(1))));
        JSType expectedType = JSTypeFactory.OBJECT;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myObject"), expectedType, flowScope,
            interpreter.restricters.get("isObject"), true);

        assertNull(actualType);
    }

    @Test
    public void testIsObject_false() {
        Node condition = NodeOperations.createCall(null, "goog.isObject",
            NodeOperations.createCall(null, "myFunction", NodeOperations.createNull()));
        JSType expectedType = null;
        TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
        FlowScope flowScope = mock(FlowScope.class);

        JSType actualType = interpreter.restrictParameter(
            NodeOperations.createName("myObject"), expectedType, flowScope,
            interpreter.restricters.get("isObject"), false);

        assertNull(actualType);
    }
}

