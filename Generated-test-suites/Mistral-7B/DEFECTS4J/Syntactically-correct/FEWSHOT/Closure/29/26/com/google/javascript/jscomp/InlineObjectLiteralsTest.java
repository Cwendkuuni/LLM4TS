package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CompilerPass;
import org.apache.commons.cli.InlineObjectLiterals;
import org.apache.commons.cli.AbstractCompiler;
import org.apache.commons.cli.ReferenceCollectingCallback;
import org.apache.commons.cli.ReferenceMap;
import org.apache.commons.cli.Scope;
import org.apache.commons.cli.Var;
import org.apache.commons.cli.Node;
import org.apache.commons.cli.NodeTraversal;
import org.apache.commons.cli.NodeUtil;
import org.apache.commons.cli.IR;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

    @Test
    public void testInlineObjectLiterals() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_";

        CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        Node root = Node.newBlock("root");
        Var v1 = Node.newVar("v1");
        Var v2 = Node.newVar("v2");
        root.addChild(v1);
        root.addChild(v2);

        Node obj1 = Node.newObjectLit();
        obj1.addChild(Node.newProp("prop1", Node.newNumber(1)));
        obj1.addChild(Node.newProp("prop2", Node.newString("str")));
        obj1.addChild(Node.newProp("prop3", Node.newObjectLit()));
        
        Node obj2 = Node.newObjectLit();
        obj2.addChild(Node.newProp("prop1", Node.newNumber(2)));
        obj2.addChild(Node.newProp("prop2", Node.newString("str2")));

        Node obj3 = Node.newObjectLit();
        obj3.addChild(Node.newProp("prop1", Node.newNumber(3)));
        obj3.addChild(Node.newProp("prop2", Node.newString("str3")));

        v1.setInitExpression(Node.newAssign(v1, obj1));
        v2.setInitExpression(Node.newAssign(v2, obj2));

        Scope scope = new Scope();
        scope.addVars(Lists.newArrayList(v1, v2));
        scope.setRoot(root);

        pass.process(null, root);

        ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
        callback.process(null, root);

        ReferenceMap referenceMap = callback.getReferenceMap();

        List<Reference> refs1 = referenceMap.getReferences(v1);
        List<Reference> refs2 = referenceMap.getReferences(v2);

        Map<String, Node> initvals1 = Maps.newHashMap();
        Map<String, Node> initvals2 = Maps.newHashMap();

        for (Reference reference : refs1) {
            if (reference.isInitializingDeclaration()) {
                fillInitialValues(reference, initvals1);
            }
        }

        for (Reference reference : refs2) {
            if (reference.isInitializingDeclaration()) {
                fillInitialValues(reference, initvals2);
            }
        }

        assertEquals(3, refs1.size());
        assertEquals(3, refs2.size());

        Iterator<Reference> it1 = referenceMap.getReferences(v1).iterator();
        Iterator<Reference> it2 = referenceMap.getReferences(v2).iterator();

        // Check object properties
        assertEquals("prop1", it1.next().getNode().isName().getString());
        assertEquals("prop1", it2.next().getNode().isName().getString());
        assertEquals(1, it1.next().getNode().getFirstChild().getNumber().intValue());
        assertEquals(2, it2.next().getNode().getFirstChild().getNumber().intValue());

        assertEquals("prop2", it1.next().getNode().isName().getString());
        assertEquals("prop2", it2.next().getNode().isName().getString());
        assertEquals("str", it1.next().getNode().getString());
        assertEquals("str2", it2.next().getNode().getString());

        // Check nested object properties
        assertEquals("prop1", it1.next().getNode().isName().getString());
        assertEquals("prop1", it2.next().getNode().isName().getString());
        assertEquals(3, it1.next().getNode().getFirstChild().getNumber().intValue());

        // Check nested object assignments
        assertEquals("prop3", it1.next().getNode().isName().getString());
        assertEquals("prop3", it2.next().getNode().isName().getString());

        // Check initial values
        assertEquals(Node.newNumber(1), initvals1.get("prop1"));
        assertEquals(Node.newNumber(2), initvals2.get("prop1"));
        assertEquals(Node.newString("str"), initvals1.get("prop2"));
        assertEquals(Node.newString("str2"), initvals2.get("prop2"));
        assertEquals(obj1, initvals1.get("prop3"));
        assertEquals(obj2, initvals2.get("prop3"));
    }

    private void fillInitialValues(Reference reference, Map<String, Node> initvals) {
        Node object = reference.getAssignedValue();
        for (Node child = object.getFirstChild(); child != null; child = child.getNext()) {
            String varname = child.getString();
            Node value = child.removeFirstChild();
            initvals.put(varname, value);
        }
    }
}

class InliningBehavior extends ReferenceCollectingCallback.Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
        super.afterExitScope(t, referenceMap);

        for (Var v : t.getScope().getVars()) {
            if (InlineObjectLiterals.this.isVarInlineForbidden(v)) {
                continue;
            }

            ReferenceCollection referenceInfo = referenceMap.getReferences(v);

            if (InlineObjectLiterals.this.isInlinableObject(referenceInfo.references)) {
                staleVars.add(v);

                Reference declaration = referenceInfo.references.get(0);
                Reference init = referenceInfo.getInitializingReference();

                InlineObjectLiterals.this.splitObject(v, declaration, init, referenceInfo);
            }
        }

        for (Var v : staleVars) {
            staleVars.remove(v);
        }
    }
}