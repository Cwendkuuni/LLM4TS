package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;

import java.util.HashMap;
import java.util.Map;

public class InlineObjectLiteralsTest {

    @Test
    public void testInlineObjectLiterals() {
        AbstractCompiler compiler = new NoOpCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test";

        InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        Node root = Node.objectLiteral();
        Node externs = Node.objectLiteral();

        root.addChildToBack(Node.assign(Node.name("x"), root));

        inlineObjectLiterals.process(externs, root);

        ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
        callback.process(externs, root);

        ReferenceMap referenceMap = callback.getReferenceMap();
        ReferenceCollection referenceInfo = referenceMap.getReferences(referenceMap.getVar("x"));

        List<Reference> references = referenceInfo.references;

        Map<String, Node> expected = new HashMap<>();
        expected.put("a", Node.number(1));
        expected.put("b", Node.number(2));

        Map<String, Node> actual = new HashMap<>();
        for (Reference ref : references) {
            if (ref.getNode() instanceof Node.GetProp) {
                String key = ((Node.GetProp) ref.getNode()).getLastChild().getString();
                actual.put(key, ref.getAssignedValue());
            }
        }

        assertEquals(expected, actual);
    }
}

class NoOpCompiler implements AbstractCompiler {
    @Override
    public void compile(Node externs, Node root) {
        // empty
    }
}