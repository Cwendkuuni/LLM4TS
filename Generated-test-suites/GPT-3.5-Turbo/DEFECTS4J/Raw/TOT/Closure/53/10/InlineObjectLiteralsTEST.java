package com.google.javascript.jscomp;

import com.google.common.base.Supplier;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InlineObjectLiteralsTest {

    InlineObjectLiterals inliner;

    @Before
    public void setUp() {
        inliner = new InlineObjectLiterals(new AbstractCompiler() {
            @Override
            public CodingConvention getCodingConvention() {
                return null;
            }
        }, new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        });
    }

    @Test
    public void testIsVarOrAssignExprLhs_VarDeclarationLhs_ReturnsTrue() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.VAR, node);
        Node grandParent = new Node(Token.EXPR_RESULT, parent);
        node.setParent(parent);
        parent.setParent(grandParent);

        boolean result = inliner.isVarOrAssignExprLhs(node);

        assertTrue(result);
    }

    @Test
    public void testIsInlinableObject_SingleValidRef_ReturnsTrue() {
        Node objectLiteral = new Node(Token.OBJECTLIT);
        Node parentNode = new Node(Token.ASSIGN);
        Node grandparentNode = new Node(Token.EXPR_RESULT, parentNode);
        objectLiteral.setParent(parentNode);
        parentNode.setParent(grandparentNode);
        parentNode.setFirstChild(new Node(Token.NAME, "x"));

        Reference reference = new Reference(null, objectLiteral);
        List<Reference> refs = new ArrayList<>();
        refs.add(reference);

        boolean result = inliner.isInlinableObject(refs);

        assertTrue(result);
    }

    @Test
    public void testComputeVarList_MultipleRefs_CorrectMapping() {
        Var var = new Var(new Node(Token.NAME));
        var.name = "x";
        Reference reference1 = new Reference(var, new Node(Token.GETPROP));
        Reference reference2 = new Reference(var, new Node(Token.GETPROP));
        List<Reference> refs = new ArrayList<>();
        refs.add(reference1);
        refs.add(reference2);

        Map<String, String> varMap = inliner.computeVarList(var, referenceInfo);

        assertEquals(1, varMap.size());
        assertTrue(varMap.containsKey("key"));
        assertTrue(varMap.containsValue("JSCompiler_object_inline_x_"));
    }

    // Additional test cases can be added for other methods in the InlineObjectLiterals class.

}