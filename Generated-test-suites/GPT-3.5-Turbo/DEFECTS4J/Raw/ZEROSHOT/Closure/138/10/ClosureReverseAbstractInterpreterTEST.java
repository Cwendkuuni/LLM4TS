import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {
  
  ClosureReverseAbstractInterpreter interpreter;
  
  @Before
  public void setup() {
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition1 = new Node(Token.CALL);
    Node callee1 = new Node(Token.GETPROP);
    Node left1 = new Node(Token.NAME, "goog");
    Node right1 = new Node(Token.STRING, "isDef");
    Node param1 = new Node(Token.NAME, "param");
    callee1.addChildToFront(left1);
    callee1.addChildToBack(right1);
    condition1.addChildToFront(callee1);
    condition1.addChildToBack(param1);
    
    TypeRestriction returnType1 = new TypeRestriction(null, true);
    
    FlowScope blindScope1 = new FlowScope();
    blindScope1.createChildFlowScope();
    
    FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, true);
    assertEquals("Expected result1", returnType1, result1);
    
    Node condition2 = new Node(Token.CALL);
    Node callee2 = new Node(Token.GETPROP);
    Node left2 = new Node(Token.NAME, "goog");
    Node right2 = new Node(Token.STRING, "isArray");
    Node param2 = new Node(Token.NAME, "param");
    callee2.addChildToFront(left2);
    callee2.addChildToBack(right2);
    condition2.addChildToFront(callee2);
    condition2.addChildToBack(param2);
    
    TypeRestriction returnType2 = new TypeRestriction(getNativeType(ARRAY_TYPE), true);
    
    FlowScope blindScope2 = new FlowScope();
    blindScope2.createChildFlowScope();
    
    FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, true);
    assertEquals("Expected result2", returnType2, result2);
  }
  
  @Test
  public void testRestrictParameter() {
    Node parameter1 = new Node(Token.NAME, "param");
    JSType type1 = null;
    FlowScope blindScope1 = new FlowScope();
    Function<TypeRestriction, JSType> restriction1 = restricters.get("isDef");
    boolean outcome1 = true;
    
    FlowScope expected1 = blindScope1;
    FlowScope result1 = interpreter.restrictParameter(parameter1, type1, blindScope1, restriction1, outcome1);
    assertEquals("Expected result1", expected1, result1);
    
    Node parameter2 = new Node(Token.NAME, "param");
    JSType type2 = new JSType();
    FlowScope blindScope2 = new FlowScope();
    Function<TypeRestriction, JSType> restriction2 = restricters.get("isArray");
    boolean outcome2 = true;
    
    FlowScope expected2 = blindScope2.createChildFlowScope();
    result2.declareNameInScope(expected2, parameter2, getNativeType(ARRAY_TYPE));
    FlowScope result2 = interpreter.restrictParameter(parameter2, type2, blindScope2, restriction2, outcome2);
    assertEquals("Expected result2", expected2, result2);
  }
  
  @Test
  public void testClosureReverseAbstractInterpreter() {
    ClosureReverseAbstractInterpreter interpreter1 = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
    
    assertEquals("Expected interpreter1", interpreter1, interpreter);
  }
  
  @Test
  public void testClosureReverseAbstractInterpreter_restrictionIndex1() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    
    ClosureReverseAbstractInterpreter interpreter2 = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    
    restricters.put("isDef", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return getRestrictedWithoutUndefined(p.type);
        } else {
          return null;
        }
       }
    });
    
    restricters.put("isNull", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return getNativeType(NULL_TYPE);
        } else {
          return getRestrictedWithoutNull(p.type);
        }
      }
    });
    
    restricters.put("isDefAndNotNull", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return getRestrictedWithoutUndefined(
              getRestrictedWithoutNull(p.type));
        } else {
          return null;
        }
      }
    });
    
    restricters.put("isString", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        return getRestrictedByTypeOfResult(p.type, "string", p.outcome);
      }
    });
    
    restricters.put("isBoolean", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        return getRestrictedByTypeOfResult(p.type, "boolean", p.outcome);
      }
    });
    
    restricters.put("isNumber", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        return getRestrictedByTypeOfResult(p.type, "number", p.outcome);
      }
    });
    
    restricters.put("isFunction", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        return getRestrictedByTypeOfResult(p.type, "function", p.outcome);
      }
    });
    
    restricters.put("isArray", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.type == null) {
          return p.outcome ? getNativeType(ARRAY_TYPE) : null;
        }

        Visitor<JSType> visitor = p.outcome ? restrictToArrayVisitor :
            restrictToNotArrayVisitor;
        return p.type.visit(visitor);
      }
    });
    
    restricters.put("isObject", new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.type == null) {
          return p.outcome ? getNativeType(OBJECT_TYPE) : null;
        }

        Visitor<JSType> visitor = p.outcome ? restrictToObjectVisitor :
            restrictToNotObjectVisitor;
        return p.type.visit(visitor);
      }
    });
    
    ClosureReverseAbstractInterpreter interpreter3 = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    
    assertEquals("Expected interpreter3", interpreter3, interpreter);
  }
}