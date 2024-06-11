package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testDevirtualizePrototypeMethods() {
        AbstractCompiler compiler = createTestCompiler();
        CompilerPass pass = new DevirtualizePrototypeMethods(compiler);
        Node root = createTestNode();
        pass.process(null, root);
        verifyRewrittenMethods(compiler, root);
    }

    private AbstractCompiler createTestCompiler() {
        return new TestCompiler();
    }

    private Node createTestNode() {
        Node root = new Node(Token.BLOCK, null);
        Node aPrototype = new Node(Token.OBJECT, null);
        root.addChildToFront(aPrototype);
        Node aPrototypePrototype = new Node(Token.OBJECT, null);
        aPrototype.addChildToFront(aPrototypePrototype);
        Node prototypeMethod = new Node(Token.ASSIGN, null);
        prototypeMethod.setLine(1);
        prototypeMethod.setColumn(1);
        aPrototypePrototype.addChildToFront(prototypeMethod);
        Node functionNode = new Node(Token.FUNCTION, null);
        prototypeMethod.addChildToFront(functionNode);
        Node nameNode = new Node(Token.IDENT, "accumulate");
        Node prototypeGetter = new Node(Token.GETPROP, null);
        prototypeGetter.setLine(1);
        prototypeGetter.setColumn(1);
        prototypeGetter.setPropertyName(nameNode);
        prototypeMethod.addChildToFront(prototypeGetter);
        Node functionBody = new Node(Token.BLOCK, null);
        functionNode.setBody(functionBody);
        Node thisVariable = new Node(Token.VAR, null);
        functionBody.addChildToFront(thisVariable);
        Node thisAssignment = new Node(Token.ASSIGN, null);
        thisAssignment.setLine(2);
        thisAssignment.setColumn(1);
        thisAssignment.setPropertyName(new Node(Token.IDENT, "total"));
        thisAssignment.setRightHandSide(thisVariable);
        functionBody.addChildToFront(thisAssignment);
        Node returnStatement = new Node(Token.RETURN, null);
        returnStatement.setLine(3);
        returnStatement.setColumn(1);
        returnStatement.setRValue(thisVariable);
        functionBody.addChildToFront(returnStatement);

        Node callSite = new Node(Token.GETPROP, null);
        callSite.setLine(5);
        callSite.setColumn(1);
        callSite.setPropertyName(nameNode);
        Node callSiteObject = new Node(Token.IDENT, "a");
        callSite.setLeftHandSide(callSiteObject);
        root.addChildToFront(callSite);

        return root;
    }

    private void verifyRewrittenMethods(AbstractCompiler compiler, Node root) {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        defFinder.process(null, root);
        List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
        Preconditions.checkState(!definitionSites.isEmpty());

        DefinitionSite definitionSite = definitionSites.get(0);
        Node rewrittenMethod = definitionSite.node;
        String rewrittenMethodName = definitionSite.definition.getName();

        Node newNameNode = rewrittenMethod.getFirstChild();
        String expectedNewMethodName = "JSCompiler_StaticMethods_accumulate";
        assertThat(newNameNode.getString()).isEqualTo(expectedNewMethodName);

        Node functionBody = rewrittenMethod.getLastChild();
        Node thisVariable = functionBody.getFirstChild();
        String expectedThisVariableName = "self";
        assertThat(thisVariable.getFirstChild().getString()).isEqualTo(expectedThisVariableName);

        Node callSite = root.getLastChild();
        Node newCallSite = callSite.getFirstChild();
        String expectedNewCallSiteMethodName = expectedNewMethodName + "(";
        assertThat(newCallSite.getString()).startsWith(expectedNewCallSiteMethodName);

        Node argument = newCallSite.getLastChild();
        String expectedArgumentName = "a";
        assertThat(argument.getString()).isEqualTo(expectedArgumentName);

        Node rewrittenMethodCall = new Node(Token.CALL, null);
        rewrittenMethodCall.setLine(5);
        rewrittenMethodCall.setColumn(1);
        rewrittenMethodCall.setPropertyName(newNameNode);
        rewrittenMethodCall.addChildToFront(thisVariable);
        rewrittenMethodCall.addChildToFront(argument);
        root.replaceChild(callSite, rewrittenMethodCall);
        compiler.reportCodeChange();

        Node rewrittenCallSite = root.getLastChild();
        assertNull(rewrittenCallSite.getParent());
        assertThat(rewrittenCallSite.toString()).isEqualTo(
            "JSCompiler_StaticMethods_accumulate(self, a)");
    }
}