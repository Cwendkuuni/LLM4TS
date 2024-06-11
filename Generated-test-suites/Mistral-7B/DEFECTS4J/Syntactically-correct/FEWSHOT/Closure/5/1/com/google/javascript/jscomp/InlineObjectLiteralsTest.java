package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

    private static class TestCompiler extends Compiler {
        TestCompiler() {
            super(Collections.emptyList(), null, null, null, null, null, null, null, null);
        }
    }

    @Test
    public void testInlineObjectLiterals() {
        TestCompiler compiler = new TestCompiler();
        Supplier<String> safeNameIdSupplier = mock(Supplier.class);
        InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        Node root = Node.newBlock("root", IR.emptyStatementList());
        Node externs = Node.newBlock("externs", IR.emptyStatementList());

        Node objLit = Node.newObjectLiteral("objLit", Lists.newArrayList(
                Node.newProperty("a", Node.newNumberLiteral(1)),
                Node.newProperty("b", Node.newStringLiteral("hello")),
                Node.newProperty("c", Node.newObjectLiteral(
                        Lists.newArrayList(
                                Node.newProperty("d", Node.newNumberLiteral(2)),
                                Node.newProperty("e", Node.newStringLiteral("world")))))));

        Node objAssign = Node.newAssign("objAssign", Node.newName("x"), objLit);
        Node statement = Node.newAssign("statement", Node.newName("y"), objAssign);

        Node objLitRef = Node.newGetProp("objLitRef", Node.newName("x"), Node.newName("a"));
        Node objPropRef = Node.newGetProp("objPropRef", Node.newName("x"), Node.newName("b"));
        Node nestedObjPropRef = Node.newGetProp("nestedObjPropRef", Node.newName("x"), Node.newName("c"), Node.newName("d"));

        Node objLitRefSelf = Node.newGetProp("objLitRefSelf", Node.newName("x"), Node.newName("x"));

        Scope scope = new Scope();
        Var xVar = scope.decl(Node.newVar("x", Node.NEW, null, null, Node.UNKNOWN, null, null, null));
        Var yVar = scope.decl(Node.newVar("y", Node.ASSIGN, null, null, Node.UNKNOWN, null, null, null));

        scope.setParent(new Scope());

        Node rootWithInitialization = Node.newBlock("rootWithInitialization", IR.list(
                new Node[]{externs, statement}));

        when(safeNameIdSupplier.get()).thenReturn("_test_");

        inlineObjectLiterals.process(externs, rootWithInitialization);

        NodeTraversal traversal = new NodeTraversal(rootWithInitialization);

        // Check that x and y have been initialized as expected
        Var x = traversal.getScope().getVar("x");
        Var y = traversal.getScope().getVar("y");

        List<Node> xInit = x.initialization;
        List<Node> yInit = y.initialization;

        assertEquals(1, xInit.size());
        assertEquals(Node.NEW, xInit.get(0).getTokenType());

        assertEquals(3, yInit.size());
        assertEquals(Node.ASSIGN, yInit.get(0).getTokenType());
        assertEquals(Node.NAME, yInit.get(1).getTokenType());
        assertEquals("x", yInit.get(1).getString());

        assertEquals(Node.ASSIGN, yInit.get(2).getTokenType());
        Node yObject = yInit.get(2);

        assertEquals(Node.NAME, yObject.getFirstChild().getTokenType());
        assertEquals("a", yObject.getFirstChild().getString());
        assertEquals(Node.ASSIGN, yObject.getLastChild().getTokenType());
        assertEquals(Node.NAME, yObject.getLastChild().getFirstChild().getTokenType());
        assertEquals("_test_a", yObject.getLastChild().getFirstChild().getString());

        assertEquals(Node.NAME, yObject.getLastChild().getLastChild().getTokenType());
        assertEquals("b", yObject.getLastChild().getLastChild().getString());
        assertEquals(Node.NAME, yObject.getLastChild().getLastChild().getLastChild().getTokenType());
        assertEquals("_test_b", yObject.getLastChild().getLastChild().getLastChild().getString());

        Node cObject = NodeUtil.getChild(yObject, 1);

        assertEquals(Node.NAME, cObject.getFirstChild().getTokenType());
        assertEquals("c", cObject.getFirstChild().getString());

        Node dObject = NodeUtil.getChild(cObject, 0);

        assertEquals(Node.NAME, dObject.getFirstChild().getTokenType());
        assertEquals("d", dObject.getFirstChild().getString());
        assertEquals(Node.NAME, dObject.getFirstChild().getLastChild().getTokenType());
        assertEquals("_test_d", dObject.getFirstChild().getLastChild().getString());

        Node eObject = NodeUtil.getChild(cObject, 1);

        assertEquals(Node.NAME, eObject.getFirstChild().getTokenType());
        assertEquals("e", eObject.getFirstChild().getString());
        assertEquals(Node.NAME, eObject.getFirstChild().getLastChild().getTokenType());
        assertEquals("_test_e", eObject.getFirstChild().getLastChild().getString());

        // Check that objLitRef, objPropRef, and nestedObjPropRef have been replaced
        Node objLitRefResolved = NodeUtil.getChild(yObject, 0);
        assertEquals(Node.NAME, objLitRefResolved.getTokenType());
        assertEquals("a", objLitRefResolved.getString());

        Node objPropRefResolved = NodeUtil.getChild(yObject, 2);
        Node objPropName = objPropRefResolved.getFirstChild();
        assertEquals(Node.NAME, objPropName.getTokenType());
        assertEquals("b", objPropName.getString());

        Node nestedObjPropRefResolved = NodeUtil.getChild(NodeUtil.getChild(yObject, 2), 1);
        Node nestedObjPropName = nestedObjPropRefResolved.getFirstChild();
        assertEquals(Node.NAME, nestedObjPropName.getTokenType());
        assertEquals("c", nestedObjPropName.getString());

        Node nestedObjPropName2 = NodeUtil.getChild(nestedObjPropRefResolved, 1);
        assertEquals(Node.NAME, nestedObjPropName2.getTokenType());
        assertEquals("d", nestedObjPropName2.getString());

        // Check that objLitRefSelf has not been replaced
        Node objLitRefSelfResolved = NodeUtil.getChild(yObject, 3);
        assertEquals(Node.GETPROP, objLitRefSelfResolved.getTokenType());
        assertEquals("x", objLitRefSelfResolved.getFirstChild().getString());
        assertEquals(Node.NAME, objLitRefSelfResolved.getLastChild().getTokenType());
        assertEquals("x", objLitRefSelfResolved.getLastChild().getString());

        // Check that objAssign has been replaced with multiple assignments
        Node objAssignResolved = NodeUtil.getChild(yInit.get(2), 2);
        assertEquals(Node.BLOCK, objAssignResolved.getTokenType());

        Node objAssignBlock = NodeUtil.getChild(objAssignResolved, 0);
        assertEquals(1, objAssignBlock.getNumChildren());

        Node topLevelAssign = objAssignBlock.getFirstChild();
        assertEquals(Node.ASSIGN, topLevelAssign.getTokenType());
        assertEquals(Node.NAME, topLevelAssign.getFirstChild().getTokenType());
        assertEquals("_test_a", topLevelAssign.getFirstChild().getString());
        assertEquals(Node.NUMBER, topLevelAssign.getLastChild().getTokenType());
        assertEquals(1, topLevelAssign.getLastChild().getInt());

        Node objAssignBlock2 = NodeUtil.getChild(objAssignResolved, 1);
        assertEquals(1, objAssignBlock2.getNumChildren());

        Node nestedAssign = objAssignBlock2.getFirstChild();
        assertEquals(Node.ASSIGN, nestedAssign.getTokenType());
        assertEquals(Node.NAME, nestedAssign.getFirstChild().getTokenType());
        assertEquals("_test_c", nestedAssign.getFirstChild().getString());

        Node nestedAssignBlock = NodeUtil.getChild(nestedAssign, 1);
        assertEquals(1, nestedAssignBlock.getNumChildren());

        Node nestedTopLevelAssign = nestedAssignBlock.getFirstChild();
        assertEquals(Node.ASSIGN, nestedTopLevelAssign.getTokenType());
        assertEquals(Node.NAME, nestedTopLevelAssign.getFirstChild().getTokenType());
        assertEquals("_test_d", nestedTopLevelAssign.getFirstChild().getString());
        assertEquals(Node.NUMBER, nestedTopLevelAssign.getLastChild().getTokenType());
        assertEquals(2, nestedTopLevelAssign.getLastChild().getInt());

        Node nestedTopLevelAssign2 = NodeUtil.getChild(nestedAssignBlock, 0);
        assertEquals(Node.ASSIGN, nestedTopLevelAssign2.getTokenType());
        assertEquals(Node.NAME, nestedTopLevelAssign2.getFirstChild().getTokenType());
        assertEquals("_test_e", nestedTopLevelAssign2.getFirstChild().getString());
        assertEquals(Node.STRING, nestedTopLevelAssign2.getLastChild().getTokenType());
        assertEquals("world", nestedTopLevelAssign2.getLastChild().getString());

        // Check that objLitRef, objPropRef, and nestedObjPropRef have no references left
        Set<Node> references = Sets.newHashSet();
        for (Node node = rootWithInitialization; node != null; node = node.getParent()) {
            for (Node child : node.getChildren()) {
                references.add(child);
                if (child instanceof Reference) {
                    Reference ref = (Reference) child;
                    for (Reference target : ref.references) {
                        references.add(target.getNode());
                    }
                }
            }
        }

        Iterator<Node> iterator = references.iterator();

        Node objLitRefIterator = iterator.next();
        assertFalse(objLitRefIterator instanceof Reference);

        Node objPropRefIterator = iterator.next();
        assertFalse(objPropRefIterator instanceof Reference);

        Node nestedObjPropRefIterator = iterator.next();
        assertFalse(nestedObjPropRefIterator instanceof Reference);

        assertFalse(iterator.hasNext());
    }
}