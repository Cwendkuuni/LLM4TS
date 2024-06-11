package org.apache.commons.cli;

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TypeRestriction;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
    private static final ClosureReverseAbstractInterpreter interpreter =
            new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    @Test
    public void testRestrictToArray() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isArray")), new Node(NULL_TYPE));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(ARRAY_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isArray")), new Node(new ObjectType(new ObjectType(ARRAY_TYPE, new JSType[0]))));
        expectedType = interpreter.getType(condition, null, true);
        assertEquals(ARRAY_TYPE, expectedType);
    }

    @Test
    public void testRestrictToNotArray() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isArray")), new Node(new ObjectType(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0]))));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isArray")), new Node(new FunctionType(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0]), ObjectType.OBJECT_TYPE)));
        expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);
    }

    @Test
    public void testRestrictToObject() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isObject")), new Node(NULL_TYPE));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(OBJECT_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isObject")), new Node(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0])));
        expectedType = interpreter.getType(condition, null, true);
        assertEquals(OBJECT_TYPE, expectedType);
    }

    @Test
    public void testRestrictToNotObject() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isObject")), new Node(new FunctionType(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0]), ObjectType.OBJECT_TYPE)));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isObject")), new Node(NULL_TYPE));
        expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);
    }

    @Test
    public void testRestrictToBoolean() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isDef")), new Node(new Boolean(true)));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(NO_OBJECT_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isDef")), new Node(new Boolean(false)));
        expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);
    }

    @Test
    public void testRestrictToString() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isString")), new Node(new String("foo")));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(ObjectType.STRING_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isString")), new Node(new String("")));
        expectedType = interpreter.getType(condition, null, true);
        assertEquals(ObjectType.STRING_TYPE, expectedType);
    }

    @Test
    public void testRestrictToFunction() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isFunction")), new Node(new FunctionType(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0]), ObjectType.OBJECT_TYPE)));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(ObjectType.FUNCTION_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isFunction")), new Node(new FunctionType(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0]), null)));
        expectedType = interpreter.getType(condition, null, true);
        assertEquals(ObjectType.FUNCTION_TYPE, expectedType);
    }

    @Test
    public void testRestrictToNull() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isNull")), new Node(NULL_TYPE));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(NULL_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isNull")), new Node(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0])));
        expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);
    }

    @Test
    public void testRestrictToUndefined() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isDef")), new Node(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0])));
        JSType expectedType = interpreter.getType(condition, null, false);
        assertEquals(NULL_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isDef")), new Node(NULL_TYPE));
        expectedType = interpreter.getType(condition, null, false);
        assertEquals(ObjectType.BOOLEAN_TYPE, expectedType);
    }

    @Test
    public void testRestrictToDefAndNotNull() {
        Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isDefAndNotNull")), new Node(new ObjectType(ObjectType.OBJECT_TYPE, new JSType[0])));
        JSType expectedType = interpreter.getType(condition, null, true);
        assertEquals(ObjectType.OBJECT_TYPE, expectedType);

        condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isDefAndNotNull")), new Node(NULL_TYPE));
        expectedType = interpreter.getType(condition, null, true);
        assertNull(expectedType);
    }
}