package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Renamer;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.test.NodeTraversalTestHelper;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

    private static class TestRenamer implements Renamer {
        private int idCounter = 0;

        @Override
        public void addDeclaredName(String name) {
            addDeclaredName(name, null);
        }

        @Override
        public void addDeclaredName(String name, Var var) {
            Preconditions.checkState(name != null);
            if (!declarations.containsKey(name)) {
                declarations.put(name, name + "_" + idCounter++);
            }
        }

        @Override
        public String getReplacementName(String oldName) {
            return declarations.get(oldName);
        }

        @Override
        public boolean stripConstIfReplaced() {
            return false;
        }

        @Override
        public Renamer forChildScope() {
            return new TestRenamer();
        }

        private Map<String, String> declarations = new HashMap<>();
    }

    @Test
    public void testMakeDeclaredNamesUnique() {
        String jsSource =
            "function foo() {" +
            "  var a = 1, b = 2;" +
            "  function bar() {}" +
            "  var c = 3;" +
            "}";

        Scope rootScope = NodeTraversalTestHelper.createScopeFromCode(jsSource);
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootScope);
        nodeTraversal.traverse(renamer);

        // Check that the names are unique
        List<String> names = Arrays.asList("a", "b", "c");
        for (String name : names) {
            Var var = rootScope.getVar(name);
            String newName = renamer.getReplacementName(name);
            assertNotSame(name, newName);
            assertEquals(newName, var.getName());
        }

        // Check that the function names are unique
        Node functionNode = rootScope.getFirstChild();
        Node functionNameNode = functionNode.getFirstChild();
        String functionName = functionNameNode.getString();
        String newFunctionName = renamer.getReplacementName(functionName);
        assertNotSame(functionName, newFunctionName);
        assertEquals(newFunctionName, functionNode.getNameString());

        // Check that object properties are not renamed
        Node objectNode = NodeTraversalTestHelper.getObjectNode(jsSource);
        Node objectPropertyNode = objectNode.getFirstChild().getNext();
        String objectPropertyName = objectPropertyNode.getString();
        String newObjectPropertyName = renamer.getReplacementName(objectPropertyName);
        assertEquals(objectPropertyName, newObjectPropertyName);
        assertEquals(objectPropertyName, objectNode.getFirstChild().getString());
    }
}