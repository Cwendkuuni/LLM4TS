
import com.google.javascript.jscomp.DefinitionSite;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsCall() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
        UseSite useSite = new UseSite();
        useSite.node = new Node();
        Assert.assertFalse(devirtualizePrototypeMethods.isCall(useSite));
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
        Node node = new Node();
        Assert.assertFalse(devirtualizePrototypeMethods.isPrototypeMethodDefinition(node));
    }

    @Test
    public void testGetRewrittenMethodName() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
        String orginalMethodName = "testMethod";
        String expected = "JSCompiler_StaticMethods_testMethod";
        Assert.assertEquals(expected, devirtualizePrototypeMethods.getRewrittenMethodName(orginalMethodName));
    }

    @Test
    public void testIsEligibleDefinition() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.inExterns = false;
        definitionSite.inGlobalScope = true;
        UseSite useSite = new UseSite();
        useSite.node = new Node();
        useSite.module = new JSModule();
        defFinder.addUseSite(definitionSite, useSite);
        Definition definition = new Definition();
        definition.setRValue(new Node(Token.FUNCTION));
        definition.setLValue(new Node(Token.GETPROP));
        definition.getLValue().addChildToBack(new Node(Token.NAME, "testMethod"));
        definition.getRValue().addChildrenToBack(new Node(Token.EMPTY));
        definition.getLValue().addChildrenToBack(new Node(Token.NAME, "prototype"));
        definition.getLValue().getLastChild().addChildToBack(new Node(Token.STRING, "testMethod"));
        definition.getRValue().addChildToBack(new Node(Token.EMPTY));
        definition.getRValue().setString("testMethod");
        definitionSite.definition = definition;
        JSModule definitionModule = new JSModule();
        definitionModule.add(definitionSite.definition);
        definitionSite.module = definitionModule;
        Assert.assertTrue(devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite));
    }

    @Test
    public void testRewriteDefinition() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.EXPR_RESULT, node);
        Node expr = new Node(Token.VAR, node);
        Node block = new Node(Token.BLOCK, expr);
        Node newNameNode = new Node(Token.NAME, "testMethod");
        newNameNode.addChildrenToBack(new Node(Token.EMPTY));
        functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME, "testMethod"));
        functionNode.addChildToBack(new Node(Token.PARAM_LIST));
        functionNode.addChildToBack(new Node(Token.BLOCK));
        functionNode.getLastChild().addChildrenToBack(new Node(Token.EMPTY));
        parent.addChildToBack(newNameNode);
        parent.addChildToBack(functionNode);
        expr.addChildToBack(new Node(Token.EXPR_RESULT, newNameNode));
        expr.addChildToBack(new Node(Token.EXPR_RESULT, functionNode));
        block.addChildToBack(expr);
        String newMethodName = "JSCompiler_StaticMethods_testMethod";
        devirtualizePrototypeMethods.rewriteDefinition(node, newMethodName);
    }

    @Test
    public void testReplaceReferenceToThis() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
        Node node = new Node(Token.THIS);
        String name = "testMethod";
        Node newName = new Node(Token.NAME, name);
        devirtualizePrototypeMethods.replaceReferencesToThis(node, name);
        Assert.assertEquals(newName, node);
    }
}
