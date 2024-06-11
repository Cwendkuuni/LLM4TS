
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor_caseTopType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        JSType result = closureReverseAbstractInterpreter.getRestrictedWithoutUndefined(closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.ARRAY_TYPE));
        JSType expected = closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.ARRAY_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToArrayVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        ObjectType objectType = mock(ObjectType.class);
        Mockito.when(objectType.isSubtype(closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.ARRAY_TYPE))).thenReturn(true);
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToArrayVisitor;
        JSType result = objectType.visit(visitor);
        JSType expected = closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.ARRAY_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToNotArrayVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        ObjectType objectType = mock(ObjectType.class);
        Mockito.when(objectType.isSubtype(closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.ARRAY_TYPE))).thenReturn(true);
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToNotArrayVisitor;
        JSType result = objectType.visit(visitor);
        JSType expected = null;
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseAllType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToNotObjectVisitor;
        JSType result = visitor.caseAllType();
        JSType expected = closureReverseAbstractInterpreter.typeRegistry.createUnionType(
              closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.NUMBER_STRING_BOOLEAN), closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.NULL_VOID));
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        ObjectType objectType = mock(ObjectType.class);
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToNotObjectVisitor;
        JSType result = objectType.visit(visitor);
        JSType expected = null;
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseFunctionType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        FunctionType functionType = mock(FunctionType.class);
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToNotObjectVisitor;
        JSType result = functionType.visit(visitor);
        JSType expected = null;
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseTopType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToObjectVisitor;
        JSType result = visitor.caseTopType(closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.NO_OBJECT_TYPE));
        JSType expected = closureReverseAbstractInterpreter.getNativeType(closureReverseAbstractInterpreter.NO_OBJECT_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        ObjectType objectType = mock(ObjectType.class);
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToObjectVisitor;
        JSType result = objectType.visit(visitor);
        JSType expected = objectType;
        assertEquals(expected, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseFunctionType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        FunctionType functionType = mock(FunctionType.class);
        Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToObjectVisitor;
        JSType result = functionType.visit(visitor);
        JSType expected = functionType;
        assertEquals(expected, result);
    }

    // Additional test cases for other methods in the ClosureReverseAbstractInterpreter class
}