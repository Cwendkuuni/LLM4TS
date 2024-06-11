import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {
    
    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        JSType topType = new JSType();
        JSType arrayType = new JSType();
        arrayType.setSubtypeOf(topType);
        
        JSType result = rai.restrictToArrayVisitor.visit(topType);
        assertEquals("Array type should be returned", arrayType, result);
    }
    
    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        JSType topType = new JSType();
        JSType arrayType = new JSType();
        arrayType.setSubtypeOf(topType);
        
        JSType result = rai.restrictToNotArrayVisitor.visit(topType);
        assertEquals("Type should be null", null, result);
    }
    
    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        JSType topType = new JSType();
        
        JSType result = rai.restrictToObjectVisitor.visit(topType);
        assertEquals("Type should be equal to topType", topType, result);
    }
    
    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        JSType topType = new JSType();
        JSType objectType = new JSType();
        objectType.setSubtypeOf(topType);
        
        JSType result = rai.restrictToNotObjectVisitor.visit(topType);
        assertEquals("Type should be null", null, result);
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        
        FlowScope result = rai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertEquals("BlindScope should be returned", blindScope, result);
    }
    
    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return getRestrictedWithoutUndefined(p.type);
                } else {
                    return  p.type != null ?
                        getNativeType(VOID_TYPE).getGreatestSubtype(p.type) : null;
              }
            }
        };
        boolean outcome = true;
        
        FlowScope result = rai.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertEquals("BlindScope should be returned", blindScope, result);
    }
    
    @Test
    public void testTypeRestriction() {
        JSType type = new JSType();
        boolean outcome = true;
        
        TypeRestriction restriction = new TypeRestriction(type, outcome);
        assertEquals("Type should be equal to type", type, restriction.type);
        assertEquals("Outcome should be true", outcome, restriction.outcome);
    }
    
}