import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
      ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
      Visitor<JSType> restrictToArrayVisitor = interpreter.restrictToArrayVisitor;
      
      // Typical use case: when the type is an array type, it should return the same type
      JSType arrayType = new ArrayType();
      JSType result = arrayType.visit(restrictToArrayVisitor);
      assertEquals(arrayType, result);

      // Edge case: when the type is not an array type, it should return null
      JSType objectType = new ObjectType();
      JSType result = objectType.visit(restrictToArrayVisitor);
      assertNull(result);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
      ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
      Visitor<JSType> restrictToNotArrayVisitor = interpreter.restrictToNotArrayVisitor;
      
      // Typical use case: when the type is an object type, it should return the same type
      JSType objectType = new ObjectType();
      JSType result = objectType.visit(restrictToNotArrayVisitor);
      assertEquals(objectType, result);

      // Edge case: when the type is an array type, it should return null
      JSType arrayType = new ArrayType();
      JSType result = arrayType.visit(restrictToNotArrayVisitor);
      assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor() {
      ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
      Visitor<JSType> restrictToObjectVisitor = interpreter.restrictToObjectVisitor;
      
      // Typical use case: when the type is an object type, it should return the same type
      JSType objectType = new ObjectType();
      JSType result = objectType.visit(restrictToObjectVisitor);
      assertEquals(objectType, result);

      // Typical use case: when the type is a function type, it should return the same type
      JSType functionType = new FunctionType();
      JSType result = functionType.visit(restrictToObjectVisitor);
      assertEquals(functionType, result);

      // Edge case: when the type is null, it should return null
      JSType nullType = new NullType();
      JSType result = nullType.visit(restrictToObjectVisitor);
      assertNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
      ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
      Visitor<JSType> restrictToNotObjectVisitor = interpreter.restrictToNotObjectVisitor;
      
      // Edge case: when the type is an object type, it should return null
      JSType objectType = new ObjectType();
      JSType result = objectType.visit(restrictToNotObjectVisitor);
      assertNull(result);

      // Edge case: when the type is a function type, it should return null
      JSType functionType = new FunctionType();
      JSType result = functionType.visit(restrictToNotObjectVisitor);
      assertNull(result);
    }
  
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
      ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
      Node condition = new Node();
      FlowScope blindScope = new FlowScope();
      
      // Edge case: when the condition is a CALL node and the callee is a GETPROP node and param is a qualified name node
      // and the restricter for the right string in the restricters map is not null, it should return the restricted parameter
      condition.setType(Token.CALL);
      Node callee = new Node();
      callee.setType(Token.GETPROP);
      Node param = new Node();
      param.setType(Token.NAME);
      callee.addChildToBack(new Node(Token.NAME, "goog"));
      callee.addChildToBack(new Node(Token.STRING, "isDef"));
      callee.addChildToBack(param);
      condition.addChildToBack(callee);
      blindScope.declareName(param, new JSType());
      FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
      assertNotNull(result);
      
      // Edge case: when the condition is not a CALL node, it should call the nextPreciserScopeKnowingConditionOutcome method
      condition.setType(Token.ADD);
      FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
      assertNull(result);
    }
    
    @Test
    public void testRestrictParameter() {
      ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
      Node parameter = new Node();
      JSType type = new JSType();
      FlowScope blindScope = new FlowScope();
      boolean outcome = true;
      
      // Typical use case: when the type is not null, it should restrict the type and update the scope
      JSType expectedType = interpreter.getRestrictedWithoutUndefined(type);
      FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
      assertNotNull(result);
      assertSame(expectedType, result.getTypeOfName(parameter));
      
      // Edge case: when the type is null, it should return the same scope
      JSType nullType = null;
      FlowScope result = interpreter.restrictParameter(parameter, nullType, blindScope, restriction, outcome);
      assertSame(blindScope, result);
    }

    @Test
    public void testTypeRestriction() {
      JSType type = new JSType();
      boolean outcome = true;
      ClosureReverseAbstractInterpreter.TypeRestriction restriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
      
      // Typical use case: should store the type and outcome correctly
      assertSame(type, restriction.type);
      assertTrue(restriction.outcome);
    }
}