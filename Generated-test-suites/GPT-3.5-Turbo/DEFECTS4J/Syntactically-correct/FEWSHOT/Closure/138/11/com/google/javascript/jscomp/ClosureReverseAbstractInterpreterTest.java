package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter rai;
    private CodingConvention convention;
    private JSTypeRegistry typeRegistry;
    private Visitor<JSType> restrictToArrayVisitor;
    private Visitor<JSType> restrictToNotArrayVisitor;
    private Visitor<JSType> restrictToObjectVisitor;
    private Visitor<JSType> restrictToNotObjectVisitor;
    private Map<String, Function<TypeRestriction, JSType>> restricters;

    private class RestrictByTrueTypeOfResultVisitor implements Visitor<JSType>{
        @Override
        public JSType caseTopType(JSType topType) {
            return null;
        }

        @Override
        public JSType caseObjectType(ObjectType type) {
            return null;
        }

        @Override
        public JSType caseFunctionType(FunctionType type) {
            return null;
        }
    }

    private class RestrictByFalseTypeOfResultVisitor implements Visitor<JSType>{
        @Override
        public JSType caseTopType(JSType topType) {
            return null;
        }

        @Override
        public JSType caseObjectType(ObjectType type) {
            return null;
        }

        @Override
        public JSType caseFunctionType(FunctionType type) {
            return null;
        }
    }

    private class TypeRestriction {
        private final JSType type;
        private final boolean outcome;

        private TypeRestriction(JSType type, boolean outcome) {
          this.type = type;
          this.outcome = outcome;
        }
    }

    @Before
    public void setUp() {
        convention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        restrictToArrayVisitor = mock(Visitor.class);
        restrictToNotArrayVisitor = mock(Visitor.class);
        restrictToObjectVisitor = mock(Visitor.class);
        restrictToNotObjectVisitor = mock(Visitor.class);
        restricters = new ImmutableMap.Builder<String, Function<TypeRestriction, JSType>>()
            .put("isDef", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  if (p.outcome) {
                    return null;
                  } else {
                    return null;
                  }
                 }
              })
              .put("isNull", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  if (p.outcome) {
                    return null;
                  } else {
                    return null;
                  }
                }
              })
              .put("isDefAndNotNull", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  if (p.outcome) {
                    return null;
                  } else {
                    return null;
                  }
                }
              })
              .put("isString", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  return null;
                }
              })
              .put("isBoolean", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  return null;
                }
              })
              .put("isNumber", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  return null;
                }
              })
              .put("isFunction", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  return null;
                }
              })
              .put("isArray", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  return null;
                }
              })
              .put("isObject", new Function<TypeRestriction, JSType>() {
                public JSType apply(TypeRestriction p) {
                  return null;
                }
              })
              .build();
        rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry, restrictToArrayVisitor,
        		restrictToNotArrayVisitor, restrictToObjectVisitor, restrictToNotObjectVisitor, restricters);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_callsNextPreciserScopeKnowingConditionOutcome() {
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        boolean outcome = false;

        FlowScope result = rai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        verify(rai).nextPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    }

    // Additional test cases
    // ...

}