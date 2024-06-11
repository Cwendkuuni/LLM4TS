package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private final CodingConvention convention;
    private final JSTypeRegistry typeRegistry;
    private final ClosureReverseAbstractInterpreter rai;
    private final Visitor<JSType> restrictToArrayVisitor;
    private final Visitor<JSType> restrictToNotArrayVisitor;
    private final Visitor<JSType> restrictToObjectVisitor;
    private final Visitor<JSType> restrictToNotObjectVisitor;
    private final Map<String, Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType>> restricters;

    public ClosureReverseAbstractInterpreterTest() {
        convention = new CodingConvention();
        typeRegistry = new JSTypeRegistry(convention);
        restrictToArrayVisitor = null;
        restrictToNotArrayVisitor = null;
        restrictToObjectVisitor = null;
        restrictToNotObjectVisitor = null;
        restricters = new ImmutableMap.Builder<String, Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType>>()
                .put("isDef", null)
                .put("isNull", null)
                .put("isDefAndNotNull", null)
                .put("isString", null)
                .put("isBoolean", null)
                .put("isNumber", null)
                .put("isFunction", null)
                .put("isArray", null)
                .put("isObject", null)
                .build();
        rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        rai.setRestrictToArrayVisitor(restrictToArrayVisitor);
        rai.setRestrictToNotArrayVisitor(restrictToNotArrayVisitor);
        rai.setRestrictToObjectVisitor(restrictToObjectVisitor);
        rai.setRestrictToNotObjectVisitor(restrictToNotObjectVisitor);
        rai.setRestricters(restricters);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;
        FlowScope resultScope = rai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Add your assertions here

    }

    @Test
    public void testRestrictParameter() {
        Node parameter = new Node();
        JSType type = null;
        FlowScope blindScope = new FlowScope();
        Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType> restriction = null;
        boolean outcome = false;
        FlowScope resultScope = rai.restrictParameter(parameter, type, blindScope, restriction, outcome);

        // Add your assertions here

    }
}